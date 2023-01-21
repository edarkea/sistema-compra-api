package org.edarkea.compra.api.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class JWTAuthorizationFilter extends OncePerRequestFilter {

    private final String HEADER = "Authorization";
    private final String PREFIX = "Bearer ";
    public final String SECRET = "yLbbe1hDthlyDpqhaOhRCDcW7iMV0CXA8zodF880m7fQivWTh3";

    @Override
    protected void doFilterInternal(HttpServletRequest peticion, HttpServletResponse respuesta, FilterChain filtroCadena) throws ServletException, IOException {
        try {
            if (validarJWTToken(peticion, respuesta)) {
                Claims claims = validarToken(peticion);
                if (claims.get("authorities") != null) {
                    cargarTokenSpringAutorizacion(claims);
                } else {
                    SecurityContextHolder.clearContext();
                }
            } else {
                SecurityContextHolder.clearContext();
            }

            filtroCadena.doFilter(peticion, respuesta);
        } catch (ExpiredJwtException | UnsupportedJwtException | MalformedJwtException e) {
            respuesta.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            ((HttpServletResponse) respuesta).sendError(HttpServletResponse.SC_UNAUTHORIZED, e.getMessage());
        }
    }

    private Claims validarToken(HttpServletRequest peticion) {
        String jwtToken = peticion.getHeader(HEADER).replace(PREFIX, "");
        return Jwts.parser().setSigningKey(SECRET.getBytes()).parseClaimsJws(jwtToken).getBody();
    }

    private void cargarTokenSpringAutorizacion(Claims claims) {
        @SuppressWarnings("unchecked")
        List<String> autorizaciones = (List) claims.get("authorities");

        UsernamePasswordAuthenticationToken auth
                = new UsernamePasswordAuthenticationToken(
                        claims.getSubject(), null,
                        autorizaciones.stream()
                                .map(SimpleGrantedAuthority::new)
                                .collect(Collectors.toList()));

        SecurityContextHolder.getContext().setAuthentication(auth);
    }

    private boolean validarJWTToken(HttpServletRequest peticion, HttpServletResponse respuesta) {
        final String autorizacionCabecera = peticion.getHeader(HEADER);
        return !(autorizacionCabecera == null || !autorizacionCabecera.startsWith(PREFIX));
    }

}
