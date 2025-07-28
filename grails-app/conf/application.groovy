// Spring Security Core plugin configuration
grails.plugin.springsecurity.userLookup.userDomainClassName = 'riverCityResources.User'
grails.plugin.springsecurity.userLookup.authorityJoinClassName = 'riverCityResources.UserRole'
grails.plugin.springsecurity.authority.className = 'riverCityResources.Role'

// Security Rules
grails.plugin.springsecurity.controllerAnnotations.staticRules = [
    [pattern: '/',               access: ['permitAll']],
    [pattern: '/error',          access: ['permitAll']],
    [pattern: '/index',          access: ['permitAll']],
    [pattern: '/index.gsp',      access: ['permitAll']],
    [pattern: '/shutdown',       access: ['permitAll']],
    [pattern: '/assets/**',      access: ['permitAll']],
    [pattern: '/**/js/**',       access: ['permitAll']],
    [pattern: '/**/css/**',      access: ['permitAll']],
    [pattern: '/**/images/**',   access: ['permitAll']],
    [pattern: '/**/favicon.ico', access: ['permitAll']],
    
    // Public access to directory pages
    [pattern: '/directory/**',   access: ['permitAll']],
    
    // Language switching (must be public)
    [pattern: '/language/**',    access: ['permitAll']],
    
    // SEO files (must be public)
    [pattern: '/robots.txt',     access: ['permitAll']],
    
    // Admin only access
    [pattern: '/resource/**',    access: ['ROLE_ADMIN']],
    [pattern: '/category/**',    access: ['ROLE_ADMIN']],
    [pattern: '/admin/**',       access: ['ROLE_ADMIN']],
    [pattern: '/user/**',        access: ['ROLE_ADMIN']],
    [pattern: '/role/**',        access: ['ROLE_ADMIN']],
    
    // Login/Logout
    [pattern: '/login/**',       access: ['permitAll']],
    [pattern: '/logout/**',      access: ['permitAll']]
]

// Additional security settings
grails.plugin.springsecurity.logout.postOnly = false
grails.plugin.springsecurity.successHandler.alwaysUseDefault = true
grails.plugin.springsecurity.successHandler.defaultTargetUrl = '/admin'
grails.plugin.springsecurity.failureHandler.defaultFailureUrl = '/login/authfail?login_error=1'

// Password encoding
grails.plugin.springsecurity.password.algorithm = 'bcrypt'
grails.plugin.springsecurity.password.bcrypt.logrounds = 10

// Remember me
grails.plugin.springsecurity.rememberMe.enabled = true
grails.plugin.springsecurity.rememberMe.cookieName = 'river_city_resources_remember_me'
grails.plugin.springsecurity.rememberMe.key = 'riverCityResourcesKey'

// Session fixation prevention
grails.plugin.springsecurity.useSessionFixationPrevention = true

// Filter chain
grails.plugin.springsecurity.filterChain.chainMap = [
    [pattern: '/assets/**',      filters: 'none'],
    [pattern: '/**/js/**',       filters: 'none'],
    [pattern: '/**/css/**',      filters: 'none'],
    [pattern: '/**/images/**',   filters: 'none'],
    [pattern: '/**/favicon.ico', filters: 'none'],
    [pattern: '/**',             filters: 'JOINED_FILTERS']
]
