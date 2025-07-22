package riverCityResources

import grails.gorm.services.Service

@Service(Category)
interface CategoryService {

    Category get(Serializable id)

    List<Category> list(Map args)

    Long count()

    void delete(Serializable id)

    Category save(Category category)

}