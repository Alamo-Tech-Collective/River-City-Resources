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

    /**
     * List resources for a provider - shows only their approved resources
     * @param user The provider user
     * @param args Map of arguments for pagination and sorting
     * @return List of approved resources submitted by the provider
     */
    List<Resource> listForProvider(User user, Map args) {
        return Resource.createCriteria().list(args) {
            eq('submittedBy', user)
            eq('approvalStatus', 'approved')
            order('name')
        }
    }

    /**
     * Count approved resources for a provider
     * @param user The provider user
     * @return Count of approved resources submitted by the provider
     */
    Long countForProvider(User user) {
        return Resource.countBySubmittedByAndApprovalStatus(user, 'approved')
    }

    /**
     * Get resources submitted by a specific provider
     * @param user The provider user
     * @param args Map of arguments for pagination and sorting
     * @return List of resources submitted by the provider
     */
    List<Resource> listByProvider(User user, Map args) {
        return Resource.createCriteria().list(args) {
            eq('submittedBy', user)
            order('name')
        }
    }

    /**
     * Count resources submitted by a specific provider
     * @param user The provider user
     * @return Count of resources submitted by the provider
     */
    Long countByProvider(User user) {
        return Resource.countBySubmittedBy(user)
    }

    /**
     * Get pending resources for admin approval
     * @param args Map of arguments for pagination and sorting
     * @return List of pending resources
     */
    List<Resource> listPending(Map args) {
        return Resource.createCriteria().list(args) {
            eq('approvalStatus', 'pending')
            order('dateCreated', 'desc')
        }
    }

    /**
     * Count pending resources
     * @return Count of pending resources
     */
    Long countPending() {
        return Resource.countByApprovalStatus('pending')
    }
}