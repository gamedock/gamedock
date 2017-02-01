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
import java.util.stream.Collectors;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.PreRemove;

@Entity
public class Game extends AbstractDomainModelEntity {

    @Column(nullable = false)
    private String name;

    private String description;

    private String picture;

    @ElementCollection
    @CollectionTable(name = "GAME_TAGS")
    private Set<String> tags = new HashSet<>();

    @ManyToOne
    private Organization organization;

    @OneToMany(orphanRemoval = true)
    private List<Player> players = new ArrayList<>();

    @OneToMany(orphanRemoval = true)
    private List<Play> plays = new ArrayList<>();

    @OneToMany(orphanRemoval = true)
    private List<Metric> metrics = new ArrayList<>();

    @OneToMany(orphanRemoval = true)
    private List<Rule> rules = new ArrayList<>();

    @OneToMany(orphanRemoval = true)
    private List<Leaderboard> leaderboards = new ArrayList<>();

    @OneToMany(orphanRemoval = true)
    private List<Event> events = new ArrayList<>();

    @OneToMany(orphanRemoval = true)
    private List<Activity> activities = new ArrayList<>();

    @OneToMany(orphanRemoval = true)
    private List<Team> teams = new ArrayList<>();

    @OneToMany(orphanRemoval = true)
    private List<Trait> traits = new ArrayList<>();

    @OneToMany(orphanRemoval = true)
    private List<Scope> scopes = new ArrayList<>();

    @PreRemove
    protected void preRemove() {
        organization.removeGame(this);
        players.clear();
        plays.clear();
        metrics.clear();
        rules.clear();
        leaderboards.clear();
        events.clear();
        activities.clear();
        teams.clear();
        traits.clear();
        scopes.clear();
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

    public Organization getOrganization() {
        return organization;
    }

    public void setOrganization(Organization organization) {
        this.organization = organization;
        this.organization.addGame(this);
    }

    public List<Player> getPlayers() {
        return players;
    }

    public void setPlayers(List<Player> players) {
        this.players = players;
    }

    public void addPlayer(Player player) {
        players.add(player);
    }

    public void removePlayer(Player player) {
        players.remove(player);
    }

    public List<Play> getPlays() {
        return plays;
    }

    public void setPlays(List<Play> plays) {
        this.plays = plays;
    }

    public void addPlay(Play play) {
        plays.add(play);
    }

    public void removePlay(Play play) {
        plays.remove(play);
    }

    public List<Metric> getMetrics() {
        return metrics;
    }

    public void setMetrics(List<Metric> metrics) {
        this.metrics = metrics;
    }

    public void addMetric(Metric metric) {
        metrics.add(metric);
    }

    public void removeMetric(Metric metric) {
        metrics.remove(metric);
    }

    public List<Rule> getRules() {
        return rules;
    }

    public void setRules(List<Rule> rules) {
        this.rules = rules;
    }

    public void addRule(Rule rule) {
        rules.add(rule);
    }

    public void removeRule(Rule rule) {
        rules.remove(rule);
    }

    public List<Leaderboard> getLeaderboards() {
        return leaderboards;
    }

    public void setLeaderboards(List<Leaderboard> leaderboards) {
        this.leaderboards = leaderboards;
    }

    public void addLeaderboard(Leaderboard leaderboard) {
        leaderboards.add(leaderboard);
    }

    public void removeLeaderboard(Leaderboard leaderboard) {
        leaderboards.remove(leaderboard);
    }

    public List<Event> getEvents() {
        return events;
    }

    public void setEvents(List<Event> events) {
        this.events = events;
    }

    public void addEvent(Event event) {
        events.add(event);
    }

    public void removeEvent(Event event) {
        events.remove(event);
    }

    public List<Activity> getActivities() {
        return activities;
    }

    public void setActivities(List<Activity> activities) {
        this.activities = activities;
    }

    public void addActivity(Activity activity) {
        activities.add(activity);
    }

    public void removeActivity(Activity activity) {
        activities.remove(activity);
    }

    public List<Team> getTeams() {
        return teams;
    }

    public void setTeams(List<Team> teams) {
        this.teams = teams;
    }

    public void addTeam(Team team) {
        teams.add(team);
    }

    public void removeTeam(Team team) {
        teams.remove(team);
    }

    public List<Trait> getTraits() {
        return traits;
    }

    public void setTraits(List<Trait> traits) {
        this.traits = traits;
    }

    public void addTrait(Trait trait) {
        traits.add(trait);
    }

    public void removeTrait(Trait trait) {
        traits.remove(trait);
    }

    public List<Scope> getScopes() {
        return scopes;
    }

    public void setScopes(List<Scope> scopes) {
        this.scopes = scopes;
    }

    public void addScope(Scope scope) {
        scopes.add(scope);
    }

    public void removeScope(Scope scope) {
        scopes.remove(scope);
    }

    public List<Character> getCharacters() {
        List<Character> characters = new ArrayList<>();
        characters.addAll(players);
        characters.addAll(teams);
        characters.addAll(teams.stream().flatMap(t -> t.getTeammates().stream()).collect(Collectors.toList()));
        return characters;
    }
}
