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

import io.gamedock.domain.Leaderboard;
import io.gamedock.dto.LeaderboardDetailsDto;
import io.gamedock.dto.LeaderboardSummaryDto;
import io.gamedock.dto.mappers.LeaderboardMapper;
import io.gamedock.rest.GamesEndpoint;
import io.gamedock.rest.LeaderboardsEndpoint;
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
public abstract class LeaderboardMapperDecorator implements LeaderboardMapper {

    @Autowired
    private LeaderboardMapper delegate;

    public LeaderboardMapperDecorator() {
    }

    @Override
    public LeaderboardSummaryDto toSummaryDto(Leaderboard leaderboard) {
        LeaderboardSummaryDto dto = delegate.toSummaryDto(leaderboard);
        dto.setParent(URI.create(linkTo(GamesEndpoint.class).slash(leaderboard.getGame()).withSelfRel().getHref()));
        dto.setHref(URI.create(linkTo(LeaderboardsEndpoint.class).slash(leaderboard).withSelfRel().getHref()));
        return dto;
    }

    @Override
    public List<LeaderboardSummaryDto> toSummaryDto(List<Leaderboard> leaderboards) {
        return leaderboards.stream().map(o -> toSummaryDto(o)).collect(Collectors.toList());
    }

    @Override
    public LeaderboardDetailsDto toDetailsDto(Leaderboard leaderboard, @MappingTarget LeaderboardDetailsDto leaderboardDto) {
        LeaderboardDetailsDto dto = delegate.toDetailsDto(leaderboard, leaderboardDto);
        dto.setParent(URI.create(linkTo(GamesEndpoint.class).slash(leaderboard.getGame()).withSelfRel().getHref()));
        dto.setHref(URI.create(linkTo(LeaderboardsEndpoint.class).slash(leaderboard).withSelfRel().getHref()));
        return dto;
    }

}
