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
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.PreRemove;

@Entity
public class Organization extends AbstractDomainModelEntity {

    @Column(nullable = false)
    private String name;

    @ManyToOne
    private Application application;

    @OneToMany(orphanRemoval = true)
    private List<EndUser> endUsers = new ArrayList<>();

    @OneToMany(orphanRemoval = true)
    private List<Game> games = new ArrayList<>();

    @PreRemove
    protected void preRemove() {
        application.removeOrganization(this);
        endUsers.clear();
        games.clear();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Application getApplication() {
        return application;
    }

    public void setApplication(Application application) {
        this.application = application;
        this.application.addOrganization(this);
    }

    public List<EndUser> getEndUsers() {
        return endUsers;
    }

    public void setEndUsers(List<EndUser> endUsers) {
        this.endUsers = endUsers;
    }

    public void addEndUser(EndUser endUser) {
        endUsers.add(endUser);
    }

    public void removeEndUser(EndUser endUser) {
        endUsers.remove(endUser);
    }

    public List<Game> getGames() {
        return games;
    }

    public void setGames(List<Game> games) {
        this.games = games;
    }

    public void addGame(Game game) {
        games.add(game);
    }

    public void removeGame(Game game) {
        games.remove(game);
    }
}
