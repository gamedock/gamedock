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

import io.gamedock.domain.PointScore;
import io.gamedock.domain.Score;
import io.gamedock.domain.SetScore;
import io.gamedock.domain.StateScore;
import io.gamedock.dsl.immutable.wrappers.PointScoreContext;
import io.gamedock.dsl.immutable.wrappers.SetScoreContext;
import io.gamedock.dsl.immutable.wrappers.StateScoreContext;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import io.gamedock.domain.Character;

public class ScoresManager {

    private final List<Score> scores;
    private final List<PointScore> points;
    private final List<SetScore> sets;
    private final List<StateScore> states;

    public ScoresManager(Character player) {
        this.scores = player.getScores();
        this.points = scores.stream().filter(s -> s instanceof PointScore).map(s -> (PointScore) s).collect(Collectors.toList());
        this.sets = scores.stream().filter(s -> s instanceof SetScore).map(s -> (SetScore) s).collect(Collectors.toList());
        this.states = scores.stream().filter(s -> s instanceof StateScore).map(s -> (StateScore) s).collect(Collectors.toList());
    }

    public PointScoreContext points(String name) {
        return points.stream().filter(s -> s.getMetric().getName().equals(name)).findFirst().map(s -> new PointScoreContext(s)).orElse(null);
    }

    public List<PointScoreContext> getPoints() {
        return Collections.unmodifiableList(points.stream().map(s -> new PointScoreContext(s)).collect(Collectors.toList()));
    }

    public SetScoreContext sets(String name) {
        return sets.stream().filter(s -> s.getMetric().getName().equals(name)).findFirst().map(s -> new SetScoreContext(s)).orElse(null);
    }

    public List<SetScoreContext> getSets() {
        return Collections.unmodifiableList(sets.stream().map(s -> new SetScoreContext(s)).collect(Collectors.toList()));
    }

    public StateScoreContext states(String name) {
        return states.stream().filter(s -> s.getMetric().getName().equals(name)).findFirst().map(s -> new StateScoreContext(s)).orElse(null);
    }

    public List<StateScoreContext> getStates() {
        return Collections.unmodifiableList(states.stream().map(s -> new StateScoreContext(s)).collect(Collectors.toList()));
    }

}
