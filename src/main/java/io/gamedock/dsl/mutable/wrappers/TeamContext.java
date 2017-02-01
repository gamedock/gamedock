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

import io.gamedock.domain.Event;
import io.gamedock.domain.Game;
import io.gamedock.domain.Team;
import io.gamedock.domain.Teammate;
import io.gamedock.dsl.mutable.managers.ScoresManager;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class TeamContext {

    protected final Team team;
    private final Game game;
    private final List<Teammate> teammates;
    private final List<Event> events;
    private final Map<String, Object> traits;
    protected final ActivityContext activity;

    public TeamContext(Team team, ActivityContext activity) {
        this.team = team;
        this.game = team.getGame();
        this.teammates = team.getTeammates();
        this.events = team.getEvents();
        this.traits = new HashMap<>();
        team.getTraitInstances().stream().forEach(t -> {
            traits.put(t.getTrait().getName(), t.getCache());
        });
        this.activity = activity;
    }

    public String getName() {
        return team.getName();
    }

    public String getDescription() {
        return team.getDescription();
    }

    public Set<String> getTags() {
        return Collections.unmodifiableSet(team.getTags());
    }

    public Date getCreatedAt() {
        return team.getCreatedAt();
    }

    public Date getUpdatedAt() {
        return team.getUpdatedAt();
    }

    public GameContext getGame() {
        return new GameContext(game, activity);
    }

    public List<TeammateContext> getTeammates() {
        return Collections.unmodifiableList(teammates.stream().map(m -> new TeammateContext(m, activity)).collect(Collectors.toList()));
    }

    public List<EventContext> getEvents() {
        return Collections.unmodifiableList(events.stream().map(e -> new EventContext(e, activity)).collect(Collectors.toList()));
    }

    public ScoresManager getScores() {
        return new ScoresManager(team, activity);
    }

    public Map<String, Object> getTraits() {
        return traits;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof TeamContext) {
            TeamContext that = (TeamContext) o;
            return this.team.equals(that.team);
        }
        return false;
    }

}
