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

import io.gamedock.domain.Activity;
import io.gamedock.domain.EndUser;
import io.gamedock.domain.Game;
import io.gamedock.domain.Play;
import io.gamedock.dto.ActivityDto;
import io.gamedock.dto.EventDto;
import io.gamedock.dto.mappers.ActivityMapper;
import io.gamedock.services.business.engine.EventEngine;
import io.gamedock.services.repositories.EndUserRepository;
import io.gamedock.services.repositories.GameRepository;
import io.gamedock.services.repositories.CharacterRepository;
import io.gamedock.services.repositories.PlayRepository;
import io.gamedock.web.config.WebMvcConfig;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import io.gamedock.domain.Character;

@RestController
@RequestMapping(path = WebMvcConfig.API_PREFIX + "events")
public class EventsEndpoint {

    private final EventEngine engine;

    private final EndUserRepository endUserRepository;

    private final GameRepository gameRepository;

    private final PlayRepository playRepository;

    private final CharacterRepository characterRepository;

    private final HttpServletRequest request;

    private final ActivityMapper activityMapper;

    @Autowired
    public EventsEndpoint(EventEngine engine, EndUserRepository endUserRepository, GameRepository gameRepository, PlayRepository playRepository, CharacterRepository characterRepository, HttpServletRequest request, ActivityMapper activityMapper) {
        this.engine = engine;
        this.endUserRepository = endUserRepository;
        this.gameRepository = gameRepository;
        this.playRepository = playRepository;
        this.characterRepository = characterRepository;
        this.request = request;
        this.activityMapper = activityMapper;
    }

    @RequestMapping(path = "", method = RequestMethod.POST)
    public HttpEntity postEvent(@Validated @RequestBody EventDto eventDto) throws Exception {
        Long organizationId = (Long) request.getAttribute("organizationId");
        EndUser endUser = endUserRepository.findByIdAndOrganization_Id(eventDto.getCharacter(), organizationId);
        Game game = gameRepository.findByIdAndOrganization_Id(eventDto.getGame(), organizationId);
        Play play = playRepository.findByIdAndGame_Id(eventDto.getPlay(), eventDto.getGame());
        Character character = characterRepository.findByCharacter_IdAndCharacter_TypeAndGame_Id(eventDto.getCharacter(), eventDto.getCharacterType(), eventDto.getGame());
        if (endUser == null || game == null || play == null || character == null) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
        Activity activity = engine.processEvent(eventDto);
        if (activity == null) {
            return new ResponseEntity(HttpStatus.ACCEPTED);
        }
        ActivityDto body = activityMapper.toDto(activity);
        return new ResponseEntity(body, HttpStatus.ACCEPTED);
    }
}
