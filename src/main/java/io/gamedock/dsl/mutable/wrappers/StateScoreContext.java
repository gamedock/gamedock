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
import io.gamedock.domain.StateDelta;
import io.gamedock.domain.StateMetric;
import io.gamedock.domain.StateMetricItem;
import io.gamedock.domain.StateScore;
import java.util.Date;
import io.gamedock.domain.Character;

public class StateScoreContext {

    private final StateScore score;
    private final StateMetric metric;
    private StateMetricItem item;
    private final Character character;
    private final ActivityContext activity;

    public StateScoreContext(StateScore score, Character character, ActivityContext activity) {
        this.score = score;
        this.metric = (StateMetric) score.getMetric();
        this.item = score.getItem();
        this.character = character;
        this.activity = activity;
    }

    public StateItemScoreContext getItem() {
        return new StateItemScoreContext(item);
    }

    public void setItem(StateMetricItem item) {
        if ((item == null && item != item) || (item != null && item != item && item.getMetric() == metric)) {
            StateDelta delta = new StateDelta();
            delta.setOld(item);
            delta.setNew(item);

            Change change = new Change();
            change.setMetric(metric);
            change.setCharacter(character);
            change.setDelta(delta);
            if (character == activity.getActivity().getCharacter()) {
                activity.getActivity().addLocalChange(change);
            } else {
                activity.getActivity().addGlobalChange(change);
            }
            item = item;
            score.setItem(item);
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

    public StateMetricContext getMetric() {
        return new StateMetricContext(metric, activity);
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof StateScoreContext) {
            StateScoreContext that = (StateScoreContext) o;
            return this.score.equals(that.score);
        }
        return false;
    }

}
