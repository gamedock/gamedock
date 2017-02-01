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

package io.gamedock.dto;

import java.net.URI;
import java.util.List;
import java.util.Set;

public class GameDetailsDto extends AbstractSummaryDto {

    private URI parent;

    private String name;

    private String description;

    private String picture;

    private Set<String> tags;

    private List<PlaySummaryDto> plays;

    private List<RuleSummaryDto> rules;

    private List<PlayerSummaryDto> players;

    private List<MetricSummaryDto> metrics;

    private List<TeamSummaryDto> teams;

    private List<LeaderboardSummaryDto> leaderboards;

    private List<ScopeSummaryDto> scopes;

    private List<TraitSummaryDto> traits;

    public URI getParent() {
        return parent;
    }

    public void setParent(URI parent) {
        this.parent = parent;
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

    public List<PlaySummaryDto> getPlays() {
        return plays;
    }

    public void setPlays(List<PlaySummaryDto> plays) {
        this.plays = plays;
    }

    public List<RuleSummaryDto> getRules() {
        return rules;
    }

    public void setRules(List<RuleSummaryDto> rules) {
        this.rules = rules;
    }

    public List<PlayerSummaryDto> getPlayers() {
        return players;
    }

    public void setPlayers(List<PlayerSummaryDto> players) {
        this.players = players;
    }

    public List<MetricSummaryDto> getMetrics() {
        return metrics;
    }

    public void setMetrics(List<MetricSummaryDto> metrics) {
        this.metrics = metrics;
    }

    public List<TeamSummaryDto> getTeams() {
        return teams;
    }

    public void setTeams(List<TeamSummaryDto> teams) {
        this.teams = teams;
    }

    public List<LeaderboardSummaryDto> getLeaderboards() {
        return leaderboards;
    }

    public void setLeaderboards(List<LeaderboardSummaryDto> leaderboards) {
        this.leaderboards = leaderboards;
    }

    public List<ScopeSummaryDto> getScopes() {
        return scopes;
    }

    public void setScopes(List<ScopeSummaryDto> scopes) {
        this.scopes = scopes;
    }

    public List<TraitSummaryDto> getTraits() {
        return traits;
    }

    public void setTraits(List<TraitSummaryDto> traits) {
        this.traits = traits;
    }

}
