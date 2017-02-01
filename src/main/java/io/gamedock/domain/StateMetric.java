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

import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;

@Entity
public class StateMetric extends Metric {

    @OneToMany(cascade = CascadeType.ALL)
    private List<StateMetricItem> items = new ArrayList<>();

    public List<StateMetricItem> getItems() {
        return items;
    }

    public void setItems(List<StateMetricItem> items) {
        if (items != null) {
            items.stream().forEach((item) -> {
                item.setMetric(this);
            });
        }
        this.items = items;
    }

    public void addItem(StateMetricItem item) {
        items.add(item);
    }

    public void removeItem(StateMetricItem item) {
        items.remove(item);
    }
}
