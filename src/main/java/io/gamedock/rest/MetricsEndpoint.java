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

import io.gamedock.domain.Metric;
import io.gamedock.domain.Game;
import io.gamedock.dto.MetricDetailsDto;
import io.gamedock.dto.MetricDto;
import io.gamedock.dto.MetricSummaryDto;
import io.gamedock.dto.mappers.MetricMapper;
import io.gamedock.rest.exception.ResourceNotFoundException;
import io.gamedock.rest.pagination.PaginationHeaders;
import io.gamedock.services.business.mechanics.Scoreizer;
import io.gamedock.services.repositories.MetricRepository;
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
@RequestMapping(path = WebMvcConfig.API_PREFIX + "metrics")
public class MetricsEndpoint {

    private final MetricRepository metricRepository;

    private final GameRepository gameRepository;

    private final ScopeRepository scopeRepository;

    private final HttpServletRequest request;

    private final MetricMapper metricMapper;

    private final Scoreizer scoreizer;

    @Autowired
    public MetricsEndpoint(MetricRepository metricRepository, GameRepository gameRepository, ScopeRepository scopeRepository, HttpServletRequest request, MetricMapper metricMapper, Scoreizer scoreizer) {
        this.metricRepository = metricRepository;
        this.gameRepository = gameRepository;
        this.scopeRepository = scopeRepository;
        this.request = request;
        this.metricMapper = metricMapper;
        this.scoreizer = scoreizer;
    }

    @RequestMapping(path = "", method = RequestMethod.GET)
    public HttpEntity getMetrics(Pageable pgbl) {
        Long gameId = (Long) request.getAttribute("gameId");
        Page<Metric> page = metricRepository.findByGame_Id(gameId, pgbl);
        List<MetricSummaryDto> body = metricMapper.toSummaryDto(page.getContent());
        HttpHeaders headers = PaginationHeaders.create(page.getNumber(), page.getSize(), page.getTotalElements());
        return new ResponseEntity(body, headers, HttpStatus.OK);
    }

    @RequestMapping(value = "", method = RequestMethod.POST)
    public HttpEntity createMetric(@Validated @RequestBody MetricDto metricDto) {
        Long gameId = (Long) request.getAttribute("gameId");
        Game game = gameRepository.findOne(gameId);
        if (scopeRepository.findByIdAndGame_Id(metricDto.getScope(), gameId) == null) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }

        Metric metric = metricMapper.metricDtoToMetric(metricDto);
        metric.setGame(game);
        metricRepository.save(metric);
        scoreizer.generate(metric, metric.getGame().getCharacters());

        MetricDetailsDto body = metricMapper.toDetailsDto(metric, new MetricDetailsDto());
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(body.getHref());
        return new ResponseEntity(body, headers, HttpStatus.CREATED);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public MetricDetailsDto getMetric(@PathVariable Long id) throws ResourceNotFoundException {
        Long gameId = (Long) request.getAttribute("gameId");
        Metric metric = metricRepository.findByIdAndGame_Id(id, gameId);
        if (metric == null) {
            throw new ResourceNotFoundException();
        }
        return metricMapper.toDetailsDto(metric, new MetricDetailsDto());
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    @ResponseStatus(HttpStatus.OK)
    public MetricDetailsDto updateMetric(@Validated @RequestBody MetricDto metricDto, @PathVariable Long id) throws ResourceNotFoundException {
        Long gameId = (Long) request.getAttribute("gameId");
        Metric metric = metricRepository.findByIdAndGame_Id(id, gameId);
        if (metric == null) {
            throw new ResourceNotFoundException();
        }
        if (scopeRepository.findByIdAndGame_Id(metricDto.getScope(), gameId) == null) {
            throw new ResourceNotFoundException();
        }
        metricMapper.updateMetricFromDto(metricDto, metric);
        metricRepository.save(metric);
        scoreizer.generate(metric, metric.getGame().getCharacters());
        return metricMapper.toDetailsDto(metric, new MetricDetailsDto());
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteMetric(@PathVariable Long id) throws ResourceNotFoundException {
        Long gameId = (Long) request.getAttribute("gameId");
        Metric metric = metricRepository.findByIdAndGame_Id(id, gameId);
        if (metric == null) {
            throw new ResourceNotFoundException();
        }
        metricRepository.delete(metric);
    }

}
