package riverCityResources

import grails.gorm.transactions.Rollback
import grails.testing.mixin.integration.Integration
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.crypto.password.PasswordEncoder
import spock.lang.Specification

@Integration
@Rollback
class UserManagementIntegrationSpec extends Specification {

    @Autowired
    UserService userService
    
    @Autowired
    PasswordEncoder passwordEncoder

    def setup() {
        Role.findOrSaveByAuthority('ROLE_ADMIN')
        Role.findOrSaveByAuthority('ROLE_USER')
    }

    void "test complete user creation workflow"() {
        when: "creating a new user with roles"
        def result = userService.createUser(
            'newadmin',
            'AdminPass123!',
            'newadmin@test.com',
            'New',
            'Admin',
            ['ROLE_ADMIN', 'ROLE_USER']
        )
        
        then: "user is created successfully"
        result.success == true
        result.user.id != null
        result.user.username == 'newadmin'
        result.user.email == 'newadmin@test.com'
        
        and: "password is properly encoded"
        passwordEncoder.matches('AdminPass123!', result.user.password)
        
        and: "roles are assigned correctly"
        def roles = userService.getUserRoles(result.user)
        roles.size() == 2
        roles*.authority.containsAll(['ROLE_ADMIN', 'ROLE_USER'])
    }

    void "test user update workflow"() {
        given: "an existing user"
        def user = new User(
            username: 'testuser',
            password: 'password',
            email: 'test@test.com',
            firstName: 'Test',
            lastName: 'User',
            enabled: true
        ).save(flush: true)
        
        when: "updating user details and roles"
        def result = userService.updateUser(user.id, [
            email: 'updated@test.com',
            firstName: 'Updated',
            enabled: 'false',
            roles: ['ROLE_ADMIN']
        ])
        
        then: "user is updated successfully"
        result.success == true
        result.user.email == 'updated@test.com'
        result.user.firstName == 'Updated'
        result.user.enabled == false
        
        and: "roles are updated"
        def roles = userService.getUserRoles(result.user)
        roles.size() == 1
        roles[0].authority == 'ROLE_ADMIN'
    }

    void "test password change workflow"() {
        given: "an existing user"
        def user = new User(
            username: 'passtest',
            password: passwordEncoder.encode('OldPass123!'),
            email: 'pass@test.com',
            firstName: 'Pass',
            lastName: 'Test',
            enabled: true,
            passwordExpired: true
        ).save(flush: true)
        
        when: "changing the password"
        def result = userService.changePassword(user.id, 'NewPass456!', 'NewPass456!')
        
        then: "password is changed successfully"
        result.success == true
        
        and: "new password works"
        def updatedUser = User.get(user.id)
        passwordEncoder.matches('NewPass456!', updatedUser.password)
        
        and: "password expired flag is cleared"
        updatedUser.passwordExpired == false
    }

    void "test user deletion constraints"() {
        given: "multiple users including admins"
        def adminRole = Role.findByAuthority('ROLE_ADMIN')
        def userRole = Role.findByAuthority('ROLE_USER')
        
        def admin1 = new User(
            username: 'admin1',
            password: 'password',
            email: 'admin1@test.com',
            firstName: 'Admin',
            lastName: 'One',
            enabled: true
        ).save(flush: true)
        UserRole.create(admin1, adminRole, true)
        
        def admin2 = new User(
            username: 'admin2',
            password: 'password',
            email: 'admin2@test.com',
            firstName: 'Admin',
            lastName: 'Two',
            enabled: true
        ).save(flush: true)
        UserRole.create(admin2, adminRole, true)
        
        def regularUser = new User(
            username: 'regular',
            password: 'password',
            email: 'regular@test.com',
            firstName: 'Regular',
            lastName: 'User',
            enabled: true
        ).save(flush: true)
        UserRole.create(regularUser, userRole, true)
        
        when: "deleting a regular user"
        def result1 = userService.deleteUser(regularUser.id)
        
        then: "deletion succeeds"
        result1.success == true
        User.findByUsername('regular') == null
        
        when: "deleting an admin when another exists"
        def result2 = userService.deleteUser(admin2.id)
        
        then: "deletion succeeds"
        result2.success == true
        User.findByUsername('admin2') == null
        
        when: "trying to delete the last admin"
        def result3 = userService.deleteUser(admin1.id)
        
        then: "deletion is prevented"
        result3.success == false
        result3.message == 'Cannot delete the last admin user'
        User.findByUsername('admin1') != null
    }

