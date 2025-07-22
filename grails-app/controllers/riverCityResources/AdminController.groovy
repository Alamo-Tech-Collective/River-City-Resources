package riverCityResources

import grails.plugin.springsecurity.annotation.Secured

@Secured(['ROLE_ADMIN'])
class AdminController {

    def index() {
        def resourceCount = Resource.count()
        def categoryCount = Category.count()
        def userCount = User.count()
        def featuredResources = Resource.findAllByFeatured(true)
        
        [resourceCount: resourceCount, 
         categoryCount: categoryCount, 
         userCount: userCount,
         featuredResources: featuredResources]
    }
}