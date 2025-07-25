package riverCityResources

import org.springframework.web.servlet.support.RequestContextUtils

class LocaleInterceptor {

    int order = HIGHEST_PRECEDENCE + 50

    LocaleInterceptor() {
        matchAll()
    }

    boolean before() {
        // Check if language is set in session and apply it
        if (session.lang) {
            def locale = session.lang == 'es' ? new Locale('es') : new Locale('en', 'US')
            RequestContextUtils.getLocaleResolver(request).setLocale(request, response, locale)
        }
        return true
    }

    boolean after() { 
        return true 
    }

    void afterView() {
        // no-op
    }
}