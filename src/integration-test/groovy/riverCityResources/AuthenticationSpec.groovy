package riverCityResources

import grails.gorm.transactions.Rollback
import grails.testing.mixin.integration.Integration
import spock.lang.Specification

@Integration
@Rollback
class AuthenticationSpec extends Specification {

    def setup() {
        // Create test admin user if not exists
        if (!User.findByUsername('testadmin')) {
            def adminRole = Role.findByAuthority('ROLE_ADMIN') ?: new Role(authority: 'ROLE_ADMIN').save(flush: true)
            def testAdmin = new User(
                username: 'testadmin',
                password: 'testpass123',
                email: 'testadmin@example.com',
                firstName: 'Test',
                lastName: 'Admin',
                enabled: true
            ).save(flush: true)
            UserRole.create(testAdmin, adminRole, true)
        }
    }

    def "test admin user can be created"() {
        when: "I create a new admin user"
        def adminRole = Role.findByAuthority('ROLE_ADMIN') ?: new Role(authority: 'ROLE_ADMIN').save(flush: true)
        def testUser = new User(
            username: 'integrationtest',
            password: 'testpass123',
            email: 'integration@example.com',
            firstName: 'Integration',
            lastName: 'Test',
            enabled: true
        ).save(flush: true)

        then: "The user is created successfully"
        testUser.id != null
        testUser.username == 'integrationtest'

        when: "I assign admin role to the user"
        UserRole.create(testUser, adminRole, true)

        then: "The user role is assigned"
        UserRole.findByUserAndRole(testUser, adminRole) != null
    }

    def "test user authentication roles"() {
        when: "I find the test admin user"
        def testUser = User.findByUsername('testadmin')

        then: "The user exists and has admin role"
        testUser != null
        testUser.enabled == true
        def adminRole = Role.findByAuthority('ROLE_ADMIN')
        adminRole != null
        UserRole.findByUserAndRole(testUser, adminRole) != null
    }
}