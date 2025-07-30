package riverCityResources

import grails.testing.web.controllers.ControllerUnitTest
import grails.testing.gorm.DomainUnitTest
import grails.testing.gorm.DataTest
import spock.lang.Specification

import static org.springframework.http.HttpStatus.*

class UserControllerSpec extends Specification implements ControllerUnitTest<UserController>, DataTest {

    UserService userService
    
    def setupSpec() {
        mockDomains User, Role, UserRole
    }

    def setup() {
        userService = Mock(UserService)
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
        model.userList == users || response.json == users
        model.userCount == 2
    }

    void "test show action with valid user"() {
        given:
        def user = new User(username: 'testuser', email: 'test@test.com', 
                          firstName: 'Test', lastName: 'User').save()
        def roles = [new Role(authority: 'ROLE_ADMIN')]
        userService.getUserRoles(user) >> roles
        
        when:
        params.id = user.id
        controller.show(user.id)
        
        then:
        model.user == user || controller.modelAndView.model.user == user
        model.userRoles == roles || controller.modelAndView.model.userRoles == roles
    }

    void "test show action with invalid user"() {
        when:
        params.id = 999L
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
        request.contentType = FORM_CONTENT_TYPE
        params.username = 'newuser'
        params.password = 'password'
        params.email = 'new@test.com'
        params.firstName = 'New'
        params.lastName = 'User'
        params.roles = 'ROLE_USER'
        controller.save()
        
        then:
        response.redirectedUrl == '/user/show/1'
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
        def user = new User(username: 'testuser', email: 'test@test.com', 
                          firstName: 'Test', lastName: 'User').save()
        def userRoles = [new Role(authority: 'ROLE_USER')]
        def allRoles = [new Role(authority: 'ROLE_ADMIN'), new Role(authority: 'ROLE_USER')]
        userService.getUserRoles(user) >> userRoles
        userService.getAllRoles() >> allRoles
        
        when:
        params.id = user.id
        controller.edit(user.id)
        
        then:
        model.user == user || controller.modelAndView.model.user == user
        model.userRoles == userRoles || controller.modelAndView.model.userRoles == userRoles
        model.allRoles == allRoles || controller.modelAndView.model.allRoles == allRoles
    }

    void "test update action success"() {
        given:
        def user = new User(id: 1L, username: 'testuser', email: 'test@test.com', 
                          firstName: 'Test', lastName: 'User').save()
        userService.updateUser(1L, _) >> [success: true, user: user]
        
        when:
        request.method = 'PUT'
        request.contentType = FORM_CONTENT_TYPE
        params.id = 1L
        params.username = 'updateduser'
        controller.update(1L)
        
        then:
        response.redirectedUrl == '/user/show/1'
        flash.message != null
    }

    void "test update action with validation errors"() {
        given:
        def user = new User(username: 'testuser', email: 'test@test.com', 
                          firstName: 'Test', lastName: 'User').save()
        user.errors.rejectValue('email', 'blank')
        def userRoles = [new Role(authority: 'ROLE_USER')]
        def allRoles = [new Role(authority: 'ROLE_ADMIN')]
        userService.updateUser(user.id, _) >> [success: false, user: user]
        userService.getUserRoles(user) >> userRoles
        userService.getAllRoles() >> allRoles
        
        when:
        request.method = 'PUT'
        params.id = user.id
        controller.update(user.id)
        
        then:
        view == 'edit'
        model.user == user
        model.userRoles == userRoles
        model.allRoles == allRoles
    }

    void "test delete action success"() {
        given:
        userService.deleteUser(1L) >> [success: true, message: 'User deleted']
        
        when:
        request.method = 'DELETE'
        request.contentType = FORM_CONTENT_TYPE
        controller.delete(1L)
        
        then:
        response.redirectedUrl == '/user/index'
        flash.message == 'User deleted'
    }

    void "test delete action failure"() {
        given:
        userService.deleteUser(1L) >> [success: false, message: 'Cannot delete']
        
        when:
        request.method = 'DELETE'
        controller.delete(1L)
        
        then:
        response.redirectedUrl == '/user/show/1'
        flash.error == 'Cannot delete'
    }

    void "test changePassword GET request"() {
        given:
        def user = new User(username: 'testuser', email: 'test@test.com', 
                          firstName: 'Test', lastName: 'User').save()
        
        when:
        request.method = 'GET'
        params.id = user.id
        controller.changePassword(user.id)
        
        then:
        view == 'changePassword'
        model.user == user
    }

    void "test changePassword POST success"() {
        given:
        userService.changePassword(1L, 'newpass', 'newpass') >> 
            [success: true, message: 'Password changed']
        
        when:
        request.method = 'POST'
        params.newPassword = 'newpass'
        params.confirmPassword = 'newpass'
        controller.changePassword(1L)
        
        then:
        response.redirectedUrl == '/user/show/1'
        flash.message == 'Password changed'
    }

    void "test changePassword POST failure"() {
        given:
        def user = new User(id: 1L, username: 'testuser', email: 'test@test.com', 
                          firstName: 'Test', lastName: 'User').save()
        userService.changePassword(1L, 'newpass', 'different') >> 
            [success: false, message: 'Passwords do not match']
        
        when:
        request.method = 'POST'
        params.id = 1L
        params.newPassword = 'newpass'
        params.confirmPassword = 'different'
        controller.changePassword(1L)
        
        then:
        view == 'changePassword'
        model.user == user
        flash.error == 'Passwords do not match'
    }

    void "test changeOwnPassword GET request"() {
        when:
        request.method = 'GET'
        controller.changeOwnPassword()
        
        then:
        view == 'changeOwnPassword'
    }

    void "test changeOwnPassword POST success"() {
        given:
        userService.changeOwnPassword('current', 'newpass', 'newpass') >> 
            [success: true, message: 'Password changed']
        
        when:
        request.method = 'POST'
        params.currentPassword = 'current'
        params.newPassword = 'newpass'
        params.confirmPassword = 'newpass'
        controller.changeOwnPassword()
        
        then:
        response.redirectedUrl == '/admin/index'
        flash.message == 'Password changed'
    }

    void "test changeOwnPassword POST failure"() {
        given:
        userService.changeOwnPassword('wrong', 'newpass', 'newpass') >> 
            [success: false, message: 'Current password incorrect']
        
        when:
        request.method = 'POST'
        params.currentPassword = 'wrong'
        params.newPassword = 'newpass'
        params.confirmPassword = 'newpass'
        controller.changeOwnPassword()
        
        then:
        view == 'changeOwnPassword'
        flash.error == 'Current password incorrect'
    }

    void "test toggleStatus action"() {
        given:
        userService.toggleUserStatus(1L) >> [success: true, message: 'User disabled']
        
        when:
        request.method = 'POST'
        controller.toggleStatus(1L)
        
        then:
        response.redirectedUrl == '/user/index'
        flash.message == 'User disabled'
    }

    void "test toggleStatus action failure"() {
        given:
        userService.toggleUserStatus(1L) >> [success: false, message: 'Cannot disable self']
        
        when:
        request.method = 'POST'
        controller.toggleStatus(1L)
        
        then:
        response.redirectedUrl == '/user/index'
        flash.error == 'Cannot disable self'
    }
}
