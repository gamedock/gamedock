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

package io.gamedock.web.interceptors;

import io.gamedock.domain.Organization;
import io.gamedock.services.repositories.OrganizationRepository;
import java.util.regex.Pattern;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Base64Utils;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

public class OrganizationAuthorizationInterceptor extends HandlerInterceptorAdapter {

    @Autowired
    private OrganizationRepository organizationRepository;

    @Override
    public boolean preHandle(HttpServletRequest req, HttpServletResponse res, Object o) throws Exception {
        String header = req.getHeader("Authorization");
        if (header == null) {
            res.sendError(HttpServletResponse.SC_UNAUTHORIZED);
            return false;
        }

        String authorization = new String(Base64Utils.decodeFromString(header));
        String[] values = Pattern.compile(":").split(authorization, 3);

        Long organizationId = -1L;
        try {
            organizationId = Long.parseLong(values[1]);
        } catch (NumberFormatException e) {
        }

        Organization organization = organizationRepository.findByIdAndApplication_Id(organizationId, (Long) req.getAttribute("applicationId"));
        if (organization == null) {
            res.sendError(HttpServletResponse.SC_UNAUTHORIZED);
            return false;
        }

        req.setAttribute("organizationId", organization.getId());
        return true;
    }

}