    void "test user search and filtering"() {
        given: "multiple users with different attributes"
        5.times { i ->
            new User(
                username: "user${i}",
                password: 'password',
                email: "user${i}@test.com",
                firstName: i % 2 == 0 ? 'John' : 'Jane',
                lastName: "Doe${i}",
                enabled: i % 2 == 0
            ).save(flush: true)
        }
        
        when: "searching for users by name"
        def searchResult = userService.listUsers([search: 'John'])
        
        then: "correct users are returned"
        searchResult.users.size() == 3
        searchResult.users.every { it.firstName == 'John' }
        
        when: "filtering by enabled status"
        def enabledResult = userService.listUsers([enabled: 'true'])
        
        then: "only enabled users are returned"
        enabledResult.users.size() == 3
        enabledResult.users.every { it.enabled == true }
        
        when: "using pagination"
        def paginatedResult = userService.listUsers([max: 2, offset: 2])
        
        then: "pagination works correctly"
        paginatedResult.users.size() == 2
        paginatedResult.totalCount == 5
    }

    void "test concurrent user operations"() {
        given: "a user to update"
        def user = new User(
            username: 'concurrent',
            password: 'password',
            email: 'concurrent@test.com',
            firstName: 'Concurrent',
            lastName: 'Test',
            enabled: true
        ).save(flush: true)
        
        when: "multiple updates happen"
        def results = []
        3.times { i ->
            results << userService.updateUser(user.id, [
                firstName: "Update${i}",
                lastName: "Test${i}"
            ])
        }
        
        then: "all updates succeed"
        results.every { it.success == true }
        
        and: "last update wins"
        def finalUser = User.get(user.id)
        finalUser.firstName == 'Update2'
        finalUser.lastName == 'Test2'
    }

    void "test password validation rules"() {
        given: "a user"
        def user = new User(
            username: 'passvalidation',
            password: 'password',
            email: 'passval@test.com',
            firstName: 'Pass',
            lastName: 'Validation',
            enabled: true
        ).save(flush: true)
        
        when: "using weak passwords"
        def weakPasswords = [
            'short',
            'alllowercase',
            'ALLUPPERCASE',
            'NoNumbers!',
            'NoSpecial123',
            'noUpper123!'
        ]
        
        def results = weakPasswords.collect { pass ->
            userService.changePassword(user.id, pass, pass)
        }
        
        then: "all weak passwords are rejected"
        results.every { it.success == false }
        results.every { it.message.contains('security requirements') }
        
        when: "using strong passwords"
        def strongPasswords = [
            'Valid123!',
            'Another@Pass1',
            'Str0ng#Pass',
            'C0mplex!ty'
        ]
        
        def strongResults = strongPasswords.collect { pass ->
            userService.changePassword(user.id, pass, pass)
        }
        
        then: "all strong passwords are accepted"
        strongResults.every { it.success == true }
    }

    void "test role management"() {
        given: "available roles"
        def adminRole = Role.findByAuthority('ROLE_ADMIN')
        def userRole = Role.findByAuthority('ROLE_USER')
        
        and: "a user without roles"
        def user = new User(
            username: 'noroles',
            password: 'password',
            email: 'noroles@test.com',
            firstName: 'No',
            lastName: 'Roles',
            enabled: true
        ).save(flush: true)
        
        when: "adding roles"
        userService.updateUser(user.id, [roles: ['ROLE_ADMIN', 'ROLE_USER']])
        
        then: "roles are assigned"
        def roles1 = userService.getUserRoles(user)
        roles1.size() == 2
        
        when: "changing roles"
        userService.updateUser(user.id, [roles: ['ROLE_USER']])
        
        then: "roles are updated"
        def roles2 = userService.getUserRoles(user)
        roles2.size() == 1
        roles2[0].authority == 'ROLE_USER'
        
        when: "removing all roles"
        userService.updateUser(user.id, [roles: []])
        
        then: "user has no roles"
        def roles3 = userService.getUserRoles(user)
        roles3.size() == 0
    }
}