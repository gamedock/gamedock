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
import io.gamedock.domain.Play;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class PlayContext {

    private final Play play;
    private final Game game;
    private final List<Event> events;
    private final ActivityContext activity;

    public PlayContext(Play play, ActivityContext activity) {
        this.play = play;
        this.game = play.getGame();
        this.events = play.getEvents();
        this.activity = activity;
    }

    public String getName() {
        return play.getName();
    }

    public String getDescription() {
        return play.getDescription();
    }

    public Set<String> getTags() {
        return Collections.unmodifiableSet(play.getTags());
    }

    public Date getCreatedAt() {
        return play.getCreatedAt();
    }

    public Date getUpdatedAt() {
        return play.getUpdatedAt();
    }

    public GameContext getGame() {
        return new GameContext(game, activity);
    }

    public List<EventContext> getEvents() {
        return Collections.unmodifiableList(events.stream().map(e -> new EventContext(e, activity)).collect(Collectors.toList()));
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof PlayContext) {
            PlayContext that = (PlayContext) o;
            return this.play.equals(that.play);
        }
        return false;
    }

}
