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
 */

package ru.l3r8y.springxrolesauthoritiesstarter.filter

import jakarta.servlet.FilterChain
import jakarta.servlet.ServletException
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.filter.OncePerRequestFilter
import java.io.IOException
import java.util.*

class XRolesAuthenticationFilter : OncePerRequestFilter() {

    private companion object {
        const val X_ROLES_HEADER = "X-Roles"
    }

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        chain: FilterChain
    ) {
        val header = request.getHeader(X_ROLES_HEADER) ?: run { chain.doFilter(request, response); return }
        val roles = header.split(",")
        val authorities = roles
            .map { it.trim() }
            .map { it.lowercase(Locale.ROOT) }
            .map { SimpleGrantedAuthority(it) }
            .toList()
        if (authorities.isEmpty()) {
            chain.doFilter(request, response)
            return
        }
        val authorized = SecurityContextHolder.getContext().authentication ?: run { chain.doFilter(request, response); return }
        val withXRoles = UsernamePasswordAuthenticationToken(
            authorized.principal,
            authorized.credentials,
            authorities
        )
        SecurityContextHolder.getContext().apply {
            this.authentication = withXRoles
        }
        chain.doFilter(request, response)
    }
}
