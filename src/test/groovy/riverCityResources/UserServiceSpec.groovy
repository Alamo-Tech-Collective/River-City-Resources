package riverCityResources

import grails.testing.services.ServiceUnitTest
import grails.testing.gorm.DomainUnitTest
import grails.testing.gorm.DataTest
import grails.plugin.springsecurity.SpringSecurityService
import org.springframework.security.crypto.password.PasswordEncoder
import spock.lang.Specification
import spock.lang.Unroll

class UserServiceSpec extends Specification implements ServiceUnitTest<UserService>, DataTest {

    def setupSpec() {
        mockDomains User, Role, UserRole
    }

    def setup() {
        service.springSecurityService = Mock(SpringSecurityService)
        service.passwordEncoder = Mock(PasswordEncoder)
    }

    void "test listUsers with pagination"() {
        given:
        def user1 = new User(username: 'user1', password: 'pass', email: 'user1@test.com', 
                            firstName: 'John', lastName: 'Doe', enabled: true).save(flush: true)
        def user2 = new User(username: 'user2', password: 'pass', email: 'user2@test.com', 
                            firstName: 'Jane', lastName: 'Smith', enabled: true).save(flush: true)
        def user3 = new User(username: 'user3', password: 'pass', email: 'user3@test.com', 
                            firstName: 'Bob', lastName: 'Johnson', enabled: false).save(flush: true)
        
        when:
        def result = service.listUsers([max: 2, offset: 0])
        
        then:
        result.users.size() == 2
        result.totalCount == 3
    }

    void "test listUsers with search"() {
        given:
        def user1 = new User(username: 'john.doe', password: 'pass', email: 'john@test.com', 
                            firstName: 'John', lastName: 'Doe', enabled: true).save()
        def user2 = new User(username: 'jane.smith', password: 'pass', email: 'jane@test.com', 
                            firstName: 'Jane', lastName: 'Smith', enabled: true).save()
        
        when:
        def result = service.listUsers([search: 'john'])
        
        then:
        result.users.size() == 1
        result.users[0].username == 'john.doe'
    }

    void "test listUsers with enabled filter"() {
        given:
        def user1 = new User(username: 'user1', password: 'pass', email: 'user1@test.com', 
                            firstName: 'John', lastName: 'Doe', enabled: true).save()
        def user2 = new User(username: 'user2', password: 'pass', email: 'user2@test.com', 
                            firstName: 'Jane', lastName: 'Smith', enabled: false).save()
        
        when:
        def result = service.listUsers([enabled: 'true'])
        
        then:
        result.users.size() == 1
        result.users[0].enabled == true
    }

    void "test createUser success"() {
        given:
        def adminRole = new Role(authority: 'ROLE_ADMIN').save()
        def userRole = new Role(authority: 'ROLE_USER').save()
        
        when:
        def result = service.createUser('newuser', 'Password123!', 'new@test.com', 
                                      'New', 'User', ['ROLE_ADMIN', 'ROLE_USER'])
        
        then:
        result.success == true
        result.user.username == 'newuser'
        result.user.email == 'new@test.com'
        UserRole.countByUser(result.user) == 2
    }

    void "test createUser with validation errors"() {
        given:
        new User(username: 'existing', password: 'pass', email: 'existing@test.com', 
                firstName: 'Existing', lastName: 'User', enabled: true).save()
        
        when:
        def result = service.createUser('existing', 'Password123!', 'invalid-email', 
                                      'New', 'User', [])
        
        then:
        result.success == false
        result.user.hasErrors()
    }

