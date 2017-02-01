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

import io.gamedock.dto.constraints.CompilableJavaScript;
import java.io.Serializable;
import java.util.Set;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class ScopeDto implements Serializable {

    @NotNull(message = "This value is required.")
    @Size(max = 25, message = "This value is too long (the maximum is {max} while the actual length is ${validatedValue.length()}).")
    private String name;

    @NotNull(message = "This value is required.")
    @Size(max = 255, message = "This value is too long (the maximum is {max} while the actual length is ${validatedValue.length()}).")
    private String description;

    @NotNull(message = "This value is required.")
    private Set<Long> traits;

    @NotNull(message = "This value is required.")
    @CompilableJavaScript
    private String predicate;

    private Set<String> tags;

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

    public Set<Long> getTraits() {
        return traits;
    }

    public void setTraits(Set<Long> traits) {
        this.traits = traits;
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

}
