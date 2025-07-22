package riverCityResources

import grails.gorm.transactions.Transactional

@Transactional
class ResourceService {

    Resource get(Serializable id) {
        return Resource.get(id)
    }

    List<Resource> list(Map args) {
        return Resource.list(args)
    }

    Long count() {
        return Resource.count()
    }

    void delete(Serializable id) {
        Resource resource = Resource.get(id)
        if (resource) {
            resource.delete(flush: true)
        }
    }

    Resource save(Resource resource) {
        return resource.save(flush: true)
    }

    /**
     * Search resources with advanced filters
     * @param query Text search query (searches name, description, and servicesOffered)
     * @param categoryIds List of category IDs to filter by
     * @param eligibilityTypes List of eligibility types to filter by
     * @param sort Property to sort by (default: 'name')
     * @return List of matching resources
     */
    List<Resource> searchWithFilters(String query, List<Long> categoryIds, List<String> eligibilityTypes, String sort = 'name') {
        return Resource.createCriteria().list {
            // Only return active resources
            eq('active', true)
            
            // Text search across multiple fields
            if (query?.trim()) {
                or {
                    ilike('name', "%${query.trim()}%")
                    ilike('description', "%${query.trim()}%")
                    ilike('servicesOffered', "%${query.trim()}%")
                }
            }
            
            // Filter by category IDs
            if (categoryIds && !categoryIds.empty) {
                category {
                    'in'('id', categoryIds)
                }
            }
            
            // Filter by eligibility types
            if (eligibilityTypes && !eligibilityTypes.empty) {
                eligibilityRequirements {
                    'in'('type', eligibilityTypes)
                }
            }
            
            // Sort results
            if (sort) {
                if (sort.contains('.')) {
                    // Handle nested properties like 'category.name'
                    def parts = sort.split('\\.')
                    if (parts.length == 2) {
                        "${parts[0]}" {
                            order(parts[1])
                        }
                    }
                } else {
                    order(sort)
                }
            }
        }
    }
}