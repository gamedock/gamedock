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

import io.gamedock.dto.ScoreDto;
import io.gamedock.dto.mappers.ScoreMapper;
import io.gamedock.services.repositories.CharacterRepository;
import io.gamedock.web.config.WebMvcConfig;
import java.util.List;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import io.gamedock.domain.Character;

@RestController
@RequestMapping(path = WebMvcConfig.API_PREFIX + "scores")
public class ScoresEndpoint {

    private final CharacterRepository characterRepository;

    private final HttpServletRequest request;

    private final ScoreMapper scoreMapper;

    @Autowired
    public ScoresEndpoint(CharacterRepository characterRepository, HttpServletRequest request, ScoreMapper scoreMapper) {
        this.characterRepository = characterRepository;
        this.request = request;
        this.scoreMapper = scoreMapper;
    }

    @RequestMapping(value = "/{characterId}", method = RequestMethod.GET)
    public HttpEntity getScores(@PathVariable Long characterId) {
        Long gameId = (Long) request.getAttribute("gameId");
        String characterType = request.getParameter("type");
        Character character = characterRepository.findByCharacter_IdAndCharacter_TypeAndGame_Id(characterId, characterType, gameId);
        if (character == null) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
        List<ScoreDto> body = character.getScores().stream().map(s -> scoreMapper.toDto(s)).collect(Collectors.toList());
        return new ResponseEntity(body, HttpStatus.OK);
    }

}
