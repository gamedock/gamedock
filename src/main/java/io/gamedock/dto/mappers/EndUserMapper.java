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

import io.gamedock.domain.EndUser;
import io.gamedock.dto.EndUserDetailsDto;
import io.gamedock.dto.EndUserDto;
import io.gamedock.dto.EndUserSummaryDto;
import io.gamedock.dto.mappers.decorators.EndUserMapperDecorator;
import java.util.List;
import org.mapstruct.DecoratedWith;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring", uses = {PlayerMapper.class})
@DecoratedWith(EndUserMapperDecorator.class)
public interface EndUserMapper {

    EndUser endUserDtoToEndUser(EndUserDto endUserDto);

    EndUserSummaryDto toSummaryDto(EndUser endUser);

    List<EndUserSummaryDto> toSummaryDto(List<EndUser> endUsers);

    EndUserDetailsDto toDetailsDto(EndUser endUser, @MappingTarget EndUserDetailsDto endUserDto);

    EndUser updateEndUserFromDto(EndUserDto endUserDto, @MappingTarget EndUser endUser);

}
