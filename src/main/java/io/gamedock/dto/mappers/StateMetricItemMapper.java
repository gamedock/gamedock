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

import io.gamedock.domain.StateMetricItem;
import io.gamedock.dto.StateMetricItemDto;
import java.util.Set;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface StateMetricItemMapper {

    StateMetricItem stateMetricItemDtoToStateMetricItem(StateMetricItemDto stateMetricItemDto);

    StateMetricItemDto toDto(StateMetricItem stateMetricItem);

    Set<StateMetricItemDto> toDto(Set<StateMetricItem> stateMetricItems);

    StateMetricItem updateStateMetricItemFromDto(StateMetricItemDto stateMetricItemDto, @MappingTarget StateMetricItem stateMetricItem);

}
