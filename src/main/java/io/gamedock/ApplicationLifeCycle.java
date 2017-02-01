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

package io.gamedock;

import java.util.concurrent.TimeUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.SmartLifecycle;
import org.springframework.stereotype.Component;

@Component
public class ApplicationLifeCycle implements SmartLifecycle {

    private static final Logger LOGGER = LoggerFactory.getLogger(Application.class);

    public boolean running = false;

    @Override
    public void start() {
        LOGGER.info("### START FROM THE LIFECYCLE ###");
        running = true;
    }

    @Override
    public void stop() {
        LOGGER.info("### STOP FROM THE LIFECYCLE ###");
        running = false;
    }

    @Override
    public void stop(final Runnable callback) {
        LOGGER.info("### Shudown initiated ### ");

        try {
            TimeUnit.MILLISECONDS.sleep(1);
            callback.run();
        } catch (final InterruptedException e) {
        }
        running = false;
    }

    @Override
    public int getPhase() {
        return Integer.MAX_VALUE;
    }

    @Override
    public boolean isAutoStartup() {
        return true;
    }

    @Override
    public boolean isRunning() {
        return running;
    }

}
