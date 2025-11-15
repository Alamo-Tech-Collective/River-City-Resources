package riverCityResources

class UrlMappings {
    static mappings = {
        "/$controller/$action?/$id?(.$format)?"{
            constraints {
                // apply constraints here
            }
        }

        "/"(controller:"directory", action:"index")
        "500"(view:'/error')
        "404"(view:'/notFound')
        
        // SEO-friendly URLs
        "/sitemap.xml"(controller: 'sitemap', action: 'index')
        "/robots.txt"(redirect: '/robots.txt')
        
        // Admin routes
        "/admin"(controller:"admin", action:"index")
        "/admin/resources"(controller:"resource", action:"index")
        "/admin/categories"(controller:"category", action:"index")
        
        // Provider routes - redirect to resource page
        "/provider"(controller:"resource", action:"index")
        
        // Login/Logout routes (handled by Spring Security)
        "/login"(controller:"login", action:"auth")
        "/login/authfail"(controller:"login", action:"authfail")

        "/about"(view:'/directory/about')
    }
}
