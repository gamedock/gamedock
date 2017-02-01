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

import io.gamedock.domain.SetScore;
import io.gamedock.dto.SetItemScoreDto;
import io.gamedock.dto.SetScoreDto;
import io.gamedock.dto.mappers.SetMetricItemMapper;
import io.gamedock.dto.mappers.SetScoreMapper;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

@Component
@Primary
public abstract class SetScoreMapperDecorator implements SetScoreMapper {

    @Autowired
    private SetScoreMapper delegate;

    @Autowired
    private SetMetricItemMapper setMetricItemMapper;

    public SetScoreMapperDecorator() {
    }

    @Override
    public SetScoreDto toDto(SetScore score) {
        SetScoreDto dto = delegate.toDto(score);
        dto.setItems(score.getItemScores().stream().map(i -> {
            SetItemScoreDto setItemScore = new SetItemScoreDto();
            setItemScore.setItem(setMetricItemMapper.toDto(i.getItem()));
            setItemScore.setCount(i.getCount());
            return setItemScore;
        }).collect(Collectors.toList()));
        return dto;
    }

}
