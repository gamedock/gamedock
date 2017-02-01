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

import io.gamedock.domain.Play;
import io.gamedock.domain.Game;
import io.gamedock.dto.PlayDetailsDto;
import io.gamedock.dto.PlayDto;
import io.gamedock.dto.PlaySummaryDto;
import io.gamedock.dto.mappers.PlayMapper;
import io.gamedock.rest.exception.ResourceNotFoundException;
import io.gamedock.rest.pagination.PaginationHeaders;
import io.gamedock.services.repositories.PlayRepository;
import io.gamedock.services.repositories.GameRepository;
import io.gamedock.services.repositories.RuleRepository;
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
@RequestMapping(path = WebMvcConfig.API_PREFIX + "plays")
public class PlaysEndpoint {

    private final PlayRepository playRepository;

    private final GameRepository gameRepository;

    private final RuleRepository ruleRepository;

    private final HttpServletRequest request;

    private final PlayMapper playMapper;

    @Autowired
    public PlaysEndpoint(PlayRepository playRepository, GameRepository gameRepository, RuleRepository ruleRepository, HttpServletRequest request, PlayMapper playMapper) {
        this.playRepository = playRepository;
        this.gameRepository = gameRepository;
        this.ruleRepository = ruleRepository;
        this.request = request;
        this.playMapper = playMapper;
    }

    @RequestMapping(path = "", method = RequestMethod.GET)
    public HttpEntity getPlays(Pageable pgbl) {
        Long gameId = (Long) request.getAttribute("gameId");
        Page<Play> page = playRepository.findByGame_Id(gameId, pgbl);
        List<PlaySummaryDto> body = playMapper.toSummaryDto(page.getContent());
        HttpHeaders headers = PaginationHeaders.create(page.getNumber(), page.getSize(), page.getTotalElements());
        return new ResponseEntity(body, headers, HttpStatus.OK);
    }

    @RequestMapping(value = "", method = RequestMethod.POST)
    public HttpEntity createPlay(@Validated @RequestBody PlayDto playDto) {
        Long gameId = (Long) request.getAttribute("gameId");
        Game game = gameRepository.findOne(gameId);
        if (!isValidRules(playDto.getRules(), gameId)) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }

        Play play = playMapper.playDtoToPlay(playDto);
        play.setGame(game);
        playRepository.save(play);

        PlayDetailsDto body = playMapper.toDetailsDto(play, new PlayDetailsDto());
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(body.getHref());
        return new ResponseEntity(body, headers, HttpStatus.CREATED);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public PlayDetailsDto getPlay(@PathVariable Long id) throws ResourceNotFoundException {
        Long gameId = (Long) request.getAttribute("gameId");
        Play play = playRepository.findByIdAndGame_Id(id, gameId);
        if (play == null) {
            throw new ResourceNotFoundException();
        }
        return playMapper.toDetailsDto(play, new PlayDetailsDto());
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public HttpEntity updatePlay(@Validated @RequestBody PlayDto playDto, @PathVariable Long id) throws ResourceNotFoundException {
        Long gameId = (Long) request.getAttribute("gameId");
        Play play = playRepository.findByIdAndGame_Id(id, gameId);
        if (play == null) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
        if (!isValidRules(playDto.getRules(), gameId)) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
        playMapper.updatePlayFromDto(playDto, play);
        playRepository.save(play);
        PlayDetailsDto body = playMapper.toDetailsDto(play, new PlayDetailsDto());
        return new ResponseEntity(body, HttpStatus.OK);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletePlay(@PathVariable Long id) throws ResourceNotFoundException {
        Long gameId = (Long) request.getAttribute("gameId");
        Play play = playRepository.findByIdAndGame_Id(id, gameId);
        if (play == null) {
            throw new ResourceNotFoundException();
        }
        playRepository.delete(play);
    }

    private boolean isValidRules(Set<Long> ruleIds, Long gameId) {
        return ruleIds.stream().map((id) -> ruleRepository.findByIdAndGame_Id(id, gameId)).noneMatch((rule) -> (rule == null));
    }
}
