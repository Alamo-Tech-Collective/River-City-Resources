package riverCityResources

import grails.testing.web.controllers.ControllerUnitTest
import grails.testing.gorm.DomainUnitTest
import spock.lang.Specification
import grails.plugin.springsecurity.SpringSecurityService

class AdminControllerSpec extends Specification implements ControllerUnitTest<AdminController>, DomainUnitTest<Resource> {

    def setup() {
        // Mock the domain classes
        mockDomains(Resource, Category, User, Role, UserRole, Contact, EligibilityRequirement)
    }

    def cleanup() {
    }

    void "test index action returns correct model"() {
        given: "Some test data"
        def category = new Category(
            name: 'Test Category',
            description: 'Test Description',
            displayOrder: 1,
            active: true
        ).save(flush: true, validate: false)
        
        def resource1 = new Resource(
            name: 'Test Resource 1',
            description: 'Description 1',
            servicesOffered: 'Services 1',
            hoursOfOperation: 'Hours 1',
            featured: true,
            category: category
        ).save(flush: true, validate: false)
        
        def resource2 = new Resource(
            name: 'Test Resource 2',
            description: 'Description 2',
            servicesOffered: 'Services 2',
            hoursOfOperation: 'Hours 2',
            featured: false,
            category: category
        ).save(flush: true, validate: false)
        
        def user = new User(
            username: 'admin',
            password: 'password',
            email: 'admin@example.com',
            firstName: 'Admin',
            lastName: 'User',
            enabled: true
        ).save(flush: true, validate: false)

        when: "The index action is called"
        def model = controller.index()

        then: "The model contains correct counts"
        model.resourceCount == 2
        model.categoryCount == 1
        model.userCount == 1
        model.featuredResources.size() == 1
        model.featuredResources[0].name == 'Test Resource 1'
    }

    void "test index action with no data"() {
        when: "The index action is called with no data"
        def model = controller.index()

        then: "The model contains zero counts"
        model.resourceCount == 0
        model.categoryCount == 0
        model.userCount == 0
        model.featuredResources.size() == 0
    }
}