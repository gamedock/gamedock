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

package io.gamedock.dto.mappers;

import io.gamedock.domain.Leaderboard;
import io.gamedock.dto.LeaderboardDetailsDto;
import io.gamedock.dto.LeaderboardDto;
import io.gamedock.dto.LeaderboardSummaryDto;
import io.gamedock.dto.mappers.decorators.LeaderboardMapperDecorator;
import java.util.List;
import org.mapstruct.DecoratedWith;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring", uses = {ScopeMapper.class, TeamMapper.class})
@DecoratedWith(LeaderboardMapperDecorator.class)
public interface LeaderboardMapper {

    LeaderboardSummaryDto toSummaryDto(Leaderboard leaderboard);

    List<LeaderboardSummaryDto> toSummaryDto(List<Leaderboard> leaderboards);

    Leaderboard leaderboardDtoToLeaderboard(LeaderboardDto leaderboardDto);

    LeaderboardDetailsDto toDetailsDto(Leaderboard leaderboard, @MappingTarget LeaderboardDetailsDto leaderboardDto);

    Leaderboard updateLeaderboardFromDto(LeaderboardDto leaderboardDto, @MappingTarget Leaderboard leaderboard);
}
