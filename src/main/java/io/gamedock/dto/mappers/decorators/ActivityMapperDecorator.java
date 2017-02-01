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

import io.gamedock.domain.Activity;
import io.gamedock.dto.ActivityDto;
import io.gamedock.dto.ChangesDto;
import io.gamedock.dto.mappers.ActivityMapper;
import io.gamedock.dto.mappers.ChangeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

@Component
@Primary
public abstract class ActivityMapperDecorator implements ActivityMapper {

    @Autowired
    private ActivityMapper delegate;

    @Autowired
    private ChangeMapper changeMapper;

    public ActivityMapperDecorator() {
    }

    @Override
    public ActivityDto toDto(Activity activity) {
        ActivityDto dto = delegate.toDto(activity);
        ChangesDto changes = new ChangesDto();
        changes.setLocals(changeMapper.toDto(activity.getLocals()));
        changes.setGlobals(changeMapper.toDto(activity.getGlobals()));
        dto.setChanges(changes);
        return dto;
    }

}
