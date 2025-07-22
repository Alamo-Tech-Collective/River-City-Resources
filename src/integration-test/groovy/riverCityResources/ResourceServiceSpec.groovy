package riverCityResources

import grails.testing.mixin.integration.Integration
import grails.gorm.transactions.Rollback
import spock.lang.Specification
import org.hibernate.SessionFactory

@Integration
@Rollback
class ResourceServiceSpec extends Specification {

    ResourceService resourceService
    SessionFactory sessionFactory

    Category transportationCategory
    Category employmentCategory
    Category educationCategory
    Resource resource1
    Resource resource2
    Resource resource3
    Resource resource4
    Contact contact1
    Contact contact2
    Contact contact3
    Contact contact4
    EligibilityRequirement ageRequirement
    EligibilityRequirement disabilityRequirement
    EligibilityRequirement incomeRequirement
    
    String timestamp

    def setup() {
        // Use timestamp to ensure unique names
        timestamp = System.currentTimeMillis().toString()
        
        // Setup categories
        transportationCategory = new Category(
            name: "Transportation_${timestamp}",
            description: 'Transportation services',
            displayOrder: 1,
            active: true
        ).save(flush: true, failOnError: true)

        employmentCategory = new Category(
            name: "Employment_${timestamp}",
            description: 'Employment services',
            displayOrder: 2,
            active: true
        ).save(flush: true, failOnError: true)

        educationCategory = new Category(
            name: "Education_${timestamp}",
            description: 'Education services',
            displayOrder: 3,
            active: true
        ).save(flush: true, failOnError: true)

        // Setup resources first (without contacts)
        resource1 = new Resource(
            name: "Metro Bus Service_${timestamp}",
            description: 'Public transportation for disabled individuals',
            servicesOffered: 'Wheelchair accessible buses, reduced fares',
            hoursOfOperation: '6:00 AM - 10:00 PM',
            category: transportationCategory,
            active: true
        ).save(flush: true, failOnError: true)

        resource2 = new Resource(
            name: "Job Training Center_${timestamp}",
            description: 'Employment skills training for people with disabilities',
            servicesOffered: 'Skills training, job placement assistance',
            hoursOfOperation: '8:00 AM - 5:00 PM',
            category: employmentCategory,
            active: true
        ).save(flush: true, failOnError: true)

        resource3 = new Resource(
            name: "Adult Education Classes_${timestamp}",
            description: 'Educational programs for adults with learning disabilities',
            servicesOffered: 'GED preparation, computer skills training',
            hoursOfOperation: '9:00 AM - 4:00 PM',
            category: educationCategory,
            active: true
        ).save(flush: true, failOnError: true)

        resource4 = new Resource(
            name: "Accessible Taxi Service_${timestamp}",
            description: 'Taxi service with wheelchair accessible vehicles',
            servicesOffered: 'Door-to-door transportation, medical appointments',
            hoursOfOperation: '24/7',
            category: transportationCategory,
            active: true
        ).save(flush: true, failOnError: true)

        // Setup contacts (with resource associations)
        contact1 = new Contact(
            phone: '210-555-1234',
            email: 'test1@example.com',
            address: '123 Main St',
            city: 'San Antonio',
            state: 'TX',
            zipCode: '78201',
            resource: resource1
        ).save(flush: true, failOnError: true)

        contact2 = new Contact(
            phone: '210-555-5678',
            email: 'test2@example.com',
            address: '456 Oak Ave',
            city: 'San Antonio',
            state: 'TX',
            zipCode: '78202',
            resource: resource2
        ).save(flush: true, failOnError: true)

        contact3 = new Contact(
            phone: '210-555-9012',
            email: 'test3@example.com',
            address: '789 Pine Dr',
            city: 'San Antonio',
            state: 'TX',
            zipCode: '78203',
            resource: resource3
        ).save(flush: true, failOnError: true)

        contact4 = new Contact(
            phone: '210-555-3456',
            email: 'test4@example.com',
            address: '321 Elm St',
            city: 'San Antonio',
            state: 'TX',
            zipCode: '78204',
            resource: resource4
        ).save(flush: true, failOnError: true)

        // Setup eligibility requirements
        ageRequirement = new EligibilityRequirement(
            requirement: 'Must be 18 years or older',
            type: 'age',
            resource: resource1
        ).save(flush: true, failOnError: true)

        disabilityRequirement = new EligibilityRequirement(
            requirement: 'Must have a documented disability',
            type: 'disability',
            resource: resource2
        ).save(flush: true, failOnError: true)

        incomeRequirement = new EligibilityRequirement(
            requirement: 'Low income qualification required',
            type: 'income',
            resource: resource3
        ).save(flush: true, failOnError: true)

        // Add age requirement to resource4 as well
        new EligibilityRequirement(
            requirement: 'Must be 16 years or older',
            type: 'age',
            resource: resource4
        ).save(flush: true, failOnError: true)
    }

