package com.moneyflow.flow.configuration;

import com.moneyflow.flow.domain.User;
import com.moneyflow.flow.repository.UserRepository;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import java.io.IOException;
import java.util.Objects;
import java.util.Optional;

public class AuthFilter extends BasicAuthenticationFilter {

    private final UserRepository userRepository;

    public AuthFilter(AuthenticationManager authenticationManager, UserRepository userRepository) {
        super(authenticationManager);
        this.userRepository = userRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest req,
                                    HttpServletResponse res,
                                    FilterChain chain) throws IOException, ServletException {

        String header = req.getHeader("Authorization");

        if (Objects.isNull(header) || !StringUtils.startsWith(header, "Bearer ")) {
            chain.doFilter(req, res);
            return;
        }

        UsernamePasswordAuthenticationToken authentication = getAuthentication(req, header);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        chain.doFilter(req, res);
    }

    private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request, String token) {

        final String bearerToken = token.replace("Bearer ", "");

        final String usernameToken = Optional.of(bearerToken)
                .map(JwtTokenValidator::getUsername)
                .orElse(StringUtils.EMPTY);

        final String username = JwtTokenValidator.getUsername(bearerToken);

        final User user = userRepository.loadUserByUsername(username);

        if (StringUtils.isNotEmpty(usernameToken) && JwtTokenValidator.isValidToken(bearerToken, user)) {
            final UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                    usernameToken,
                    null,
                    user.getAuthorities());
            auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            return auth;
        }

        return null;
    }

}
