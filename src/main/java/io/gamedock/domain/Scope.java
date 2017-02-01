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
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.PreRemove;

@Entity
public class Scope extends AbstractDomainModelEntity {

    @Column(nullable = false)
    private String name;

    private String description;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String predicate;

    @ElementCollection
    @CollectionTable(name = "SCOPE_TAGS")
    private Set<String> tags = new HashSet<>();

    @ManyToOne
    private Game game;

    @ManyToMany
    private Set<Trait> traits = new HashSet<>();

    @OneToMany(orphanRemoval = true)
    private List<Metric> metrics = new ArrayList<>();

    @OneToMany(orphanRemoval = true)
    private List<Leaderboard> leaderboards = new ArrayList<>();

    @PreRemove
    protected void preRemove() {
        game.removeScope(this);
        metrics.clear();
        leaderboards.clear();
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

    public String getPredicate() {
        return predicate;
    }

    public void setPredicate(String predicate) {
        this.predicate = predicate;
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
        this.game.addScope(this);
    }

    public Set<Trait> getTraits() {
        return traits;
    }

    public void setTraits(Set<Trait> traits) {
        this.traits = traits;
    }

    public void addTrait(Trait trait) {
        if (!traits.contains(trait)) {
            traits.add(trait);
        }
    }

    public void removeTrait(Trait trait) {
        traits.remove(trait);
    }

    public List<Metric> getMetrics() {
        return metrics;
    }

    public void setMetrics(List<Metric> metrics) {
        this.metrics = metrics;
    }

    public void addMetric(Metric metric) {
        if (!metrics.contains(metric)) {
            metrics.add(metric);
        }
    }

    public void removeMetric(Metric metric) {
        metrics.remove(metric);
    }

    public List<Leaderboard> getLeaderboards() {
        return leaderboards;
    }

    public void setLeaderboards(List<Leaderboard> leaderboards) {
        this.leaderboards = leaderboards;
    }

    public void addLeaderboard(Leaderboard leaderboard) {
        if (!leaderboards.contains(leaderboard)) {
            leaderboards.add(leaderboard);
        }
    }

    public void removeLeaderbord(Leaderboard leaderboard) {
        leaderboards.remove(leaderboard);
    }

}
