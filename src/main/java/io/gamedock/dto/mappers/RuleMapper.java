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

import io.gamedock.domain.Rule;
import io.gamedock.dto.RuleDetailsDto;
import io.gamedock.dto.RuleDto;
import io.gamedock.dto.RuleSummaryDto;
import io.gamedock.dto.mappers.decorators.RuleMapperDecorator;
import java.util.List;
import java.util.Set;
import org.mapstruct.DecoratedWith;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
@DecoratedWith(RuleMapperDecorator.class)
public interface RuleMapper {

    Rule ruleDtoToRule(RuleDto ruleDto);

    Rule ruleIdToRule(Long ruleId);

    RuleSummaryDto toSummaryDto(Rule rule);

    List<RuleSummaryDto> toSummaryDto(List<Rule> rules);

    Set<RuleSummaryDto> toSummaryDto(Set<Rule> rules);

    RuleDetailsDto toDetailsDto(Rule rule, @MappingTarget RuleDetailsDto ruleDto);

    Rule updateRuleFromDto(RuleDto ruleDto, @MappingTarget Rule rule);

}