    void "test searchWithFilters filters by category only"() {
        when:
        List<Resource> results = resourceService.searchWithFilters(null, [transportationCategory.id], null, 'name')

        then:
        results.size() == 2
        results.every { it.category.id == transportationCategory.id }
        results[0].name.startsWith('Accessible Taxi Service')
        results[1].name.startsWith('Metro Bus Service')
    }

    void "test searchWithFilters filters by multiple categories"() {
        when:
        List<Resource> results = resourceService.searchWithFilters(null, [transportationCategory.id, employmentCategory.id], null, 'name')

        then:
        results.size() == 3
        results.every { it.category.id in [transportationCategory.id, employmentCategory.id] }
    }

    void "test searchWithFilters filters by eligibility type only"() {
        when:
        List<Resource> allResults = resourceService.searchWithFilters(null, null, ['age'], 'name')
        // Filter to only include resources created in this test
        List<Resource> results = allResults.findAll { it.name.endsWith("_${timestamp}") }

        then:
        results.size() == 2
        results.every { resource ->
            resource.eligibilityRequirements.any { it.type == 'age' }
        }
    }

    void "test searchWithFilters filters by multiple eligibility types"() {
        when:
        List<Resource> allResults = resourceService.searchWithFilters(null, null, ['age', 'disability'], 'name')
        // Filter to only include resources created in this test
        List<Resource> results = allResults.findAll { it.name.endsWith("_${timestamp}") }

        then:
        results.size() == 3
        results.every { resource ->
            resource.eligibilityRequirements.any { it.type in ['age', 'disability'] }
        }
    }

    void "test searchWithFilters combines category and eligibility filters"() {
        when:
        List<Resource> results = resourceService.searchWithFilters(null, [transportationCategory.id], ['age'], 'name')

        then:
        results.size() == 2
        results.every { it.category.id == transportationCategory.id }
        results.every { resource ->
            resource.eligibilityRequirements.any { it.type == 'age' }
        }
    }

    void "test searchWithFilters with search query only"() {
        when:
        List<Resource> allResults = resourceService.searchWithFilters('bus', null, null, 'name')
        // Filter to only include resources created in this test
        List<Resource> results = allResults.findAll { it.name.endsWith("_${timestamp}") }

        then:
        results.size() == 1
        results[0].name.startsWith('Metro Bus Service')
    }

    void "test searchWithFilters with search query and category filter"() {
        when:
        List<Resource> results = resourceService.searchWithFilters('training', [employmentCategory.id], null, 'name')

        then:
        results.size() == 1
        results[0].name.startsWith('Job Training Center')
    }

    void "test searchWithFilters with search query and eligibility filter"() {
        when:
        List<Resource> allResults = resourceService.searchWithFilters('education', null, ['income'], 'name')
        // Filter to only include resources created in this test
        List<Resource> results = allResults.findAll { it.name.endsWith("_${timestamp}") }

        then:
        results.size() == 1
        results[0].name.startsWith('Adult Education Classes')
    }

