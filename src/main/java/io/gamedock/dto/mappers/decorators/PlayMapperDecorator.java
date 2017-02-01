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

import io.gamedock.domain.Play;
import io.gamedock.dto.PlayDetailsDto;
import io.gamedock.dto.PlaySummaryDto;
import io.gamedock.dto.mappers.PlayMapper;
import io.gamedock.rest.GamesEndpoint;
import io.gamedock.rest.PlaysEndpoint;
import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import org.springframework.stereotype.Component;

@Component
@Primary
public abstract class PlayMapperDecorator implements PlayMapper {

    @Autowired
    private PlayMapper delegate;

    public PlayMapperDecorator() {
    }

    @Override
    public PlaySummaryDto toSummaryDto(Play play) {
        PlaySummaryDto dto = delegate.toSummaryDto(play);
        dto.setParent(URI.create(linkTo(GamesEndpoint.class).slash(play.getGame()).withSelfRel().getHref()));
        dto.setHref(URI.create(linkTo(PlaysEndpoint.class).slash(play).withSelfRel().getHref()));
        return dto;
    }

    @Override
    public List<PlaySummaryDto> toSummaryDto(List<Play> plays) {
        return plays.stream().map(o -> toSummaryDto(o)).collect(Collectors.toList());
    }

    @Override
    public PlayDetailsDto toDetailsDto(Play play, PlayDetailsDto playDto) {
        PlayDetailsDto dto = delegate.toDetailsDto(play, playDto);
        dto.setParent(URI.create(linkTo(GamesEndpoint.class).slash(play.getGame()).withSelfRel().getHref()));
        dto.setHref(URI.create(linkTo(PlaysEndpoint.class).slash(play).withSelfRel().getHref()));
        return dto;
    }

}
