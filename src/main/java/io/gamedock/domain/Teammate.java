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
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.PreRemove;

@Entity
public class Teammate extends AbstractDomainModelEntity implements Character {

    @ManyToOne
    private Player player;

    @ManyToOne
    private Team team;

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
        team.removeTeammate(this);
        player.removeTeammate(this);
        scores.clear();
        traitInstances.clear();
        events.clear();
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
        this.player.addTeammate(this);
    }

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
        this.team.addTeammate(this);
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
