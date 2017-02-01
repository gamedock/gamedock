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

import java.io.Serializable;
import java.util.Map;
import java.util.Set;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class TraitDto implements Serializable {

    @NotNull(message = "This value is required.")
    @Size(max = 25, message = "This value is too long (the maximum is {max} while the actual length is ${validatedValue.length()}).")
    private String name;

    @NotNull(message = "This value is required.")
    @Size(max = 255, message = "This value is too long (the maximum is {max} while the actual length is ${validatedValue.length()}).")
    private String description;

    @Size(max = 255, message = "This value is too long (the maximum is {max} while the actual length is ${validatedValue.length()}).")
    private String picture;

    @NotNull(message = "This value is required.")
    private Map<String, Object> properties;

    private Set<String> tags;

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getPicture() {
        return picture;
    }

    public Map<String, Object> getProperties() {
        return properties;
    }

    public Set<String> getTags() {
        return tags;
    }
}
