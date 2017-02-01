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

import io.gamedock.domain.StateMetricItem;
import java.util.Date;

public class StateMetricItemContext {

    private final StateMetricItem item;

    public StateMetricItemContext(StateMetricItem item) {
        this.item = item;
    }

    public String getName() {
        return item.getName();
    }

    public String getDescription() {
        return item.getDescription();
    }

    public Date getCreatedAt() {
        return item.getCreatedAt();
    }

    public Date getUpdatedAt() {
        return item.getUpdatedAt();
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof StateMetricItemContext) {
            StateMetricItemContext that = (StateMetricItemContext) o;
            return this.item.equals(that.item);
        }
        return false;
    }

}
