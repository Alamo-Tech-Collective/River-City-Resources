package riverCityResources

import grails.testing.gorm.DomainUnitTest
import spock.lang.Specification

class ContactSpec extends Specification implements DomainUnitTest<Contact> {

    def setup() {
        mockDomain Contact
    }

    def cleanup() {
    }

    void "test valid contact creation"() {
        when: "A valid contact is created"
        def contact = new Contact(
            phone: '(210) 555-1234',
            email: 'test@example.com',
            address: '123 Main Street',
            city: 'San Antonio',
            state: 'TX',
            zipCode: '78201',
            website: 'http://www.example.com'
        )

        then: "The contact is valid"
        contact.validate()
        !contact.hasErrors()
    }

    void "test contact with minimal required fields"() {
        when: "A contact is created with only city and state"
        def contact = new Contact(
            city: 'San Antonio',
            state: 'TX'
        )

        then: "The contact is valid"
        contact.validate()
        !contact.hasErrors()
    }

    void "test city is required"() {
        when: "A contact is created without a city"
        def contact = new Contact(
            state: 'TX'
        )

        then: "The contact is invalid"
        !contact.validate()
        contact.hasErrors()
        contact.errors['city'].code == 'nullable'
    }

    void "test state is required"() {
        when: "A contact is created without a state"
        def contact = new Contact(
            city: 'San Antonio'
        )

        then: "The contact is invalid"
        !contact.validate()
        contact.hasErrors()
        contact.errors['state'].code == 'nullable'
    }

    void "test state must be exactly 2 characters"() {
        when: "A contact is created with invalid state length"
        def contact = new Contact(
            city: 'San Antonio',
            state: 'Texas'
        )

        then: "The contact is invalid"
        !contact.validate()
        contact.hasErrors()
        contact.errors['state'].code in ['size.toosmall', 'size.toobig']
    }

    void "test email validation"() {
        when: "A contact is created with invalid email"
        def contact = new Contact(
            city: 'San Antonio',
            state: 'TX',
            email: 'invalid-email'
        )

        then: "The contact is invalid"
        !contact.validate()
        contact.hasErrors()
        contact.errors['email'].code == 'email.invalid'
    }

    void "test website validation"() {
        when: "A contact is created with invalid website"
        def contact = new Contact(
            city: 'San Antonio',
            state: 'TX',
            website: 'not-a-url'
        )

        then: "The contact is invalid"
        !contact.validate()
        contact.hasErrors()
        contact.errors['website'].code == 'url.invalid'
    }

    void "test phone format validation"() {
        when: "A contact is created with invalid phone"
        def contact = new Contact(
            city: 'San Antonio',
            state: 'TX',
            phone: 'abc-def-ghij'
        )

        then: "The contact is invalid"
        !contact.validate()
        contact.hasErrors()
        contact.errors['phone'].code == 'matches.invalid'
    }

    void "test zipCode format validation"() {
        when: "A contact is created with invalid zipCode"
        def contact = new Contact(
            city: 'San Antonio',
            state: 'TX',
            zipCode: 'invalid-zip'
        )

        then: "The contact is invalid"
        !contact.validate()
        contact.hasErrors()
        contact.errors['zipCode'].code == 'matches.invalid'
    }

    void "test valid zipCode formats"() {
        when: "A contact is created with valid 5-digit zipCode"
        def contact1 = new Contact(
            city: 'San Antonio',
            state: 'TX',
            zipCode: '78201'
        )

        and: "A contact is created with valid 9-digit zipCode"
        def contact2 = new Contact(
            city: 'San Antonio',
            state: 'TX',
            zipCode: '78201-1234'
        )

        then: "Both contacts are valid"
        contact1.validate()
        !contact1.hasErrors()
        contact2.validate()
        !contact2.hasErrors()
    }

    void "test toString method"() {
        when: "A contact is created with address"
        def contact = new Contact(
            address: '123 Main Street',
            city: 'San Antonio',
            state: 'TX'
        )

        then: "toString returns the address"
        contact.toString() == '123 Main Street'

        when: "A contact is created without address"
        def contactNoAddress = new Contact(
            city: 'San Antonio',
            state: 'TX'
        )

        then: "toString returns 'Contact'"
        contactNoAddress.toString() == 'Contact'
    }
}
