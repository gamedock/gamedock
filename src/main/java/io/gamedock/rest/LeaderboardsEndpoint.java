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

import io.gamedock.domain.Leaderboard;
import io.gamedock.domain.Game;
import io.gamedock.dto.LeaderboardDetailsDto;
import io.gamedock.dto.LeaderboardDto;
import io.gamedock.dto.LeaderboardSummaryDto;
import io.gamedock.dto.mappers.LeaderboardMapper;
import io.gamedock.rest.exception.ResourceNotFoundException;
import io.gamedock.rest.pagination.PaginationHeaders;
import io.gamedock.services.business.mechanics.Rankizer;
import io.gamedock.services.repositories.LeaderboardRepository;
import io.gamedock.services.repositories.GameRepository;
import io.gamedock.services.repositories.ScopeRepository;
import io.gamedock.services.repositories.TeamRepository;
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
@RequestMapping(path = WebMvcConfig.API_PREFIX + "leaderboards")
public class LeaderboardsEndpoint {

    private final LeaderboardRepository leaderboardRepository;

    private final GameRepository gameRepository;

    private final TeamRepository teamRepository;

    private final ScopeRepository scopeRepository;

    private final HttpServletRequest request;

    private final LeaderboardMapper leaderboardMapper;

    private final Rankizer leaderboardProcessor;

    @Autowired
    public LeaderboardsEndpoint(LeaderboardRepository leaderboardRepository, GameRepository gameRepository, ScopeRepository scopeRepository, TeamRepository teamRepository, HttpServletRequest request, LeaderboardMapper leaderboardMapper, Rankizer leaderboardProcessor) {
        this.leaderboardRepository = leaderboardRepository;
        this.gameRepository = gameRepository;
        this.teamRepository = teamRepository;
        this.scopeRepository = scopeRepository;
        this.request = request;
        this.leaderboardMapper = leaderboardMapper;
        this.leaderboardProcessor = leaderboardProcessor;
    }

    @RequestMapping(path = "", method = RequestMethod.GET)
    public HttpEntity getLeaderboards(Pageable pgbl) {
        Long gameId = (Long) request.getAttribute("gameId");
        Page<Leaderboard> page = leaderboardRepository.findByGame_Id(gameId, pgbl);
        List<LeaderboardSummaryDto> body = leaderboardMapper.toSummaryDto(page.getContent());
        HttpHeaders headers = PaginationHeaders.create(page.getNumber(), page.getSize(), page.getTotalElements());
        return new ResponseEntity(body, headers, HttpStatus.OK);
    }

    @RequestMapping(value = "", method = RequestMethod.POST)
    public HttpEntity createLeaderboard(@Validated @RequestBody LeaderboardDto leaderboardDto) {
        Long gameId = (Long) request.getAttribute("gameId");
        Game game = gameRepository.findOne(gameId);
        if (leaderboardDto.getTeam() != null && teamRepository.findByIdAndGame_Id(leaderboardDto.getTeam(), gameId) == null) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
        if (scopeRepository.findByIdAndGame_Id(leaderboardDto.getScope(), gameId) == null) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }

        Leaderboard leaderboard = leaderboardMapper.leaderboardDtoToLeaderboard(leaderboardDto);
        leaderboard.setGame(game);
        leaderboardRepository.save(leaderboard);

        LeaderboardDetailsDto body = leaderboardMapper.toDetailsDto(leaderboard, new LeaderboardDetailsDto());
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(body.getHref());
        return new ResponseEntity(body, headers, HttpStatus.CREATED);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public LeaderboardDetailsDto getLeaderboard(@PathVariable Long id) throws ResourceNotFoundException {
        Long gameId = (Long) request.getAttribute("gameId");
        Leaderboard leaderboard = leaderboardRepository.findByIdAndGame_Id(id, gameId);
        if (leaderboard == null) {
            throw new ResourceNotFoundException();
        }
        return leaderboardProcessor.processLeaderboard(leaderboard);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public HttpEntity updateLeaderboard(@Validated @RequestBody LeaderboardDto leaderboardDto, @PathVariable Long id) throws ResourceNotFoundException {
        Long gameId = (Long) request.getAttribute("gameId");
        Leaderboard leaderboard = leaderboardRepository.findByIdAndGame_Id(id, gameId);
        if (leaderboard == null) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
        if (scopeRepository.findByIdAndGame_Id(leaderboardDto.getScope(), gameId) == null) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
        if (leaderboardDto.getTeam() != null && teamRepository.findByIdAndGame_Id(leaderboardDto.getTeam(), gameId) == null) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
        leaderboardMapper.updateLeaderboardFromDto(leaderboardDto, leaderboard);
        leaderboardRepository.save(leaderboard);
        LeaderboardSummaryDto body = leaderboardMapper.toSummaryDto(leaderboard);
        return new ResponseEntity(body, HttpStatus.OK);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteLeaderboard(@PathVariable Long id) throws ResourceNotFoundException {
        Long gameId = (Long) request.getAttribute("gameId");
        Leaderboard leaderboard = leaderboardRepository.findByIdAndGame_Id(id, gameId);
        if (leaderboard == null) {
            throw new ResourceNotFoundException();
        }
        leaderboardRepository.delete(leaderboard);
    }

}
