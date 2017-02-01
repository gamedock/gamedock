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

import io.gamedock.domain.Application;
import io.gamedock.dto.ApplicationDetailsDto;
import io.gamedock.dto.ApplicationDto;
import io.gamedock.dto.ApplicationSummaryDto;
import io.gamedock.dto.mappers.decorators.ApplicationMapperDecorator;
import java.util.List;
import org.mapstruct.DecoratedWith;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring", uses = OrganizationMapper.class)
@DecoratedWith(ApplicationMapperDecorator.class)
public interface ApplicationMapper {

    Application applicationDtoToApplication(ApplicationDto applicationDto);

    ApplicationSummaryDto toSummaryDto(Application application);

    List<ApplicationSummaryDto> toSummaryDto(List<Application> applications);

    ApplicationDetailsDto toDetailsDto(Application application, @MappingTarget ApplicationDetailsDto applicationDto);

    Application updateApplicationFromDto(ApplicationDto applicationDto, @MappingTarget Application application);
}
