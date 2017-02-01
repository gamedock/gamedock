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

import io.gamedock.domain.SetItemScore;
import io.gamedock.domain.SetMetric;
import io.gamedock.domain.SetScore;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import io.gamedock.domain.Character;

public class SetScoreContext {

    private final SetScore score;
    private final SetMetric metric;
    private final List<SetItemScore> itemScores;
    private final Character character;
    private final ActivityContext activity;

    public SetScoreContext(SetScore score, Character character, ActivityContext activity) {
        this.score = score;
        this.metric = (SetMetric) score.getMetric();
        this.itemScores = score.getItemScores();
        this.character = character;
        this.activity = activity;
    }

    public SetItemScoreContext items(String name) {
        return itemScores.stream().filter(i -> i.getItem().getName().equals(name)).findFirst().map(i -> new SetItemScoreContext(metric, i, character, activity)).orElse(null);
    }

    public List<SetItemScoreContext> getItems() {
        return Collections.unmodifiableList(itemScores.stream().map(i -> new SetItemScoreContext(metric, i, character, activity)).collect(Collectors.toList()));
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

    public SetMetricContext getMetric() {
        return new SetMetricContext(metric, activity);
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof SetScoreContext) {
            SetScoreContext that = (SetScoreContext) o;
            return this.score.equals(that.score);
        }
        return false;
    }

}
