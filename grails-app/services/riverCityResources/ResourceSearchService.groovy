package riverCityResources

import grails.gorm.transactions.Transactional
import grails.gorm.DetachedCriteria

@Transactional(readOnly = true)
class ResourceSearchService {

    def search(String query, Long categoryId = null, Map params = [:]) {
        def criteria = new DetachedCriteria(Resource)
        
        if (query) {
            criteria = criteria.where {
                or {
                    ilike('name', "%${query}%")
                    ilike('description', "%${query}%")
                    ilike('address', "%${query}%")
                    ilike('city', "%${query}%")
                    ilike('services', "%${query}%")
                }
            }
        }
        
        if (categoryId) {
            criteria = criteria.where {
                category.id == categoryId
            }
        }
        
        // Add pagination parameters
        def max = params.max ?: 10
        def offset = params.offset ?: 0
        
        def results = criteria.list(max: max, offset: offset, sort: params.sort ?: 'name', order: params.order ?: 'asc')
        def total = criteria.count()
        
        [results: results, total: total]
    }
    
    def searchByCategory(Long categoryId, Map params = [:]) {
        def criteria = new DetachedCriteria(Resource).where {
            category.id == categoryId
        }
        
        def max = params.max ?: 10
        def offset = params.offset ?: 0
        
        def results = criteria.list(max: max, offset: offset, sort: params.sort ?: 'name', order: params.order ?: 'asc')
        def total = criteria.count()
        
        [results: results, total: total]
    }
    
    def getCategories() {
        Category.list(sort: 'name')
    }
    
    def getResourcesByMultipleCategories(List<Long> categoryIds, Map params = [:]) {
        def criteria = new DetachedCriteria(Resource).where {
            category.id in categoryIds
        }
        
        def max = params.max ?: 10
        def offset = params.offset ?: 0
        
        def results = criteria.list(max: max, offset: offset, sort: params.sort ?: 'name', order: params.order ?: 'asc')
        def total = criteria.count()
        
        [results: results, total: total]
    }
}