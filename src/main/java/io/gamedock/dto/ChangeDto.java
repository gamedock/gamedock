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

import java.io.Serializable;

public class ChangeDto implements Serializable {

    private CharacterDto character;

    private MetricSummaryDto metric;

    private DeltaDto delta;

    public CharacterDto getCharacter() {
        return character;
    }

    public void setCharacter(CharacterDto character) {
        this.character = character;
    }

    public MetricSummaryDto getMetric() {
        return metric;
    }

    public void setMetric(MetricSummaryDto metric) {
        this.metric = metric;
    }

    public DeltaDto getDelta() {
        return delta;
    }

    public void setDelta(DeltaDto delta) {
        this.delta = delta;
    }

}
