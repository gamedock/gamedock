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

import java.util.List;
import java.util.Map;

public class TeammateDetailsDto extends AbstractSummaryDto {

    private TeamSummaryDto team;

    private PlayerSummaryDto player;

    private List<ScoreDto> scores;

    private Map<String, Object> traits;

    public TeamSummaryDto getTeam() {
        return team;
    }

    public void setTeam(TeamSummaryDto team) {
        this.team = team;
    }

    public PlayerSummaryDto getPlayer() {
        return player;
    }

    public void setPlayer(PlayerSummaryDto player) {
        this.player = player;
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
