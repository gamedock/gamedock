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

import io.gamedock.domain.StateMetric;
import io.gamedock.domain.Game;
import io.gamedock.dto.StateMetricDetailsDto;
import io.gamedock.dto.StateMetricDto;
import io.gamedock.dto.StateMetricSummaryDto;
import io.gamedock.dto.mappers.StateMetricMapper;
import io.gamedock.rest.exception.ResourceNotFoundException;
import io.gamedock.rest.pagination.PaginationHeaders;
import io.gamedock.services.business.mechanics.Scoreizer;
import io.gamedock.services.repositories.StateMetricRepository;
import io.gamedock.services.repositories.GameRepository;
import io.gamedock.services.repositories.ScopeRepository;
import io.gamedock.web.config.WebMvcConfig;
import java.util.List;
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
@RequestMapping(path = WebMvcConfig.API_PREFIX + "states")
public class StateMetricsEndpoint {

    private final StateMetricRepository stateMetricRepository;

    private final GameRepository gameRepository;

    private final ScopeRepository scopeRepository;

    private final HttpServletRequest request;

    private final StateMetricMapper stateMetricMapper;

    private final Scoreizer scoreizer;

    @Autowired
    public StateMetricsEndpoint(StateMetricRepository stateMetricRepository, GameRepository gameRepository, ScopeRepository scopeRepository, HttpServletRequest request, StateMetricMapper stateMetricMapper, Scoreizer scoreizer) {
        this.stateMetricRepository = stateMetricRepository;
        this.gameRepository = gameRepository;
        this.scopeRepository = scopeRepository;
        this.request = request;
        this.stateMetricMapper = stateMetricMapper;
        this.scoreizer = scoreizer;
    }

    @RequestMapping(path = "", method = RequestMethod.GET)
    public HttpEntity getStateMetrics(Pageable pgbl) {
        Long gameId = (Long) request.getAttribute("gameId");
        Page<StateMetric> page = stateMetricRepository.findByGame_Id(gameId, pgbl);
        List<StateMetricSummaryDto> body = stateMetricMapper.toSummaryDto(page.getContent());
        HttpHeaders headers = PaginationHeaders.create(page.getNumber(), page.getSize(), page.getTotalElements());
        return new ResponseEntity(body, headers, HttpStatus.OK);
    }

    @RequestMapping(value = "", method = RequestMethod.POST)
    public HttpEntity createStateMetric(@Validated @RequestBody StateMetricDto stateMetricDto) {
        Long gameId = (Long) request.getAttribute("gameId");
        Game game = gameRepository.findOne(gameId);
        if (scopeRepository.findByIdAndGame_Id(stateMetricDto.getScope(), gameId) == null) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }

        StateMetric stateMetric = stateMetricMapper.stateMetricDtoToStateMetric(stateMetricDto);
        stateMetric.setGame(game);
        stateMetricRepository.save(stateMetric);
        scoreizer.generate(stateMetric, game.getCharacters());

        StateMetricDetailsDto body = stateMetricMapper.toDetailsDto(stateMetric, new StateMetricDetailsDto());
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(body.getHref());
        return new ResponseEntity(body, headers, HttpStatus.CREATED);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public StateMetricDetailsDto getStateMetric(@PathVariable Long id) throws ResourceNotFoundException {
        Long gameId = (Long) request.getAttribute("gameId");
        StateMetric stateMetric = stateMetricRepository.findByIdAndGame_Id(id, gameId);
        if (stateMetric == null) {
            throw new ResourceNotFoundException();
        }
        return stateMetricMapper.toDetailsDto(stateMetric, new StateMetricDetailsDto());
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    @ResponseStatus(HttpStatus.OK)
    public StateMetricDetailsDto updateStateMetric(@Validated @RequestBody StateMetricDto stateMetricDto, @PathVariable Long id) throws ResourceNotFoundException {
        Long gameId = (Long) request.getAttribute("gameId");
        StateMetric stateMetric = stateMetricRepository.findByIdAndGame_Id(id, gameId);
        if (stateMetric == null) {
            throw new ResourceNotFoundException();
        }
        if (scopeRepository.findByIdAndGame_Id(stateMetricDto.getScope(), gameId) == null) {
            throw new ResourceNotFoundException();
        }
        stateMetric.getScores().clear();
        stateMetricMapper.updateStateMetricFromDto(stateMetricDto, stateMetric);
        stateMetricRepository.save(stateMetric);
        scoreizer.generate(stateMetric, stateMetric.getGame().getCharacters());
        return stateMetricMapper.toDetailsDto(stateMetric, new StateMetricDetailsDto());
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteStateMetric(@PathVariable Long id) throws ResourceNotFoundException {
        Long gameId = (Long) request.getAttribute("gameId");
        StateMetric stateMetric = stateMetricRepository.findByIdAndGame_Id(id, gameId);
        if (stateMetric == null) {
            throw new ResourceNotFoundException();
        }
        stateMetricRepository.delete(stateMetric);
    }

}
