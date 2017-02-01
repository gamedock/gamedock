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

package io.gamedock.domain;

import javax.persistence.Entity;

@Entity
public class PointDelta extends Delta {

    private Long _old;

    private Long _new;

    public Long getOld() {
        return _old;
    }

    public void setOld(Long _old) {
        this._old = _old;
    }

    public Long getNew() {
        return _new;
    }

    public void setNew(Long _new) {
        this._new = _new;
    }

}