    void "test updateUser success"() {
        given:
        def user = new User(username: 'testuser', password: 'pass', email: 'test@test.com', 
                          firstName: 'Test', lastName: 'User', enabled: true).save()
        def adminRole = new Role(authority: 'ROLE_ADMIN').save()
        
        when:
        def result = service.updateUser(user.id, [
            username: 'updateduser',
            email: 'updated@test.com',
            firstName: 'Updated',
            lastName: 'Name',
            enabled: 'false',
            roles: ['ROLE_ADMIN']
        ])
        
        then:
        result.success == true
        result.user.username == 'updateduser'
        result.user.email == 'updated@test.com'
        result.user.firstName == 'Updated'
        result.user.enabled == false
        UserRole.exists(user.id, adminRole.id)
    }

    void "test updateUser not found"() {
        when:
        def result = service.updateUser(999L, [:])
        
        then:
        result.success == false
        result.message == 'User not found'
    }

    @Unroll
    void "test password validation: #password should be #expectedValid"() {
        given:
        def user = new User(username: 'passtest', password: 'oldpass', email: 'test@test.com', 
                          firstName: 'Test', lastName: 'User', enabled: true).save()
        
        when:
        def result = service.changePassword(user.id, password, password)
        
        then:
        if (expectedValid) {
            result.success == true
            result.message == 'Password changed successfully'
        } else {
            result.success == false
            result.message.contains('security requirements') || result.message.contains('Password does not meet')
        }
        
        where:
        password           | expectedValid
        'short'           | false
        'alllowercase123' | false
        'ALLUPPERCASE123' | false
        'NoSpecialChar123'| false
        'Valid123!'       | true
        'Another@Pass1'   | true
    }

    void "test changePassword success"() {
        given:
        def user = new User(username: 'testuser', password: 'oldpass', email: 'test@test.com', 
                          firstName: 'Test', lastName: 'User', enabled: true).save()
        
        when:
        def result = service.changePassword(user.id, 'NewPass123!', 'NewPass123!')
        
        then:
        result.success == true
        result.message == 'Password changed successfully'
        user.passwordExpired == false
    }

    void "test changePassword passwords don't match"() {
        given:
        def user = new User(username: 'testuser', password: 'oldpass', email: 'test@test.com', 
                          firstName: 'Test', lastName: 'User', enabled: true).save()
        
        when:
        def result = service.changePassword(user.id, 'NewPass123!', 'DifferentPass123!')
        
        then:
        result.success == false
        result.message == 'Passwords do not match'
    }

    void "test changeOwnPassword success"() {
        given:
        def user = new User(username: 'testuser', password: 'encoded', email: 'test@test.com', 
                          firstName: 'Test', lastName: 'User', enabled: true).save()
        service.springSecurityService.currentUser >> user
        service.passwordEncoder.matches('oldpass', 'encoded') >> true
        
        when:
        def result = service.changeOwnPassword('oldpass', 'NewPass123!', 'NewPass123!')
        
        then:
        result.success == true
    }

    void "test changeOwnPassword incorrect current password"() {
        given:
        def user = new User(username: 'testuser', password: 'encoded', email: 'test@test.com', 
                          firstName: 'Test', lastName: 'User', enabled: true).save()
        service.springSecurityService.currentUser >> user
        service.passwordEncoder.matches('wrongpass', 'encoded') >> false
        
        when:
        def result = service.changeOwnPassword('wrongpass', 'NewPass123!', 'NewPass123!')
        
        then:
        result.success == false
        result.message == 'Current password is incorrect'
    }

    void "test deleteUser success"() {
        given:
        def user = new User(username: 'testuser', password: 'pass', email: 'test@test.com', 
                          firstName: 'Test', lastName: 'User', enabled: true).save(flush: true)
        def currentUser = new User(username: 'admin', password: 'pass', email: 'admin@test.com', 
                                 firstName: 'Admin', lastName: 'User', enabled: true).save(flush: true)
        service.springSecurityService.currentUser >> currentUser
        
        when:
        def result = service.deleteUser(user.id)
        
        then:
        result.success == true
        result.message == 'User deleted successfully'
        User.count() == 1
    }

