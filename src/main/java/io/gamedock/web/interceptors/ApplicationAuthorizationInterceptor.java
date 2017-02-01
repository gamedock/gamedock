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

import io.gamedock.domain.Application;
import io.gamedock.services.repositories.ApplicationRepository;
import java.util.regex.Pattern;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Base64Utils;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

public class ApplicationAuthorizationInterceptor extends HandlerInterceptorAdapter {

    @Autowired
    private ApplicationRepository applicationRepository;

    @Override
    public boolean preHandle(HttpServletRequest req, HttpServletResponse res, Object o) throws Exception {
        String header = req.getHeader("Authorization");
        if (header == null) {
            res.sendError(HttpServletResponse.SC_UNAUTHORIZED);
            return false;
        }

        String authorization = new String(Base64Utils.decodeFromString(header));
        String[] values = Pattern.compile(":").split(authorization, 2);

        Long applicationId = -1L;
        try {
            applicationId = Long.parseLong(values[0]);
        } catch (NumberFormatException e) {
        }

        Application application = applicationRepository.findOne(applicationId);
        if (application == null) {
            res.sendError(HttpServletResponse.SC_UNAUTHORIZED);
            return false;
        }

        req.setAttribute("applicationId", application.getId());
        return true;
    }

}
