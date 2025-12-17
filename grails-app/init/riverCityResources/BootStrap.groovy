package riverCityResources

class BootStrap {
    def bootstrapService

    def init = { servletContext ->
        environments {
            development {
//                bootstrapService.createInitialSecurityRoles()
//                bootstrapService.createInitialAdminUser()
//                bootstrapService.createProviderUsers()
//                bootstrapService.createInitialCategories()
//                bootstrapService.createComprehensiveTestData()
//                bootstrapService.createProviderTestResources()
            }
        }
    }

    def destroy = {
    }

}