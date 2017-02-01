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

import io.gamedock.domain.SetItemScore;
import io.gamedock.domain.SetMetric;
import io.gamedock.domain.SetScore;
import io.gamedock.dto.SetMetricDetailsDto;
import io.gamedock.dto.SetMetricSummaryDto;
import io.gamedock.dto.mappers.SetMetricMapper;
import io.gamedock.rest.GamesEndpoint;
import io.gamedock.rest.SetMetricsEndpoint;
import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import org.springframework.stereotype.Component;

@Component
@Primary
public abstract class SetMetricMapperDecorator implements SetMetricMapper {

    @Autowired
    private SetMetricMapper delegate;

    public SetMetricMapperDecorator() {
    }

    @Override
    public SetScore metricToScore(SetMetric setMetric) {
        SetScore setScore = new SetScore();
        setScore.setMetric(setMetric);
        setScore.setItemScores(setMetric.getItems().stream().map(i -> {
            SetItemScore itemScore = new SetItemScore();
            itemScore.setCount(i.getConstraints().getDefault());
            itemScore.setItem(i);
            return itemScore;
        }).collect(Collectors.toList()));
        return setScore;
    }

    @Override
    public SetMetricSummaryDto toSummaryDto(SetMetric setMetric) {
        SetMetricSummaryDto dto = delegate.toSummaryDto(setMetric);
        dto.setParent(URI.create(linkTo(GamesEndpoint.class).slash(setMetric.getGame()).withSelfRel().getHref()));
        dto.setHref(URI.create(linkTo(SetMetricsEndpoint.class).slash(setMetric).withSelfRel().getHref()));
        dto.setType("set");
        return dto;
    }

    @Override
    public List<SetMetricSummaryDto> toSummaryDto(List<SetMetric> setMetrics) {
        return setMetrics.stream().map(o -> toSummaryDto(o)).collect(Collectors.toList());
    }

    @Override
    public SetMetricDetailsDto toDetailsDto(SetMetric setMetric, SetMetricDetailsDto setMetricDto) {
        SetMetricDetailsDto dto = delegate.toDetailsDto(setMetric, setMetricDto);
        dto.setParent(URI.create(linkTo(GamesEndpoint.class).slash(setMetric.getGame()).withSelfRel().getHref()));
        dto.setHref(URI.create(linkTo(SetMetricsEndpoint.class).slash(setMetric).withSelfRel().getHref()));
        return dto;
    }

}
