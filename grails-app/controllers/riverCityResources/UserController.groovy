package riverCityResources

import grails.plugin.springsecurity.annotation.Secured
import grails.validation.ValidationException

@Secured(['ROLE_ADMIN'])
class UserController {

    UserService userService

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE", changePassword: "POST", toggleStatus: "POST"]

    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        def result = userService.listUsers(params)
        respond result.users, model: [userList: result.users, userCount: result.totalCount]
    }

    def show(Long id) {
        def user = User.get(id)
        if (!user) {
            notFound()
            return
        }
        def roles = userService.getUserRoles(user)
        respond user, model: [userRoles: roles]
    }

    def create() {
        def roles = userService.getAllRoles()
        respond new User(params), model: [allRoles: roles]
    }

    def save() {
        def roleNames = params.list('roles')
        
        def result = userService.createUser(
            params.username,
            params.password,
            params.email,
            params.firstName,
            params.lastName,
            roleNames
        )
        
        if (!result.success) {
            def roles = userService.getAllRoles()
            respond result.user, view: 'create', model: [allRoles: roles]
            return
        }
        
        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.created.message', args: [message(code: 'user.label', default: 'User'), result.user.username])
                redirect action: 'show', id: result.user.id
            }
            '*' { respond result.user, [status: CREATED] }
        }
    }

    def edit(Long id) {
        def user = User.get(id)
        if (!user) {
            notFound()
            return
        }
        def userRoles = userService.getUserRoles(user)
        def allRoles = userService.getAllRoles()
        respond user, model: [userRoles: userRoles, allRoles: allRoles]
    }

    def update(Long id) {
        def user = User.get(id)
        if (!user) {
            notFound()
            return
        }
        
        def updateParams = [:]
        updateParams.username = params.username
        updateParams.email = params.email
        updateParams.firstName = params.firstName
        updateParams.lastName = params.lastName
        updateParams.enabled = params.enabled
        updateParams.accountExpired = params.accountExpired
        updateParams.accountLocked = params.accountLocked
        updateParams.passwordExpired = params.passwordExpired
        updateParams.roles = params.list('roles')
        
        def result = userService.updateUser(id, updateParams)
        
        if (!result.success) {
            def userRoles = userService.getUserRoles(user)
            def allRoles = userService.getAllRoles()
            respond result.user, view: 'edit', model: [userRoles: userRoles, allRoles: allRoles]
            return
        }
        
        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.updated.message', args: [message(code: 'user.label', default: 'User'), result.user.username])
                redirect action: 'show', id: result.user.id
            }
            '*' { respond result.user, [status: OK] }
        }
    }

    def delete(Long id) {
        def result = userService.deleteUser(id)
        
        if (!result.success) {
            flash.error = result.message
            redirect action: 'show', id: id
            return
        }
        
        request.withFormat {
            form multipartForm {
                flash.message = result.message
                redirect action: 'index', method: 'GET'
            }
            '*' { render status: NO_CONTENT }
        }
    }

    def changePassword(Long id) {
        if (request.method == 'GET') {
            def user = User.get(id)
            if (!user) {
                notFound()
                return
            }
            render view: 'changePassword', model: [user: user]
            return
        }
        
        def result = userService.changePassword(id, params.newPassword, params.confirmPassword)
        
        if (!result.success) {
            def user = User.get(id)
            flash.error = result.message
            render view: 'changePassword', model: [user: user]
            return
        }
        
        flash.message = result.message
        redirect action: 'show', id: id
    }

    @Secured(['IS_AUTHENTICATED_FULLY'])
    def changeOwnPassword() {
        if (request.method == 'GET') {
            render view: 'changeOwnPassword'
            return
        }
        
        def result = userService.changeOwnPassword(
            params.currentPassword,
            params.newPassword,
            params.confirmPassword
        )
        
        if (!result.success) {
            flash.error = result.message
            render view: 'changeOwnPassword'
            return
        }
        
        flash.message = result.message
        redirect controller: 'admin', action: 'index'
    }

    def toggleStatus(Long id) {
        def result = userService.toggleUserStatus(id)
        
        if (!result.success) {
            flash.error = result.message
        } else {
            flash.message = result.message
        }
        
        redirect action: 'index'
    }

    protected void notFound() {
        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.not.found.message', args: [message(code: 'user.label', default: 'User'), params.id])
                redirect action: 'index', method: 'GET'
            }
            '*' { render status: NOT_FOUND }
        }
    }
}
