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

package io.gamedock.dto.mappers.decorators;

import io.gamedock.domain.Player;
import io.gamedock.domain.Team;
import io.gamedock.domain.Teammate;
import io.gamedock.dto.CharacterDto;
import io.gamedock.dto.mappers.TeamMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import io.gamedock.domain.Character;
import io.gamedock.dto.mappers.TeammateMapper;
import io.gamedock.dto.mappers.CharacterMapper;
import io.gamedock.dto.mappers.PlayerMapper;

@Component
@Primary
public abstract class CharacterMapperDecorator implements CharacterMapper {

    @Autowired
    private PlayerMapper characterMapper;

    @Autowired
    private TeammateMapper teammateMapper;

    @Autowired
    private TeamMapper teamMapper;

    public CharacterMapperDecorator() {
    }

    @Override
    public CharacterDto toDto(Character character) {
        if (character instanceof Player) {
            return characterMapper.toSummaryDto((Player) character);
        } else if (character instanceof Teammate) {
            return teammateMapper.toSummaryDto((Teammate) character);
        } else {
            return teamMapper.toSummaryDto((Team) character);
        }
    }

}
