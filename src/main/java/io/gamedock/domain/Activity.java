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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.PreRemove;
import org.hibernate.annotations.Any;
import org.hibernate.annotations.AnyMetaDef;
import org.hibernate.annotations.MetaValue;

@Entity
public class Activity extends AbstractDomainModelEntity {

    private Date timestamp;

    @ManyToOne
    private Game game;

    @ManyToOne
    private Play play;

    @Any(metaColumn = @Column(name = "CHARACTER_TYPE"))
    @AnyMetaDef(idType = "long", metaType = "string", metaValues = {
        @MetaValue(targetEntity = Player.class, value = "PLAYER"),
        @MetaValue(targetEntity = Teammate.class, value = "TEAMMATE"),
        @MetaValue(targetEntity = Team.class, value = "TEAM")
    })
    @JoinColumn(name = "CHARACTER_ID", nullable = true)
    private Character character;

    @OneToMany(cascade = CascadeType.ALL)
    private List<Change> locals = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL)
    private List<Change> globals = new ArrayList<>();

    @PreRemove
    protected void preRemove() {
        game.removeActivity(this);
        play.removeActivity(this);
        character.removeActivity(this);
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
        this.game.addActivity(this);
    }

    public Play getPlay() {
        return play;
    }

    public void setPlay(Play play) {
        this.play = play;
        this.play.addActivity(this);
    }

    public Character getCharacter() {
        return character;
    }

    public void setCharacter(Character character) {
        this.character = character;
        if (character != null) {
            this.character.addActivity(this);
        }
    }

    public List<Change> getLocals() {
        return locals;
    }

    public void setLocals(List<Change> locals) {
        this.locals = locals;
    }

    public void addLocalChange(Change change) {
        locals.add(change);
    }

    public void removeLocalChange(Change change) {
        locals.remove(change);
    }

    public List<Change> getGlobals() {
        return globals;
    }

    public void setGlobals(List<Change> globals) {
        this.globals = globals;
    }

    public void addGlobalChange(Change change) {
        globals.add(change);
    }

    public void removeGlobalChange(Change change) {
        globals.remove(change);
    }
}
