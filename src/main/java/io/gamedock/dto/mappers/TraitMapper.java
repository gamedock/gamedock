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

import io.gamedock.domain.Trait;
import io.gamedock.dto.TraitDetailsDto;
import io.gamedock.dto.TraitDto;
import io.gamedock.dto.TraitSummaryDto;
import io.gamedock.dto.mappers.decorators.TraitMapperDecorator;
import java.util.List;
import org.mapstruct.DecoratedWith;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
@DecoratedWith(TraitMapperDecorator.class)
public interface TraitMapper {

    Trait traitIdToTrait(Long traitId);

    TraitSummaryDto toSummaryDto(Trait trait);

    List<TraitSummaryDto> toSummaryDto(List<Trait> traits);

    Trait traitDtoToTrait(TraitDto traitDto);

    TraitDetailsDto toDetailsDto(Trait trait, @MappingTarget TraitDetailsDto traitDto);

    Trait updateTraitFromDto(TraitDto traitDto, @MappingTarget Trait trait);
}
