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

import io.gamedock.domain.Game;
import io.gamedock.domain.Organization;
import io.gamedock.dto.GameDetailsDto;
import io.gamedock.dto.GameDto;
import io.gamedock.dto.GameSummaryDto;
import io.gamedock.dto.mappers.GameMapper;
import io.gamedock.rest.exception.ResourceNotFoundException;
import io.gamedock.rest.pagination.PaginationHeaders;
import io.gamedock.services.repositories.GameRepository;
import io.gamedock.services.repositories.OrganizationRepository;
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

@RestController
@RequestMapping(path = WebMvcConfig.API_PREFIX + "games")
public class GamesEndpoint {

    private final GameRepository gameRepository;

    private final OrganizationRepository organizationRepository;

    private final HttpServletRequest request;

    private final GameMapper gameMapper;

    @Autowired
    public GamesEndpoint(GameRepository gameRepository, OrganizationRepository organizationRepository, HttpServletRequest request, GameMapper gameMapper) {
        this.gameRepository = gameRepository;
        this.organizationRepository = organizationRepository;
        this.request = request;
        this.gameMapper = gameMapper;
    }

    @RequestMapping(path = "", method = RequestMethod.GET)
    public HttpEntity getGames(Pageable pgbl) {
        Long organizationId = (Long) request.getAttribute("organizationId");
        Page<Game> page = gameRepository.findByOrganization_Id(organizationId, pgbl);
        List<GameSummaryDto> body = gameMapper.toSummaryDto(page.getContent());
        HttpHeaders headers = PaginationHeaders.create(page.getNumber(), page.getSize(), page.getTotalElements());
        return new ResponseEntity(body, headers, HttpStatus.OK);
    }

    @RequestMapping(value = "", method = RequestMethod.POST)
    public HttpEntity createGame(@Validated @RequestBody GameDto gameDto) {
        Long organizationId = (Long) request.getAttribute("organizationId");
        Organization organization = organizationRepository.findOne(organizationId);

        Game game = gameMapper.gameDtoToGame(gameDto);
        game.setOrganization(organization);
        gameRepository.save(game);

        GameDetailsDto body = gameMapper.toDetailsDto(game, new GameDetailsDto());
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(body.getHref());
        return new ResponseEntity(body, headers, HttpStatus.CREATED);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public GameDetailsDto getGame(@PathVariable Long id) throws ResourceNotFoundException {
        Long organizationId = (Long) request.getAttribute("organizationId");
        Game game = gameRepository.findByIdAndOrganization_Id(id, organizationId);
        if (game == null) {
            throw new ResourceNotFoundException();
        }
        return gameMapper.toDetailsDto(game, new GameDetailsDto());
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    @ResponseStatus(HttpStatus.OK)
    public GameDetailsDto updateGame(@Validated @RequestBody GameDto gameDto, @PathVariable Long id) throws ResourceNotFoundException {
        Long organizationId = (Long) request.getAttribute("organizationId");
        Game game = gameRepository.findByIdAndOrganization_Id(id, organizationId);
        if (game == null) {
            throw new ResourceNotFoundException();
        }
        gameMapper.updateGameFromDto(gameDto, game);
        gameRepository.save(game);
        return gameMapper.toDetailsDto(game, new GameDetailsDto());
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteGame(@PathVariable Long id) throws ResourceNotFoundException {
        Long organizationId = (Long) request.getAttribute("organizationId");
        Game game = gameRepository.findByIdAndOrganization_Id(id, organizationId);
        if (game == null) {
            throw new ResourceNotFoundException();
        }
        gameRepository.delete(game);
    }

}
