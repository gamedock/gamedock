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
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class EndUserDto implements Serializable {

    @NotNull(message = "This value is required.")
    @Size(max = 25, message = "This value is too long (the maximum is {max} while the actual length is ${validatedValue.length()}).")
    private String firstName;

    @NotNull(message = "This value is required.")
    @Size(max = 25, message = "This value is too long (the maximum is {max} while the actual length is ${validatedValue.length()}).")
    private String lastName;

    @NotNull(message = "This value is required.")
    @Size(max = 75, message = "This value is too long (the maximum is {max} while the actual length is ${validatedValue.length()}).")
    private String email;

    @Size(max = 255, message = "This value is too long (the maximum is {max} while the actual length is ${validatedValue.length()}).")
    private String picture;

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

}
