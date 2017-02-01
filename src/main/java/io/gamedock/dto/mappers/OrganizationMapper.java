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

package io.gamedock.dto.mappers;

import io.gamedock.domain.Organization;
import io.gamedock.dto.OrganizationDetailsDto;
import io.gamedock.dto.OrganizationDto;
import io.gamedock.dto.OrganizationSummaryDto;
import io.gamedock.dto.mappers.decorators.OrganizationMapperDecorator;
import java.util.List;
import org.mapstruct.DecoratedWith;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring", uses = {EndUserMapper.class, GameMapper.class})
@DecoratedWith(OrganizationMapperDecorator.class)
public interface OrganizationMapper {

    Organization organizationDtoToOrganization(OrganizationDto organizationDto);

    OrganizationSummaryDto toSummaryDto(Organization organization);

    List<OrganizationSummaryDto> toSummaryDto(List<Organization> organizations);

    OrganizationDetailsDto toDetailsDto(Organization organization, @MappingTarget OrganizationDetailsDto organizationDto);

    Organization updateOrganizationFromDto(OrganizationDto organizationDto, @MappingTarget Organization organization);
}
