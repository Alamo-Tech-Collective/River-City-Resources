package riverCityResources

class Category {
    String name
    String description
    Integer displayOrder = 0
    Boolean active = true
    Date dateCreated
    Date lastUpdated

    static hasMany = [resources: Resource]

    static constraints = {
        name blank: false, unique: true, maxSize: 100
        description nullable: true, maxSize: 500
        displayOrder min: 0
    }

    static mapping = {
        sort 'displayOrder'
        resources sort: 'name'
    }

    String toString() {
        name
    }
}
