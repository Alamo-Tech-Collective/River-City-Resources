package riverCityResources

import grails.gorm.transactions.Transactional
import grails.plugin.springsecurity.SpringSecurityService
import org.springframework.security.crypto.password.PasswordEncoder

@Transactional
class UserService {

    SpringSecurityService springSecurityService
    PasswordEncoder passwordEncoder

    def listUsers(Map params) {
        params.max = Math.min(params.max ? params.max as Integer : 10, 100)
        params.offset = params.offset ? params.offset as Integer : 0
        
        def criteria = User.createCriteria()
        def results = criteria.list(params) {
            if (params.search) {
                or {
                    ilike('username', "%${params.search}%")
                    ilike('email', "%${params.search}%")
                    ilike('firstName', "%${params.search}%")
                    ilike('lastName', "%${params.search}%")
                }
            }
            if (params.enabled != null && params.enabled != '') {
                eq('enabled', params.enabled.toBoolean())
            }
            if (params.sort && params.order) {
                order(params.sort, params.order)
            } else {
                order('username', 'asc')
            }
        }
        
        [users: results, totalCount: results.totalCount]
    }

    def createUser(String username, String password, String email, String firstName, String lastName, List<String> roleNames) {
        def user = new User(
            username: username,
            password: password,
            email: email,
            firstName: firstName,
            lastName: lastName,
            enabled: true,
            accountExpired: false,
            accountLocked: false,
            passwordExpired: false
        )
        
        if (!user.save(flush: true)) {
            return [success: false, user: user, errors: user.errors]
        }
        
        roleNames.each { roleName ->
            def role = Role.findByAuthority(roleName)
            if (role) {
                UserRole.create(user, role, true)
            }
        }
        
        return [success: true, user: user]
    }

    def updateUser(Long userId, Map params) {
        def user = User.get(userId)
        if (!user) {
            return [success: false, message: 'User not found']
        }
        
        user.username = params.username ?: user.username
        user.email = params.email ?: user.email
        user.firstName = params.firstName ?: user.firstName
        user.lastName = params.lastName ?: user.lastName
        user.enabled = params.enabled != null ? params.enabled.toBoolean() : user.enabled
        user.accountExpired = params.accountExpired != null ? params.accountExpired.toBoolean() : user.accountExpired
        user.accountLocked = params.accountLocked != null ? params.accountLocked.toBoolean() : user.accountLocked
        user.passwordExpired = params.passwordExpired != null ? params.passwordExpired.toBoolean() : user.passwordExpired
        
        if (!user.save(flush: true)) {
            return [success: false, user: user, errors: user.errors]
        }
        
        if (params.roles != null) {
            UserRole.removeAll(user)
            params.roles.each { roleName ->
                def role = Role.findByAuthority(roleName)
                if (role) {
                    UserRole.create(user, role, true)
                }
            }
        }
        
        return [success: true, user: user]
    }

    def changePassword(Long userId, String newPassword, String confirmPassword) {
        if (newPassword != confirmPassword) {
            return [success: false, message: 'Passwords do not match']
        }
        
        if (!isPasswordValid(newPassword)) {
            return [success: false, message: 'Password does not meet security requirements. It must be at least 8 characters long and contain at least one uppercase letter, one lowercase letter, one number, and one special character.']
        }
        
        def user = User.get(userId)
        if (!user) {
            return [success: false, message: 'User not found']
        }
        
        user.password = newPassword
        user.passwordExpired = false
        
        if (!user.save(flush: true)) {
            return [success: false, user: user, errors: user.errors]
        }
        
        return [success: true, message: 'Password changed successfully']
    }

    def changeOwnPassword(String currentPassword, String newPassword, String confirmPassword) {
        def currentUser = springSecurityService.currentUser as User
        
        if (!passwordEncoder.matches(currentPassword, currentUser.password)) {
            return [success: false, message: 'Current password is incorrect']
        }
        
        return changePassword(currentUser.id, newPassword, confirmPassword)
    }

    def deleteUser(Long userId) {
        def user = User.get(userId)
        if (!user) {
            return [success: false, message: 'User not found']
        }
        
        def currentUser = springSecurityService.currentUser as User
        if (currentUser.id == userId) {
            return [success: false, message: 'Cannot delete your own account']
        }
        
        def adminRole = Role.findByAuthority('ROLE_ADMIN')
        def adminCount = UserRole.countByRole(adminRole)
        def userIsAdmin = adminRole ? UserRole.exists(userId, adminRole.id) : false
        
        if (userIsAdmin && adminCount <= 1) {
            return [success: false, message: 'Cannot delete the last admin user']
        }
        
        UserRole.removeAll(user)
        user.delete(flush: true)
        
        return [success: true, message: 'User deleted successfully']
    }

    def toggleUserStatus(Long userId) {
        def user = User.get(userId)
        if (!user) {
            return [success: false, message: 'User not found']
        }
        
        def currentUser = springSecurityService.currentUser as User
        if (currentUser.id == userId) {
            return [success: false, message: 'Cannot disable your own account']
        }
        
        user.enabled = !user.enabled
        
        if (!user.save(flush: true)) {
            return [success: false, user: user, errors: user.errors]
        }
        
        return [success: true, user: user, message: "User ${user.enabled ? 'enabled' : 'disabled'} successfully"]
    }

    def getUserRoles(User user) {
        UserRole.findAllByUser(user).collect { it.role }
    }

    def getAllRoles() {
        Role.list(sort: 'authority')
    }

    private boolean isPasswordValid(String password) {
        if (!password || password.length() < 8) {
            return false
        }
        
        boolean hasUpperCase = password =~ /[A-Z]/
        boolean hasLowerCase = password =~ /[a-z]/
        boolean hasNumber = password =~ /[0-9]/
        boolean hasSpecial = password =~ /[!@#$%^&*()_+\-=\[\]{};':"\\|,.<>\/?]/
        
        return hasUpperCase && hasLowerCase && hasNumber && hasSpecial
    }

    def countUsers() {
        User.count()
    }

    def countActiveUsers() {
        User.countByEnabled(true)
    }

    def findUserByUsername(String username) {
        User.findByUsername(username)
    }

    def findUserByEmail(String email) {
        User.findByEmail(email)
    }
}
