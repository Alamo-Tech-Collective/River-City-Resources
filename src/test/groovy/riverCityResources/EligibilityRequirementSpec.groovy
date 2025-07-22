package riverCityResources

import grails.testing.gorm.DomainUnitTest
import spock.lang.Specification

class EligibilityRequirementSpec extends Specification implements DomainUnitTest<EligibilityRequirement> {

    def setup() {
        mockDomain EligibilityRequirement
    }

    def cleanup() {
    }

    void "test valid eligibility requirement creation"() {
        when: "A valid eligibility requirement is created"
        def requirement = new EligibilityRequirement(
            requirement: 'Must be 18 years or older',
            type: 'age'
        )
        def isValid = requirement.validate()

        then: "The requirement is valid"
        isValid == true
        !requirement.hasErrors()
        
        when: "Check validation errors if any"
        if (!isValid) {
            println "Validation errors: ${requirement.errors}"
        }
        
        then:
        true
    }

    void "test requirement text is required"() {
        when: "An eligibility requirement is created without requirement text"
        def requirement = new EligibilityRequirement(
            type: 'age'
        )

        then: "The requirement is invalid"
        !requirement.validate()
        requirement.hasErrors()
        requirement.errors['requirement'].code == 'nullable'
    }

    void "test type is required"() {
        when: "An eligibility requirement is created without type"
        def requirement = new EligibilityRequirement(
            requirement: 'Must be 18 years or older'
        )

        then: "The requirement is invalid"
        !requirement.validate()
        requirement.hasErrors()
        requirement.errors['type'].code == 'nullable'
    }

    void "test type must be from valid list"() {
        when: "An eligibility requirement is created with invalid type"
        def requirement = new EligibilityRequirement(
            requirement: 'Must be 18 years or older',
            type: 'invalid_type'
        )

        then: "The requirement is invalid"
        !requirement.validate()
        requirement.hasErrors()
        requirement.errors['type'].code == 'not.inList'
    }

    void "test all valid types are accepted"() {
        when: "Requirements are created with all valid types"
        def ageRequirement = new EligibilityRequirement(
            requirement: 'Must be 18 or older',
            type: 'age'
        )
        def disabilityRequirement = new EligibilityRequirement(
            requirement: 'Must have a disability',
            type: 'disability'
        )
        def incomeRequirement = new EligibilityRequirement(
            requirement: 'Must meet income requirements',
            type: 'income'
        )
        def otherRequirement = new EligibilityRequirement(
            requirement: 'Must be a resident',
            type: 'other'
        )

        then: "All requirements are valid"
        ageRequirement.validate() == true
        !ageRequirement.hasErrors()
        disabilityRequirement.validate() == true
        !disabilityRequirement.hasErrors()
        incomeRequirement.validate() == true
        !incomeRequirement.hasErrors()
        otherRequirement.validate() == true
        !otherRequirement.hasErrors()
    }

    void "test requirement max size constraint"() {
        when: "An eligibility requirement is created with text exceeding max size"
        def requirement = new EligibilityRequirement(
            requirement: 'a' * 501, // 501 characters, exceeds maxSize: 500
            type: 'other'
        )

        then: "The requirement is invalid"
        !requirement.validate()
        requirement.hasErrors()
        requirement.errors['requirement'].code == 'maxSize.exceeded'
    }

    void "test toString method"() {
        when: "An eligibility requirement is created"
        def requirement = new EligibilityRequirement(
            requirement: 'Must be 18 years or older',
            type: 'age'
        )

        then: "toString returns the requirement text"
        requirement.toString() == 'Must be 18 years or older'
    }

    void "test requirement can be saved with resource"() {
        given: "A resource with category"
        def category = new Category(name: 'Test Category', displayOrder: 1, active: true)
        def resource = new Resource(
            name: 'Test Resource',
            description: 'Test Description',
            category: category
        )

        when: "An eligibility requirement is created and associated with resource"
        def requirement = new EligibilityRequirement(
            requirement: 'Must be 18 years or older',
            type: 'age',
            resource: resource
        )

        then: "The requirement is valid"
        requirement.validate()
        !requirement.hasErrors()
        requirement.resource == resource
    }
}
