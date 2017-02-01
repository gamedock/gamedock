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

import io.gamedock.domain.Rule;
import io.gamedock.domain.Game;
import io.gamedock.dto.RuleDetailsDto;
import io.gamedock.dto.RuleDto;
import io.gamedock.dto.RuleSummaryDto;
import io.gamedock.dto.mappers.RuleMapper;
import io.gamedock.rest.exception.ResourceNotFoundException;
import io.gamedock.rest.pagination.PaginationHeaders;
import io.gamedock.services.repositories.RuleRepository;
import io.gamedock.services.repositories.GameRepository;
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
@RequestMapping(path = WebMvcConfig.API_PREFIX + "rules")
public class RulesEndpoint {

    private final RuleRepository ruleRepository;

    private final GameRepository gameRepository;

    private final HttpServletRequest request;

    private final RuleMapper ruleMapper;

    @Autowired
    public RulesEndpoint(RuleRepository ruleRepository, GameRepository gameRepository, HttpServletRequest request, RuleMapper ruleMapper) {
        this.ruleRepository = ruleRepository;
        this.gameRepository = gameRepository;
        this.request = request;
        this.ruleMapper = ruleMapper;
    }

    @RequestMapping(path = "", method = RequestMethod.GET)
    public HttpEntity getRules(Pageable pgbl) {
        Long gameId = (Long) request.getAttribute("gameId");
        Page<Rule> page = ruleRepository.findByGame_Id(gameId, pgbl);
        List<RuleSummaryDto> body = ruleMapper.toSummaryDto(page.getContent());
        HttpHeaders headers = PaginationHeaders.create(page.getNumber(), page.getSize(), page.getTotalElements());
        return new ResponseEntity(body, headers, HttpStatus.OK);
    }

    @RequestMapping(value = "", method = RequestMethod.POST)
    public HttpEntity createRule(@Validated @RequestBody RuleDto ruleDto) {
        Long gameId = (Long) request.getAttribute("gameId");
        Game game = gameRepository.findOne(gameId);

        Rule rule = ruleMapper.ruleDtoToRule(ruleDto);
        rule.setGame(game);
        ruleRepository.save(rule);

        RuleDetailsDto body = ruleMapper.toDetailsDto(rule, new RuleDetailsDto());
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(body.getHref());
        return new ResponseEntity(body, headers, HttpStatus.CREATED);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public RuleDetailsDto getRule(@PathVariable Long id) throws ResourceNotFoundException {
        Long gameId = (Long) request.getAttribute("gameId");
        Rule rule = ruleRepository.findByIdAndGame_Id(id, gameId);
        if (rule == null) {
            throw new ResourceNotFoundException();
        }
        return ruleMapper.toDetailsDto(rule, new RuleDetailsDto());
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    @ResponseStatus(HttpStatus.OK)
    public RuleDetailsDto updateRule(@Validated @RequestBody RuleDto ruleDto, @PathVariable Long id) throws ResourceNotFoundException {
        Long gameId = (Long) request.getAttribute("gameId");
        Rule rule = ruleRepository.findByIdAndGame_Id(id, gameId);
        if (rule == null) {
            throw new ResourceNotFoundException();
        }
        ruleMapper.updateRuleFromDto(ruleDto, rule);
        ruleRepository.save(rule);
        return ruleMapper.toDetailsDto(rule, new RuleDetailsDto());
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteRule(@PathVariable Long id) throws ResourceNotFoundException {
        Long gameId = (Long) request.getAttribute("gameId");
        Rule rule = ruleRepository.findByIdAndGame_Id(id, gameId);
        if (rule == null) {
            throw new ResourceNotFoundException();
        }
        ruleRepository.delete(rule);
    }

}
