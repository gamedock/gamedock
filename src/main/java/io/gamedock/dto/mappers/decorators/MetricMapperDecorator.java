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

import io.gamedock.domain.Metric;
import io.gamedock.domain.PointMetric;
import io.gamedock.domain.Score;
import io.gamedock.domain.SetMetric;
import io.gamedock.domain.StateMetric;
import io.gamedock.dto.MetricDetailsDto;
import io.gamedock.dto.MetricDto;
import io.gamedock.dto.MetricSummaryDto;
import io.gamedock.dto.PointMetricDetailsDto;
import io.gamedock.dto.PointMetricDto;
import io.gamedock.dto.SetMetricDetailsDto;
import io.gamedock.dto.SetMetricDto;
import io.gamedock.dto.StateMetricDetailsDto;
import io.gamedock.dto.StateMetricDto;
import io.gamedock.dto.mappers.MetricMapper;
import io.gamedock.dto.mappers.PointMetricMapper;
import io.gamedock.dto.mappers.SetMetricMapper;
import io.gamedock.dto.mappers.StateMetricMapper;
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
public abstract class MetricMapperDecorator implements MetricMapper {

    @Autowired
    private PointMetricMapper pointMetricMapper;

    @Autowired
    private SetMetricMapper setMetricMapper;

    @Autowired
    private StateMetricMapper stateMetricMapper;

    public MetricMapperDecorator() {
    }

    @Override
    public Score metricToScore(Metric metric) {
        if (metric instanceof PointMetric) {
            return pointMetricMapper.metricToScore((PointMetric) metric);
        } else if (metric instanceof SetMetric) {
            return setMetricMapper.metricToScore((SetMetric) metric);
        } else {
            return stateMetricMapper.metricToScore((StateMetric) metric);
        }
    }

    @Override
    public MetricSummaryDto toSummaryDto(Metric metric) {
        MetricSummaryDto dto;
        if (metric instanceof PointMetric) {
            dto = pointMetricMapper.toSummaryDto((PointMetric) metric);
        } else if (metric instanceof SetMetric) {
            dto = setMetricMapper.toSummaryDto((SetMetric) metric);
        } else {
            dto = stateMetricMapper.toSummaryDto((StateMetric) metric);
        }
        String className = metric.getClass().getSimpleName();
        try {
            dto.setHref(URI.create(linkTo(Class.forName("io.probedock.gamedock.rest." + className + "sEndpoint")).slash(metric.getId()).withSelfRel().getHref()));
        } catch (ClassNotFoundException ex) {
        }
        return dto;
    }

    @Override
    public List<MetricSummaryDto> toSummaryDto(List<Metric> metrics) {
        return metrics.stream().map(o -> toSummaryDto(o)).collect(Collectors.toList());
    }

    @Override
    public Metric metricDtoToMetric(MetricDto metricDto) {
        if (metricDto instanceof PointMetricDto) {
            return pointMetricMapper.pointMetricDtoToPointMetric((PointMetricDto) metricDto);
        } else if (metricDto instanceof SetMetricDto) {
            return setMetricMapper.setMetricDtoToSetMetric((SetMetricDto) metricDto);
        } else {
            return stateMetricMapper.stateMetricDtoToStateMetric((StateMetricDto) metricDto);
        }
    }

    @Override
    public MetricDetailsDto toDetailsDto(Metric metric, @MappingTarget MetricDetailsDto metricDto) {
        if (metric instanceof PointMetric) {
            return pointMetricMapper.toDetailsDto((PointMetric) metric, new PointMetricDetailsDto());
        } else if (metric instanceof SetMetric) {
            return setMetricMapper.toDetailsDto((SetMetric) metric, new SetMetricDetailsDto());
        } else {
            return stateMetricMapper.toDetailsDto((StateMetric) metric, new StateMetricDetailsDto());
        }
    }

    @Override
    public Metric updateMetricFromDto(MetricDto metricDto, @MappingTarget Metric metric) {
        if (metricDto instanceof PointMetricDto) {
            return pointMetricMapper.updatePointMetricFromDto((PointMetricDto) metricDto, (PointMetric) metric);
        } else if (metricDto instanceof SetMetricDto) {
            return setMetricMapper.updateSetMetricFromDto((SetMetricDto) metricDto, (SetMetric) metric);
        } else {
            return stateMetricMapper.updateStateMetricFromDto((StateMetricDto) metricDto, (StateMetric) metric);
        }
    }

}
