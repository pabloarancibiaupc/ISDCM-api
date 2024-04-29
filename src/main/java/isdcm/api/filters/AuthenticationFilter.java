package isdcm.api.filters;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Base64;
import java.util.Date;


@WebFilter("/*")
public class AuthenticationFilter implements Filter {

    private final static String audience = "localhost:8080/api";
    private final static String keyBase64 = "crgvLrH16i32bEGukqrsPapijuANmfilnZS/Jhpecng3DYaVLcBvGV75116FNkjjvl6k2dIQSEDMzC7sA/eT1w==";
    
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        String path = httpRequest.getPathInfo();
        String method = httpRequest.getMethod();
        if (method.equals("POST") && path.startsWith("/usuarios")) {
            chain.doFilter(request, response);
        } else {
            String authorizationHeader = httpRequest.getHeader("Authorization");
            if (authorizationHeader == null) {
                httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                return;
            }
            String jwt = authorizationHeader.substring(7);
            System.out.println(jwt);
            Claims claims;
            try {
                claims = Jwts.parser()
                            .setSigningKey(Base64.getDecoder().decode(keyBase64))
                            .parseClaimsJws(jwt)
                            .getBody();
            } catch (JwtException e) {
                System.out.println(e);
                httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                return;
            }
            Date currentDate = new Date();
            Date expirationDate = claims.getExpiration();
            Date notBeforeDate = claims.getNotBefore();
            if (!claims.getAudience().equals(audience) ||
                expirationDate.compareTo(currentDate) < 0 ||
                notBeforeDate.compareTo(currentDate) > 0
            ) {
                httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                return;
            }
            chain.doFilter(request, response);
        }
    }
}
