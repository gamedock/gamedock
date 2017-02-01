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

import io.gamedock.domain.PointScore;
import io.gamedock.domain.Score;
import io.gamedock.domain.SetScore;
import io.gamedock.domain.StateScore;
import io.gamedock.dto.ScoreDto;
import io.gamedock.dto.mappers.PointScoreMapper;
import io.gamedock.dto.mappers.ScoreMapper;
import io.gamedock.dto.mappers.SetScoreMapper;
import io.gamedock.dto.mappers.StateScoreMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

@Component
@Primary
public abstract class ScoreMapperDecorator implements ScoreMapper {

    @Autowired
    private PointScoreMapper pointScoreMapper;

    @Autowired
    private SetScoreMapper setScoreMapper;

    @Autowired
    private StateScoreMapper stateScoreMapper;

    public ScoreMapperDecorator() {
    }

    @Override
    public ScoreDto toDto(Score score) {
        if (score instanceof PointScore) {
            return pointScoreMapper.toDto((PointScore) score);
        } else if (score instanceof SetScore) {
            return setScoreMapper.toDto((SetScore) score);
        } else {
            return stateScoreMapper.toDto((StateScore) score);
        }
    }

}
