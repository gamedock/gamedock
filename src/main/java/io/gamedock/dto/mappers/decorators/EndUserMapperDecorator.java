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

import io.gamedock.domain.EndUser;
import io.gamedock.dto.EndUserDetailsDto;
import io.gamedock.dto.EndUserSummaryDto;
import io.gamedock.dto.mappers.EndUserMapper;
import io.gamedock.rest.EndUsersEndpoint;
import io.gamedock.rest.OrganizationsEndpoint;
import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

@Component
@Primary
public abstract class EndUserMapperDecorator implements EndUserMapper {

    @Autowired
    private EndUserMapper delegate;

    public EndUserMapperDecorator() {
    }

    @Override
    public EndUserSummaryDto toSummaryDto(EndUser endUser) {
        EndUserSummaryDto dto = delegate.toSummaryDto(endUser);
        dto.setParent(URI.create(linkTo(OrganizationsEndpoint.class).slash(endUser.getOrganization()).withSelfRel().getHref()));
        dto.setHref(URI.create(linkTo(EndUsersEndpoint.class).slash(endUser).withSelfRel().getHref()));
        return dto;
    }

    @Override
    public List<EndUserSummaryDto> toSummaryDto(List<EndUser> endUsers) {
        return endUsers.stream().map(b -> toSummaryDto(b)).collect(Collectors.toList());
    }

    @Override
    public EndUserDetailsDto toDetailsDto(EndUser endUser, EndUserDetailsDto endUserDto) {
        EndUserDetailsDto dto = delegate.toDetailsDto(endUser, endUserDto);
        dto.setParent(URI.create(linkTo(OrganizationsEndpoint.class).slash(endUser.getOrganization()).withSelfRel().getHref()));
        dto.setHref(URI.create(linkTo(EndUsersEndpoint.class).slash(endUser).withSelfRel().getHref()));
        return dto;
    }

}
