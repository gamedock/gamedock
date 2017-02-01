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
import io.gamedock.dto.mappers.TraitInstanceMapper;
import io.gamedock.dsl.immutable.managers.ScoresManager;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import io.gamedock.domain.Character;

public class CharacterContext {

    protected final Character character;
    private final List<Event> events;
    private final Map<String, Object> traits;

    public CharacterContext(Character character) {
        this.character = character;
        this.events = character.getEvents();
        this.traits = new TraitInstanceMapper().traitInstanceToMap(character.getTraitInstances());
    }

    public Date getCreatedAt() {
        return character.getCreatedAt();
    }

    public Date getUpdatedAt() {
        return character.getUpdatedAt();
    }

    public List<EventContext> getEvents() {
        return Collections.unmodifiableList(events.stream().map(e -> new EventContext(e)).collect(Collectors.toList()));
    }

    public ScoresManager getScores() {
        return new ScoresManager(character);
    }

    public Map<String, Object> getTraits() {
        return Collections.unmodifiableMap(traits);
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof CharacterContext) {
            CharacterContext that = (CharacterContext) o;
            return this.character.equals(that.character);
        }
        return false;
    }

}
