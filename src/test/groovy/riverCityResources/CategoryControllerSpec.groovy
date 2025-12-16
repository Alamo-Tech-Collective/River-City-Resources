package riverCityResources

import grails.testing.gorm.DomainUnitTest
import grails.testing.web.controllers.ControllerUnitTest
import grails.validation.ValidationException
import spock.lang.*

class CategoryControllerSpec extends Specification implements ControllerUnitTest<CategoryController>, DomainUnitTest<Category> {

    def populateValidParams(params) {
        assert params != null

        params["name"] = 'Test Category'
        params["description"] = 'Test Description'
        params["displayOrder"] = 1
        params["active"] = true
    }

    void "Test the index action returns the correct model"() {
        given:
        controller.categoryService = Mock(CategoryService) {
            1 * list(_) >> []
            1 * count() >> 0
        }

        when:"The index action is executed"
        controller.index()

        then:"The model is correct"
        !model.categoryList
        model.categoryCount == 0
    }

    void "Test the create action returns the correct model"() {
        when:"The create action is executed"
        controller.create()

        then:"The model is correctly created"
        model.category!= null
    }

    void "Test the save action with a null instance"() {
        when:"Save is called for a domain instance that doesn't exist"
        request.contentType = FORM_CONTENT_TYPE
        request.method = 'POST'
        controller.save(null)

        then:"A redirect to index is returned"
        response.redirectedUrl == '/admin/categories'
        flash.message != null
    }

    void "Test the save action correctly persists"() {
        given:
        controller.categoryService = Mock(CategoryService) {
            1 * save(_ as Category)
        }

        when:"The save action is executed with a valid instance"
        response.reset()
        request.contentType = FORM_CONTENT_TYPE
        request.method = 'POST'
        populateValidParams(params)
        def category = new Category(params)
        category.id = 1

        controller.save(category)

        then:"A redirect is issued to the show action"
        response.redirectedUrl == '/category/show/1'
        controller.flash.message != null
    }

    void "Test the save action with an invalid instance"() {
        given:
        controller.categoryService = Mock(CategoryService) {
            1 * save(_ as Category) >> { Category category ->
                throw new ValidationException("Invalid instance", category.errors)
            }
        }

        when:"The save action is executed with an invalid instance"
        request.contentType = FORM_CONTENT_TYPE
        request.method = 'POST'
        def category = new Category()
        controller.save(category)

        then:"The create view is rendered again with the correct model"
        model.category != null
        view == 'create'
    }

    void "Test the show action with a null id"() {
        given:
        controller.categoryService = Mock(CategoryService) {
            1 * get(null) >> null
        }

        when:"The show action is executed with a null domain"
        controller.show(null)

        then:"A 404 error is returned"
        response.status == 404
    }

    void "Test the show action with a valid id"() {
        given:
        controller.categoryService = Mock(CategoryService) {
            1 * get(2) >> new Category()
        }

        when:"A domain instance is passed to the show action"
        controller.show(2)

        then:"A model is populated containing the domain instance"
        model.category instanceof Category
    }

    void "Test the edit action with a null id"() {
        given:
        controller.categoryService = Mock(CategoryService) {
            1 * get(null) >> null
        }

        when:"The show action is executed with a null domain"
        controller.edit(null)

        then:"A 404 error is returned"
        response.status == 404
    }

    void "Test the edit action with a valid id"() {
        given:
        controller.categoryService = Mock(CategoryService) {
            1 * get(2) >> new Category()
        }

        when:"A domain instance is passed to the show action"
        controller.edit(2)

        then:"A model is populated containing the domain instance"
        model.category instanceof Category
    }


    void "Test the update action with a null instance"() {
        when:"Save is called for a domain instance that doesn't exist"
        request.contentType = FORM_CONTENT_TYPE
        request.method = 'PUT'
        controller.update(null)

        then:"A redirect to index is returned"
        response.redirectedUrl == '/admin/categories'
        flash.message != null
    }

    void "Test the update action correctly persists"() {
        given:
        controller.categoryService = Mock(CategoryService) {
            1 * save(_ as Category)
        }
        // Populate params BEFORE setting method to PUT to avoid stream closed error
        populateValidParams(params)
        def category = new Category(params)
        category.id = 1

        when:"The save action is executed with a valid instance"
        response.reset()
        request.contentType = FORM_CONTENT_TYPE
        request.method = 'PUT'
        controller.update(category)

        then:"A redirect is issued to the show action"
        response.redirectedUrl == '/category/show/1'
        controller.flash.message != null
    }

    void "Test the update action with an invalid instance"() {
        given:
        controller.categoryService = Mock(CategoryService) {
            1 * save(_ as Category) >> { Category category ->
                throw new ValidationException("Invalid instance", category.errors)
            }
        }
        def category = new Category()

        when:"The save action is executed with an invalid instance"
        request.contentType = JSON_CONTENT_TYPE
        request.method = 'PUT'
        controller.update(category)

        then:"The edit view is rendered again with the correct model"
        model.category != null
        view == 'edit'
    }

    void "Test the delete action with a null instance"() {
        when:"The delete action is called for a null instance"
        request.contentType = FORM_CONTENT_TYPE
        request.method = 'DELETE'
        controller.delete(null)

        then:"A redirect to index is returned"
        response.redirectedUrl == '/admin/categories'
        flash.message != null
    }

    void "Test the delete action with an instance"() {
        given:
        controller.categoryService = Mock(CategoryService) {
            1 * delete(2)
        }

        when:"The domain instance is passed to the delete action"
        request.contentType = FORM_CONTENT_TYPE
        request.method = 'DELETE'
        controller.delete(2)

        then:"The user is redirected to index"
        response.redirectedUrl == '/admin/categories'
        flash.message != null
    }
}






