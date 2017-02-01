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

package io.gamedock.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class StateDeltaDto extends DeltaDto {

    @JsonProperty("old")
    private StateMetricItemDto _old;

    @JsonProperty("new")
    private StateMetricItemDto _new;

    public StateMetricItemDto getOld() {
        return _old;
    }

    public void setOld(StateMetricItemDto _old) {
        this._old = _old;
    }

    public StateMetricItemDto getNew() {
        return _new;
    }

    public void setNew(StateMetricItemDto _new) {
        this._new = _new;
    }

}
