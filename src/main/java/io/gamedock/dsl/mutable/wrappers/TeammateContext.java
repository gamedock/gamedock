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
import io.gamedock.domain.Player;
import io.gamedock.domain.Team;
import io.gamedock.domain.Teammate;
import io.gamedock.dsl.mutable.managers.ScoresManager;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class TeammateContext {

    protected final Teammate teammate;
    private final Player player;
    private final Team team;
    private final List<Event> events;
    private final Map<String, Object> traits;
    protected final ActivityContext activity;

    public TeammateContext(Teammate teammate, ActivityContext activity) {
        this.teammate = teammate;
        this.player = teammate.getPlayer();
        this.team = teammate.getTeam();
        this.events = teammate.getEvents();
        this.traits = new HashMap<>();
        teammate.getTraitInstances().stream().forEach(t -> {
            traits.put(t.getTrait().getName(), t.getCache());
        });
        this.activity = activity;
    }

    public Date getCreatedAt() {
        return teammate.getCreatedAt();
    }

    public Date getUpdatedAt() {
        return teammate.getUpdatedAt();
    }

    public ScoresManager getScores() {
        return new ScoresManager(teammate, activity);
    }

    public PlayerContext getPlayer() {
        return new PlayerContext(player, activity);
    }

    public TeamContext getTeam() {
        return new TeamContext(team, activity);
    }

    public List<EventContext> getEvents() {
        return Collections.unmodifiableList(events.stream().map(e -> new EventContext(e, activity)).collect(Collectors.toList()));
    }

    public Map<String, Object> getTraits() {
        return traits;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof TeammateContext) {
            TeammateContext that = (TeammateContext) o;
            return this.teammate.equals(that.teammate);
        }
        return false;
    }

}
