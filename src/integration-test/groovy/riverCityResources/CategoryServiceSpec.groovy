package riverCityResources

import grails.testing.mixin.integration.Integration
import grails.gorm.transactions.Rollback
import spock.lang.Specification
import org.hibernate.SessionFactory

@Integration
@Rollback
class CategoryServiceSpec extends Specification {

    CategoryService categoryService
    SessionFactory sessionFactory

    private Long setupData() {
        // Use timestamp to ensure unique names
        def timestamp = System.currentTimeMillis()
        
        new Category(name: "Transportation_${timestamp}", description: 'Transportation services', displayOrder: 1, active: true).save(flush: true, failOnError: true)
        new Category(name: "Employment_${timestamp}", description: 'Employment services', displayOrder: 2, active: true).save(flush: true, failOnError: true)
        Category category = new Category(name: "Education_${timestamp}", description: 'Education services', displayOrder: 3, active: true).save(flush: true, failOnError: true)
        new Category(name: "Healthcare_${timestamp}", description: 'Healthcare services', displayOrder: 4, active: true).save(flush: true, failOnError: true)
        new Category(name: "Housing_${timestamp}", description: 'Housing services', displayOrder: 5, active: true).save(flush: true, failOnError: true)
        category.id
    }

    void "test get"() {
        setupData()

        expect:
        categoryService.get(1) != null
    }

    void "test list"() {
        def timestamp = System.currentTimeMillis()
        // Create categories with known timestamp
        new Category(name: "Test1_${timestamp}", description: 'Test 1', displayOrder: 101, active: true).save(flush: true, failOnError: true)
        new Category(name: "Test2_${timestamp}", description: 'Test 2', displayOrder: 102, active: true).save(flush: true, failOnError: true)
        new Category(name: "Test3_${timestamp}", description: 'Test 3', displayOrder: 103, active: true).save(flush: true, failOnError: true)
        new Category(name: "Test4_${timestamp}", description: 'Test 4', displayOrder: 104, active: true).save(flush: true, failOnError: true)

        when:
        // Get all categories and filter for our test categories
        List<Category> allCategories = categoryService.list(max: 100)
        List<Category> testCategories = allCategories.findAll { it.name.contains("_${timestamp}") }
        
        then:
        testCategories.size() == 4
        testCategories[2].name == "Test3_${timestamp}"
        testCategories[3].name == "Test4_${timestamp}"
    }

    void "test count"() {
        def initialCount = categoryService.count()
        setupData()

        expect:
        categoryService.count() == initialCount + 5
    }

    void "test delete"() {
        def initialCount = categoryService.count()
        Long categoryId = setupData()

        expect:
        categoryService.count() == initialCount + 5

        when:
        categoryService.delete(categoryId)
        sessionFactory.currentSession.flush()

        then:
        categoryService.count() == initialCount + 4
    }

    void "test save"() {
        when:
        Category category = new Category(
            name: "New Category_${System.currentTimeMillis()}",
            description: 'New category description',
            displayOrder: 6,
            active: true
        )
        categoryService.save(category)

        then:
        category.id != null
    }
}
