package riverCityResources

class EligibilityRequirement {
    String requirement
    String type // age, disability, income, other
    
    Date dateCreated
    Date lastUpdated

    static belongsTo = [resource: Resource]

    static constraints = {
        requirement blank: false, maxSize: 500
        type inList: ['age', 'disability', 'income', 'other']
        resource nullable: true
    }

    String toString() {
        requirement
    }
}
