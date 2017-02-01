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

package io.gamedock.domain;

import java.util.Date;
import java.util.List;

public interface Character {

    Long getId();

    Date getCreatedAt();

    Date getUpdatedAt();

    List<Activity> getActivities();

    void setActivities(List<Activity> activities);

    void addActivity(Activity activity);

    void removeActivity(Activity activity);

    List<Score> getScores();

    void setScores(List<Score> scores);

    void addScore(Score score);

    default void removeScore(Score score) {
        if (score instanceof SetScore) {
            SetScore setScore = (SetScore) score;
            setScore.getItemScores().stream().forEach(i -> i.setItem(null));
            setScore.getItemScores().clear();
        }
    }

    List<TraitInstance> getTraitInstances();

    void setTraitInstances(List<TraitInstance> traits);

    void addTraitInstance(TraitInstance traitInstance);

    void removeTraitInstance(TraitInstance traitInstance);

    List<Event> getEvents();

    void setEvents(List<Event> events);

    void addEvent(Event event);

    void removeEvent(Event event);
}
