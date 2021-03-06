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
import javax.persistence.OneToMany;
import javax.persistence.PreRemove;

@Entity
public class Application extends AbstractDomainModelEntity {

    @Column(nullable = false)
    private String name;

    @OneToMany(orphanRemoval = true)
    private List<Organization> organizations = new ArrayList<>();

    @PreRemove
    protected void preRemove() {
        organizations.clear();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Organization> getOrganizations() {
        return organizations;
    }

    public void setOrganizations(List<Organization> organizations) {
        this.organizations = organizations;
    }

    public void addOrganization(Organization organization) {
        organizations.add(organization);
    }

    public void removeOrganization(Organization organization) {
        organizations.remove(organization);
    }
}
