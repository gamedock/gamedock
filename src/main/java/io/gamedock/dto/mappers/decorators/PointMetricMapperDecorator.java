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

import io.gamedock.domain.PointMetric;
import io.gamedock.domain.PointScore;
import io.gamedock.dto.PointMetricDetailsDto;
import io.gamedock.dto.PointMetricSummaryDto;
import io.gamedock.dto.mappers.PointMetricMapper;
import io.gamedock.rest.GamesEndpoint;
import io.gamedock.rest.PointMetricsEndpoint;
import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import org.springframework.stereotype.Component;

@Component
@Primary
public abstract class PointMetricMapperDecorator implements PointMetricMapper {

    @Autowired
    private PointMetricMapper delegate;

    public PointMetricMapperDecorator() {
    }

    @Override
    public PointScore metricToScore(PointMetric pointMetric) {
        PointScore pointScore = new PointScore();
        pointScore.setMetric(pointMetric);
        pointScore.setValue(pointMetric.getConstraints().getDefault());
        return pointScore;
    }

    @Override
    public PointMetricSummaryDto toSummaryDto(PointMetric pointMetric) {
        PointMetricSummaryDto dto = delegate.toSummaryDto(pointMetric);
        dto.setParent(URI.create(linkTo(GamesEndpoint.class).slash(pointMetric.getGame()).withSelfRel().getHref()));
        dto.setHref(URI.create(linkTo(PointMetricsEndpoint.class).slash(pointMetric).withSelfRel().getHref()));
        dto.setType("point");
        return dto;
    }

    @Override
    public List<PointMetricSummaryDto> toSummaryDto(List<PointMetric> pointMetrics) {
        return pointMetrics.stream().map(o -> toSummaryDto(o)).collect(Collectors.toList());
    }

    @Override
    public PointMetricDetailsDto toDetailsDto(PointMetric pointMetric, PointMetricDetailsDto pointMetricDto) {
        PointMetricDetailsDto dto = delegate.toDetailsDto(pointMetric, pointMetricDto);
        dto.setParent(URI.create(linkTo(GamesEndpoint.class).slash(pointMetric.getGame()).withSelfRel().getHref()));
        dto.setHref(URI.create(linkTo(PointMetricsEndpoint.class).slash(pointMetric).withSelfRel().getHref()));
        return dto;
    }

}
