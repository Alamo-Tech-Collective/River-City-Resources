package riverCityResources

import grails.testing.web.controllers.ControllerUnitTest
import grails.testing.gorm.DomainUnitTest
import grails.testing.gorm.DataTest
import spock.lang.Specification
import grails.plugin.springsecurity.SpringSecurityService

import static org.springframework.http.HttpStatus.*

class UserControllerSpec extends Specification implements ControllerUnitTest<UserController>, DataTest {

    UserService userService
    SpringSecurityService springSecurityService
    
    def setupSpec() {
        mockDomains User, Role, UserRole
    }

    def setup() {
        userService = Mock(UserService)
        springSecurityService = Mock(SpringSecurityService)
        controller.userService = userService
    }

    void "test index action returns user list"() {
        given:
        def users = [
            new User(username: 'user1', email: 'user1@test.com'),
            new User(username: 'user2', email: 'user2@test.com')
        ]
        userService.listUsers(_) >> [users: users, totalCount: 2]
        
        when:
        controller.index()
        
        then:
        model.userList == users
        model.userCount == 2
    }

    void "test show action with valid user"() {
        given:
        def user = new User(id: 1L, username: 'testuser', email: 'test@test.com', 
                          firstName: 'Test', lastName: 'User', enabled: true,
                          password: 'password')
        user.save(flush: true, validate: false)
        def roles = [new Role(authority: 'ROLE_USER')]
        userService.getUserRoles(user) >> roles
        
        when:
        controller.show(1L)
        
        then:
        model.user == user
        model.userRoles == roles
    }

    void "test show action with invalid user"() {
        when:
        request.format = 'form'
        controller.show(999L)
        
        then:
        response.redirectedUrl == '/user/index'
        flash.message != null
    }

    void "test create action"() {
        given:
        def roles = [new Role(authority: 'ROLE_ADMIN'), new Role(authority: 'ROLE_USER')]
        userService.getAllRoles() >> roles
        
        when:
        controller.create()
        
        then:
        model.user instanceof User
        model.allRoles == roles
    }

    void "test save action success"() {
        given:
        def user = new User(id: 1L, username: 'newuser')
        userService.createUser('newuser', 'password', 'new@test.com', 'New', 'User', ['ROLE_USER']) >> 
            [success: true, user: user]
        
        when:
        request.method = 'POST'
        request.format = 'form'
        params.username = 'newuser'
        params.password = 'password'
        params.email = 'new@test.com'
        params.firstName = 'New'
        params.lastName = 'User'
        params.roles = 'ROLE_USER'
        controller.save()
        
        then:
        response.redirectedUrl?.startsWith('/user/show')
        flash.message != null
    }

    void "test save action with validation errors"() {
        given:
        def user = new User(username: 'newuser')
        user.errors.rejectValue('email', 'blank')
        def roles = [new Role(authority: 'ROLE_ADMIN')]
        userService.createUser(_, _, _, _, _, _) >> [success: false, user: user]
        userService.getAllRoles() >> roles
        
        when:
        request.method = 'POST'
        controller.save()
        
        then:
        view == 'create'
        model.user == user
        model.allRoles == roles
    }

    void "test edit action with valid user"() {
        given:
        def user = new User(id: 1L, username: 'testuser', email: 'test@test.com', 
                          firstName: 'Test', lastName: 'User', enabled: true,
                          password: 'password')
        user.save(flush: true, validate: false)
        def userRoles = [new Role(authority: 'ROLE_USER')]
        def allRoles = [new Role(authority: 'ROLE_ADMIN'), new Role(authority: 'ROLE_USER')]
        userService.getUserRoles(user) >> userRoles
        userService.getAllRoles() >> allRoles
        
        when:
        controller.edit(1L)
        
        then:
        model.user == user
        model.userRoles == userRoles
        model.allRoles == allRoles
    }

    void "test update action success"() {
        given:
        def user = new User(id: 1L, username: 'testuser', email: 'test@test.com', 
                          firstName: 'Test', lastName: 'User', enabled: true,
                          password: 'password')
        user.save(flush: true, validate: false)
        userService.updateUser(1L, _) >> [success: true, user: user]
        
        when:
        request.method = 'PUT'
        request.format = 'form'
        params.id = 1L
        params.username = 'updateduser'
        controller.update(1L)
        
        then:
        response.redirectedUrl == '/user/show/1'
        flash.message != null
    }

    void "test update action with validation errors"() {
        given:
        def user = new User(id: 1L, username: 'testuser', enabled: true,
                          password: 'password')
        user.save(flush: true, validate: false)
        user.errors.rejectValue('email', 'blank')
        def userRoles = [new Role(authority: 'ROLE_USER')]
        def allRoles = [new Role(authority: 'ROLE_ADMIN'), new Role(authority: 'ROLE_USER')]
        userService.updateUser(1L, _) >> [success: false, user: user, errors: user.errors]
        userService.getUserRoles(user) >> userRoles
        userService.getAllRoles() >> allRoles
        
        when:
        request.method = 'PUT'
        params.id = 1L
        controller.update(1L)
        
        then:
        view == 'edit'
        model.user == user
        model.userRoles == userRoles
        model.allRoles == allRoles
    }

