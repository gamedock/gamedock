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

import io.gamedock.domain.Player;
import io.gamedock.domain.Team;
import io.gamedock.domain.Teammate;
import io.gamedock.domain.Character;

public class ContextCast {

    public EndUserContext user(CharacterContext context) {
        if (context.character instanceof Player) {
            return new EndUserContext(((Player) context.character).getEndUser(), context.activity);
        } else if (context.character instanceof Teammate) {
            return new EndUserContext(((Teammate) context.character).getPlayer().getEndUser(), context.activity);
        } else {
            return null;
        }
    }

    public PlayerContext player(CharacterContext context) {
        if (context.character instanceof Player) {
            return new PlayerContext((Player) context.character, context.activity);
        } else {
            return null;
        }
    }

    public TeammateContext teammate(CharacterContext context) {
        if (context.character instanceof Teammate) {
            return new TeammateContext((Teammate) context.character, context.activity);
        } else {
            return null;
        }
    }

    public TeamContext team(CharacterContext context) {
        if (context.character instanceof Team) {
            return new TeamContext((Team) context.character, context.activity);
        } else {
            return null;
        }
    }

    public CharacterContext character(Object o) {
        if (o instanceof PlayerContext) {
            PlayerContext context = (PlayerContext) o;
            return new CharacterContext((Character) context.player, context.activity);
        } else if (o instanceof TeammateContext) {
            TeammateContext context = (TeammateContext) o;
            return new CharacterContext((Character) context.teammate, context.activity);
        } else if (o instanceof TeamContext) {
            TeamContext context = (TeamContext) o;
            return new CharacterContext((Character) context.team, context.activity);
        } else if (o instanceof CharacterContext) {
            return (CharacterContext) o;
        } else {
            return null;
        }
    }

}
