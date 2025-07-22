package riverCityResources

import grails.testing.gorm.DomainUnitTest
import spock.lang.Specification

class UserSpec extends Specification implements DomainUnitTest<User> {

    def setup() {
        mockDomain User
    }

    def cleanup() {
    }

    void "test valid user creation"() {
        when: "A valid user is created"
        def user = new User(
            username: 'testuser',
            password: 'password123',
            email: 'test@example.com',
            firstName: 'Test',
            lastName: 'User',
            enabled: true
        )

        then: "The user is valid"
        user.validate()
        !user.hasErrors()
    }

    void "test username is required"() {
        when: "A user is created without username"
        def user = new User(
            password: 'password123',
            email: 'test@example.com',
            firstName: 'Test',
            lastName: 'User'
        )

        then: "The user is invalid"
        !user.validate()
        user.hasErrors()
        user.errors['username'].code == 'nullable'
    }

    void "test email must be valid format"() {
        when: "A user is created with invalid email"
        def user = new User(
            username: 'testuser',
            password: 'password123',
            email: 'invalid-email',
            firstName: 'Test',
            lastName: 'User'
        )

        then: "The user is invalid"
        !user.validate()
        user.hasErrors()
        user.errors['email'].code == 'email.invalid'
    }

    void "test password is required"() {
        when: "A user is created without password"
        def user = new User(
            username: 'testuser',
            email: 'test@example.com',
            firstName: 'Test',
            lastName: 'User'
        )

        then: "The user is invalid"
        !user.validate()
        user.hasErrors()
        user.errors['password'].code == 'nullable'
    }

    void "test username must be unique"() {
        given: "An existing user"
        def existingUser = new User(
            username: 'testuser',
            password: 'password123',
            email: 'test1@example.com',
            firstName: 'Test',
            lastName: 'User'
        ).save(flush: true)

        when: "Another user is created with same username"
        def newUser = new User(
            username: 'testuser',
            password: 'password456',
            email: 'test2@example.com',
            firstName: 'Test2',
            lastName: 'User2'
        )

        then: "The new user is invalid"
        !newUser.save()
        newUser.hasErrors()
        newUser.errors['username'].code == 'unique'
    }
}