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
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class SetMetricItemDto implements Serializable {

    @Size(max = 25, message = "This value is too long (the maximum is {max} while the actual length is ${validatedValue.length()}).")
    @NotNull(message = "This value is required.")
    private String name;

    @Size(max = 255, message = "This value is too long (the maximum is {max} while the actual length is ${validatedValue.length()}).")
    @NotNull(message = "This value is required.")
    private String description;

    @Size(max = 255, message = "This value is too long (the maximum is {max} while the actual length is ${validatedValue.length()}).")
    private String picture;

    @NotNull(message = "This value is required.")
    @Valid
    private SetMetricItemConstraintsDto constraints;

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

    public SetMetricItemConstraintsDto getConstraints() {
        return constraints;
    }

    public void setConstraints(SetMetricItemConstraintsDto constraints) {
        this.constraints = constraints;
    }

}
