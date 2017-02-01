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

import io.gamedock.domain.Teammate;
import io.gamedock.dto.TeammateDetailsDto;
import io.gamedock.dto.TeammateDto;
import io.gamedock.dto.TeammateSummaryDto;
import io.gamedock.dto.mappers.TeammateMapper;
import io.gamedock.dto.mappers.TraitInstanceMapper;
import io.gamedock.rest.TeammatesEndpoint;
import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import org.springframework.stereotype.Component;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

@Component
@Primary
public abstract class TeammateMapperDecorator implements TeammateMapper {

    @Autowired
    private TeammateMapper delegate;

    @Autowired
    private TraitInstanceMapper traitInstanceMapper;

    public TeammateMapperDecorator() {
    }

    @Override
    public Teammate teammateDtoToTeammate(TeammateDto teammateDto) {
        Teammate teammate = delegate.teammateDtoToTeammate(teammateDto);
        teammate.setTraitInstances(traitInstanceMapper.traitInstanceDtoToTraitInstance(teammateDto.getTraits(), teammate));
        return teammate;
    }

    @Override
    public TeammateSummaryDto toSummaryDto(Teammate teammate) {
        TeammateSummaryDto dto = delegate.toSummaryDto(teammate);
        dto.setHref(URI.create(linkTo(TeammatesEndpoint.class).slash(teammate).withSelfRel().getHref()));
        return dto;
    }

    @Override
    public List<TeammateSummaryDto> toSummaryDto(List<Teammate> teammates) {
        return teammates.stream().map(o -> toSummaryDto(o)).collect(Collectors.toList());
    }

    @Override
    public TeammateDetailsDto toDetailsDto(Teammate teammate, TeammateDetailsDto teammateDto) {
        TeammateDetailsDto dto = delegate.toDetailsDto(teammate, teammateDto);
        dto.setHref(URI.create(linkTo(TeammatesEndpoint.class).slash(teammate).withSelfRel().getHref()));
        dto.setTraits(traitInstanceMapper.traitInstanceToMap(teammate.getTraitInstances()));
        return dto;
    }

}
