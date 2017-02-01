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

import io.gamedock.domain.StateMetric;
import io.gamedock.domain.StateScore;
import io.gamedock.dto.StateMetricDetailsDto;
import io.gamedock.dto.StateMetricDto;
import io.gamedock.dto.StateMetricSummaryDto;
import io.gamedock.dto.mappers.decorators.StateMetricMapperDecorator;
import java.util.List;
import org.mapstruct.DecoratedWith;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring", uses = {StateMetricItemMapper.class, ScopeMapper.class})
@DecoratedWith(StateMetricMapperDecorator.class)
public interface StateMetricMapper {

    StateScore metricToScore(StateMetric stateMetric);

    StateMetric stateMetricDtoToStateMetric(StateMetricDto stateMetricDto);

    StateMetricSummaryDto toSummaryDto(StateMetric stateMetric);

    List<StateMetricSummaryDto> toSummaryDto(List<StateMetric> stateMetrics);

    StateMetricDetailsDto toDetailsDto(StateMetric stateMetric, @MappingTarget StateMetricDetailsDto stateMetricDto);

    StateMetric updateStateMetricFromDto(StateMetricDto stateMetricDto, @MappingTarget StateMetric stateMetric);

}
