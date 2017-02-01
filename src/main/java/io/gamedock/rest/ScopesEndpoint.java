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

package io.gamedock.rest;

import io.gamedock.domain.Scope;
import io.gamedock.domain.Game;
import io.gamedock.dto.ScopeDetailsDto;
import io.gamedock.dto.ScopeDto;
import io.gamedock.dto.ScopeSummaryDto;
import io.gamedock.dto.mappers.ScopeMapper;
import io.gamedock.rest.exception.ResourceNotFoundException;
import io.gamedock.rest.pagination.PaginationHeaders;
import io.gamedock.services.repositories.ScopeRepository;
import io.gamedock.services.repositories.GameRepository;
import io.gamedock.services.repositories.TraitRepository;
import io.gamedock.web.config.WebMvcConfig;
import java.util.List;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = WebMvcConfig.API_PREFIX + "scopes")
public class ScopesEndpoint {

    private final ScopeRepository scopeRepository;

    private final GameRepository gameRepository;

    private final TraitRepository traitRepository;

    private final HttpServletRequest request;

    private final ScopeMapper scopeMapper;

    @Autowired
    public ScopesEndpoint(ScopeRepository scopeRepository, GameRepository gameRepository, TraitRepository traitRepository, HttpServletRequest request, ScopeMapper scopeMapper) {
        this.scopeRepository = scopeRepository;
        this.gameRepository = gameRepository;
        this.traitRepository = traitRepository;
        this.request = request;
        this.scopeMapper = scopeMapper;
    }

    @RequestMapping(path = "", method = RequestMethod.GET)
    public HttpEntity getScopes(Pageable pgbl) {
        Long gameId = (Long) request.getAttribute("gameId");
        Page<Scope> page = scopeRepository.findByGame_Id(gameId, pgbl);
        List<ScopeSummaryDto> body = scopeMapper.toSummaryDto(page.getContent());
        HttpHeaders headers = PaginationHeaders.create(page.getNumber(), page.getSize(), page.getTotalElements());
        return new ResponseEntity(body, headers, HttpStatus.OK);
    }

    @RequestMapping(value = "", method = RequestMethod.POST)
    public HttpEntity createScope(@Validated @RequestBody ScopeDto scopeDto) {
        Long gameId = (Long) request.getAttribute("gameId");
        Game game = gameRepository.findOne(gameId);
        if (!isValidTraits(scopeDto.getTraits(), gameId)) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }

        Scope scope = scopeMapper.scopeDtoToScope(scopeDto);
        scope.setGame(game);
        scopeRepository.save(scope);

        ScopeDetailsDto body = scopeMapper.toDetailsDto(scope, new ScopeDetailsDto());
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(body.getHref());
        return new ResponseEntity(body, headers, HttpStatus.CREATED);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public ScopeDetailsDto getScope(@PathVariable Long id) throws ResourceNotFoundException {
        Long gameId = (Long) request.getAttribute("gameId");
        Scope scope = scopeRepository.findByIdAndGame_Id(id, gameId);
        if (scope == null) {
            throw new ResourceNotFoundException();
        }
        return scopeMapper.toDetailsDto(scope, new ScopeDetailsDto());
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    @ResponseStatus(HttpStatus.OK)
    public HttpEntity updateScope(@Validated @RequestBody ScopeDto scopeDto, @PathVariable Long id) throws ResourceNotFoundException {
        Long gameId = (Long) request.getAttribute("gameId");
        Scope scope = scopeRepository.findByIdAndGame_Id(id, gameId);
        if (scope == null) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
        if (!isValidTraits(scopeDto.getTraits(), gameId)) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
        scopeMapper.updateScopeFromDto(scopeDto, scope);
        scopeRepository.save(scope);
        ScopeDetailsDto body = scopeMapper.toDetailsDto(scope, new ScopeDetailsDto());
        return new ResponseEntity(body, HttpStatus.OK);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteScope(@PathVariable Long id) throws ResourceNotFoundException {
        Long gameId = (Long) request.getAttribute("gameId");
        Scope scope = scopeRepository.findByIdAndGame_Id(id, gameId);
        if (scope == null) {
            throw new ResourceNotFoundException();
        }
        scopeRepository.delete(scope);
    }

    private boolean isValidTraits(Set<Long> traitIds, Long gameId) {
        return traitIds.stream().map((id) -> traitRepository.findByIdAndGame_Id(id, gameId)).noneMatch((rule) -> (rule == null));
    }
}
