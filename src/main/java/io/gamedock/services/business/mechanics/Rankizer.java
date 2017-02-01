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

package io.gamedock.services.business.mechanics;

import io.gamedock.domain.Game;
import io.gamedock.domain.Leaderboard;
import io.gamedock.domain.Scope;
import io.gamedock.domain.Team;
import io.gamedock.dto.LeaderboardDetailsDto;
import io.gamedock.dto.LeaderboardRankDto;
import io.gamedock.dto.LeaderboardRankCharacterDto;
import io.gamedock.dto.mappers.LeaderboardMapper;
import io.gamedock.dsl.immutable.wrappers.ContextCast;
import io.gamedock.dsl.immutable.wrappers.GameContext;
import io.gamedock.dsl.immutable.wrappers.CharacterContext;
import io.gamedock.dsl.immutable.wrappers.TeamContext;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import jdk.nashorn.api.scripting.JSObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import io.gamedock.domain.Character;
import io.gamedock.dto.mappers.LeaderboardRankCharacterMapper;

@Service
public class Rankizer {

    private final LeaderboardMapper leaderboardMapper;

    private final Scopeizer scopeizer;

    private final LeaderboardRankCharacterMapper rankCharacterMapper;

    @Autowired
    public Rankizer(LeaderboardMapper leaderboardMapper, Scopeizer scopeizer, LeaderboardRankCharacterMapper rankCharacterMapper) {
        this.leaderboardMapper = leaderboardMapper;
        this.scopeizer = scopeizer;
        this.rankCharacterMapper = rankCharacterMapper;
    }

    public LeaderboardDetailsDto processLeaderboard(Leaderboard leaderboard) {
        /* Retrieve leaderboard context */
        Game game = leaderboard.getGame();
        Scope scope = leaderboard.getScope();
        Team team = leaderboard.getTeam();

        /* Retrieve formula and capture functions */
        String formula = leaderboard.getFormula();
        String capture = leaderboard.getCapture();

        /* Retrieve considered characters in the leaderboard computation */
        List<Character> characters;
        if (team == null) {
            characters = game.getCharacters().stream().filter(c -> scopeizer.apply(scope, c)).collect(Collectors.toList());
        } else {
            characters = team.getTeammates().stream().filter(m -> scopeizer.apply(scope, m)).collect(Collectors.toList());
        }

        /* Setup context variable */
        GameContext gameContext = new GameContext(game);
        ContextCast cast = new ContextCast();

        /* Setup Nashorn engine */
        ScriptEngine engine = new ScriptEngineManager().getEngineByName("nashorn");

        /* Bind context variables */
        engine.put("$game", gameContext);
        engine.put("$cast", cast);
        if (team != null) {
            TeamContext teamContext = new TeamContext(team);
            engine.put("$team", teamContext);
        }

        /* Setup formula and capture functions */
        JSObject valuator;
        JSObject capturer;
        try {
            valuator = (JSObject) engine.eval(formula);
            capturer = (JSObject) engine.eval(capture);
        } catch (ScriptException ex) {
            Logger.getLogger(Rankizer.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }

        List<LeaderboardRankDto> ranking = characters.stream().map(character -> {
            /* Setup character context variable */
            CharacterContext characterContext = new CharacterContext(character);
            engine.put("$self", characterContext);

            /* Evaluating formula */
            Object o = valuator.call(null);
            Double score;
            if (o instanceof Double) {
                score = (Double) o;
            } else if (o instanceof Long) {
                score = ((Long) o).doubleValue();
            } else {
                score = 0.0;
            }

            /* Evaluating capture */
            Map<String, Object> captured = (Map<String, Object>) capturer.call(null);

            /* Generating rank entry */
            LeaderboardRankDto leaderbordRank = new LeaderboardRankDto();
            LeaderboardRankCharacterDto characterDto = rankCharacterMapper.toDto(character);
            leaderbordRank.setCharacter(characterDto);
            leaderbordRank.setScore(score);
            leaderbordRank.setCaptured(captured);
            return leaderbordRank;
        })
                .sorted((LeaderboardRankDto r1, LeaderboardRankDto r2) -> Double.compare(r2.getScore(), r1.getScore()))
                .collect(Collectors.toList());

        /* Ranking entries according to the computed characters scores */
        Long rank = 0L;
        LeaderboardRankDto previous = null;
        for (LeaderboardRankDto r : ranking) {
            if (previous == null || r.getScore().compareTo(previous.getScore()) != 0) {
                rank++;
            }
            r.setRank(rank);
            previous = r;
        }

        LeaderboardDetailsDto dto = leaderboardMapper.toDetailsDto(leaderboard, new LeaderboardDetailsDto());
        dto.setRanking(ranking);
        return dto;
    }

}
