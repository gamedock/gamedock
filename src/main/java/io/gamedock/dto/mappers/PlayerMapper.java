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

import io.gamedock.domain.Player;
import io.gamedock.dto.PlayerDetailsDto;
import io.gamedock.dto.PlayerDto;
import io.gamedock.dto.PlayerSummaryDto;
import io.gamedock.dto.mappers.decorators.PlayerMapperDecorator;
import java.util.List;
import org.mapstruct.DecoratedWith;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring", uses = {GameMapper.class, EndUserMapper.class, TeammateMapper.class, ScoreMapper.class})
@DecoratedWith(PlayerMapperDecorator.class)
public interface PlayerMapper {

    Player playerDtoToPlayer(PlayerDto playerDto);

    PlayerSummaryDto toSummaryDto(Player player);

    List<PlayerSummaryDto> toSummaryDto(List<Player> players);

    PlayerDetailsDto toDetailsDto(Player player, @MappingTarget PlayerDetailsDto playerDto);

    Player updatePlayerFromDto(PlayerDto playerDto, @MappingTarget Player player);

}
