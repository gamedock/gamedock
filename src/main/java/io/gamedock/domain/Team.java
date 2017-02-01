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
public class Team extends AbstractDomainModelEntity implements Character {

    @Column(nullable = false)
    private String name;

    private String description;

    private String picture;

    @ElementCollection
    @CollectionTable(name = "TEAM_TAGS")
    private Set<String> tags = new HashSet<>();

    @ManyToOne
    private Game game;

    @OneToMany(orphanRemoval = true)
    private List<Teammate> teammates = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL)
    private List<Activity> activities = new ArrayList<>();

    @OneToMany(orphanRemoval = true)
    private List<Score> scores = new ArrayList<>();

    @OneToMany(orphanRemoval = true, cascade = CascadeType.ALL)
    private List<TraitInstance> traitInstances = new ArrayList<>();

    @OneToMany(orphanRemoval = true)
    private List<Event> events = new ArrayList<>();

    @PreRemove
    protected void preRemove() {
        game.removeTeam(this);
        teammates.clear();
        scores.clear();
        traitInstances.clear();
        events.clear();
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
        this.game.addTeam(this);
    }

    public List<Teammate> getTeammates() {
        return teammates;
    }

    public void setTeammates(List<Teammate> teammates) {
        this.teammates = teammates;
    }

    public void addTeammate(Teammate teammate) {
        teammates.add(teammate);
    }

    public void removeTeammate(Teammate teammate) {
        teammates.remove(teammate);
    }

    @Override
    public List<Activity> getActivities() {
        return activities;
    }

    @Override
    public void setActivities(List<Activity> activities) {
        this.activities = activities;
    }

    @Override
    public void addActivity(Activity activity) {
        activities.add(activity);
    }

    @Override
    public void removeActivity(Activity activity) {
        activities.remove(activity);
    }

    @Override
    public List<Score> getScores() {
        return scores;
    }

    @Override
    public void setScores(List<Score> scores) {
        this.scores = scores;
    }

    @Override
    public void addScore(Score score) {
        scores.add(score);
    }

    @Override
    public void removeScore(Score score) {
        Character.super.removeScore(score);
        scores.remove(score);
    }

    @Override
    public List<TraitInstance> getTraitInstances() {
        return traitInstances;
    }

    @Override
    public void setTraitInstances(List<TraitInstance> traitInstances) {
        this.traitInstances = traitInstances;
    }

    @Override
    public void addTraitInstance(TraitInstance traitInstance) {
        traitInstances.add(traitInstance);
    }

    @Override
    public void removeTraitInstance(TraitInstance traitInstance) {
        traitInstances.remove(traitInstance);
    }

    @Override
    public List<Event> getEvents() {
        return events;
    }

    @Override
    public void setEvents(List<Event> events) {
        this.events = events;
    }

    @Override
    public void addEvent(Event event) {
        events.add(event);
    }

    @Override
    public void removeEvent(Event event) {
        events.remove(event);
    }

}
