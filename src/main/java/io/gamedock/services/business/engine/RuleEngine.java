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

package io.gamedock.services.business.engine;

import io.gamedock.domain.Activity;
import io.gamedock.domain.Character;
import io.gamedock.domain.Event;
import io.gamedock.domain.Game;
import io.gamedock.domain.Play;
import io.gamedock.domain.Rule;
import java.util.Set;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import jdk.nashorn.api.scripting.JSObject;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class RuleEngine {

    public Activity process(Event event) {
        /* Retrieve event context */
        Play play = event.getPlay();
        Game game = event.getGame();
        Character character = event.getCharacter();

        /* Setup event activity */
        Activity activity = new Activity();
        activity.setCharacter(character);
        activity.setGame(game);
        activity.setPlay(play);
        activity.setTimestamp(event.getTimestamp());

        /* Retrieve play rules */
        Set<Rule> rules = play.getRules();
        rules.stream().filter((rule) -> (test(event, rule))).forEach((rule) -> {
            apply(event, rule, activity);
        });

        return activity;
    }

    private Boolean test(Event event, Rule rule) {
        /* Retrieve event context */
        Game game = event.getGame();
        Character character = event.getCharacter();

        /* Retrieve rule conditions */
        Set<String> conditions = rule.getConditions();

        /* Setup context variables */
        io.gamedock.dsl.immutable.wrappers.EventContext eventContext = new io.gamedock.dsl.immutable.wrappers.EventContext(event);
        io.gamedock.dsl.immutable.wrappers.CharacterContext characterContext = new io.gamedock.dsl.immutable.wrappers.CharacterContext(character);
        io.gamedock.dsl.immutable.wrappers.GameContext gameContext = new io.gamedock.dsl.immutable.wrappers.GameContext(game);
        io.gamedock.dsl.immutable.wrappers.ContextCast cast = new io.gamedock.dsl.immutable.wrappers.ContextCast();

        /* Setup Nashorn engine */
        ScriptEngine engine = new ScriptEngineManager().getEngineByName("nashorn");

        /* Bind context variables */
        engine.put("$event", eventContext);
        engine.put("$self", characterContext);
        engine.put("$game", gameContext);
        engine.put("$cast", cast);

        /* Evaluate rules conditions */
        Boolean evaluation = conditions.stream().allMatch(condition -> {
            JSObject function;
            Object result;
            try {
                function = (JSObject) engine.eval(condition);
                result = function.call(null);
            } catch (ScriptException ex) {
                return false;
            }
            return result instanceof Boolean && (Boolean) result == true;
        });

        return evaluation;
    }

    private void apply(Event event, Rule rule, Activity activity) {
        /* Retrieve event context */
        Game game = event.getGame();
        Character character = event.getCharacter();

        /* Retrieve rule actions */
        Set<String> actions = rule.getActions();

        /* Setup activity context */
        io.gamedock.dsl.mutable.wrappers.ActivityContext activityContext = new io.gamedock.dsl.mutable.wrappers.ActivityContext();
        activityContext.setActivity(activity);

        /* Setup context variables */
        io.gamedock.dsl.mutable.wrappers.EventContext eventContext = new io.gamedock.dsl.mutable.wrappers.EventContext(event, activityContext);
        io.gamedock.dsl.mutable.wrappers.CharacterContext characterContext = new io.gamedock.dsl.mutable.wrappers.CharacterContext(character, activityContext);
        io.gamedock.dsl.mutable.wrappers.GameContext gameContext = new io.gamedock.dsl.mutable.wrappers.GameContext(game, activityContext);
        io.gamedock.dsl.mutable.wrappers.ContextCast cast = new io.gamedock.dsl.mutable.wrappers.ContextCast();

        /* Setup Nashorn engine */
        ScriptEngine engine = new ScriptEngineManager().getEngineByName("nashorn");

        /* Bind context variables */
        engine.put("$event", eventContext);
        engine.put("$self", characterContext);
        engine.put("$game", gameContext);
        engine.put("$cast", cast);

        /* Apply rules actions */
        actions.stream().allMatch(action -> {
            try {
                JSObject function = (JSObject) engine.eval(action);
                function.call(null);
            } catch (ScriptException ex) {
                return false;
            }
            return true;
        });
    }

}
