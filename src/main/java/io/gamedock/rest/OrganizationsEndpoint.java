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
import io.gamedock.domain.Organization;
import io.gamedock.dto.OrganizationDetailsDto;
import io.gamedock.dto.OrganizationDto;
import io.gamedock.dto.OrganizationSummaryDto;
import io.gamedock.dto.mappers.OrganizationMapper;
import io.gamedock.rest.exception.ResourceNotFoundException;
import io.gamedock.rest.pagination.PaginationHeaders;
import io.gamedock.services.repositories.ApplicationRepository;
import io.gamedock.services.repositories.OrganizationRepository;
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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.validation.annotation.Validated;

@RestController
@RequestMapping(path = WebMvcConfig.API_PREFIX + "organizations")
public class OrganizationsEndpoint {

    private final OrganizationRepository organizationRepository;

    private final ApplicationRepository applicationRepository;

    private final HttpServletRequest request;

    private final OrganizationMapper organizationMapper;

    @Autowired
    public OrganizationsEndpoint(OrganizationRepository organizationRepository, ApplicationRepository applicationRepository, HttpServletRequest request, OrganizationMapper organizationMapper) {
        this.organizationRepository = organizationRepository;
        this.applicationRepository = applicationRepository;
        this.request = request;
        this.organizationMapper = organizationMapper;
    }

    @RequestMapping(path = "", method = RequestMethod.GET)
    public HttpEntity getOrganizations(Pageable pgbl) {
        Long applicationId = (Long) request.getAttribute("applicationId");
        Page<Organization> page = organizationRepository.findByApplication_Id(applicationId, pgbl);
        List<OrganizationSummaryDto> body = organizationMapper.toSummaryDto(page.getContent());
        HttpHeaders headers = PaginationHeaders.create(page.getNumber(), page.getSize(), page.getTotalElements());
        return new ResponseEntity(body, headers, HttpStatus.OK);
    }

    @RequestMapping(value = "", method = RequestMethod.POST)
    public HttpEntity createOrganization(@Validated @RequestBody OrganizationDto organizationDto) {
        Long applicationId = (Long) request.getAttribute("applicationId");
        Application application = applicationRepository.findOne(applicationId);
        Organization organization = organizationMapper.organizationDtoToOrganization(organizationDto);
        organization.setApplication(application);
        organizationRepository.save(organization);
        OrganizationDetailsDto body = organizationMapper.toDetailsDto(organization, new OrganizationDetailsDto());
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(body.getHref());
        return new ResponseEntity(body, headers, HttpStatus.CREATED);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public OrganizationDetailsDto getOrganization(@PathVariable Long id) throws ResourceNotFoundException {
        Long applicationId = (Long) request.getAttribute("applicationId");
        Organization organization = organizationRepository.findByIdAndApplication_Id(id, applicationId);
        if (organization == null) {
            throw new ResourceNotFoundException();
        }
        return organizationMapper.toDetailsDto(organization, new OrganizationDetailsDto());
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    @ResponseStatus(HttpStatus.OK)
    public OrganizationDetailsDto updateOrganization(@Validated @RequestBody OrganizationDto organizationDto, @PathVariable Long id) throws ResourceNotFoundException {
        Long applicationId = (Long) request.getAttribute("applicationId");
        Organization organization = organizationRepository.findByIdAndApplication_Id(id, applicationId);
        if (organization == null) {
            throw new ResourceNotFoundException();
        }
        organizationMapper.updateOrganizationFromDto(organizationDto, organization);
        organizationRepository.save(organization);
        return organizationMapper.toDetailsDto(organization, new OrganizationDetailsDto());
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteOrganization(@PathVariable Long id) throws ResourceNotFoundException {
        Long applicationId = (Long) request.getAttribute("applicationId");
        Organization organization = organizationRepository.findByIdAndApplication_Id(id, applicationId);
        if (organization == null) {
            throw new ResourceNotFoundException();
        }
        organizationRepository.delete(organization);
    }

}
