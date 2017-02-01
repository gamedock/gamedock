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

import io.gamedock.domain.EndUser;
import io.gamedock.domain.Event;
import io.gamedock.domain.Game;
import io.gamedock.domain.Player;
import io.gamedock.domain.Teammate;
import io.gamedock.dto.mappers.TraitInstanceMapper;
import io.gamedock.dsl.immutable.managers.ScoresManager;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class PlayerContext {

    protected final Player player;
    private final EndUser endUser;
    private final Game game;
    private final List<Event> events;
    private final List<Teammate> teammates;
    private final Map<String, Object> traits;

    public PlayerContext(Player player) {
        this.player = player;
        this.endUser = player.getEndUser();
        this.game = player.getGame();
        this.events = player.getEvents();
        this.teammates = player.getTeammates();
        this.traits = new TraitInstanceMapper().traitInstanceToMap(player.getTraitInstances());
    }

    public Date getCreatedAt() {
        return player.getCreatedAt();
    }

    public Date getUpdatedAt() {
        return player.getUpdatedAt();
    }

    public EndUserContext getUser() {
        return new EndUserContext(endUser);
    }

    public GameContext getGame() {
        return new GameContext(game);
    }

    public List<EventContext> getEvents() {
        return Collections.unmodifiableList(events.stream().map(e -> new EventContext(e)).collect(Collectors.toList()));
    }

    public List<TeammateContext> getTeammates() {
        return Collections.unmodifiableList(teammates.stream().map(m -> new TeammateContext(m)).collect(Collectors.toList()));
    }

    public ScoresManager getScores() {
        return new ScoresManager(player);
    }

    public Map<String, Object> getTraits() {
        return Collections.unmodifiableMap(traits);
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof PlayerContext) {
            PlayerContext that = (PlayerContext) o;
            return this.player.equals(that.player);
        }
        return false;
    }

}