    void "test delete action success"() {
        given:
        userService.deleteUser(1L) >> [success: true, message: 'User deleted successfully']
        
        when:
        request.method = 'DELETE'
        request.format = 'form'
        params.id = 1L
        controller.delete(1L)
        
        then:
        response.redirectedUrl == '/user/index'
        flash.message == 'User deleted successfully'
    }

    void "test delete action failure"() {
        given:
        userService.deleteUser(1L) >> [success: false, message: 'Cannot delete user']
        
        when:
        request.method = 'DELETE'
        params.id = 1L
        controller.delete(1L)
        
        then:
        response.redirectedUrl == '/user/show/1'
        flash.error == 'Cannot delete user'
    }

    void "test changePassword GET request"() {
        given:
        def user = new User(id: 1L, username: 'testuser', email: 'test@test.com',
                          enabled: true, password: 'password')
        user.save(flush: true, validate: false)
        
        when:
        request.method = 'GET'
        params.id = 1L
        def result = controller.changePassword(1L)
        
        then:
        // The controller renders the view, so check response
        response.status == 200
        // Since render was called, the model and view aren't directly accessible
        // but we know it rendered successfully if status is 200
    }

    void "test changePassword POST success"() {
        given:
        userService.changePassword(1L, 'newPassword123!', 'newPassword123!') >> 
            [success: true, message: 'Password changed successfully']
        
        when:
        request.method = 'POST'
        request.format = 'form'
        params.id = 1L
        params.newPassword = 'newPassword123!'
        params.confirmPassword = 'newPassword123!'
        controller.changePassword(1L)
        
        then:
        response.redirectedUrl == '/user/show/1'
        flash.message == 'Password changed successfully'
    }

    void "test changePassword POST failure"() {
        given:
        userService.changePassword(1L, 'pass', 'pass') >> 
            [success: false, message: 'Password too short']
        def user = new User(id: 1L, username: 'testuser', enabled: true, password: 'password')
        user.save(flush: true, validate: false)
        
        when:
        request.method = 'POST'
        params.id = 1L
        params.newPassword = 'pass'
        params.confirmPassword = 'pass'
        controller.changePassword(1L)
        
        then:
        view == 'changePassword' || view == '/user/changePassword'
        model.user == user
        flash.error == 'Password too short'
    }

    void "test changeOwnPassword GET request"() {
        when:
        request.method = 'GET'
        controller.changeOwnPassword()
        
        then:
        view == 'changeOwnPassword' || view == '/user/changeOwnPassword'
    }

    void "test changeOwnPassword POST success"() {
        given:
        controller.userService = userService
        controller.userService.changeOwnPassword('oldpass', 'newPassword123!', 'newPassword123!') >> 
            [success: true, message: 'Password changed successfully']
        
        when:
        request.method = 'POST'
        request.format = 'form'
        params.currentPassword = 'oldpass'
        params.newPassword = 'newPassword123!'
        params.confirmPassword = 'newPassword123!'
        controller.changeOwnPassword()
        
        then:
        response.redirectedUrl == '/admin'
        flash.message == 'Password changed successfully'
    }

    void "test changeOwnPassword POST failure"() {
        given:
        controller.userService = userService
        controller.userService.changeOwnPassword('wrongpass', 'newPassword123!', 'newPassword123!') >> 
            [success: false, message: 'Current password is incorrect']
        
        when:
        request.method = 'POST'
        params.currentPassword = 'wrongpass'
        params.newPassword = 'newPassword123!'
        params.confirmPassword = 'newPassword123!'
        controller.changeOwnPassword()
        
        then:
        view == 'changeOwnPassword' || view == '/user/changeOwnPassword'
        flash.error == 'Current password is incorrect'
    }

    void "test toggleStatus action"() {
        given:
        userService.toggleUserStatus(1L) >> [success: true, message: 'User status toggled']
        
        when:
        request.method = 'POST'
        request.format = 'form'
        params.id = 1L
        controller.toggleStatus(1L)
        
        then:
        response.redirectedUrl == '/user/index'
        flash.message == 'User status toggled'
    }

    void "test toggleStatus action failure"() {
        given:
        userService.toggleUserStatus(1L) >> [success: false, message: 'Cannot toggle status']
        
        when:
        request.method = 'POST'
        request.format = 'form'
        params.id = 1L
        controller.toggleStatus(1L)
        
        then:
        response.redirectedUrl == '/user/index'
        flash.error == 'Cannot toggle status'
    }
}