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

package io.gamedock.dsl.immutable.wrappers;

import io.gamedock.domain.StateMetric;
import io.gamedock.domain.StateMetricItem;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class StateMetricContext {

    private final StateMetric metric;
    private final List<StateMetricItem> items;

    public StateMetricContext(StateMetric metric) {
        this.metric = metric;
        this.items = metric.getItems();
    }

    public String getName() {
        return metric.getName();
    }

    public String getDescription() {
        return metric.getDescription();
    }

    public Set<String> getTags() {
        return Collections.unmodifiableSet(metric.getTags());
    }

    public Date getCreatedAt() {
        return metric.getCreatedAt();
    }

    public Date getUpdatedAt() {
        return metric.getUpdatedAt();
    }

    public StateMetricItemContext items(String name) {
        return items.stream().filter(i -> i.getName().equals(name)).findFirst().map(i -> new StateMetricItemContext(i)).orElse(null);
    }

    public List<StateMetricItemContext> getItems() {
        return Collections.unmodifiableList(items.stream().map(i -> new StateMetricItemContext(i)).collect(Collectors.toList()));
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof StateMetricContext) {
            StateMetricContext that = (StateMetricContext) o;
            return this.metric.equals(that.metric);
        }
        return false;
    }

}
