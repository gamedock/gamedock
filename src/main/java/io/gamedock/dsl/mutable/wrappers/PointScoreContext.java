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

import io.gamedock.domain.Change;
import io.gamedock.domain.PointDelta;
import io.gamedock.domain.PointMetric;
import io.gamedock.domain.PointScore;
import java.util.Date;
import io.gamedock.domain.Character;

public class PointScoreContext {

    private final PointScore score;
    private final PointMetric metric;
    private final Character character;
    private final ActivityContext activity;

    public PointScoreContext(PointScore score, Character character, ActivityContext activity) {
        this.score = score;
        this.metric = (PointMetric) score.getMetric();
        this.character = character;
        this.activity = activity;
    }

    public Long getValue() {
        return score.getValue();
    }

    public void setValue(Long value) {
        Long min = metric.getConstraints().getMin();
        Long max = metric.getConstraints().getMax();
        if (value != null && !value.equals(score.getValue()) && min <= value && value <= max) {
            PointDelta delta = new PointDelta();
            delta.setOld(score.getValue());
            delta.setNew(value);

            Change change = new Change();
            change.setMetric(metric);
            change.setDelta(delta);
            change.setCharacter(character);

            if (character == activity.getActivity().getCharacter()) {
                activity.getActivity().addLocalChange(change);
            } else {
                activity.getActivity().addGlobalChange(change);
            }
            score.setValue(value);
        }
    }

    public String getName() {
        return metric.getName();
    }

    public String getDescription() {
        return metric.getDescription();
    }

    public Date getCreatedAt() {
        return score.getCreatedAt();
    }

    public Date getUpdatedAt() {
        return score.getUpdatedAt();
    }

    public PointMetricContext getMetric() {
        return new PointMetricContext(metric, activity);
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
