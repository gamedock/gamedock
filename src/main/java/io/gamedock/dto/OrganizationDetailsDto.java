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

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public class OrganizationDetailsDto extends OrganizationSummaryDto {

    @JsonProperty("endusers")
    private List<EndUserSummaryDto> endUsers;

    private List<GameSummaryDto> games;

    public List<EndUserSummaryDto> getEndUsers() {
        return endUsers;
    }

    public void setEndUsers(List<EndUserSummaryDto> endUsers) {
        this.endUsers = endUsers;
    }

    public List<GameSummaryDto> getGames() {
        return games;
    }

    public void setGames(List<GameSummaryDto> games) {
        this.games = games;
    }

}
