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

package io.gamedock.services.business.engine;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.gamedock.domain.Activity;
import io.gamedock.domain.Event;
import io.gamedock.domain.Game;
import io.gamedock.domain.Play;
import io.gamedock.dto.EventDto;
import io.gamedock.services.repositories.EventRepository;
import io.gamedock.services.repositories.GameRepository;
import io.gamedock.services.repositories.CharacterRepository;
import io.gamedock.services.repositories.PlayRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import io.gamedock.domain.Character;

@Service
@Transactional
public class EventEngine {

    private final EventRepository eventRepository;

    private final GameRepository gameRepository;

    private final PlayRepository playRepository;

    private final CharacterRepository characterRepository;

    private final RuleEngine ruleEngine;

    @Autowired
    public EventEngine(EventRepository eventRepository, GameRepository gameRepository, PlayRepository playRepository, CharacterRepository characterRepository, RuleEngine ruleEngine) {
        this.eventRepository = eventRepository;
        this.gameRepository = gameRepository;
        this.playRepository = playRepository;
        this.characterRepository = characterRepository;
        this.ruleEngine = ruleEngine;
    }

    public Activity processEvent(EventDto eventDto) throws Exception {
        Game game = gameRepository.findOne(eventDto.getGame());
        Play play = playRepository.findOne(eventDto.getPlay());
        Character character = characterRepository.findByCharacter_IdAndCharacter_TypeAndGame_Id(eventDto.getCharacter(), eventDto.getCharacterType(), eventDto.getGame());

        Event event = new Event();
        event.setGame(game);
        event.setCharacter(character);
        event.setPlay(play);
        event.setProperties(new ObjectMapper().writeValueAsString(eventDto.getProperties()));
        eventRepository.save(event);

        return ruleEngine.process(event);
    }
}
