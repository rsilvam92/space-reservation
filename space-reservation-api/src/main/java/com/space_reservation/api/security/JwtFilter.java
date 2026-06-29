package com.space_reservation.api.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final JwtService jwtService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String path = request.getRequestURI();
        if (path.contains("/api/auth/")) {
            filterChain.doFilter(request, response);
            return;
        }

        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = authHeader.substring(7);

        try {
            String email = jwtService.extractEmail(token);
            String role = jwtService.extractRole(token);

            if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                List<SimpleGrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("ROLE_" + role));

                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        email,
                        null,
                        authorities
                );

                SecurityContextHolder.getContext().setAuthentication(authToken);

                request.setAttribute("userRole", role);
            }

            filterChain.doFilter(request, response);

        } catch (io.jsonwebtoken.security.SignatureException | io.jsonwebtoken.MalformedJwtException ex) {
            System.out.println("Error validando el token JWT: " + ex.getMessage());
            convertirExcepcionEnJson(response, request, "TOKEN_INVALIDO", "Error validando el token JWT: " + ex.getMessage());

        } catch (io.jsonwebtoken.ExpiredJwtException ex) {
            System.out.println("Error validando el token JWT: El token ha expirado.");
            convertirExcepcionEnJson(response, request, "TOKEN_EXPIRADO", "El token JWT ha expirado.");
        }
    }

    private void convertirExcepcionEnJson(HttpServletResponse response, HttpServletRequest request, String errorType, String mensaje) throws IOException {
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");

        Map<String, Object> errorBody = new HashMap<>();
        errorBody.put("timestamp", LocalDateTime.now().toString());
        errorBody.put("status", HttpStatus.UNAUTHORIZED.value());
        errorBody.put("error", errorType);
        errorBody.put("message", mensaje);
        errorBody.put("path", request.getRequestURI());

        ObjectMapper mapper = new ObjectMapper();
        response.getWriter().write(mapper.writeValueAsString(errorBody));
    }
}