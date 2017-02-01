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

package io.gamedock.dsl.mutable.wrappers;

import io.gamedock.domain.PointMetric;
import io.gamedock.domain.PointMetricConstraints;
import java.util.Collections;
import java.util.Date;
import java.util.Set;

public class PointMetricContext {

    private final PointMetric metric;
    private final PointMetricConstraints constraints;
    private final ActivityContext activity;

    public PointMetricContext(PointMetric metric, ActivityContext activity) {
        this.metric = metric;
        this.constraints = metric.getConstraints();
        this.activity = activity;
    }

    public String getName() {
        return metric.getName();
    }

    public String getDescription() {
        return metric.getDescription();
    }

    public PointMetricConstraintsContext getConstraints() {
        return new PointMetricConstraintsContext(constraints);
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

    @Override
    public boolean equals(Object o) {
        if (o instanceof PointMetricContext) {
            PointMetricContext that = (PointMetricContext) o;
            return this.metric.equals(that.metric);
        }
        return false;
    }

}
