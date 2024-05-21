/*
 * MIT License
 *
 * Copyright (c) 2024 Ivan Ivanchuk
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 *
 */

package ru.l3r8y.springxrolesauthoritiesstarter.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Stream;

public class XRolesAuthenticationFilter extends OncePerRequestFilter {

    private static final String X_ROLES_HEADER = "X-Roles";

    @Override
    protected void doFilterInternal(
        final HttpServletRequest request,
        final HttpServletResponse response,
        final FilterChain chain
    ) throws ServletException, IOException {
        Optional.ofNullable(request.getHeader(XRolesAuthenticationFilter.X_ROLES_HEADER))
            .ifPresent(
                roles -> {
                    final List<SimpleGrantedAuthority> authorities = Stream
                        .of(roles.split(","))
                        .map(String::trim)
                        .map(role -> role.toLowerCase(Locale.ROOT))
                        .map(SimpleGrantedAuthority::new)
                        .toList();
                    Optional.ofNullable(
                        SecurityContextHolder
                            .getContext()
                            .getAuthentication()
                        )
                            .ifPresent(
                                authorized -> {
                                    final Authentication withXRoles =
                                        new UsernamePasswordAuthenticationToken(
                                            authorized.getPrincipal(),
                                            authorized.getCredentials(),
                                            authorities
                                        );
                                    SecurityContextHolder
                                        .getContext()
                                        .setAuthentication(withXRoles);
                                }
                            );
                }
            );
        chain.doFilter(request, response);
    }
}
