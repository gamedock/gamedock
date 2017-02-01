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

package io.gamedock.dto.mappers.decorators;

import io.gamedock.domain.Team;
import io.gamedock.dto.TeamDetailsDto;
import io.gamedock.dto.TeamDto;
import io.gamedock.dto.TeamSummaryDto;
import io.gamedock.dto.mappers.TeamMapper;
import io.gamedock.dto.mappers.TraitInstanceMapper;
import io.gamedock.rest.GamesEndpoint;
import io.gamedock.rest.TeamsEndpoint;
import io.gamedock.services.repositories.TeamRepository;
import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;
import org.mapstruct.MappingTarget;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import org.springframework.stereotype.Component;

@Component
@Primary
public abstract class TeamMapperDecorator implements TeamMapper {

    @Autowired
    private TeamMapper delegate;

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private TraitInstanceMapper traitInstanceMapper;

    public TeamMapperDecorator() {
    }

    @Override
    public Team teamIdToTeam(Long teamId) {
        if (teamId == null) {
            return null;
        } else {
            return teamRepository.findOne(teamId);
        }
    }

    @Override
    public Team teamDtoToTeam(TeamDto teamDto) {
        Team team = delegate.teamDtoToTeam(teamDto);
        team.setTraitInstances(traitInstanceMapper.traitInstanceDtoToTraitInstance(teamDto.getTraits(), team));
        return team;
    }

    @Override
    public TeamSummaryDto toSummaryDto(Team team) {
        if (team != null) {
            TeamSummaryDto dto = delegate.toSummaryDto(team);
            dto.setParent(URI.create(linkTo(GamesEndpoint.class).slash(team.getGame()).withSelfRel().getHref()));
            dto.setHref(URI.create(linkTo(TeamsEndpoint.class).slash(team).withSelfRel().getHref()));
            return dto;
        } else {
            return null;
        }
    }

    @Override
    public List<TeamSummaryDto> toSummaryDto(List<Team> teams) {
        return teams.stream().map(o -> toSummaryDto(o)).collect(Collectors.toList());
    }

    @Override
    public TeamDetailsDto toDetailsDto(Team team, TeamDetailsDto teamDto) {
        TeamDetailsDto dto = delegate.toDetailsDto(team, teamDto);
        dto.setParent(URI.create(linkTo(GamesEndpoint.class).slash(team.getGame()).withSelfRel().getHref()));
        dto.setHref(URI.create(linkTo(TeamsEndpoint.class).slash(team).withSelfRel().getHref()));
        dto.setTraits(traitInstanceMapper.traitInstanceToMap(team.getTraitInstances()));
        return dto;
    }

    @Override
    public Team updateTeamFromDto(TeamDto teamDto, @MappingTarget Team team) {
        Team tm = delegate.updateTeamFromDto(teamDto, team);
        return tm;
    }

}
