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

import io.gamedock.domain.Game;
import io.gamedock.services.repositories.GameRepository;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

public class ParentGameInterceptor extends HandlerInterceptorAdapter {

    @Autowired
    private GameRepository gameRepository;

    @Override
    public boolean preHandle(HttpServletRequest req, HttpServletResponse res, Object o) throws Exception {
        Long gameId = -1L;
        try {
            gameId = Long.parseLong(req.getParameter("game"));
        } catch (Exception e) {
        }

        Game game = gameRepository.findByIdAndOrganization_Id(gameId, (Long) req.getAttribute("organizationId"));
        if (game == null) {
            res.sendError(HttpServletResponse.SC_NOT_FOUND);
            return false;
        }

        req.setAttribute("gameId", game.getId());
        return true;
    }

}
