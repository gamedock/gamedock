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

import io.gamedock.domain.PointMetric;
import io.gamedock.domain.PointScore;
import java.util.Date;

public class PointScoreContext {

    private final PointScore score;
    private final PointMetric metic;

    public PointScoreContext(PointScore score) {
        this.score = score;
        this.metic = (PointMetric) score.getMetric();
    }

    public Long getValue() {
        return score.getValue();
    }

    public String getName() {
        return metic.getName();
    }

    public String getDescription() {
        return metic.getDescription();
    }

    public Date getCreatedAt() {
        return score.getCreatedAt();
    }

    public Date getUpdatedAt() {
        return score.getUpdatedAt();
    }

    public PointMetricContext getMetric() {
        return new PointMetricContext(metic);
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof PointScoreContext) {
            PointScoreContext that = (PointScoreContext) o;
            return this.score.equals(that.score);
        }
        return false;
    }

}
