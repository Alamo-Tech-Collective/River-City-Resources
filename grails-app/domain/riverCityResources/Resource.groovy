package riverCityResources

class Resource {
    String name
    String description
    String servicesOffered // Store as text, display as bullet points
    String hoursOfOperation
    String approvalStatus = 'approved'
    String rejectionReason
    Boolean featured = false
    Boolean active = true
    Integer viewCount = 0

    User submittedBy // The user who submitted the resource
    User approvedBy // The user who approved the resource

    Date dateCreated
    Date lastUpdated
    Date approvedDate

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
        approvalStatus inList: ['pending', 'approved', 'rejected']
        submittedBy nullable: true
        approvedBy nullable: true
        approvedDate nullable: true
        rejectionReason nullable: true, maxSize: 500
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
