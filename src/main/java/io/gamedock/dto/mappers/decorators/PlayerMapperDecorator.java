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
import io.gamedock.dto.PlayerDetailsDto;
import io.gamedock.dto.PlayerDto;
import io.gamedock.dto.PlayerSummaryDto;
import io.gamedock.dto.mappers.TraitInstanceMapper;
import io.gamedock.rest.PlayersEndpoint;
import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;
import org.mapstruct.MappingTarget;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import io.gamedock.dto.mappers.PlayerMapper;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

@Component
@Primary
public abstract class PlayerMapperDecorator implements PlayerMapper {

    @Autowired
    private PlayerMapper delegate;

    @Autowired
    private TraitInstanceMapper traitInstanceMapper;

    public PlayerMapperDecorator() {
    }

    @Override
    public Player playerDtoToPlayer(PlayerDto playerDto) {
        Player player = delegate.playerDtoToPlayer(playerDto);
        player.setTraitInstances(traitInstanceMapper.traitInstanceDtoToTraitInstance(playerDto.getTraits(), player));
        return player;
    }

    @Override
    public PlayerSummaryDto toSummaryDto(Player player) {
        PlayerSummaryDto dto = delegate.toSummaryDto(player);
        dto.setHref(URI.create(linkTo(PlayersEndpoint.class).slash(player).withSelfRel().getHref()));
        return dto;
    }

    @Override
    public List<PlayerSummaryDto> toSummaryDto(List<Player> players) {
        return players.stream().map(o -> toSummaryDto(o)).collect(Collectors.toList());
    }

    @Override
    public PlayerDetailsDto toDetailsDto(Player player, PlayerDetailsDto playerDto) {
        PlayerDetailsDto dto = delegate.toDetailsDto(player, playerDto);
        dto.setHref(URI.create(linkTo(PlayersEndpoint.class).slash(player).withSelfRel().getHref()));
        dto.setTraits(traitInstanceMapper.traitInstanceToMap(player.getTraitInstances()));
        return dto;
    }

    @Override
    public Player updatePlayerFromDto(PlayerDto playerDto, @MappingTarget Player player) {
        return delegate.updatePlayerFromDto(playerDto, player);
    }

}
