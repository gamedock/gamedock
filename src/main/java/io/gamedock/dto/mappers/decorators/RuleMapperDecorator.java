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

import io.gamedock.domain.Rule;
import io.gamedock.dto.RuleDetailsDto;
import io.gamedock.dto.RuleSummaryDto;
import io.gamedock.dto.mappers.RuleMapper;
import io.gamedock.rest.GamesEndpoint;
import io.gamedock.rest.RulesEndpoint;
import io.gamedock.services.repositories.RuleRepository;
import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import org.springframework.stereotype.Component;

@Component
@Primary
public abstract class RuleMapperDecorator implements RuleMapper {

    @Autowired
    private RuleMapper delegate;

    @Autowired
    private RuleRepository ruleRepository;

    public RuleMapperDecorator() {
    }

    @Override
    public Rule ruleIdToRule(Long ruleId) {
        return ruleRepository.findById(ruleId);
    }

    @Override
    public RuleSummaryDto toSummaryDto(Rule rule) {
        RuleSummaryDto dto = delegate.toSummaryDto(rule);
        dto.setParent(URI.create(linkTo(GamesEndpoint.class).slash(rule.getGame()).withSelfRel().getHref()));
        dto.setHref(URI.create(linkTo(RulesEndpoint.class).slash(rule).withSelfRel().getHref()));
        return dto;
    }

    @Override
    public List<RuleSummaryDto> toSummaryDto(List<Rule> rules) {
        return rules.stream().map(o -> toSummaryDto(o)).collect(Collectors.toList());
    }

    @Override
    public RuleDetailsDto toDetailsDto(Rule rule, RuleDetailsDto ruleDto) {
        RuleDetailsDto dto = delegate.toDetailsDto(rule, ruleDto);
        dto.setParent(URI.create(linkTo(GamesEndpoint.class).slash(rule.getGame()).withSelfRel().getHref()));
        dto.setHref(URI.create(linkTo(RulesEndpoint.class).slash(rule).withSelfRel().getHref()));
        return dto;
    }

}
