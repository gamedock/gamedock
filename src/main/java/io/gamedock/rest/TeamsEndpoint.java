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

import io.gamedock.domain.Team;
import io.gamedock.domain.Game;
import io.gamedock.dto.TeamDetailsDto;
import io.gamedock.dto.TeamDto;
import io.gamedock.dto.TeamSummaryDto;
import io.gamedock.dto.TraitInstanceDto;
import io.gamedock.dto.mappers.TeamMapper;
import io.gamedock.rest.exception.ResourceNotFoundException;
import io.gamedock.rest.pagination.PaginationHeaders;
import io.gamedock.services.business.mechanics.Scoreizer;
import io.gamedock.services.repositories.TeamRepository;
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

@RestController
@RequestMapping(path = WebMvcConfig.API_PREFIX + "teams")
public class TeamsEndpoint {

    private final TeamRepository teamRepository;

    private final GameRepository gameRepository;

    private final TraitRepository traitRepository;

    private final HttpServletRequest request;

    private final TeamMapper teamMapper;

    private final Scoreizer scoreizer;

    @Autowired
    public TeamsEndpoint(TeamRepository teamRepository, GameRepository gameRepository, TraitRepository traitRepository, HttpServletRequest request, TeamMapper teamMapper, Scoreizer scoreizer) {
        this.teamRepository = teamRepository;
        this.gameRepository = gameRepository;
        this.traitRepository = traitRepository;
        this.request = request;
        this.teamMapper = teamMapper;
        this.scoreizer = scoreizer;
    }

    @RequestMapping(path = "", method = RequestMethod.GET)
    public HttpEntity getTeams(Pageable pgbl) {
        Long gameId = (Long) request.getAttribute("gameId");
        Page<Team> page = teamRepository.findByGame_Id(gameId, pgbl);
        List<TeamSummaryDto> body = teamMapper.toSummaryDto(page.getContent());
        HttpHeaders headers = PaginationHeaders.create(page.getNumber(), page.getSize(), page.getTotalElements());
        return new ResponseEntity(body, headers, HttpStatus.OK);
    }

    @RequestMapping(value = "", method = RequestMethod.POST)
    public HttpEntity createTeam(@Validated @RequestBody TeamDto teamDto) {
        Long gameId = (Long) request.getAttribute("gameId");
        Game game = gameRepository.findOne(gameId);
        if (!isValidTraits(teamDto.getTraits(), gameId)) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }

        Team team = teamMapper.teamDtoToTeam(teamDto);
        team.setGame(game);
        teamRepository.save(team);
        scoreizer.generate(game.getMetrics(), team);

        TeamDetailsDto body = teamMapper.toDetailsDto(team, new TeamDetailsDto());
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(body.getHref());
        return new ResponseEntity(body, headers, HttpStatus.CREATED);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public TeamDetailsDto getTeam(@PathVariable Long id) throws ResourceNotFoundException {
        Long gameId = (Long) request.getAttribute("gameId");
        Team team = teamRepository.findByIdAndGame_Id(id, gameId);
        if (team == null) {
            throw new ResourceNotFoundException();
        }
        return teamMapper.toDetailsDto(team, new TeamDetailsDto());
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public HttpEntity updateTeam(@Validated @RequestBody TeamDto teamDto, @PathVariable Long id) throws ResourceNotFoundException {
        Long gameId = (Long) request.getAttribute("gameId");
        Team team = teamRepository.findByIdAndGame_Id(id, gameId);
        if (team == null) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
        if (!isValidTraits(teamDto.getTraits(), gameId)) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
        teamMapper.updateTeamFromDto(teamDto, team);
        teamRepository.save(team);
        TeamDetailsDto body = teamMapper.toDetailsDto(team, new TeamDetailsDto());;
        return new ResponseEntity(body, HttpStatus.OK);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteTeam(@PathVariable Long id) throws ResourceNotFoundException {
        Long gameId = (Long) request.getAttribute("gameId");
        Team team = teamRepository.findByIdAndGame_Id(id, gameId);
        if (team == null) {
            throw new ResourceNotFoundException();
        }
        teamRepository.delete(team);
    }

    private boolean isValidTraits(List<TraitInstanceDto> traitIds, Long gameId) {
        return traitIds.stream().map((t) -> traitRepository.findByIdAndGame_Id(t.getTrait(), gameId)).noneMatch((trait) -> (trait == null));
    }

}
