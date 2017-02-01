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

package io.gamedock.dto.constraints.validators;

import javax.script.Compilable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import io.gamedock.dto.constraints.CompilableJavaScript;

public class CompilableJavaScriptValidator implements ConstraintValidator<CompilableJavaScript, String> {

    @Override
    public void initialize(CompilableJavaScript a) {

    }

    @Override
    public boolean isValid(String js, ConstraintValidatorContext context) {
        if (js == null) {
            return true;
        }
        try {
            ScriptEngine engine = new ScriptEngineManager().getEngineByName("nashorn");
            Compilable compilable = (Compilable) engine;
            compilable.compile(js);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

}
