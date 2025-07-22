package riverCityResources

import grails.testing.gorm.DomainUnitTest
import spock.lang.Specification

class CategorySpec extends Specification implements DomainUnitTest<Category> {

    def setup() {
        mockDomain Category
    }

    def cleanup() {
    }

    void "test valid category creation"() {
        when: "A valid category is created"
        def category = new Category(
            name: 'Test Category',
            description: 'Test Description',
            displayOrder: 1,
            active: true
        )

        then: "The category is valid"
        category.validate()
        !category.hasErrors()
    }

    void "test name is required"() {
        when: "A category is created without a name"
        def category = new Category(
            description: 'Test Description',
            displayOrder: 1,
            active: true
        )

        then: "The category is invalid"
        !category.validate()
        category.hasErrors()
        category.errors['name'].code == 'nullable'
    }

    void "test name must be unique"() {
        given: "An existing category"
        def existingCategory = new Category(
            name: 'Existing Category',
            description: 'Test Description',
            displayOrder: 1,
            active: true
        ).save(flush: true)

        when: "Another category is created with the same name"
        def newCategory = new Category(
            name: 'Existing Category',
            description: 'Another Description',
            displayOrder: 2,
            active: true
        )

        then: "The new category is invalid"
        !newCategory.validate()
        newCategory.hasErrors()
        newCategory.errors['name'].code == 'unique'
    }

    void "test displayOrder minimum value"() {
        when: "A category is created with negative displayOrder"
        def category = new Category(
            name: 'Test Category',
            description: 'Test Description',
            displayOrder: -1,
            active: true
        )

        then: "The category is invalid"
        !category.validate()
        category.hasErrors()
        category.errors['displayOrder'].code == 'min.notmet'
    }

    void "test description can be null"() {
        when: "A category is created without description"
        def category = new Category(
            name: 'Test Category',
            displayOrder: 1,
            active: true
        )

        then: "The category is valid"
        category.validate()
        !category.hasErrors()
    }

    void "test name max size constraint"() {
        when: "A category is created with a name exceeding max size"
        def category = new Category(
            name: 'a' * 101, // 101 characters, exceeds maxSize: 100
            description: 'Test Description',
            displayOrder: 1,
            active: true
        )

        then: "The category is invalid"
        !category.validate()
        category.hasErrors()
        category.errors['name'].code == 'maxSize.exceeded'
    }

    void "test toString method"() {
        when: "A category is created"
        def category = new Category(
            name: 'Test Category',
            description: 'Test Description',
            displayOrder: 1,
            active: true
        )

        then: "toString returns the name"
        category.toString() == 'Test Category'
    }
}
