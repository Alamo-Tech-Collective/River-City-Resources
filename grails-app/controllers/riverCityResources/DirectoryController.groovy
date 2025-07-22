package riverCityResources

import grails.gorm.transactions.Transactional

class DirectoryController {
    
    def resourceService

    def index() {
        def categories = Category.findAllByActive(true, [sort: 'displayOrder'])
        def featuredResources = Resource.findAllByFeaturedAndActive(true, true, [max: 6])
        
        [categories: categories, featuredResources: featuredResources]
    }
    
    def category(Long id) {
        def category = Category.get(id)
        if (!category || !category.active) {
            flash.message = "Category not found"
            redirect(action: "index")
            return
        }
        
        def resources = Resource.findAllByCategoryAndActive(category, true, [sort: 'name'])
        [category: category, resources: resources]
    }
    
    @Transactional
    def resource(Long id) {
        def resource = Resource.get(id)
        if (!resource || !resource.active) {
            flash.message = "Resource not found"
            redirect(action: "index")
            return
        }
        
        // Increment view count
        resource.viewCount++
        resource.save(flush: true)
        
        [resource: resource]
    }
    
    def search() {
        def query = params.q?.trim()
        def categoryIds = []
        def eligibilityTypes = []
        def sort = params.sort ?: 'name'
        
        // Handle single category ID
        if (params.long('categoryId')) {
            categoryIds.add(params.long('categoryId'))
        }
        
        // Handle multiple category IDs (for future enhancement)
        if (params.list('categoryIds')) {
            categoryIds.addAll(params.list('categoryIds').collect { it as Long })
        }
        
        // Handle single eligibility type
        if (params.eligibilityType) {
            eligibilityTypes.add(params.eligibilityType)
        }
        
        // Handle multiple eligibility types (for future enhancement)
        if (params.list('eligibilityTypes')) {
            eligibilityTypes.addAll(params.list('eligibilityTypes'))
        }
        
        // Use ResourceService for search
        def resources = resourceService.searchWithFilters(
            (query == null || query.isEmpty()) ? null : query,
            categoryIds.isEmpty() ? null : categoryIds,
            eligibilityTypes.isEmpty() ? null : eligibilityTypes,
            sort
        )
        
        def categories = Category.findAllByActive(true, [sort: 'displayOrder'])
        
        [resources: resources, query: query, categories: categories, 
         selectedCategoryId: params.long('categoryId'), 
         selectedEligibilityType: params.eligibilityType,
         selectedCategoryIds: categoryIds,
         selectedEligibilityTypes: eligibilityTypes]
    }
}
