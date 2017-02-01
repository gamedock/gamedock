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

public class LeaderboardRankDto implements Serializable {

    private LeaderboardRankCharacterDto character;

    private Double score;

    private Map<String, Object> captured;

    private Long rank;

    public LeaderboardRankCharacterDto getCharacter() {
        return character;
    }

    public void setCharacter(LeaderboardRankCharacterDto character) {
        this.character = character;
    }

    public Double getScore() {
        return score;
    }

    public void setScore(Double score) {
        this.score = score;
    }

    public Map<String, Object> getCaptured() {
        return captured;
    }

    public void setCaptured(Map<String, Object> captured) {
        this.captured = captured;
    }

    public Long getRank() {
        return rank;
    }

    public void setRank(Long rank) {
        this.rank = rank;
    }

}
