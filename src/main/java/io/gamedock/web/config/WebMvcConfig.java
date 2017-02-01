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

package io.gamedock.web.config;

import io.gamedock.web.interceptors.ApplicationAuthorizationInterceptor;
import io.gamedock.web.interceptors.OrganizationAuthorizationInterceptor;
import io.gamedock.web.interceptors.ParentGameInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
public class WebMvcConfig extends WebMvcConfigurerAdapter {

    public static final String API_PREFIX = "/api/";

    @Bean
    public ApplicationAuthorizationInterceptor getApplicationAuthorizationInterceptor() {
        return new ApplicationAuthorizationInterceptor();
    }

    @Bean
    public OrganizationAuthorizationInterceptor getOrganizationAuthorizationInterceptor() {
        return new OrganizationAuthorizationInterceptor();
    }

    @Bean
    public ParentGameInterceptor getParentGameInterceptor() {
        return new ParentGameInterceptor();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        registry.addInterceptor(getApplicationAuthorizationInterceptor())
                .addPathPatterns(API_PREFIX.concat("**"))
                .excludePathPatterns(API_PREFIX.concat("applications"), API_PREFIX.concat("applications/*"));
        registry.addInterceptor(getOrganizationAuthorizationInterceptor())
                .addPathPatterns(API_PREFIX.concat("**"))
                .excludePathPatterns(API_PREFIX.concat("applications"), API_PREFIX.concat("applications/*"), API_PREFIX.concat("organizations"), API_PREFIX.concat("organizations/*"));
        registry.addInterceptor(getParentGameInterceptor())
                .addPathPatterns(
                        API_PREFIX.concat("teams"), API_PREFIX.concat("teams/*"),
                        API_PREFIX.concat("rules"), API_PREFIX.concat("rules/*"),
                        API_PREFIX.concat("leaderboards"), API_PREFIX.concat("leaderboards/*"),
                        API_PREFIX.concat("metrics"), API_PREFIX.concat("metrics/*"),
                        API_PREFIX.concat("points"), API_PREFIX.concat("points/*"),
                        API_PREFIX.concat("sets"), API_PREFIX.concat("sets/*"),
                        API_PREFIX.concat("states"), API_PREFIX.concat("states/*"),
                        API_PREFIX.concat("scopes"), API_PREFIX.concat("scopes/*"),
                        API_PREFIX.concat("scores"), API_PREFIX.concat("scores/*"),
                        API_PREFIX.concat("activities"), API_PREFIX.concat("activities/*"),
                        API_PREFIX.concat("traits"), API_PREFIX.concat("traits/*"),
                        API_PREFIX.concat("plays"), API_PREFIX.concat("plays/*"));
    }

}
