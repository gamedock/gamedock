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

import io.gamedock.domain.Player;
import io.gamedock.domain.Team;
import io.gamedock.domain.Teammate;
import io.gamedock.domain.Character;

public class ContextCast {

    public EndUserContext user(CharacterContext context) {
        if (context.character instanceof Player) {
            return new EndUserContext(((Player) context.character).getEndUser());
        } else if (context.character instanceof Teammate) {
            return new EndUserContext(((Teammate) context.character).getPlayer().getEndUser());
        } else {
            return null;
        }
    }

    public PlayerContext player(CharacterContext context) {
        if (context.character instanceof Player) {
            return new PlayerContext((Player) context.character);
        } else {
            return null;
        }
    }

    public TeammateContext teammate(CharacterContext context) {
        if (context.character instanceof Teammate) {
            return new TeammateContext((Teammate) context.character);
        } else {
            return null;
        }
    }

    public TeamContext team(CharacterContext context) {
        if (context.character instanceof Team) {
            return new TeamContext((Team) context.character);
        } else {
            return null;
        }
    }

    public CharacterContext character(Object o) {
        if (o instanceof PlayerContext) {
            return new CharacterContext((Character) ((PlayerContext) o).player);
        } else if (o instanceof TeammateContext) {
            return new CharacterContext((Character) ((TeammateContext) o).teammate);
        } else if (o instanceof TeamContext) {
            return new CharacterContext((Character) ((TeamContext) o).team);
        } else if (o instanceof CharacterContext) {
            return (CharacterContext) o;
        } else {
            return null;
        }
    }

}
