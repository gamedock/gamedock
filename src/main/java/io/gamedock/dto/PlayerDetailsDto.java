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
import java.util.Map;

public class PlayerDetailsDto extends AbstractSummaryDto {

    @JsonProperty("enduser")
    private EndUserSummaryDto endUser;

    private GameSummaryDto game;

    private List<TeammateSummaryDto> teammates;

    private List<ScoreDto> scores;

    private Map<String, Object> traits;

    public EndUserSummaryDto getEndUser() {
        return endUser;
    }

    public void setEndUser(EndUserSummaryDto endUser) {
        this.endUser = endUser;
    }

    public GameSummaryDto getGame() {
        return game;
    }

    public void setGame(GameSummaryDto game) {
        this.game = game;
    }

    public List<TeammateSummaryDto> getTeammates() {
        return teammates;
    }

    public void setTeammates(List<TeammateSummaryDto> teammates) {
        this.teammates = teammates;
    }

    public List<ScoreDto> getScores() {
        return scores;
    }

    public void setScores(List<ScoreDto> scores) {
        this.scores = scores;
    }

    public Map<String, Object> getTraits() {
        return traits;
    }

    public void setTraits(Map<String, Object> traits) {
        this.traits = traits;
    }

}
