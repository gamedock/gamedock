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

import io.gamedock.domain.Event;
import io.gamedock.domain.Game;
import io.gamedock.domain.Player;
import io.gamedock.domain.Play;
import io.gamedock.domain.Team;
import io.gamedock.dto.mappers.TraitInstanceMapper;
import io.gamedock.dsl.immutable.managers.MetricsManager;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import io.gamedock.domain.Character;

public class GameContext {

    private final Game game;
    private final List<Event> events;
    private final List<Team> teams;
    private final List<Player> players;
    private final List<Character> characters;
    private final List<Play> plays;
    private final Map<String, Object> traits;

    public GameContext(Game game) {
        this.game = game;
        this.events = game.getEvents();
        this.teams = game.getTeams();
        this.players = game.getPlayers();
        this.characters = game.getCharacters();
        this.plays = game.getPlays();
        this.traits = new TraitInstanceMapper().traitToMap(game.getTraits());
    }

    public String getName() {
        return game.getName();
    }

    public String getDescription() {
        return game.getDescription();
    }

    public Set<String> getTags() {
        return Collections.unmodifiableSet(game.getTags());
    }

    public Date getCreatedAt() {
        return game.getCreatedAt();
    }

    public Date getUpdatedAt() {
        return game.getUpdatedAt();
    }

    public List<EventContext> getEvents() {
        return Collections.unmodifiableList(events.stream().map(e -> new EventContext(e)).collect(Collectors.toList()));
    }

    public List<TeamContext> getTeams() {
        return Collections.unmodifiableList(teams.stream().map(t -> new TeamContext(t)).collect(Collectors.toList()));
    }

    public List<PlayerContext> getPlayers() {
        return Collections.unmodifiableList(players.stream().map(m -> new PlayerContext(m)).collect(Collectors.toList()));
    }

    public MetricsManager getMetrics() {
        return new MetricsManager(game);
    }

    public List<CharacterContext> getCharacters() {
        return Collections.unmodifiableList(characters.stream().map(p -> new CharacterContext(p)).collect(Collectors.toList()));
    }

    public List<PlayContext> getPlays() {
        return Collections.unmodifiableList(plays.stream().map(p -> new PlayContext(p)).collect(Collectors.toList()));
    }

    public Map<String, Object> getTraits() {
        return Collections.unmodifiableMap(traits);
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof GameContext) {
            GameContext that = (GameContext) o;
            return this.game.equals(that.game);
        }
        return false;
    }

}
