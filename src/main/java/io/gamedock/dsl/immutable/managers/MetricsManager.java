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

package io.gamedock.dsl.immutable.managers;

import io.gamedock.domain.Game;
import io.gamedock.domain.Metric;
import io.gamedock.domain.PointMetric;
import io.gamedock.domain.SetMetric;
import io.gamedock.domain.StateMetric;
import io.gamedock.dsl.immutable.wrappers.PointMetricContext;
import io.gamedock.dsl.immutable.wrappers.SetMetricContext;
import io.gamedock.dsl.immutable.wrappers.StateMetricContext;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class MetricsManager {

    private final List<Metric> metrics;
    private final List<PointMetric> points;
    private final List<SetMetric> sets;
    private final List<StateMetric> states;

    public MetricsManager(Game game) {
        this.metrics = game.getMetrics();
        this.points = metrics.stream().filter(m -> m instanceof PointMetric).map(m -> (PointMetric) m).collect(Collectors.toList());
        this.sets = metrics.stream().filter(m -> m instanceof SetMetric).map(m -> (SetMetric) m).collect(Collectors.toList());
        this.states = metrics.stream().filter(m -> m instanceof StateMetric).map(m -> (StateMetric) m).collect(Collectors.toList());
    }

    public PointMetricContext points(String name) {
        return points.stream().filter(m -> m.getName().equals(name)).findFirst().map(m -> new PointMetricContext(m)).orElse(null);
    }

    public List<PointMetricContext> getPoints() {
        return Collections.unmodifiableList(points.stream().map(m -> new PointMetricContext(m)).collect(Collectors.toList()));
    }

    public SetMetricContext sets(String name) {
        return sets.stream().filter(m -> m.getName().equals(name)).findFirst().map(m -> new SetMetricContext(m)).orElse(null);
    }

    public List<SetMetricContext> getSets() {
        return Collections.unmodifiableList(sets.stream().map(m -> new SetMetricContext(m)).collect(Collectors.toList()));
    }

    public StateMetricContext states(String name) {
        return states.stream().filter(m -> m.getName().equals(name)).findFirst().map(m -> new StateMetricContext(m)).orElse(null);
    }

    public List<StateMetricContext> getStates() {
        return Collections.unmodifiableList(states.stream().map(m -> new StateMetricContext(m)).collect(Collectors.toList()));
    }

}
