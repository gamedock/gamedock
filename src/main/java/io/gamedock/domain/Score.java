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

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PreRemove;
import org.hibernate.annotations.Any;
import org.hibernate.annotations.AnyMetaDef;
import org.hibernate.annotations.MetaValue;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public class Score extends AbstractDomainModelEntity {

    @ManyToOne
    private Metric metric;

    @Any(metaColumn = @Column(name = "CHARACTER_TYPE"))
    @AnyMetaDef(idType = "long", metaType = "string", metaValues = {
        @MetaValue(targetEntity = Player.class, value = "PLAYER"),
        @MetaValue(targetEntity = Teammate.class, value = "TEAMMATE"),
        @MetaValue(targetEntity = Team.class, value = "TEAM")
    })
    @JoinColumn(name = "CHARACTER_ID", nullable = true)
    private Character character;

    @PreRemove
    protected void preRemove() {
        metric.removeScore(this);
        character.removeScore(this);
    }

    public Metric getMetric() {
        return metric;
    }

    public void setMetric(Metric metric) {
        this.metric = metric;
        if (metric != null) {
            this.metric.addScore(this);
        }
    }

    public Character getCharacter() {
        return character;
    }

    public void setCharacter(Character character) {
        this.character = character;
        if (character != null) {
            this.character.addScore(this);
        }
    }
}
