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

import io.gamedock.domain.Game;
import io.gamedock.dto.GameDetailsDto;
import io.gamedock.dto.GameSummaryDto;
import io.gamedock.dto.mappers.GameMapper;
import io.gamedock.rest.GamesEndpoint;
import io.gamedock.rest.OrganizationsEndpoint;
import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

@Component
@Primary
public abstract class GameMapperDecorator implements GameMapper {

    @Autowired
    private GameMapper delegate;

    public GameMapperDecorator() {
    }

    @Override
    public GameSummaryDto toSummaryDto(Game game) {
        GameSummaryDto dto = delegate.toSummaryDto(game);
        dto.setParent(URI.create(linkTo(OrganizationsEndpoint.class).slash(game.getOrganization()).withSelfRel().getHref()));
        dto.setHref(URI.create(linkTo(GamesEndpoint.class).slash(game).withSelfRel().getHref()));
        return dto;
    }

    @Override
    public List<GameSummaryDto> toSummaryDto(List<Game> games) {
        return games.stream().map(b -> toSummaryDto(b)).collect(Collectors.toList());
    }

    @Override
    public GameDetailsDto toDetailsDto(Game game, GameDetailsDto gameDto) {
        GameDetailsDto dto = delegate.toDetailsDto(game, gameDto);
        dto.setParent(URI.create(linkTo(OrganizationsEndpoint.class).slash(game.getOrganization()).withSelfRel().getHref()));
        dto.setHref(URI.create(linkTo(GamesEndpoint.class).slash(game).withSelfRel().getHref()));
        return dto;
    }

}
