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
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PreRemove;
import org.hibernate.annotations.Any;
import org.hibernate.annotations.AnyMetaDef;
import org.hibernate.annotations.MetaValue;

@Entity
public class Event extends AbstractDomainModelEntity {

    private Date timestamp;

    @Column(columnDefinition = "TEXT")
    private String properties;

    @ManyToOne
    private Game game;

    @Any(metaColumn = @Column(name = "CHARACTER_TYPE"))
    @AnyMetaDef(idType = "long", metaType = "string", metaValues = {
        @MetaValue(targetEntity = Player.class, value = "PLAYER"),
        @MetaValue(targetEntity = Teammate.class, value = "TEAMMATE"),
        @MetaValue(targetEntity = Team.class, value = "TEAM")
    })
    @JoinColumn(name = "CHARACTER_ID", nullable = true)
    private Character character;

    @ManyToOne
    private Play play;

    @PreRemove
    protected void preRemove() {
        game.removeEvent(this);
        character.removeEvent(this);
        play.removeEvent(this);
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public String getProperties() {
        return properties;
    }

    public void setProperties(String properties) {
        this.properties = properties;
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
        this.game.addEvent(this);
    }

    public Character getCharacter() {
        return character;
    }

    public void setCharacter(Character character) {
        this.character = character;
        this.character.addEvent(this);
    }

    public Play getPlay() {
        return play;
    }

    public void setPlay(Play play) {
        this.play = play;
        this.play.addEvent(this);
    }
}
