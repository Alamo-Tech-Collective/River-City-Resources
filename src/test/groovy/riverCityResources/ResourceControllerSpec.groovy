package riverCityResources

import grails.testing.gorm.DomainUnitTest
import grails.testing.web.controllers.ControllerUnitTest
import grails.validation.ValidationException
import spock.lang.*

class ResourceControllerSpec extends Specification implements ControllerUnitTest<ResourceController>, DomainUnitTest<Resource> {

    def setup() {
        // Mock related domain classes
        mockDomains(Category, Contact, EligibilityRequirement)
    }

    def populateValidParams(params) {
        assert params != null
        
        params["name"] = "Test Resource"
        params["description"] = "This is a test resource description"
        params["servicesOffered"] = "Service 1\nService 2\nService 3"
        params["hoursOfOperation"] = "Monday-Friday: 9AM-5PM"
        params["featured"] = false
        params["active"] = true
        
        // Create a category for the resource
        def category = new Category(name: "Test Category", description: "Test", displayOrder: 1)
        category.save(flush: true)
        params["category.id"] = category.id
        
        // Add contact information
        params["contact"] = [
            phone: "(210) 555-1234",
            email: "test@example.com",
            address: "123 Test Street",
            city: "San Antonio",
            state: "TX",
            zipCode: "78201",
            website: "http://www.example.com"
        ]
        
        // Add eligibility requirements
        params["eligibilityRequirement"] = ["Must be 18 or older", "San Antonio resident"]
        params["eligibilityType"] = ["age", "residency"]
    }

    void "Test the index action returns the correct model"() {
        given:
        controller.resourceService = Mock(ResourceService) {
            1 * list(_) >> []
            1 * count() >> 0
        }

        when:"The index action is executed"
        controller.index()

        then:"The model is correct"
        !model.resourceList
        model.resourceCount == 0
    }
    
    void "Test the index action with pagination"() {
        given:
        def resources = (1..25).collect { new Resource(name: "Resource $it") }
        controller.resourceService = Mock(ResourceService) {
            1 * list(_) >> resources[0..9]
            1 * count() >> 25
        }
        
        when:"The index action is executed with default pagination"
        controller.index()
        
        then:"Only 10 resources are returned"
        model.resourceList.size() == 10
        model.resourceCount == 25
    }
    
    void "Test the index action respects max parameter constraint"() {
        given:
        controller.resourceService = Mock(ResourceService) {
            1 * list([max: 100]) >> []
            1 * count() >> 0
        }
        
        when:"The index action is executed with max > 100"
        params.max = 200
        controller.index(200)
        
        then:"Max is capped at 100"
        params.max == 100
    }

    void "Test the create action returns the correct model"() {
        given:
        def category1 = new Category(name: "Category 1", displayOrder: 1, active: true)
        def category2 = new Category(name: "Category 2", displayOrder: 2, active: true)
        def category3 = new Category(name: "Category 3", displayOrder: 3, active: false)
        [category1, category2, category3].each { it.save(flush: true) }
        
        when:"The create action is executed"
        controller.create()

        then:"The model is correctly created"
        model.resource != null
        model.resource instanceof Resource
        model.resource.contact != null
        model.categories.size() == 2 // Only active categories
        model.categories[0].name == "Category 1"
        model.categories[1].name == "Category 2"
    }

    void "Test the save action with a null instance"() {
        when:"Save is called for a domain instance that doesn't exist"
        request.contentType = FORM_CONTENT_TYPE
        request.method = 'POST'
        controller.save(null)

        then:"A redirect to index is returned"
        response.redirectedUrl == '/admin/resources'
        flash.message != null
    }

    void "Test the save action correctly persists"() {
        given:
        populateValidParams(params)
        def savedResource = null
        controller.resourceService = Mock(ResourceService) {
            1 * save(_ as Resource) >> { Resource r -> 
                savedResource = r
                r.id = 1
                r
            }
        }

        when:"The save action is executed with a valid instance"
        response.reset()
        request.contentType = FORM_CONTENT_TYPE
        request.method = 'POST'
        controller.save()

        then:"A redirect is issued to the show action"
        response.redirectedUrl == '/resource/show/1'
        controller.flash.message != null
        savedResource.name == "Test Resource"
        savedResource.contact != null
        savedResource.contact.phone == "(210) 555-1234"
        savedResource.eligibilityRequirements.size() == 2
    }
    
    void "Test the save action handles contact information correctly"() {
        given:
        populateValidParams(params)
        def savedResource = null
        controller.resourceService = Mock(ResourceService) {
            1 * save(_ as Resource) >> { Resource r -> 
                savedResource = r
                r.id = 1
                r
            }
        }
        
        when:"The save action is executed with contact params"
        request.contentType = FORM_CONTENT_TYPE
        request.method = 'POST'
        controller.save()
        
        then:"Contact is properly created and associated"
        savedResource.contact != null
        savedResource.contact.phone == "(210) 555-1234"
        savedResource.contact.email == "test@example.com"
        savedResource.contact.address == "123 Test Street"
        savedResource.contact.city == "San Antonio"
        savedResource.contact.state == "TX"
        savedResource.contact.zipCode == "78201"
        savedResource.contact.website == "http://www.example.com"
    }
    
