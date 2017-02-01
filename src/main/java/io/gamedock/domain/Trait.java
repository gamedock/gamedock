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
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.PreRemove;

@Entity
public class Trait extends AbstractDomainModelEntity {

    @Column(nullable = false)
    private String name;

    private String description;

    private String picture;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String schema;

    @ElementCollection
    @CollectionTable(name = "TRAIT_TAGS")
    private Set<String> tags = new HashSet<>();

    @ManyToOne
    private Game game;

    @OneToMany(orphanRemoval = true, cascade = CascadeType.ALL)
    private List<TraitInstance> traitInstances = new ArrayList<>();

    @PreRemove
    protected void preRemove() {
        game.removeTrait(this);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getSchema() {
        return schema;
    }

    public void setSchema(String schema) {
        this.schema = schema;
    }

    public Set<String> getTags() {
        return tags;
    }

    public void setTags(Set<String> tags) {
        this.tags = tags;
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
        this.game.addTrait(this);
    }

    public List<TraitInstance> getTraitInstances() {
        return traitInstances;
    }

    public void setTraitInstances(List<TraitInstance> traitInstances) {
        this.traitInstances = traitInstances;
    }

    public void addTraitInstance(TraitInstance traitInstance) {
        traitInstances.add(traitInstance);
    }

    public void removeTraitInstance(TraitInstance traitInstance) {
        traitInstances.remove(traitInstance);
    }

}
