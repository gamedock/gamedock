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

import io.gamedock.domain.StateMetric;
import io.gamedock.domain.StateScore;
import io.gamedock.dto.StateMetricDetailsDto;
import io.gamedock.dto.StateMetricSummaryDto;
import io.gamedock.dto.mappers.StateMetricMapper;
import io.gamedock.rest.GamesEndpoint;
import io.gamedock.rest.StateMetricsEndpoint;
import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import org.springframework.stereotype.Component;

@Component
@Primary
public abstract class StateMetricMapperDecorator implements StateMetricMapper {

    @Autowired
    private StateMetricMapper delegate;

    public StateMetricMapperDecorator() {
    }

    @Override
    public StateScore metricToScore(StateMetric stateMetric) {
        StateScore stateScore = new StateScore();
        stateScore.setMetric(stateMetric);
        stateScore.setItem(null);
        return stateScore;
    }

    @Override
    public StateMetricSummaryDto toSummaryDto(StateMetric stateMetric) {
        StateMetricSummaryDto dto = delegate.toSummaryDto(stateMetric);
        dto.setParent(URI.create(linkTo(GamesEndpoint.class).slash(stateMetric.getGame()).withSelfRel().getHref()));
        dto.setHref(URI.create(linkTo(StateMetricsEndpoint.class).slash(stateMetric).withSelfRel().getHref()));
        dto.setType("state");
        return dto;
    }

    @Override
    public List<StateMetricSummaryDto> toSummaryDto(List<StateMetric> stateMetrics) {
        return stateMetrics.stream().map(o -> toSummaryDto(o)).collect(Collectors.toList());
    }

    @Override
    public StateMetricDetailsDto toDetailsDto(StateMetric stateMetric, StateMetricDetailsDto stateMetricDto) {
        StateMetricDetailsDto dto = delegate.toDetailsDto(stateMetric, stateMetricDto);
        dto.setParent(URI.create(linkTo(GamesEndpoint.class).slash(stateMetric.getGame()).withSelfRel().getHref()));
        dto.setHref(URI.create(linkTo(StateMetricsEndpoint.class).slash(stateMetric).withSelfRel().getHref()));
        return dto;
    }

}
