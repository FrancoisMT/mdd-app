package mdd_api.mdd_api.filter;

import java.io.IOException;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import mdd_api.mdd_api.services.JWTService;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

	private final HandlerExceptionResolver handlerExceptionResolver;
	private final JWTService jwtService;
    private final UserDetailsService userDetailsService;
    
    public JwtAuthenticationFilter(JWTService jwtService, 
    		UserDetailsService userDetailsService, 
    		HandlerExceptionResolver handlerExceptionResolver) {
            this.jwtService = jwtService;
            this.userDetailsService = userDetailsService;
            this.handlerExceptionResolver = handlerExceptionResolver;
    }
    
    @Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
    	
        String authHeader = request.getHeader("Authorization");
		
		 if (authHeader == null || !authHeader.startsWith("Bearer ")) {
	            filterChain.doFilter(request, response);
	            return;
	     }
		 
		 try {
			 
			 	String jwt = authHeader.substring(7);
	            String userEmail = jwtService.extractUserMail(jwt);
	            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

	            if (userEmail != null && authentication == null) {
	                UserDetails userDetails = this.userDetailsService.loadUserByUsername(userEmail);

	                if (jwtService.isTokenValid(jwt, userDetails)) {
	                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
	                            userDetails,
	                            null,
	                            userDetails.getAuthorities()
	                    );

	                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
	                    SecurityContextHolder.getContext().setAuthentication(authToken);
	                }
	            }

	            filterChain.doFilter(request, response);
	        } catch (Exception exception) {
	        	exception.printStackTrace();
	            handlerExceptionResolver.resolveException(request, response, null, exception);
	        }
		
		
	}
    
	
	
}
