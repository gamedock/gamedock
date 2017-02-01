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

import com.fasterxml.jackson.databind.ObjectMapper;
import io.gamedock.domain.Event;
import io.gamedock.domain.Game;
import io.gamedock.domain.Play;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import io.gamedock.domain.Character;

public class EventContext {

    private final Event event;
    private final Character character;
    private final Game game;
    private final Play play;
    private final ActivityContext activity;

    public EventContext(Event event, ActivityContext activity) {
        this.event = event;
        this.character = event.getCharacter();
        this.game = event.getGame();
        this.play = event.getPlay();
        this.activity = activity;
    }

    public Date getTimestamp() {
        return event.getTimestamp();
    }

    public Date getCreatedAt() {
        return event.getCreatedAt();
    }

    public Date getUpdatedAt() {
        return event.getUpdatedAt();
    }

    public Map<String, Object> getProperties() {
        try {
            return Collections.unmodifiableMap(new ObjectMapper().readValue(event.getProperties(), HashMap.class));
        } catch (Exception ex) {
            return Collections.unmodifiableMap(new HashMap());
        }
    }

    public CharacterContext getCharacter() {
        return new CharacterContext(character, activity);
    }

    public GameContext getGame() {
        return new GameContext(game, activity);
    }

    public PlayContext getPlay() {
        return new PlayContext(play, activity);
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof EventContext) {
            EventContext that = (EventContext) o;
            return this.event.equals(that.event);
        }
        return false;
    }

}
