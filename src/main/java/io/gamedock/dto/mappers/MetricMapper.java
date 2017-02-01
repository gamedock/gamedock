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

import io.gamedock.domain.Metric;
import io.gamedock.domain.Score;
import io.gamedock.dto.MetricDetailsDto;
import io.gamedock.dto.MetricDto;
import io.gamedock.dto.MetricSummaryDto;
import io.gamedock.dto.mappers.decorators.MetricMapperDecorator;
import java.util.List;
import org.mapstruct.DecoratedWith;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring", uses = {ScopeMapper.class})
@DecoratedWith(MetricMapperDecorator.class)
public interface MetricMapper {

    Score metricToScore(Metric metric);

    MetricSummaryDto toSummaryDto(Metric metric);

    List<MetricSummaryDto> toSummaryDto(List<Metric> metrics);

    Metric metricDtoToMetric(MetricDto metricDto);

    MetricDetailsDto toDetailsDto(Metric metric, @MappingTarget MetricDetailsDto metricDto);

    Metric updateMetricFromDto(MetricDto metricDto, @MappingTarget Metric metric);
}
