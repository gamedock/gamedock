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

package io.gamedock.dto.mappers.decorators;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.gamedock.domain.Trait;
import io.gamedock.dto.TraitDetailsDto;
import io.gamedock.dto.TraitDto;
import io.gamedock.dto.TraitSummaryDto;
import io.gamedock.dto.mappers.TraitMapper;
import io.gamedock.rest.GamesEndpoint;
import io.gamedock.rest.TraitsEndpoint;
import io.gamedock.services.repositories.TraitRepository;
import java.io.IOException;
import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import org.mapstruct.MappingTarget;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

@Component
@Primary
public abstract class TraitMapperDecorator implements TraitMapper {

    @Autowired
    private TraitMapper delegate;

    @Autowired
    private TraitRepository traitRepository;

    public TraitMapperDecorator() {
    }

    @Override
    public Trait traitIdToTrait(Long traitId) {
        return traitRepository.findOne(traitId);
    }

    @Override
    public TraitSummaryDto toSummaryDto(Trait trait) {
        TraitSummaryDto dto = delegate.toSummaryDto(trait);
        dto.setParent(URI.create(linkTo(GamesEndpoint.class).slash(trait.getGame()).withSelfRel().getHref()));
        dto.setHref(URI.create(linkTo(TraitsEndpoint.class).slash(trait).withSelfRel().getHref()));
        return dto;
    }

    @Override
    public List<TraitSummaryDto> toSummaryDto(List<Trait> traits) {
        return traits.stream().map(o -> toSummaryDto(o)).collect(Collectors.toList());
    }

    @Override
    public TraitDetailsDto toDetailsDto(Trait trait, TraitDetailsDto traitDto) {
        TraitDetailsDto dto = delegate.toDetailsDto(trait, traitDto);
        dto.setParent(URI.create(linkTo(GamesEndpoint.class).slash(trait.getGame()).withSelfRel().getHref()));
        dto.setHref(URI.create(linkTo(TraitsEndpoint.class).slash(trait).withSelfRel().getHref()));
        try {
            dto.setProperties(new ObjectMapper().readValue(trait.getSchema(), HashMap.class));
        } catch (IOException ex) {
            dto.setProperties(new HashMap());
        }
        return dto;
    }

    @Override
    public Trait traitDtoToTrait(TraitDto traitDto) {
        Trait trait = delegate.traitDtoToTrait(traitDto);
        try {
            trait.setSchema(new ObjectMapper().writeValueAsString(traitDto.getProperties()));
        } catch (JsonProcessingException ex) {
            trait.setSchema("");
        }
        return trait;
    }

    @Override
    public Trait updateTraitFromDto(TraitDto traitDto, @MappingTarget Trait trait) {
        trait = delegate.updateTraitFromDto(traitDto, trait);
        try {
            trait.setSchema(new ObjectMapper().writeValueAsString(traitDto.getProperties()));
        } catch (JsonProcessingException ex) {
            trait.setSchema("");
        }
        return trait;
    }

}
