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

import io.gamedock.domain.PointMetric;
import io.gamedock.domain.Game;
import io.gamedock.dto.PointMetricDetailsDto;
import io.gamedock.dto.PointMetricDto;
import io.gamedock.dto.PointMetricSummaryDto;
import io.gamedock.dto.mappers.PointMetricMapper;
import io.gamedock.rest.exception.ResourceNotFoundException;
import io.gamedock.rest.pagination.PaginationHeaders;
import io.gamedock.services.business.mechanics.Scoreizer;
import io.gamedock.services.repositories.PointMetricRepository;
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
@RequestMapping(path = WebMvcConfig.API_PREFIX + "points")
public class PointMetricsEndpoint {

    private final PointMetricRepository pointMetricRepository;

    private final GameRepository gameRepository;

    private final ScopeRepository scopeRepository;

    private final HttpServletRequest request;

    private final PointMetricMapper pointMetricMapper;

    private final Scoreizer scoreizer;

    @Autowired
    public PointMetricsEndpoint(PointMetricRepository pointMetricRepository, GameRepository gameRepository, ScopeRepository scopeRepository, HttpServletRequest request, PointMetricMapper pointMetricMapper, Scoreizer scoreizer) {
        this.pointMetricRepository = pointMetricRepository;
        this.gameRepository = gameRepository;
        this.scopeRepository = scopeRepository;
        this.request = request;
        this.pointMetricMapper = pointMetricMapper;
        this.scoreizer = scoreizer;
    }

    @RequestMapping(path = "", method = RequestMethod.GET)
    public HttpEntity getPointMetrics(Pageable pgbl) {
        Long gameId = (Long) request.getAttribute("gameId");
        Page<PointMetric> page = pointMetricRepository.findByGame_Id(gameId, pgbl);
        List<PointMetricSummaryDto> body = pointMetricMapper.toSummaryDto(page.getContent());
        HttpHeaders headers = PaginationHeaders.create(page.getNumber(), page.getSize(), page.getTotalElements());
        return new ResponseEntity(body, headers, HttpStatus.OK);
    }

    @RequestMapping(value = "", method = RequestMethod.POST)
    public HttpEntity createPointMetric(@Validated @RequestBody PointMetricDto pointMetricDto) {
        Long gameId = (Long) request.getAttribute("gameId");
        Game game = gameRepository.findOne(gameId);
        if (scopeRepository.findByIdAndGame_Id(pointMetricDto.getScope(), gameId) == null) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }

        PointMetric pointMetric = pointMetricMapper.pointMetricDtoToPointMetric(pointMetricDto);
        pointMetric.setGame(game);
        pointMetricRepository.save(pointMetric);
        scoreizer.generate(pointMetric, game.getCharacters());

        PointMetricDetailsDto body = pointMetricMapper.toDetailsDto(pointMetric, new PointMetricDetailsDto());
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(body.getHref());
        return new ResponseEntity(body, headers, HttpStatus.CREATED);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public PointMetricDetailsDto getPointMetric(@PathVariable Long id) throws ResourceNotFoundException {
        Long gameId = (Long) request.getAttribute("gameId");
        PointMetric pointMetric = pointMetricRepository.findByIdAndGame_Id(id, gameId);
        if (pointMetric == null) {
            throw new ResourceNotFoundException();
        }
        return pointMetricMapper.toDetailsDto(pointMetric, new PointMetricDetailsDto());
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    @ResponseStatus(HttpStatus.OK)
    public PointMetricDetailsDto updatePointMetric(@Validated @RequestBody PointMetricDto pointMetricDto, @PathVariable Long id) throws ResourceNotFoundException {
        Long gameId = (Long) request.getAttribute("gameId");
        PointMetric pointMetric = pointMetricRepository.findByIdAndGame_Id(id, gameId);
        if (pointMetric == null) {
            throw new ResourceNotFoundException();
        }
        if (scopeRepository.findByIdAndGame_Id(pointMetricDto.getScope(), gameId) == null) {
            throw new ResourceNotFoundException();
        }
        pointMetric.getScores().clear();
        pointMetricMapper.updatePointMetricFromDto(pointMetricDto, pointMetric);
        pointMetricRepository.save(pointMetric);
        scoreizer.generate(pointMetric, pointMetric.getGame().getCharacters());
        return pointMetricMapper.toDetailsDto(pointMetric, new PointMetricDetailsDto());
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletePointMetric(@PathVariable Long id) throws ResourceNotFoundException {
        Long gameId = (Long) request.getAttribute("gameId");
        PointMetric pointMetric = pointMetricRepository.findByIdAndGame_Id(id, gameId);
        if (pointMetric == null) {
            throw new ResourceNotFoundException();
        }
        pointMetricRepository.delete(pointMetric);
    }

}