    void "test deleteUser cannot delete self"() {
        given:
        def user = new User(username: 'testuser', password: 'pass', email: 'test@test.com', 
                          firstName: 'Test', lastName: 'User', enabled: true).save()
        service.springSecurityService.currentUser >> user
        
        when:
        def result = service.deleteUser(user.id)
        
        then:
        result.success == false
        result.message == 'Cannot delete your own account'
    }

    void "test deleteUser cannot delete last admin"() {
        given:
        def adminRole = new Role(authority: 'ROLE_ADMIN').save()
        def adminUser = new User(username: 'admin', password: 'pass', email: 'admin@test.com', 
                               firstName: 'Admin', lastName: 'User', enabled: true).save()
        UserRole.create(adminUser, adminRole, true)
        def currentUser = new User(username: 'other', password: 'pass', email: 'other@test.com', 
                                 firstName: 'Other', lastName: 'User', enabled: true).save()
        service.springSecurityService.currentUser >> currentUser
        
        when:
        def result = service.deleteUser(adminUser.id)
        
        then:
        result.success == false
        result.message == 'Cannot delete the last admin user'
    }

    void "test toggleUserStatus success"() {
        given:
        def user = new User(username: 'testuser', password: 'pass', email: 'test@test.com', 
                          firstName: 'Test', lastName: 'User', enabled: true).save()
        def currentUser = new User(username: 'admin', password: 'pass', email: 'admin@test.com', 
                                 firstName: 'Admin', lastName: 'User', enabled: true).save()
        service.springSecurityService.currentUser >> currentUser
        
        when:
        def result = service.toggleUserStatus(user.id)
        
        then:
        result.success == true
        result.user.enabled == false
        result.message == 'User disabled successfully'
    }

    void "test toggleUserStatus cannot disable self"() {
        given:
        def user = new User(username: 'testuser', password: 'pass', email: 'test@test.com', 
                          firstName: 'Test', lastName: 'User', enabled: true).save()
        service.springSecurityService.currentUser >> user
        
        when:
        def result = service.toggleUserStatus(user.id)
        
        then:
        result.success == false
        result.message == 'Cannot disable your own account'
    }

    void "test getUserRoles"() {
        given:
        def user = new User(username: 'testuser', password: 'pass', email: 'test@test.com', 
                          firstName: 'Test', lastName: 'User', enabled: true).save()
        def adminRole = new Role(authority: 'ROLE_ADMIN').save()
        def userRole = new Role(authority: 'ROLE_USER').save()
        UserRole.create(user, adminRole, true)
        UserRole.create(user, userRole, true)
        
        when:
        def roles = service.getUserRoles(user)
        
        then:
        roles.size() == 2
        roles*.authority.containsAll(['ROLE_ADMIN', 'ROLE_USER'])
    }

    void "test getAllRoles"() {
        given:
        new Role(authority: 'ROLE_ADMIN').save()
        new Role(authority: 'ROLE_USER').save()
        new Role(authority: 'ROLE_GUEST').save()
        
        when:
        def roles = service.getAllRoles()
        
        then:
        roles.size() == 3
        roles[0].authority == 'ROLE_ADMIN'
    }

    void "test countUsers"() {
        given:
        3.times { i ->
            new User(username: "user${i}", password: 'pass', email: "user${i}@test.com", 
                    firstName: 'First', lastName: 'Last', enabled: true).save()
        }
        
        when:
        def count = service.countUsers()
        
        then:
        count == 3
    }

    void "test countActiveUsers"() {
        given:
        new User(username: 'user1', password: 'pass', email: 'user1@test.com', 
                firstName: 'First', lastName: 'Last', enabled: true).save()
        new User(username: 'user2', password: 'pass', email: 'user2@test.com', 
                firstName: 'First', lastName: 'Last', enabled: true).save()
        new User(username: 'user3', password: 'pass', email: 'user3@test.com', 
                firstName: 'First', lastName: 'Last', enabled: false).save()
        
        when:
        def count = service.countActiveUsers()
        
        then:
        count == 2
    }
}
