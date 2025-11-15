package riverCityResources

import grails.plugin.springsecurity.annotation.Secured
import grails.plugin.springsecurity.SpringSecurityService

@Secured(['ROLE_PROVIDER'])
class ProviderController {

    ResourceService resourceService
    SpringSecurityService springSecurityService

    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        def currentUser = springSecurityService.currentUser as User
        def resourceList = resourceService.listForProvider(currentUser, params)
        def resourceCount = resourceService.countForProvider(currentUser)
        def isEmpty = (resourceList == null || resourceList.empty)
        respond resourceList, model:[
            resourceCount: resourceCount,
            isEmpty: isEmpty,
            isProvider: true,
            currentUser: currentUser
        ]
    }
}