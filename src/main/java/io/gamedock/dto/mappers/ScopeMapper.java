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

import io.gamedock.domain.Scope;
import io.gamedock.dto.ScopeDetailsDto;
import io.gamedock.dto.ScopeDto;
import io.gamedock.dto.ScopeSummaryDto;
import io.gamedock.dto.mappers.decorators.ScopeMapperDecorator;
import java.util.List;
import org.mapstruct.DecoratedWith;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring", uses = {RuleMapper.class, TraitMapper.class})
@DecoratedWith(ScopeMapperDecorator.class)
public interface ScopeMapper {

    Scope scopeIdToScope(Long scopeId);

    Scope scopeDtoToScope(ScopeDto scopeDto);

    ScopeSummaryDto toSummaryDto(Scope scope);

    List<ScopeSummaryDto> toSummaryDto(List<Scope> scopes);

    ScopeDetailsDto toDetailsDto(Scope scope, @MappingTarget ScopeDetailsDto scopeDto);

    Scope updateScopeFromDto(ScopeDto scopeDto, @MappingTarget Scope scope);

}
