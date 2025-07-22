package riverCityResources

import grails.testing.gorm.DomainUnitTest
import spock.lang.Specification

class ResourceSpec extends Specification implements DomainUnitTest<Resource> {

    def setup() {
        mockDomains Resource, Category, Contact, EligibilityRequirement
    }

    def cleanup() {
    }

    void "test valid resource creation"() {
        given: "A valid category"
        def category = new Category(name: 'Test Category', displayOrder: 1, active: true)
        
        when: "A valid resource is created"
        def resource = new Resource(
            name: 'Test Resource',
            description: 'This is a test resource description',
            servicesOffered: 'Service 1\nService 2',
            hoursOfOperation: 'Monday-Friday: 9AM-5PM',
            featured: false,
            active: true,
            category: category
        )

        then: "The resource is valid"
        resource.validate()
        !resource.hasErrors()
    }

    void "test name is required"() {
        given: "A valid category"
        def category = new Category(name: 'Test Category', displayOrder: 1, active: true)
        
        when: "A resource is created without a name"
        def resource = new Resource(
            description: 'This is a test resource description',
            category: category
        )

        then: "The resource is invalid"
        !resource.validate()
        resource.hasErrors()
        resource.errors['name'].code == 'nullable'
    }

    void "test description is required"() {
        given: "A valid category"
        def category = new Category(name: 'Test Category', displayOrder: 1, active: true)
        
        when: "A resource is created without a description"
        def resource = new Resource(
            name: 'Test Resource',
            category: category
        )

        then: "The resource is invalid"
        !resource.validate()
        resource.hasErrors()
        resource.errors['description'].code == 'nullable'
    }

    void "test name must be unique"() {
        given: "A valid category and existing resource"
        def category = new Category(name: 'Test Category', displayOrder: 1, active: true)
        def existingResource = new Resource(
            name: 'Existing Resource',
            description: 'Existing Description',
            category: category
        ).save(flush: true)

        when: "Another resource is created with the same name"
        def newResource = new Resource(
            name: 'Existing Resource',
            description: 'Another Description',
            category: category
        )

        then: "The new resource is invalid"
        !newResource.validate()
        newResource.hasErrors()
        newResource.errors['name'].code == 'unique'
    }

    void "test viewCount minimum value"() {
        given: "A valid category"
        def category = new Category(name: 'Test Category', displayOrder: 1, active: true)
        
        when: "A resource is created with negative viewCount"
        def resource = new Resource(
            name: 'Test Resource',
            description: 'Test Description',
            viewCount: -1,
            category: category
        )

        then: "The resource is invalid"
        !resource.validate()
        resource.hasErrors()
        resource.errors['viewCount'].code == 'min.notmet'
    }

    void "test optional fields can be null"() {
        given: "A valid category"
        def category = new Category(name: 'Test Category', displayOrder: 1, active: true)
        
        when: "A resource is created with minimal required fields"
        def resource = new Resource(
            name: 'Test Resource',
            description: 'Test Description',
            category: category
        )

        then: "The resource is valid"
        resource.validate()
        !resource.hasErrors()
        resource.servicesOffered == null
        resource.hoursOfOperation == null
        resource.featured == false
        resource.active == true
        resource.viewCount == 0
    }

    void "test name max size constraint"() {
        given: "A valid category"
        def category = new Category(name: 'Test Category', displayOrder: 1, active: true)
        
        when: "A resource is created with name exceeding max size"
        def resource = new Resource(
            name: 'a' * 256, // 256 characters, exceeds maxSize: 255
            description: 'Test Description',
            category: category
        )

        then: "The resource is invalid"
        !resource.validate()
        resource.hasErrors()
        resource.errors['name'].code == 'maxSize.exceeded'
    }

    void "test description max size constraint"() {
        given: "A valid category"
        def category = new Category(name: 'Test Category', displayOrder: 1, active: true)
        
        when: "A resource is created with description exceeding max size"
        def resource = new Resource(
            name: 'Test Resource',
            description: 'a' * 2001, // 2001 characters, exceeds maxSize: 2000
            category: category
        )

        then: "The resource is invalid"
        !resource.validate()
        resource.hasErrors()
        resource.errors['description'].code == 'maxSize.exceeded'
    }

    void "test toString method"() {
        given: "A valid category"
        def category = new Category(name: 'Test Category', displayOrder: 1, active: true)
        
        when: "A resource is created"
        def resource = new Resource(
            name: 'Test Resource',
            description: 'Test Description',
            category: category
        )

        then: "toString returns the name"
        resource.toString() == 'Test Resource'
    }

    void "test resource with contact relationship"() {
        given: "A valid category and contact"
        def category = new Category(name: 'Test Category', displayOrder: 1, active: true)
        def contact = new Contact(city: 'San Antonio', state: 'TX')
        
        when: "A resource is created with contact"
        def resource = new Resource(
            name: 'Test Resource',
            description: 'Test Description',
            category: category,
            contact: contact
        )

        then: "The resource is valid and has contact"
        resource.validate()
        !resource.hasErrors()
        resource.contact == contact
    }

    void "test resource with eligibility requirements"() {
        given: "A valid category and eligibility requirement"
        def category = new Category(name: 'Test Category', displayOrder: 1, active: true)
        def requirement = new EligibilityRequirement(
            requirement: 'Must be 18 or older',
            type: 'age'
        )
        
        when: "A resource is created with eligibility requirements"
        def resource = new Resource(
            name: 'Test Resource',
            description: 'Test Description',
            category: category
        )
        resource.addToEligibilityRequirements(requirement)

        then: "The resource is valid and has eligibility requirements"
        resource.validate()
        !resource.hasErrors()
        resource.eligibilityRequirements.size() == 1
        resource.eligibilityRequirements.first().requirement == 'Must be 18 or older'
    }
}
