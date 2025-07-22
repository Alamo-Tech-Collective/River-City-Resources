package riverCityResources

class Resource {
    String name
    String description
    String servicesOffered // Store as text, display as bullet points
    String hoursOfOperation
    Boolean featured = false
    Boolean active = true
    Integer viewCount = 0
    
    Date dateCreated
    Date lastUpdated

    static belongsTo = [category: Category]
    static hasOne = [contact: Contact]
    static hasMany = [eligibilityRequirements: EligibilityRequirement]

    static constraints = {
        name blank: false, unique: true, maxSize: 255
        description blank: false, maxSize: 2000
        servicesOffered nullable: true, maxSize: 5000
        hoursOfOperation nullable: true, maxSize: 500
        viewCount min: 0
        contact nullable: true
        category nullable: false
    }

    static mapping = {
        description type: 'text'
        servicesOffered type: 'text'
        sort 'name'
    }

    String toString() {
        name
    }
}
