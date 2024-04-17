package ru.ellaid.jwt.auth.filter

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.User
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.web.filter.OncePerRequestFilter
import ru.ellaid.jwt.auth.helper.JwtAuthHelper

class JwtAuthFilter(
    private val jwtAuthHelper: JwtAuthHelper
): OncePerRequestFilter() {

    companion object {
        private const val BEARER_PREFIX = "Bearer "
        private const val HEADER_NAME = "Authorization"
    }

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {

        val authHeader = request.getHeader(HEADER_NAME)
        if (authHeader == null || authHeader.isEmpty() || !authHeader.startsWith(BEARER_PREFIX)) {
            filterChain.doFilter(request, response)
            return
        }

        val token = authHeader.substring(BEARER_PREFIX.length)

        if (SecurityContextHolder.getContext().authentication == null) {
            val user = User.builder()
                .username(jwtAuthHelper.extractUsername(token))
                .password("")
                .roles(jwtAuthHelper.extractRole(token))
                .build()

            val authToken = UsernamePasswordAuthenticationToken(
                user,
                null,
                user.authorities
            ).apply {
                details = WebAuthenticationDetailsSource().buildDetails(request)
            }

            val context = SecurityContextHolder.createEmptyContext().apply {
                authentication = authToken
            }

            SecurityContextHolder.setContext(context)
        }

        filterChain.doFilter(request, response)
    }
}
