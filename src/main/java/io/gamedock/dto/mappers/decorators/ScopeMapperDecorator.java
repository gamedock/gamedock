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

import io.gamedock.domain.Scope;
import io.gamedock.dto.ScopeDetailsDto;
import io.gamedock.dto.ScopeSummaryDto;
import io.gamedock.dto.mappers.ScopeMapper;
import io.gamedock.rest.GamesEndpoint;
import io.gamedock.rest.ScopesEndpoint;
import io.gamedock.services.repositories.ScopeRepository;
import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;
import org.mapstruct.MappingTarget;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import org.springframework.stereotype.Component;

@Component
@Primary
public abstract class ScopeMapperDecorator implements ScopeMapper {

    @Autowired
    private ScopeMapper delegate;

    @Autowired
    private ScopeRepository scopeRepository;

    public ScopeMapperDecorator() {
    }

    @Override
    public Scope scopeIdToScope(Long scopeId) {
        return scopeRepository.findOne(scopeId);
    }

    @Override
    public ScopeSummaryDto toSummaryDto(Scope scope) {
        ScopeSummaryDto dto = delegate.toSummaryDto(scope);
        dto.setParent(URI.create(linkTo(GamesEndpoint.class).slash(scope.getGame()).withSelfRel().getHref()));
        dto.setHref(URI.create(linkTo(ScopesEndpoint.class).slash(scope).withSelfRel().getHref()));
        return dto;
    }

    @Override
    public List<ScopeSummaryDto> toSummaryDto(List<Scope> scopes) {
        return scopes.stream().map(o -> toSummaryDto(o)).collect(Collectors.toList());
    }

    @Override
    public ScopeDetailsDto toDetailsDto(Scope scope, @MappingTarget ScopeDetailsDto scopeDto) {
        ScopeDetailsDto dto = delegate.toDetailsDto(scope, scopeDto);
        dto.setParent(URI.create(linkTo(GamesEndpoint.class).slash(scope.getGame()).withSelfRel().getHref()));
        dto.setHref(URI.create(linkTo(ScopesEndpoint.class).slash(scope).withSelfRel().getHref()));
        return dto;
    }

}
