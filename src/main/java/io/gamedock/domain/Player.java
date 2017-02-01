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
public class Player extends AbstractDomainModelEntity implements Character {

    @ManyToOne
    private EndUser endUser;

    @ManyToOne
    private Game game;

    @OneToMany(orphanRemoval = true)
    private List<Teammate> teammates = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL)
    private List<Activity> activities = new ArrayList<>();

    @OneToMany(orphanRemoval = true)
    private List<Score> scores = new ArrayList<>();

    @OneToMany(orphanRemoval = true)
    private List<Event> events = new ArrayList<>();

    @OneToMany(orphanRemoval = true, cascade = CascadeType.ALL)
    private List<TraitInstance> traitInstances = new ArrayList<>();

    @PreRemove
    protected void preRemove() {
        endUser.removePlayer(this);
        game.removePlayer(this);
        teammates.clear();
        activities.clear();
        scores.clear();
        events.clear();
        traitInstances.clear();
    }

    public EndUser getEndUser() {
        return endUser;
    }

    public void setEndUser(EndUser endUser) {
        this.endUser = endUser;
        this.endUser.addPlayer(this);
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
        this.game.addPlayer(this);
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
}
