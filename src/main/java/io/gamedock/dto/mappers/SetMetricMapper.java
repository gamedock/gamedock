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

import io.gamedock.domain.SetMetric;
import io.gamedock.domain.SetScore;
import io.gamedock.dto.SetMetricDetailsDto;
import io.gamedock.dto.SetMetricDto;
import io.gamedock.dto.SetMetricSummaryDto;
import io.gamedock.dto.mappers.decorators.SetMetricMapperDecorator;
import java.util.List;
import org.mapstruct.DecoratedWith;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring", uses = {SetMetricItemMapper.class, ScopeMapper.class})
@DecoratedWith(SetMetricMapperDecorator.class)
public interface SetMetricMapper {

    SetScore metricToScore(SetMetric setMetric);

    SetMetric setMetricDtoToSetMetric(SetMetricDto setMetricDto);

    SetMetricSummaryDto toSummaryDto(SetMetric setMetric);

    List<SetMetricSummaryDto> toSummaryDto(List<SetMetric> setMetrics);

    SetMetricDetailsDto toDetailsDto(SetMetric setMetric, @MappingTarget SetMetricDetailsDto setMetricDto);

    SetMetric updateSetMetricFromDto(SetMetricDto setMetricDto, @MappingTarget SetMetric setMetric);

}
