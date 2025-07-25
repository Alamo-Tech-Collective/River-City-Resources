package riverCityResources

import org.springframework.web.servlet.support.RequestContextUtils

class LanguageController {

    def switchLanguage() {
        String lang = params.lang
        
        // Validate language parameter
        if (lang && (lang == 'en' || lang == 'es')) {
            session.lang = lang
            
            // Set the locale for the current request
            def locale = lang == 'es' ? new Locale('es') : new Locale('en', 'US')
            RequestContextUtils.getLocaleResolver(request).setLocale(request, response, locale)
        }
        
        // Redirect back to the referring page or home if no referrer
        String redirectUrl = request.getHeader('referer') ?: g.createLink(controller: 'directory', action: 'index')
        redirect(url: redirectUrl)
    }
}