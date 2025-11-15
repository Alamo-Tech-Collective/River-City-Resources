import riverCityResources.UserPasswordEncoderListener
import riverCityResources.CustomAuthenticationSuccessHandler
// Place your Spring DSL code here
beans = {
    userPasswordEncoderListener(UserPasswordEncoderListener)
    
    // Custom authentication success handler
    authenticationSuccessHandler(CustomAuthenticationSuccessHandler) {
        springSecurityService = ref('springSecurityService')
    }
}