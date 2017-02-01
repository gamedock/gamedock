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

import io.gamedock.domain.Player;
import io.gamedock.domain.Teammate;
import io.gamedock.domain.Team;
import io.gamedock.dto.TeammateDetailsDto;
import io.gamedock.dto.TeammateDto;
import io.gamedock.dto.TeammateSummaryDto;
import io.gamedock.dto.TraitInstanceDto;
import io.gamedock.rest.exception.ResourceNotFoundException;
import io.gamedock.rest.pagination.PaginationHeaders;
import io.gamedock.services.business.mechanics.Scoreizer;
import io.gamedock.services.repositories.TeamRepository;
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
import io.gamedock.services.repositories.TeammateRepository;
import io.gamedock.dto.mappers.TeammateMapper;
import io.gamedock.services.repositories.PlayerRepository;

@RestController
@RequestMapping(path = WebMvcConfig.API_PREFIX + "teammates")
public class TeammatesEndpoint {

    private final TeammateRepository teammateRepository;

    private final TeamRepository teamRepository;

    private final PlayerRepository playerRepository;

    private final TraitRepository traitRepository;

    private final HttpServletRequest request;

    private final TeammateMapper teammateMapper;

    private final Scoreizer scoreizer;

    @Autowired
    public TeammatesEndpoint(TeammateRepository teammateRepository, TeamRepository teamRepository, PlayerRepository playerRepository, TraitRepository traitRepository, HttpServletRequest request, TeammateMapper teammateMapper, Scoreizer scoreizer) {
        this.teammateRepository = teammateRepository;
        this.teamRepository = teamRepository;
        this.playerRepository = playerRepository;
        this.traitRepository = traitRepository;
        this.request = request;
        this.teammateMapper = teammateMapper;
        this.scoreizer = scoreizer;
    }

    @RequestMapping(path = "", method = RequestMethod.GET)
    public HttpEntity getTeammates(Pageable pgbl) {
        Long teamId = getQueryParameter("team");
        if (teamId == null) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
        Page<Teammate> page = teammateRepository.findByTeam_Id(teamId, pgbl);
        List<TeammateSummaryDto> body = teammateMapper.toSummaryDto(page.getContent());
        HttpHeaders headers = PaginationHeaders.create(page.getNumber(), page.getSize(), page.getTotalElements());
        return new ResponseEntity(body, headers, HttpStatus.OK);
    }

    @RequestMapping(value = "", method = RequestMethod.POST)
    public HttpEntity createTeammate(@Validated @RequestBody TeammateDto teammateDto) {
        Long teamId = getQueryParameter("team");
        Long playerId = getQueryParameter("player");
        if (teamId == null || playerId == null) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
        if (teammateRepository.findByPlayer_IdAndTeam_Id(playerId, teamId) != null) {
            return new ResponseEntity(HttpStatus.CONFLICT);
        }
        Team team = teamRepository.findOne(teamId);
        Player player = playerRepository.findOne(playerId);
        if (team == null || player == null || team.getGame() != player.getGame()) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
        if (!isValidTraits(teammateDto.getTraits(), team.getGame().getId())) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }

        Teammate teammate = teammateMapper.teammateDtoToTeammate(teammateDto);
        teammate.setTeam(team);
        teammate.setPlayer(player);
        teammateRepository.save(teammate);
        scoreizer.generate(teammate.getTeam().getGame().getMetrics(), teammate);

        TeammateDetailsDto body = teammateMapper.toDetailsDto(teammate, new TeammateDetailsDto());
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(body.getHref());
        return new ResponseEntity(body, headers, HttpStatus.CREATED);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public TeammateDetailsDto getTeammate(@PathVariable Long id) throws ResourceNotFoundException {
        Teammate teammate = teammateRepository.findOne(id);
        if (teammate == null) {
            throw new ResourceNotFoundException();
        }
        return teammateMapper.toDetailsDto(teammate, new TeammateDetailsDto());
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public HttpEntity updateTeammate(@Validated @RequestBody TeammateDto teammateDto, @PathVariable Long id) throws ResourceNotFoundException {
        Teammate teammate = teammateRepository.findOne(id);
        if (teammate == null) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
        if (!isValidTraits(teammateDto.getTraits(), teammate.getTeam().getGame().getId())) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
        teammateMapper.updateTeammateFromDto(teammateDto, teammate);
        teammateRepository.save(teammate);
        TeammateDetailsDto body = teammateMapper.toDetailsDto(teammate, new TeammateDetailsDto());;
        return new ResponseEntity(body, HttpStatus.OK);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteTeammate(@PathVariable Long id) throws ResourceNotFoundException {
        Teammate teammate = teammateRepository.findOne(id);
        if (teammate == null) {
            throw new ResourceNotFoundException();
        }
        teammateRepository.delete(teammate);
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
