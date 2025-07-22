package riverCityResources

class Contact {
    String phone
    String email
    String address
    String city
    String state
    String zipCode
    String website
    
    Date dateCreated
    Date lastUpdated

    static belongsTo = [resource: Resource]

    static constraints = {
        phone nullable: true, matches: /[\d\s\-\(\)\.]+/, maxSize: 50
        email nullable: true, email: true, maxSize: 255
        address nullable: true, maxSize: 255
        city blank: false, nullable: false, maxSize: 100
        state blank: false, nullable: false, size: 2..2
        zipCode nullable: true, matches: /\d{5}(-\d{4})?/
        website nullable: true, url: true, maxSize: 500
        resource nullable: true
    }

    String toString() {
        "${address ?: 'Contact'}"
    }
}
