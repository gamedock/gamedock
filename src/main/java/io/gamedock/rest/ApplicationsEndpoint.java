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

import io.gamedock.domain.Application;
import io.gamedock.dto.ApplicationDetailsDto;
import io.gamedock.dto.ApplicationDto;
import io.gamedock.dto.ApplicationSummaryDto;
import io.gamedock.dto.mappers.ApplicationMapper;
import io.gamedock.rest.exception.ResourceNotFoundException;
import io.gamedock.rest.pagination.PaginationHeaders;
import io.gamedock.services.repositories.ApplicationRepository;
import io.gamedock.web.config.WebMvcConfig;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.validation.annotation.Validated;

@RestController
@RequestMapping(path = WebMvcConfig.API_PREFIX + "applications")
public class ApplicationsEndpoint {

    private final ApplicationRepository applicationRepository;

    private final ApplicationMapper applicationMapper;

    @Autowired
    public ApplicationsEndpoint(ApplicationRepository applicationRepository, ApplicationMapper applicationMapper) {
        this.applicationRepository = applicationRepository;
        this.applicationMapper = applicationMapper;
    }

    @RequestMapping(path = "", method = RequestMethod.GET)
    public HttpEntity getApplications(Pageable pgbl) {
        Page<Application> page = applicationRepository.findAll(pgbl);
        List<ApplicationSummaryDto> body = applicationMapper.toSummaryDto(page.getContent());
        HttpHeaders headers = PaginationHeaders.create(page.getNumber(), page.getSize(), page.getTotalElements());
        return new ResponseEntity(body, headers, HttpStatus.OK);
    }

    @RequestMapping(value = "", method = RequestMethod.POST)
    public HttpEntity createApplication(@Validated @RequestBody ApplicationDto applicationDto) {
        Application application = applicationMapper.applicationDtoToApplication(applicationDto);
        applicationRepository.save(application);
        ApplicationDetailsDto body = applicationMapper.toDetailsDto(application, new ApplicationDetailsDto());
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(body.getHref());
        return new ResponseEntity(body, headers, HttpStatus.CREATED);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public ApplicationDetailsDto getApplication(@PathVariable Long id) throws ResourceNotFoundException {
        Application application = applicationRepository.findOne(id);
        if (application == null) {
            throw new ResourceNotFoundException();
        }
        return applicationMapper.toDetailsDto(application, new ApplicationDetailsDto());
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    @ResponseStatus(HttpStatus.OK)
    public ApplicationDetailsDto updateApplication(@Validated @RequestBody ApplicationDto applicationDto, @PathVariable Long id) throws ResourceNotFoundException {
        Application application = applicationRepository.findOne(id);
        if (application == null) {
            throw new ResourceNotFoundException();
        }
        applicationMapper.updateApplicationFromDto(applicationDto, application);
        applicationRepository.save(application);
        return applicationMapper.toDetailsDto(application, new ApplicationDetailsDto());
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteApplication(@PathVariable Long id) throws ResourceNotFoundException {
        Application application = applicationRepository.findOne(id);
        if (application == null) {
            throw new ResourceNotFoundException();
        }
        applicationRepository.delete(application);
    }

}
