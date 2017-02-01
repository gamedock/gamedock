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

package io.gamedock.services.repositories;

import io.gamedock.domain.Player;
import io.gamedock.domain.Team;
import io.gamedock.domain.Teammate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import io.gamedock.domain.Character;

@Service
public class CharacterRepository {

    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private TeammateRepository teammateRepository;

    @Autowired
    private TeamRepository teamRepository;

    public CharacterRepository() {
    }

    public Character findByCharacter_IdAndCharacter_TypeAndGame_Id(Long id, String type, Long gameId) {
        Character character;
        if (null != type) {
            switch (type) {
                case "player":
                    character = playerRepository.findOne(id);
                    if (character == null) {
                        return null;
                    } else if (!((Player) character).getGame().getId().equals(gameId)) {
                        return null;
                    } else {
                        return character;
                    }
                case "teammate":
                    character = teammateRepository.findOne(id);
                    if (character == null) {
                        return null;
                    } else if (!((Teammate) character).getTeam().getGame().getId().equals(gameId)) {
                        return null;
                    } else {
                        return character;
                    }
                case "team":
                    character = teamRepository.findOne(id);
                    if (character == null) {
                        return null;
                    } else if (!((Team) character).getGame().getId().equals(gameId)) {
                        return null;
                    } else {
                        return character;
                    }
                default:
                    return null;
            }
        } else {
            return null;
        }
    }

}
