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

import io.gamedock.domain.Organization;
import io.gamedock.dto.OrganizationDetailsDto;
import io.gamedock.dto.OrganizationSummaryDto;
import io.gamedock.dto.mappers.OrganizationMapper;
import io.gamedock.rest.ApplicationsEndpoint;
import io.gamedock.rest.OrganizationsEndpoint;
import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import org.springframework.stereotype.Component;

@Component
@Primary
public abstract class OrganizationMapperDecorator implements OrganizationMapper {

    @Autowired
    private OrganizationMapper delegate;

    public OrganizationMapperDecorator() {
    }

    @Override
    public OrganizationSummaryDto toSummaryDto(Organization organization) {
        OrganizationSummaryDto dto = delegate.toSummaryDto(organization);
        dto.setParent(URI.create(linkTo(ApplicationsEndpoint.class).slash(organization.getApplication()).withSelfRel().getHref()));
        dto.setHref(URI.create(linkTo(OrganizationsEndpoint.class).slash(organization).withSelfRel().getHref()));
        return dto;
    }

    @Override
    public List<OrganizationSummaryDto> toSummaryDto(List<Organization> organizations) {
        return organizations.stream().map(o -> toSummaryDto(o)).collect(Collectors.toList());
    }

    @Override
    public OrganizationDetailsDto toDetailsDto(Organization organization, OrganizationDetailsDto organizationDto) {
        OrganizationDetailsDto dto = delegate.toDetailsDto(organization, organizationDto);
        dto.setParent(URI.create(linkTo(ApplicationsEndpoint.class).slash(organization.getApplication()).withSelfRel().getHref()));
        dto.setHref(URI.create(linkTo(OrganizationsEndpoint.class).slash(organization).withSelfRel().getHref()));
        return dto;
    }

}
