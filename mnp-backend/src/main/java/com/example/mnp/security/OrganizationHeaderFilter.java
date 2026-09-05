package com.example.mnp.security;

import com.example.mnp.dto.ErrorResponseDto;
import com.example.mnp.domain.Operator;
import com.example.mnp.repository.OperatorRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

@Component
public class OrganizationHeaderFilter extends OncePerRequestFilter {

    public static final String HEADER_NAME = "organization";
    private final OperatorRepository operatorRepository;

    public OrganizationHeaderFilter(OperatorRepository operatorRepository) {
        this.operatorRepository = operatorRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String requestPath = request.getRequestURI();

        if (requestPath.startsWith("/api/operators")) {
            filterChain.doFilter(request, response);
            return;
        }

        String organizationHeader = request.getHeader(HEADER_NAME);

        if (organizationHeader != null && !organizationHeader.isBlank()) {

            Optional<Operator> operatorOptional = operatorRepository.findByCode(organizationHeader.trim().toLowerCase());

            if (operatorOptional.isPresent()) {
                Operator operator = operatorOptional.get();

                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        operator,
                        null,
                        AuthorityUtils.NO_AUTHORITIES
                );

                SecurityContextHolder.getContext().setAuthentication(authentication);
            } else {
                sendErrorResponse(response, HttpServletResponse.SC_UNAUTHORIZED,
                        "Unauthorized: Invalid 'organization' header provided.");
                return;
            }
        } else {
            sendErrorResponse(response, HttpServletResponse.SC_UNAUTHORIZED,
                    "Unauthorized: Missing required 'organization' header.");
            return;
        }

        filterChain.doFilter(request, response);
    }

    private void sendErrorResponse(HttpServletResponse response, int status, String message) throws IOException {
        response.setStatus(status);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());

        ErrorResponseDto errorResponseBody = new ErrorResponseDto(
                status,
                "Unauthorized",
                message
        );

        new ObjectMapper().writeValue(response.getWriter(), errorResponseBody);
    }
}