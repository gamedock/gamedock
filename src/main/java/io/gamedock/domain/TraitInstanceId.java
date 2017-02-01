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

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import org.hibernate.annotations.Any;
import org.hibernate.annotations.AnyMetaDef;
import org.hibernate.annotations.MetaValue;

@Embeddable
public class TraitInstanceId implements Serializable {

    private Character character;

    private Trait trait;

    @Any(metaColumn = @Column(name = "CHARACTER_TYPE"))
    @AnyMetaDef(idType = "long", metaType = "string", metaValues = {
        @MetaValue(targetEntity = Player.class, value = "PLAYER"),
        @MetaValue(targetEntity = Teammate.class, value = "TEAMMATE"),
        @MetaValue(targetEntity = Team.class, value = "TEAM")
    })
    @JoinColumn(name = "CHARACTER_ID")
    public Character getCharacter() {
        return character;
    }

    public void setCharacter(Character character) {
        this.character = character;
    }

    @ManyToOne
    public Trait getTrait() {
        return trait;
    }

    public void setTrait(Trait trait) {
        this.trait = trait;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        TraitInstanceId that = (TraitInstanceId) o;

        if (trait != null ? !trait.equals(that.trait) : that.trait != null) {
            return false;
        }
        if (character != null ? !character.equals(that.character) : that.character != null) {
            return false;
        }

        return true;
    }

    public int hashCode() {
        int result;
        result = (trait != null ? trait.hashCode() : 0);
        result = 31 * result + (character != null ? character.hashCode() : 0);
        return result;
    }

}
