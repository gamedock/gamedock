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

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import javax.persistence.AssociationOverride;
import javax.persistence.AssociationOverrides;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.PreRemove;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "TRAIT_INSTANCE")
@AssociationOverrides({
    @AssociationOverride(name = "pk.trait", joinColumns = @JoinColumn(name = "TRAIT_ID")),
    @AssociationOverride(name = "pk.character", joinColumns = @JoinColumn(name = "CHARACTER_ID"))
})
public class TraitInstance implements Serializable {

    private TraitInstanceId pk = new TraitInstanceId();

    private String properties;

    private boolean dirty;

    private Map<String, Object> cache;

    @PreRemove
    protected void preRemove() {
        getPk().getCharacter().removeTraitInstance(this);
        getPk().getTrait().removeTraitInstance(this);
    }

    @PreUpdate
    protected void preUpdate() {
        if (cache != null) {
            try {
                properties = new ObjectMapper().writeValueAsString(cache);
            } catch (JsonProcessingException ex) {
            }
        }
    }

    @EmbeddedId
    public TraitInstanceId getPk() {
        return pk;
    }

    public void setPk(TraitInstanceId pk) {
        this.pk = pk;
    }

    public String getProperties() {
        return properties;
    }

    public void setProperties(String properties) {
        this.properties = properties;
    }

    public boolean getDirty() {
        return dirty;
    }

    public void setDirty(boolean dirty) {
        this.dirty = dirty;
    }

    @Transient
    public Character getCharacter() {
        return getPk().getCharacter();
    }

    public void setCharacter(Character character) {
        getPk().setCharacter(character);
        character.addTraitInstance(this);
    }

    @Transient
    public Trait getTrait() {
        return getPk().getTrait();
    }

    public void setTrait(Trait trait) {
        getPk().setTrait(trait);
        trait.addTraitInstance(this);
    }

    @Transient
    public Map<String, Object> getCache() {
        if (cache == null) {
            try {
                cache = new ObjectMapper().readValue(properties, HashMap.class);
                dirty = !dirty;
            } catch (IOException ex) {
            }
        }
        return cache;
    }

    public void setCache(Map<String, Object> cache) {
        this.cache = cache;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        TraitInstance that = (TraitInstance) o;
        if (getPk() != null ? !getPk().equals(that.getPk()) : that.getPk() != null) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        return (getPk() != null ? getPk().hashCode() : 0);
    }
}
