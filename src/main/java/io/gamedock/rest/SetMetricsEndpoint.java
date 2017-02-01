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

import io.gamedock.domain.SetMetric;
import io.gamedock.domain.Game;
import io.gamedock.dto.SetMetricDetailsDto;
import io.gamedock.dto.SetMetricDto;
import io.gamedock.dto.SetMetricSummaryDto;
import io.gamedock.dto.mappers.SetMetricMapper;
import io.gamedock.rest.exception.ResourceNotFoundException;
import io.gamedock.rest.pagination.PaginationHeaders;
import io.gamedock.services.business.mechanics.Scoreizer;
import io.gamedock.services.repositories.SetMetricRepository;
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
@RequestMapping(path = WebMvcConfig.API_PREFIX + "sets")
public class SetMetricsEndpoint {

    private final SetMetricRepository setMetricRepository;

    private final GameRepository gameRepository;

    private final ScopeRepository scopeRepository;

    private final HttpServletRequest request;

    private final SetMetricMapper setMetricMapper;

    private final Scoreizer scoreizer;

    @Autowired
    public SetMetricsEndpoint(SetMetricRepository setMetricRepository, GameRepository gameRepository, ScopeRepository scopeRepository, HttpServletRequest request, SetMetricMapper setMetricMapper, Scoreizer scoreizer) {
        this.setMetricRepository = setMetricRepository;
        this.gameRepository = gameRepository;
        this.scopeRepository = scopeRepository;
        this.request = request;
        this.setMetricMapper = setMetricMapper;
        this.scoreizer = scoreizer;
    }

    @RequestMapping(path = "", method = RequestMethod.GET)
    public HttpEntity getSetMetrics(Pageable pgbl) {
        Long gameId = (Long) request.getAttribute("gameId");
        Page<SetMetric> page = setMetricRepository.findByGame_Id(gameId, pgbl);
        List<SetMetricSummaryDto> body = setMetricMapper.toSummaryDto(page.getContent());
        HttpHeaders headers = PaginationHeaders.create(page.getNumber(), page.getSize(), page.getTotalElements());
        return new ResponseEntity(body, headers, HttpStatus.OK);
    }

    @RequestMapping(value = "", method = RequestMethod.POST)
    public HttpEntity createSetMetric(@Validated @RequestBody SetMetricDto setMetricDto) {
        Long gameId = (Long) request.getAttribute("gameId");
        Game game = gameRepository.findOne(gameId);
        if (scopeRepository.findByIdAndGame_Id(setMetricDto.getScope(), gameId) == null) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }

        SetMetric setMetric = setMetricMapper.setMetricDtoToSetMetric(setMetricDto);
        setMetric.setGame(game);
        setMetricRepository.save(setMetric);
        scoreizer.generate(setMetric, game.getCharacters());

        SetMetricDetailsDto body = setMetricMapper.toDetailsDto(setMetric, new SetMetricDetailsDto());
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(body.getHref());
        return new ResponseEntity(body, headers, HttpStatus.CREATED);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public SetMetricDetailsDto getSetMetric(@PathVariable Long id) throws ResourceNotFoundException {
        Long gameId = (Long) request.getAttribute("gameId");
        SetMetric setMetric = setMetricRepository.findByIdAndGame_Id(id, gameId);
        if (setMetric == null) {
            throw new ResourceNotFoundException();
        }
        return setMetricMapper.toDetailsDto(setMetric, new SetMetricDetailsDto());
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    @ResponseStatus(HttpStatus.OK)
    public SetMetricDetailsDto updateSetMetric(@Validated @RequestBody SetMetricDto setMetricDto, @PathVariable Long id) throws ResourceNotFoundException {
        Long gameId = (Long) request.getAttribute("gameId");
        SetMetric setMetric = setMetricRepository.findByIdAndGame_Id(id, gameId);
        if (setMetric == null) {
            throw new ResourceNotFoundException();
        }
        if (scopeRepository.findByIdAndGame_Id(setMetricDto.getScope(), gameId) == null) {
            throw new ResourceNotFoundException();
        }
        setMetric.getScores().clear();
        setMetricMapper.updateSetMetricFromDto(setMetricDto, setMetric);
        setMetricRepository.save(setMetric);
        scoreizer.generate(setMetric, setMetric.getGame().getCharacters());
        return setMetricMapper.toDetailsDto(setMetric, new SetMetricDetailsDto());
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteSetMetric(@PathVariable Long id) throws ResourceNotFoundException {
        Long gameId = (Long) request.getAttribute("gameId");
        SetMetric setMetric = setMetricRepository.findByIdAndGame_Id(id, gameId);
        if (setMetric == null) {
            throw new ResourceNotFoundException();
        }
        setMetricRepository.delete(setMetric);
    }

}
