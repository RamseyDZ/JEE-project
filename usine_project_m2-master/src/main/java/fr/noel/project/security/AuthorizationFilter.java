package fr.noel.project.security;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

@Slf4j
public class AuthorizationFilter extends OncePerRequestFilter {


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        response.addHeader("Access-Control-Allow-Origin", "*");
        response.addHeader("Access-Control-Allow-Headers", "Origin, Accept, X-Requested-With, Content-Type, Access-Contol-Request-Method, Access-Control-Request-Headers,Authorization");
        response.addHeader("Access-Control-Expose-Headers", "Access-Control-Allow-Origin,Access-Control-Allow-Credentials,Authorization");
        response.setHeader("Access-Control-Allow-Methods", "ACL, CANCELUPLOAD, CHECKIN, CHECKOUT, COPY, DELETE, GET, HEAD, LOCK, MKCALENDAR, MKCOL, MOVE, OPTIONS, POST, PROPFIND, PROPPATCH, PUT, REPORT, SEARCH, UNCHECKOUT, UNLOCK, UPDATE, VERSION-CONTROL");

        String jwt = request.getHeader(SecurityUtil.TOKEN_STRING);

        if (request.getMethod().equals("OPTIONS")) {
            response.setStatus(HttpServletResponse.SC_OK);
        } else {
            if (jwt == null || !jwt.startsWith(SecurityUtil.TOKEN_PREFIX)) {
                filterChain.doFilter(request, response);
                return;
            }

            Claims claims = null;
            try {
                claims = Jwts.parser().setSigningKey(SecurityUtil.SECRET)
                        .parseClaimsJws(jwt.replace(SecurityUtil.TOKEN_PREFIX, "")).getBody();
            } catch (ExpiredJwtException e) {
                request.setAttribute("expired", e.getMessage());
                log.info("Error : " + e.getMessage());
            }

            if (claims != null) {
                String username = claims.getSubject();
                ArrayList<Map<String, String>> roles = (ArrayList<Map<String, String>>) claims.get("roles");
                Collection<GrantedAuthority> authorities = new ArrayList<>();
                roles.forEach(role -> {
                    authorities.add((new SimpleGrantedAuthority("ROLE_"+role.get("authority"))));
                });
                System.out.println("Authorities granted : " + authorities);
                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, null, authorities);
                System.out.println("authenticationToken authenticationToken : " + authenticationToken);
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }

            filterChain.doFilter(request, response);

        }
    }
}
