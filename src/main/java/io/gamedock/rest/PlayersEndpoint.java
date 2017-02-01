/*
 * This file is part of GameDock.
 * 
 * GameDock is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * GameDock is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with GameDock.  If not, see <http://www.gnu.org/licenses/>.
 */

package io.gamedock.rest;

import io.gamedock.domain.EndUser;
import io.gamedock.domain.Player;
import io.gamedock.domain.Game;
import io.gamedock.dto.PlayerDetailsDto;
import io.gamedock.dto.PlayerDto;
import io.gamedock.dto.PlayerSummaryDto;
import io.gamedock.dto.TraitInstanceDto;
import io.gamedock.rest.exception.ResourceNotFoundException;
import io.gamedock.rest.pagination.PaginationHeaders;
import io.gamedock.services.business.mechanics.Scoreizer;
import io.gamedock.services.repositories.EndUserRepository;
import io.gamedock.services.repositories.GameRepository;
import io.gamedock.services.repositories.TraitRepository;
import io.gamedock.web.config.WebMvcConfig;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import io.gamedock.services.repositories.PlayerRepository;
import io.gamedock.dto.mappers.PlayerMapper;

@RestController
@RequestMapping(path = WebMvcConfig.API_PREFIX + "players")
public class PlayersEndpoint {

    private final PlayerRepository playerRepository;

    private final GameRepository gameRepository;

    private final EndUserRepository endUserRepository;

    private final TraitRepository traitRepository;

    private final HttpServletRequest request;

    private final PlayerMapper playerMapper;

    private final Scoreizer scoreizer;

    @Autowired
    public PlayersEndpoint(PlayerRepository playerRepository, GameRepository gameRepository, EndUserRepository endUserRepository, TraitRepository traitRepository, HttpServletRequest request, PlayerMapper playerMapper, Scoreizer scoreizer) {
        this.playerRepository = playerRepository;
        this.gameRepository = gameRepository;
        this.endUserRepository = endUserRepository;
        this.traitRepository = traitRepository;
        this.request = request;
        this.playerMapper = playerMapper;
        this.scoreizer = scoreizer;
    }

    @RequestMapping(path = "", method = RequestMethod.GET)
    public HttpEntity getPlayers(Pageable pgbl) {
        Long gameId = getQueryParameter("game");
        if (gameId == null) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
        Page<Player> page = playerRepository.findByGame_Id(gameId, pgbl);
        List<PlayerSummaryDto> body = playerMapper.toSummaryDto(page.getContent());
        HttpHeaders headers = PaginationHeaders.create(page.getNumber(), page.getSize(), page.getTotalElements());
        return new ResponseEntity(body, headers, HttpStatus.OK);
    }

    @RequestMapping(value = "", method = RequestMethod.POST)
    public HttpEntity createPlayer(@Validated @RequestBody PlayerDto playerDto) {
        Long gameId = getQueryParameter("game");
        Long endUserId = getQueryParameter("enduser");
        if (gameId == null || endUserId == null) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
        if (!isValidTraits(playerDto.getTraits(), gameId)) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
        if (playerRepository.findByEndUser_IdAndGame_Id(endUserId, gameId) != null) {
            return new ResponseEntity(HttpStatus.CONFLICT);
        }
        Game game = gameRepository.findOne(gameId);
        EndUser endUser = endUserRepository.findOne(endUserId);
        if (game == null || endUser == null) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }

        Player player = playerMapper.playerDtoToPlayer(playerDto);
        player.setGame(game);
        player.setEndUser(endUser);
        playerRepository.save(player);
        scoreizer.generate(game.getMetrics(), player);

        PlayerDetailsDto body = playerMapper.toDetailsDto(player, new PlayerDetailsDto());
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(body.getHref());
        return new ResponseEntity(body, headers, HttpStatus.CREATED);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public PlayerDetailsDto getPlayer(@PathVariable Long id) throws ResourceNotFoundException {
        Player player = playerRepository.findOne(id);
        if (player == null) {
            throw new ResourceNotFoundException();
        }
        return playerMapper.toDetailsDto(player, new PlayerDetailsDto());
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public HttpEntity updatePlayer(@Validated @RequestBody PlayerDto playerDto, @PathVariable Long id) throws ResourceNotFoundException {
        Player player = playerRepository.findOne(id);
        if (player == null) {
            throw new ResourceNotFoundException();
        }
        if (!isValidTraits(playerDto.getTraits(), player.getGame().getId())) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
        playerMapper.updatePlayerFromDto(playerDto, player);
        playerRepository.save(player);
        PlayerDetailsDto body = playerMapper.toDetailsDto(player, new PlayerDetailsDto());
        return new ResponseEntity(body, HttpStatus.OK);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletePlayer(@PathVariable Long id) throws ResourceNotFoundException {
        Player player = playerRepository.findOne(id);
        if (player == null) {
            throw new ResourceNotFoundException();
        }
        playerRepository.delete(player);
    }

    private Long getQueryParameter(String name) {
        String parameter = request.getParameter(name);
        Long value = null;
        try {
            value = Long.parseLong(parameter);
        } catch (Exception e) {
        }
        return value;
    }

    private boolean isValidTraits(List<TraitInstanceDto> traitIds, Long gameId) {
        return traitIds.stream().map((t) -> traitRepository.findByIdAndGame_Id(t.getTrait(), gameId)).noneMatch((trait) -> (trait == null));
    }

}
