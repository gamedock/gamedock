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

import io.gamedock.domain.Application;
import io.gamedock.dto.ApplicationDetailsDto;
import io.gamedock.dto.ApplicationSummaryDto;
import io.gamedock.dto.mappers.ApplicationMapper;
import io.gamedock.rest.ApplicationsEndpoint;
import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import org.springframework.stereotype.Component;

@Component
@Primary
public abstract class ApplicationMapperDecorator implements ApplicationMapper {

    @Autowired
    private ApplicationMapper delegate;

    public ApplicationMapperDecorator() {
    }

    @Override
    public ApplicationSummaryDto toSummaryDto(Application application) {
        ApplicationSummaryDto dto = delegate.toSummaryDto(application);
        dto.setHref(URI.create(linkTo(ApplicationsEndpoint.class).slash(application).withSelfRel().getHref()));
        return dto;
    }

    @Override
    public List<ApplicationSummaryDto> toSummaryDto(List<Application> applications) {
        return applications.stream().map(a -> toSummaryDto(a)).collect(Collectors.toList());
    }

    @Override
    public ApplicationDetailsDto toDetailsDto(Application application, ApplicationDetailsDto applicationDto) {
        ApplicationDetailsDto dto = delegate.toDetailsDto(application, applicationDto);
        dto.setHref(URI.create(linkTo(ApplicationsEndpoint.class).slash(application).withSelfRel().getHref()));
        return dto;
    }

}
