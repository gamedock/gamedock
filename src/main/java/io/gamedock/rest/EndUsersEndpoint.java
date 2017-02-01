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

import io.gamedock.domain.EndUser;
import io.gamedock.domain.Organization;
import io.gamedock.dto.EndUserDetailsDto;
import io.gamedock.dto.EndUserDto;
import io.gamedock.dto.EndUserSummaryDto;
import io.gamedock.dto.mappers.EndUserMapper;
import io.gamedock.rest.exception.ResourceNotFoundException;
import io.gamedock.rest.pagination.PaginationHeaders;
import io.gamedock.services.repositories.EndUserRepository;
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
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = WebMvcConfig.API_PREFIX + "endusers")
public class EndUsersEndpoint {

    private final EndUserRepository endUserRepository;

    private final OrganizationRepository organizationRepository;

    private final HttpServletRequest request;

    private final EndUserMapper endUserMapper;

    @Autowired
    public EndUsersEndpoint(EndUserRepository endUserRepository, OrganizationRepository organizationRepository, HttpServletRequest request, EndUserMapper endUserMapper) {
        this.endUserRepository = endUserRepository;
        this.organizationRepository = organizationRepository;
        this.request = request;
        this.endUserMapper = endUserMapper;
    }

    @RequestMapping(path = "", method = RequestMethod.GET)
    public HttpEntity getEndUsers(Pageable pgbl) {
        Long organizationId = (Long) request.getAttribute("organizationId");
        Page<EndUser> page = endUserRepository.findByOrganization_Id(organizationId, pgbl);
        List<EndUserSummaryDto> body = endUserMapper.toSummaryDto(page.getContent());
        HttpHeaders headers = PaginationHeaders.create(page.getNumber(), page.getSize(), page.getTotalElements());
        return new ResponseEntity(body, headers, HttpStatus.OK);
    }

    @RequestMapping(value = "", method = RequestMethod.POST)
    public HttpEntity createEndUser(@Validated @RequestBody EndUserDto endUserDto) {
        Long organizationId = (Long) request.getAttribute("organizationId");
        Organization organization = organizationRepository.findOne(organizationId);

        EndUser endUser = endUserMapper.endUserDtoToEndUser(endUserDto);
        endUser.setOrganization(organization);
        endUserRepository.save(endUser);

        EndUserDetailsDto body = endUserMapper.toDetailsDto(endUser, new EndUserDetailsDto());
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(body.getHref());
        return new ResponseEntity(body, headers, HttpStatus.CREATED);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public EndUserDetailsDto getEndUser(@PathVariable Long id) throws ResourceNotFoundException {
        Long organizationId = (Long) request.getAttribute("organizationId");
        EndUser endUser = endUserRepository.findByIdAndOrganization_Id(id, organizationId);
        if (endUser == null) {
            throw new ResourceNotFoundException();
        }
        return endUserMapper.toDetailsDto(endUser, new EndUserDetailsDto());
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    @ResponseStatus(HttpStatus.OK)
    public EndUserDetailsDto updateEndUser(@Validated @RequestBody EndUserDto endUserDto, @PathVariable Long id) throws ResourceNotFoundException {
        Long organizationId = (Long) request.getAttribute("organizationId");
        EndUser endUser = endUserRepository.findByIdAndOrganization_Id(id, organizationId);
        if (endUser == null) {
            throw new ResourceNotFoundException();
        }
        endUserMapper.updateEndUserFromDto(endUserDto, endUser);
        endUserRepository.save(endUser);
        return endUserMapper.toDetailsDto(endUser, new EndUserDetailsDto());
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteEndUser(@PathVariable Long id) throws ResourceNotFoundException {
        Long organizationId = (Long) request.getAttribute("organizationId");
        EndUser endUser = endUserRepository.findByIdAndOrganization_Id(id, organizationId);
        if (endUser == null) {
            throw new ResourceNotFoundException();
        }
        endUserRepository.delete(endUser);
    }

}