    void "Test the save action handles eligibility requirements correctly"() {
        given:
        populateValidParams(params)
        def savedResource = null
        controller.resourceService = Mock(ResourceService) {
            1 * save(_ as Resource) >> { Resource r -> 
                savedResource = r
                r.id = 1
                r
            }
        }
        
        when:"The save action is executed with eligibility params"
        request.contentType = FORM_CONTENT_TYPE
        request.method = 'POST'
        controller.save()
        
        then:"Eligibility requirements are properly created"
        savedResource.eligibilityRequirements.size() == 2
        savedResource.eligibilityRequirements.find { it.requirement == "Must be 18 or older" }.type == "age"
        savedResource.eligibilityRequirements.find { it.requirement == "San Antonio resident" }.type == "residency"
    }
    
    void "Test the save action filters empty eligibility requirements"() {
        given:
        populateValidParams(params)
        params["eligibilityRequirement"] = ["Valid requirement", "", "  ", "Another requirement"]
        params["eligibilityType"] = ["age", "other", "other", "residency"]
        def savedResource = null
        controller.resourceService = Mock(ResourceService) {
            1 * save(_ as Resource) >> { Resource r -> 
                savedResource = r
                r.id = 1
                r
            }
        }
        
        when:"The save action is executed with some empty eligibility requirements"
        request.contentType = FORM_CONTENT_TYPE
        request.method = 'POST'
        controller.save()
        
        then:"Only non-empty requirements are saved"
        savedResource.eligibilityRequirements.size() == 2
        savedResource.eligibilityRequirements*.requirement.contains("Valid requirement")
        savedResource.eligibilityRequirements*.requirement.contains("Another requirement")
    }

    void "Test the save action with an invalid instance"() {
        given:
        def category = new Category(name: "Test Category", active: true)
        category.save(flush: true)
        controller.resourceService = Mock(ResourceService) {
            1 * save(_ as Resource) >> { Resource resource ->
                resource.errors.rejectValue('name', 'blank')
                throw new ValidationException("Invalid instance", resource.errors)
            }
        }

        when:"The save action is executed with an invalid instance"
        request.contentType = FORM_CONTENT_TYPE
        request.method = 'POST'
        params.name = "" // Invalid - blank name
        params.description = "Test"
        params["category.id"] = category.id
        controller.save()

        then:"The create view is rendered again with the correct model"
        view == 'create'
        model.categories != null
        model.categories.size() == 1
    }

    void "Test the show action with a null id"() {
        given:
        controller.resourceService = Mock(ResourceService) {
            1 * get(null) >> null
        }

        when:"The show action is executed with a null domain"
        controller.show(null)

        then:"A 404 status is returned"
        response.status == 404
    }

    void "Test the show action with a valid id"() {
        given:
        controller.resourceService = Mock(ResourceService) {
            1 * get(2) >> new Resource()
        }

        when:"A domain instance is passed to the show action"
        controller.show(2)

        then:"A model is populated containing the domain instance"
        model.resource instanceof Resource
    }

    void "Test the edit action with a null id"() {
        given:
        controller.resourceService = Mock(ResourceService) {
            1 * get(null) >> null
        }

        when:"The edit action is executed with a null domain"
        controller.edit(null)

        then:"A 404 status is returned"
        response.status == 404
    }

    void "Test the edit action with a valid id"() {
        given:
        def category1 = new Category(name: "Category 1", displayOrder: 1, active: true)
        def category2 = new Category(name: "Category 2", displayOrder: 2, active: true)  
        [category1, category2].each { it.save(flush: true) }
        
        def resource = new Resource(name: "Test Resource", description: "Test")
        resource.category = category1
        controller.resourceService = Mock(ResourceService) {
            1 * get(2) >> resource
        }

        when:"A domain instance is passed to the edit action"
        controller.edit(2)

        then:"A model is populated containing the domain instance and categories"
        model.resource instanceof Resource
        model.resource.name == "Test Resource"
        model.categories.size() == 2
        model.categories*.name == ["Category 1", "Category 2"]
    }


    void "Test the update action with a null instance"() {
        when:"Update is called for a domain instance that doesn't exist"
        request.contentType = FORM_CONTENT_TYPE
        request.method = 'PUT'
        controller.update(null)

        then:"A redirect to index is returned"
        response.redirectedUrl == '/admin/resources'
        flash.message != null
    }

