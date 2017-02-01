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

import java.util.HashSet;
import java.util.Set;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.PreRemove;
import org.hibernate.annotations.Type;

@Entity
public class Rule extends AbstractDomainModelEntity {

    @Column(nullable = false)
    private String name;

    private String description;

    @ElementCollection
    @CollectionTable(name = "RULE_TAGS")
    private Set<String> tags = new HashSet<>();

    @ManyToOne
    private Game game;

    @ElementCollection
    @CollectionTable
    @Type(type = "text")
    private Set<String> conditions = new HashSet<>();

    @ElementCollection
    @CollectionTable
    @Type(type = "text")
    private Set<String> actions = new HashSet<>();

    @PreRemove
    protected void preRemove() {
        game.removeRule(this);
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
        this.game.addRule(this);
    }

    public Set<String> getConditions() {
        return conditions;
    }

    public void setConditions(Set<String> conditions) {
        this.conditions = conditions;
    }

    public void addCondition(String condition) {
        conditions.add(condition);
    }

    public void removeCondition(String condition) {
        conditions.remove(condition);
    }

    public Set<String> getActions() {
        return actions;
    }

    public void setActions(Set<String> actions) {
        this.actions = actions;
    }

    public void addAction(String action) {
        actions.add(action);
    }

    public void removeAction(String action) {
        actions.remove(action);
    }
}
