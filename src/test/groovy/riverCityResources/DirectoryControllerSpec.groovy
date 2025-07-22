package riverCityResources

import grails.testing.web.controllers.ControllerUnitTest
import grails.testing.gorm.DomainUnitTest
import spock.lang.Specification

class DirectoryControllerSpec extends Specification implements ControllerUnitTest<DirectoryController>, DomainUnitTest<Resource> {

    def setup() {
        // Mock the domain classes
        mockDomains(Resource, Category, Contact, EligibilityRequirement, User)
    }

    def cleanup() {
    }

    void "test index action returns correct model"() {
        given: "Some test data"
        def category1 = new Category(name: 'Category 1', displayOrder: 1, active: true).save(flush: true, failOnError: true)
        def category2 = new Category(name: 'Category 2', displayOrder: 2, active: true).save(flush: true, failOnError: true)  
        def category3 = new Category(name: 'Category 3', displayOrder: 3, active: false).save(flush: true, failOnError: true)
        
        def contact1 = new Contact(phone: '123-456-7890', city: 'San Antonio', state: 'TX').save(flush: true, failOnError: true)
        def resource1 = new Resource(
            name: 'Resource 1',
            description: 'Description 1',
            featured: true,
            active: true,
            category: category1,
            contact: contact1
        ).save(flush: true, failOnError: true)
        
        def contact2 = new Contact(phone: '098-765-4321', city: 'San Antonio', state: 'TX').save(flush: true, failOnError: true)
        def resource2 = new Resource(
            name: 'Resource 2',
            description: 'Description 2',
            featured: false,
            active: true,
            category: category2,
            contact: contact2
        ).save(flush: true, failOnError: true)

        when: "The index action is called"
        def model = controller.index()

        then: "The model contains active categories and featured resources"
        model.categories.size() == 2
        model.categories*.active.every { it == true }
        model.featuredResources.size() == 1
        model.featuredResources[0].name == 'Resource 1'
    }

    void "test category action with valid category"() {
        given: "A category with resources"
        def category = new Category(name: 'Test Category', displayOrder: 1, active: true).save(flush: true, failOnError: true)
        def contact = new Contact(phone: '555-555-5555', city: 'San Antonio', state: 'TX').save(flush: true, failOnError: true)
        def resource = new Resource(
            name: 'Test Resource',
            description: 'Test Description',
            active: true,
            category: category,
            contact: contact
        ).save(flush: true, failOnError: true)

        when: "The category action is called"
        def model = controller.category(category.id)

        then: "The model contains the category and its resources"
        model.category == category
        model.resources.size() == 1
        model.resources[0].name == 'Test Resource'
    }

    void "test category action with inactive category"() {
        given: "An inactive category"
        def category = new Category(name: 'Inactive Category', displayOrder: 1, active: false).save(flush: true)

        when: "The category action is called"
        controller.category(category.id)

        then: "User is redirected to index"
        response.redirectedUrl == '/'
        flash.message == 'Category not found'
    }

    void "test category action with non-existent category"() {
        when: "The category action is called with non-existent ID"
        controller.category(999L)

        then: "User is redirected to index"
        response.redirectedUrl == '/'
        flash.message == 'Category not found'
    }

    void "test resource action with valid resource"() {
        given: "A resource"
        def category = new Category(name: 'Test Category', displayOrder: 1, active: true).save(flush: true, failOnError: true)
        def contact = new Contact(phone: '555-111-2222', city: 'San Antonio', state: 'TX').save(flush: true, failOnError: true)
        def resource = new Resource(
            name: 'Test Resource',
            description: 'Test Description',
            active: true,
            viewCount: 5,
            category: category,
            contact: contact
        ).save(flush: true, failOnError: true)

        when: "The resource action is called"
        def model = controller.resource(resource.id)

        then: "The model contains the resource and view count is incremented"
        model.resource == resource
        resource.viewCount == 6
    }

    void "test resource action with inactive resource"() {
        given: "An inactive resource"
        def category = new Category(name: 'Test Category', displayOrder: 1, active: true).save(flush: true, failOnError: true)
        def contact = new Contact(phone: '555-333-4444', city: 'San Antonio', state: 'TX').save(flush: true, failOnError: true)
        def resource = new Resource(
            name: 'Inactive Resource',
            description: 'Test Description',
            active: false,
            category: category,
            contact: contact
        ).save(flush: true, failOnError: true)

        when: "The resource action is called"
        controller.resource(resource.id)

        then: "User is redirected to index"
        response.redirectedUrl == '/'
        flash.message == 'Resource not found'
    }

    void "test resource action with non-existent resource"() {
        when: "The resource action is called with non-existent ID"
        controller.resource(999L)

        then: "User is redirected to index"
        response.redirectedUrl == '/'
        flash.message == 'Resource not found'
    }

    void "test search action with query"() {
        given: "Mock resource service"
        def mockResourceService = Mock(ResourceService) {
            1 * searchWithFilters('test', null, null, 'name') >> []
        }
        controller.resourceService = mockResourceService
        
        def category = new Category(name: 'Test Category', displayOrder: 1, active: true).save(flush: true)

        when: "The search action is called with query"
        params.q = 'test'
        def model = controller.search()

        then: "The model contains search results"
        model.query == 'test'
        model.resources != null
        model.categories.size() == 1
    }

    void "test search action with filters"() {
        given: "Mock resource service"
        def mockResourceService = Mock(ResourceService) {
            1 * searchWithFilters(null, [1L], ['age'], 'name') >> []
        }
        controller.resourceService = mockResourceService
        
        def category = new Category(name: 'Test Category', displayOrder: 1, active: true).save(flush: true)

        when: "The search action is called with filters"
        params.categoryId = 1L
        params.eligibilityType = 'age'
        def model = controller.search()

        then: "The model contains filtered results"
        model.selectedCategoryId == 1L
        model.selectedEligibilityType == 'age'
        model.resources != null
        model.categories.size() == 1
    }
}
