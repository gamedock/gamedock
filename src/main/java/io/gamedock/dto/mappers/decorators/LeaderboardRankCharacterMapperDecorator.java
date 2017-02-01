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

package io.gamedock.dto.mappers.decorators;

import io.gamedock.domain.Player;
import io.gamedock.domain.Team;
import io.gamedock.domain.Teammate;
import io.gamedock.dto.LeaderboardRankCharacterDto;
import io.gamedock.rest.PlayersEndpoint;
import io.gamedock.rest.TeammatesEndpoint;
import io.gamedock.rest.TeamsEndpoint;
import java.net.URI;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import io.gamedock.domain.Character;
import io.gamedock.dto.mappers.LeaderboardRankCharacterMapper;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

@Component
@Primary
public abstract class LeaderboardRankCharacterMapperDecorator implements LeaderboardRankCharacterMapper {

    public LeaderboardRankCharacterMapperDecorator() {
    }

    @Override
    public LeaderboardRankCharacterDto toDto(Character character) {
        LeaderboardRankCharacterDto dto = new LeaderboardRankCharacterDto();
        if (character instanceof Player) {
            Player player = (Player) character;
            dto.setType("player");
            dto.setHref(URI.create(linkTo(PlayersEndpoint.class).slash(player).withSelfRel().getHref()));
            dto.setName(player.getEndUser().getFirstName() + " " + player.getEndUser().getLastName());
        } else if (character instanceof Teammate) {
            Teammate teammate = (Teammate) character;
            dto.setType("teammate");
            dto.setHref(URI.create(linkTo(TeammatesEndpoint.class).slash(teammate).withSelfRel().getHref()));
            dto.setName(teammate.getPlayer().getEndUser().getFirstName() + " " + teammate.getPlayer().getEndUser().getLastName());
        } else {
            Team team = (Team) character;
            dto.setType("team");
            dto.setHref(URI.create(linkTo(TeamsEndpoint.class).slash(team).withSelfRel().getHref()));
            dto.setName(team.getName());
        }
        return dto;
    }

}
