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
import io.gamedock.domain.SetDelta;
import io.gamedock.domain.SetItemScore;
import io.gamedock.domain.SetMetric;
import io.gamedock.domain.SetMetricItem;
import java.util.Date;
import io.gamedock.domain.Character;

public class SetItemScoreContext {

    private final SetMetric metric;
    private final SetItemScore score;
    private final SetMetricItem item;
    private final Character character;
    private final ActivityContext activity;

    public SetItemScoreContext(SetMetric metric, SetItemScore score, Character character, ActivityContext activity) {
        this.metric = metric;
        this.score = score;
        this.item = score.getItem();
        this.character = character;
        this.activity = activity;
    }

    public Long getCount() {
        return score.getCount();
    }

    public void setCount(Long count) {
        Long min = item.getConstraints().getMin();
        Long max = item.getConstraints().getMax();
        if (count != null && !count.equals(score.getCount()) && min <= count && count <= max) {
            SetDelta delta = new SetDelta();
            delta.setItem(item);
            delta.setOld(score.getCount());
            delta.setNew(count);

            Change change = new Change();
            change.setMetric(metric);
            change.setCharacter(character);
            change.setDelta(delta);
            if (character == activity.getActivity().getCharacter()) {
                activity.getActivity().addLocalChange(change);
            } else {
                activity.getActivity().addGlobalChange(change);
            }
            score.setCount(count);
        }
    }

    public String getName() {
        return item.getName();
    }

    public String getDescription() {
        return item.getDescription();
    }

    public Date getCreatedAt() {
        return score.getCreatedAt();
    }

    public Date getUpdatedAt() {
        return score.getUpdatedAt();
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof SetItemScoreContext) {
            SetItemScoreContext that = (SetItemScoreContext) o;
            return this.score.equals(that.score);
        }
        return false;
    }

}
