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

import io.gamedock.domain.PointMetric;
import io.gamedock.domain.PointScore;
import io.gamedock.dto.PointMetricDetailsDto;
import io.gamedock.dto.PointMetricDto;
import io.gamedock.dto.PointMetricSummaryDto;
import io.gamedock.dto.mappers.decorators.PointMetricMapperDecorator;
import java.util.List;
import org.mapstruct.DecoratedWith;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring", uses = {PointMetricConstraintsMapper.class, ScopeMapper.class})
@DecoratedWith(PointMetricMapperDecorator.class)
public interface PointMetricMapper {

    PointScore metricToScore(PointMetric pointMetric);

    PointMetric pointMetricDtoToPointMetric(PointMetricDto pointMetricDto);

    PointMetricSummaryDto toSummaryDto(PointMetric pointMetric);

    List<PointMetricSummaryDto> toSummaryDto(List<PointMetric> pointMetrics);

    PointMetricDetailsDto toDetailsDto(PointMetric pointMetric, @MappingTarget PointMetricDetailsDto pointMetricDto);

    PointMetric updatePointMetricFromDto(PointMetricDto pointMetricDto, @MappingTarget PointMetric pointMetric);

}
