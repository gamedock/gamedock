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

import io.gamedock.domain.Scope;
import io.gamedock.dsl.immutable.wrappers.ContextCast;
import io.gamedock.dsl.immutable.wrappers.CharacterContext;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import jdk.nashorn.api.scripting.JSObject;
import org.springframework.stereotype.Service;
import io.gamedock.domain.Character;

@Service
public class Scopeizer {

    public boolean apply(Scope scope, Character character) {
        String predicate = scope.getPredicate();

        CharacterContext characterContext = new CharacterContext(character);
        ContextCast cast = new ContextCast();

        ScriptEngine engine = new ScriptEngineManager().getEngineByName("nashorn");
        engine.put("$self", characterContext);
        engine.put("$cast", cast);

        Object result;
        try {
            JSObject function = (JSObject) engine.eval(predicate);
            result = function.call(null);
        } catch (ScriptException ex) {
            return false;
        }
        return result instanceof Boolean && (Boolean) result == true;
    }

}
