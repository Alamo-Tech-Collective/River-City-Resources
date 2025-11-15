package riverCityResources

import grails.plugin.springsecurity.SpringSecurityService
import groovy.transform.CompileStatic
import org.springframework.security.core.Authentication
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler

import javax.servlet.ServletException
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import java.io.IOException

@CompileStatic
class CustomAuthenticationSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

    SpringSecurityService springSecurityService

    @Override
    void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                 Authentication authentication) throws ServletException, IOException {
        
        // Get authorities from authentication object
        def authorities = authentication.authorities
        
        // Check if user has ROLE_PROVIDER
        def isProvider = authorities?.any { it.authority == 'ROLE_PROVIDER' }
        // Check if user has ROLE_ADMIN
        def isAdmin = authorities?.any { it.authority == 'ROLE_ADMIN' }
        
        // Redirect based on role
        if (isProvider && !isAdmin) {
            // Provider (not admin) -> redirect to /provider
            setDefaultTargetUrl('/provider')
        } else if (isAdmin) {
            // Admin (may also have provider role) -> redirect to /admin
            setDefaultTargetUrl('/admin')
        } else {
            // Fallback to default
            setDefaultTargetUrl('/')
        }
        
        super.onAuthenticationSuccess(request, response, authentication)
    }
}

