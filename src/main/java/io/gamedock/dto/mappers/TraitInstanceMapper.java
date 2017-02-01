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

package io.gamedock.dto.mappers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.gamedock.domain.Trait;
import io.gamedock.domain.TraitInstance;
import io.gamedock.dto.TraitInstanceDto;
import io.gamedock.services.repositories.TraitRepository;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import io.gamedock.domain.Character;

@Component
public class TraitInstanceMapper {

    @Autowired
    private TraitRepository traitRepository;

    public List<TraitInstance> traitInstanceDtoToTraitInstance(List<TraitInstanceDto> traitInstanceDtos, Character character) {
        return traitInstanceDtos.stream().map(t -> {
            Trait trait = traitRepository.findOne(t.getTrait());
            TraitInstance traitInstance = new TraitInstance();
            traitInstance.setCharacter(character);
            traitInstance.setTrait(trait);
            Map<String, Object> properties;
            try {
                properties = new ObjectMapper().readValue(trait.getSchema(), HashMap.class);
            } catch (IOException ex) {
                properties = new HashMap<>();
            }
            for (Map.Entry<String, Object> entry : t.getValues().entrySet()) {
                if (properties.get(entry.getKey()) != null) {
                    properties.put(entry.getKey(), entry.getValue());
                }
            }
            try {
                traitInstance.setProperties(new ObjectMapper().writeValueAsString(properties));
            } catch (JsonProcessingException ex) {
                traitInstance.setProperties(trait.getSchema());
            }
            return traitInstance;
        }).collect(Collectors.toList());
    }

    public Map<String, Object> traitInstanceToMap(List<TraitInstance> traitInstances) {
        Map<String, Object> traits = new HashMap<>();
        traitInstances.stream().forEach((t) -> {
            Map<String, Object> trait;
            try {
                trait = new ObjectMapper().readValue(t.getProperties(), HashMap.class);
            } catch (IOException ex) {
                trait = new HashMap<>();
            }
            traits.put(t.getTrait().getName(), trait);
        });
        return traits;
    }

    public Map<String, Object> traitToMap(List<Trait> traits) {
        Map<String, Object> _traits = new HashMap<>();
        traits.stream().forEach((t) -> {
            Map<String, Object> trait;
            try {
                trait = new ObjectMapper().readValue(t.getSchema(), HashMap.class);
            } catch (IOException ex) {
                trait = new HashMap<>();
            }
            _traits.put(t.getName(), trait);
        });
        return _traits;
    }

}
