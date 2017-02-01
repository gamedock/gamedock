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

import io.gamedock.domain.Trait;
import io.gamedock.domain.Game;
import io.gamedock.dto.TraitDetailsDto;
import io.gamedock.dto.TraitDto;
import io.gamedock.dto.TraitSummaryDto;
import io.gamedock.dto.mappers.TraitMapper;
import io.gamedock.rest.exception.ResourceNotFoundException;
import io.gamedock.rest.pagination.PaginationHeaders;
import io.gamedock.services.repositories.TraitRepository;
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
@RequestMapping(path = WebMvcConfig.API_PREFIX + "traits")
public class TraitsEndpoint {

    private final TraitRepository traitRepository;

    private final GameRepository gameRepository;

    private final HttpServletRequest request;

    private final TraitMapper traitMapper;

    @Autowired
    public TraitsEndpoint(TraitRepository traitRepository, GameRepository gameRepository, HttpServletRequest request, TraitMapper traitMapper) {
        this.traitRepository = traitRepository;
        this.gameRepository = gameRepository;
        this.request = request;
        this.traitMapper = traitMapper;
    }

    @RequestMapping(path = "", method = RequestMethod.GET)
    public HttpEntity getTraits(Pageable pgbl) {
        Long gameId = (Long) request.getAttribute("gameId");
        Page<Trait> page = traitRepository.findByGame_Id(gameId, pgbl);
        List<TraitSummaryDto> body = traitMapper.toSummaryDto(page.getContent());
        HttpHeaders headers = PaginationHeaders.create(page.getNumber(), page.getSize(), page.getTotalElements());
        return new ResponseEntity(body, headers, HttpStatus.OK);
    }

    @RequestMapping(value = "", method = RequestMethod.POST)
    public HttpEntity createTrait(@Validated @RequestBody TraitDto traitDto) {
        Long gameId = (Long) request.getAttribute("gameId");
        Game game = gameRepository.findOne(gameId);

        Trait trait = traitMapper.traitDtoToTrait(traitDto);
        trait.setGame(game);
        traitRepository.save(trait);

        TraitDetailsDto body = traitMapper.toDetailsDto(trait, new TraitDetailsDto());
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(body.getHref());
        return new ResponseEntity(body, headers, HttpStatus.CREATED);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public TraitDetailsDto getTrait(@PathVariable Long id) throws ResourceNotFoundException {
        Long gameId = (Long) request.getAttribute("gameId");
        Trait trait = traitRepository.findByIdAndGame_Id(id, gameId);
        if (trait == null) {
            throw new ResourceNotFoundException();
        }
        return traitMapper.toDetailsDto(trait, new TraitDetailsDto());
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    @ResponseStatus(HttpStatus.OK)
    public TraitDetailsDto updateTrait(@Validated @RequestBody TraitDto traitDto, @PathVariable Long id) throws ResourceNotFoundException {
        Long gameId = (Long) request.getAttribute("gameId");
        Trait trait = traitRepository.findByIdAndGame_Id(id, gameId);
        if (trait == null) {
            throw new ResourceNotFoundException();
        }
        traitMapper.updateTraitFromDto(traitDto, trait);
        traitRepository.save(trait);
        return traitMapper.toDetailsDto(trait, new TraitDetailsDto());
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteTrait(@PathVariable Long id) throws ResourceNotFoundException {
        Long gameId = (Long) request.getAttribute("gameId");
        Trait trait = traitRepository.findByIdAndGame_Id(id, gameId);
        if (trait == null) {
            throw new ResourceNotFoundException();
        }
        traitRepository.delete(trait);
    }

}