    void "Test the update action correctly persists"() {
        given:
        populateValidParams(params)
        def resource = new Resource(params)
        resource.id = 1
        controller.resourceService = Mock(ResourceService) {
            1 * save(_ as Resource) >> resource
        }

        when:"The update action is executed with a valid instance"
        response.reset()
        request.contentType = FORM_CONTENT_TYPE
        request.method = 'PUT'
        controller.update(resource)

        then:"A redirect is issued to the show action"
        response.redirectedUrl == '/resource/show/1'
        controller.flash.message != null
    }

    void "Test the update action with an invalid instance"() {
        given:
        controller.resourceService = Mock(ResourceService) {
            1 * save(_ as Resource) >> { Resource resource ->
                throw new ValidationException("Invalid instance", resource.errors)
            }
        }

        when:"The save action is executed with an invalid instance"
        request.contentType = FORM_CONTENT_TYPE
        request.method = 'PUT'
        controller.update(new Resource())

        then:"The edit view is rendered again with the correct model"
        model.resource != null
        view == 'edit'
    }

    void "Test the delete action with a null instance"() {
        when:"The delete action is called for a null instance"
        request.contentType = FORM_CONTENT_TYPE
        request.method = 'DELETE'
        controller.delete(null)

        then:"A redirect to index is returned"
        response.redirectedUrl == '/admin/resources'
        flash.message != null
    }

    void "Test the delete action with an instance"() {
        given:
        controller.resourceService = Mock(ResourceService) {
            1 * delete(2)
        }

        when:"The domain instance is passed to the delete action"
        request.contentType = FORM_CONTENT_TYPE
        request.method = 'DELETE'
        controller.delete(2)

        then:"The user is redirected to index"
        response.redirectedUrl == '/admin/resources'
        flash.message != null
    }
    
    // Additional edge case tests
    
    void "Test index action with custom max parameter"() {
        given:
        def resources = (1..5).collect { new Resource(name: "Resource $it") }
        controller.resourceService = Mock(ResourceService) {
            1 * list([max: 5]) >> resources
            1 * count() >> 5
        }
        
        when:"Index is called with custom max"
        controller.index(5)
        
        then:"The custom max is used"
        model.resourceList.size() == 5
        model.resourceCount == 5
    }
    
    void "Test show action returns null for non-existent resource"() {
        given:
        controller.resourceService = Mock(ResourceService) {
            1 * get(999) >> null
        }
        
        when:"Show is called with non-existent ID"
        controller.show(999)
        
        then:"A 404 status is returned"
        response.status == 404
    }
    
    void "Test edit action returns redirect for non-existent resource"() {
        given:
        controller.resourceService = Mock(ResourceService) {
            1 * get(999) >> null
        }
        
        when:"Edit is called with non-existent ID"
        controller.edit(999)
        
        then:"A 404 status is returned"
        response.status == 404
    }
    
    void "Test save action with missing category"() {
        given:
        params.name = "Test Resource"
        params.description = "Test Description"
        // No category.id provided
        
        controller.resourceService = Mock(ResourceService) {
            1 * save(_ as Resource) >> { Resource r ->
                r.errors.rejectValue('category', 'nullable')
                throw new ValidationException("Category is required", r.errors)
            }
        }
        
        when:"Save is called without category"
        request.contentType = FORM_CONTENT_TYPE
        request.method = 'POST'
        controller.save()
        
        then:"Create view is shown with errors"
        view == 'create'
    }
    
    void "Test delete action with JSON format"() {
        given:
        controller.resourceService = Mock(ResourceService) {
            1 * delete(1)
        }
        
        when:"Delete is called with JSON format"
        request.contentType = 'application/json'
        request.method = 'DELETE'
        response.format = 'json'
        controller.delete(1)
        
        then:"NO_CONTENT status is returned"
        response.status == 204
    }
    
    void "Test save action with JSON format"() {
        given:
        populateValidParams(params)
        def resource = new Resource(params)
        resource.id = 1
        controller.resourceService = Mock(ResourceService) {
            1 * save(_ as Resource) >> resource
        }
        
        when:"Save is called with JSON format"
        request.contentType = 'application/json'
        request.method = 'POST'
        response.format = 'json'
        controller.save(resource)
        
        then:"CREATED status is returned"
        response.status == 201
    }
    
    void "Test update action with validation error"() {
        given:
        def resource = new Resource(name: "", description: "Test")
        controller.resourceService = Mock(ResourceService) {
            1 * save(_ as Resource) >> { Resource r ->
                r.errors.rejectValue('name', 'blank')
                throw new ValidationException("Invalid", r.errors)
            }
        }
        
        when:"Update is called with invalid data"
        request.contentType = FORM_CONTENT_TYPE
        request.method = 'PUT'
        controller.update(resource)
        
        then:"Edit view is rendered"
        view == 'edit'
        model.resource != null
    }
}



