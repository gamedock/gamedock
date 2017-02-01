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
public class SetScore extends Score {

    @OneToMany(cascade = CascadeType.ALL)
    private List<SetItemScore> itemScores = new ArrayList<>();

    public List<SetItemScore> getItemScores() {
        return itemScores;
    }

    public void setItemScores(List<SetItemScore> itemScores) {
        this.itemScores = itemScores;
    }

    public void addSetItemScore(SetItemScore setItemScore) {
        itemScores.add(setItemScore);
    }

    public void removeSetItemScore(SetItemScore setItemScore) {
        itemScores.remove(setItemScore);
    }
}
