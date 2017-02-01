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

import io.gamedock.domain.Delta;
import io.gamedock.domain.PointDelta;
import io.gamedock.domain.SetDelta;
import io.gamedock.domain.StateDelta;
import io.gamedock.dto.DeltaDto;
import io.gamedock.dto.mappers.DeltaMapper;
import io.gamedock.dto.mappers.PointDeltaMapper;
import io.gamedock.dto.mappers.SetDeltaMapper;
import io.gamedock.dto.mappers.StateDeltaMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

@Component
@Primary
public abstract class DeltaMapperDecorator implements DeltaMapper {

    @Autowired
    private PointDeltaMapper pointDeltaMapper;

    @Autowired
    private SetDeltaMapper setDeltaMapper;

    @Autowired
    private StateDeltaMapper stateDeltaMapper;

    public DeltaMapperDecorator() {
    }

    @Override
    public DeltaDto toDto(Delta delta) {
        if (delta instanceof PointDelta) {
            return pointDeltaMapper.toDto((PointDelta) delta);
        } else if (delta instanceof SetDelta) {
            return setDeltaMapper.toDto((SetDelta) delta);
        } else {
            return stateDeltaMapper.toDto((StateDelta) delta);
        }
    }

}