    void "test searchWithFilters with all filters combined"() {
        when:
        List<Resource> results = resourceService.searchWithFilters('transportation', [transportationCategory.id], ['age'], 'name')

        then:
        results.size() == 2
        results.every { it.category.id == transportationCategory.id }
        results.every { resource ->
            resource.eligibilityRequirements.any { it.type == 'age' }
        }
    }

    void "test searchWithFilters returns empty list when no matches"() {
        when:
        List<Resource> results = resourceService.searchWithFilters('nonexistent', null, null, 'name')

        then:
        results.size() == 0
    }

    void "test searchWithFilters only returns active resources"() {
        given:
        resource1.active = false
        resource1.save(flush: true)

        when:
        List<Resource> results = resourceService.searchWithFilters(null, [transportationCategory.id], null, 'name')

        then:
        results.size() == 1
        results[0].name.startsWith('Accessible Taxi Service')
    }

    void "test searchWithFilters sorts results correctly"() {
        when:
        List<Resource> allResultsByName = resourceService.searchWithFilters(null, null, null, 'name')
        List<Resource> allResultsByCategory = resourceService.searchWithFilters(null, null, null, 'category.name')
        // Filter to only include resources created in this test
        List<Resource> resultsByName = allResultsByName.findAll { it.name.endsWith("_${timestamp}") }
        List<Resource> resultsByCategory = allResultsByCategory.findAll { it.name.endsWith("_${timestamp}") }

        then:
        resultsByName.size() == 4
        resultsByName[0].name.startsWith('Accessible Taxi Service')
        resultsByName[1].name.startsWith('Adult Education Classes')
        resultsByName[2].name.startsWith('Job Training Center')
        resultsByName[3].name.startsWith('Metro Bus Service')

        resultsByCategory.size() == 4
        resultsByCategory[0].category.name.startsWith('Education')
        resultsByCategory[1].category.name.startsWith('Employment')
        resultsByCategory[2].category.name.startsWith('Transportation')
        resultsByCategory[3].category.name.startsWith('Transportation')
    }

    void "test searchWithFilters handles empty filter lists"() {
        when:
        List<Resource> allResults = resourceService.searchWithFilters(null, [], [], 'name')
        // Filter to only include resources created in this test
        List<Resource> results = allResults.findAll { it.name.endsWith("_${timestamp}") }

        then:
        results.size() == 4
    }

    void "test searchWithFilters handles null parameters"() {
        when:
        List<Resource> allResults = resourceService.searchWithFilters(null, null, null, 'name')
        // Filter to only include resources created in this test
        List<Resource> results = allResults.findAll { it.name.endsWith("_${timestamp}") }

        then:
        results.size() == 4
    }

    void "test get"() {
        expect:
        resourceService.get(resource1.id) != null
        resourceService.get(resource1.id).name.startsWith('Metro Bus Service')
    }

    void "test list"() {
        when:
        List<Resource> resourceList = resourceService.list(max: 2, offset: 0)

        then:
        resourceList.size() == 2
    }

    void "test count"() {
        given:
        def initialCount = Resource.countByNameNotLike('%_' + timestamp)
        
        expect:
        resourceService.count() == initialCount + 4
    }

    void "test delete"() {
        given:
        Long resourceId = resource1.id
        def initialCount = resourceService.count()
        
        when:
        resourceService.delete(resourceId)
        sessionFactory.currentSession.flush()

        then:
        resourceService.count() == initialCount - 1
    }

    void "test save"() {
        when:
        Resource resource = new Resource(
            name: 'Test Resource',
            description: 'Test description',
            category: transportationCategory,
            active: true
        )
        Resource savedResource = resourceService.save(resource)

        then:
        savedResource.id != null
        savedResource.name == 'Test Resource'
        
        when: "add contact to saved resource"
        def contact = new Contact(
            phone: '210-555-7890',
            email: 'test@example.com',
            address: '999 Test St',
            city: 'San Antonio',
            state: 'TX',
            zipCode: '78205',
            resource: savedResource
        ).save(flush: true, failOnError: true)
        
        then:
        savedResource.contact == contact
    }
}
