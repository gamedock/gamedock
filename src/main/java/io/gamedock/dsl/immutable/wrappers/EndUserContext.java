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
import io.gamedock.domain.Player;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class EndUserContext {

    protected final EndUser endUser;
    private final List<Player> players;

    public EndUserContext(EndUser endUser) {
        this.endUser = endUser;
        this.players = endUser.getPlayers();
    }

    public String getFirstName() {
        return endUser.getFirstName();
    }

    public String getLastName() {
        return endUser.getLastName();
    }

    public String getEmail() {
        return endUser.getEmail();
    }

    public Date getCreatedAt() {
        return endUser.getCreatedAt();
    }

    public Date getUpdatedAt() {
        return endUser.getUpdatedAt();
    }

    public List<PlayerContext> getPlayers() {
        return Collections.unmodifiableList(players.stream().map(m -> new PlayerContext(m)).collect(Collectors.toList()));
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof EndUserContext) {
            EndUserContext that = (EndUserContext) o;
            return this.endUser.equals(that.endUser);
        }
        return false;
    }

}
