package riverCityResources

import grails.validation.ValidationException
import grails.plugin.springsecurity.annotation.Secured
import grails.plugin.springsecurity.SpringSecurityService
import static org.springframework.http.HttpStatus.*

@Secured(['ROLE_ADMIN', 'ROLE_PROVIDER'])
class ResourceController {

    ResourceService resourceService
    SpringSecurityService springSecurityService

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        def currentUser = springSecurityService.currentUser as User
        def isProvider = currentUser?.authorities?.any { it.authority == 'ROLE_PROVIDER' }
        
        if (isProvider) {
            // Providers can only see their own approved resources
            def resourceList = resourceService.listForProvider(currentUser, params)
            def resourceCount = resourceService.countForProvider(currentUser)
            def isEmpty = (resourceList == null || resourceList.empty)
            respond resourceList, 
                   model:[
                       resourceCount: resourceCount, 
                       isEmpty: isEmpty,
                       isProvider: true,
                       currentUser: currentUser
                   ]
        } else {
            // Admins can see all resources
            def resourceList = resourceService.list(params)
            def resourceCount = resourceService.count()
            respond resourceList, model:[
                resourceCount: resourceCount,
                isProvider: false,
                isEmpty: false,
                currentUser: currentUser
            ]
        }
    }

    def show(Long id) {
        respond resourceService.get(id)
    }

    def create() {
        def resource = new Resource(params)
        resource.contact = new Contact()
        respond resource, model: [categories: Category.findAllByActive(true, [sort: 'displayOrder'])]
    }

    def save(Resource resource) {
        if (resource == null) {
            notFound()
            return
        }
        
        def currentUser = springSecurityService.currentUser as User
        def isProvider = currentUser?.authorities?.any { it.authority == 'ROLE_PROVIDER' }
        
        // Handle contact information
        if (params.contact) {
            resource.contact = new Contact(params.contact)
        }
        
        // Handle eligibility requirements
        def eligibilityList = params.list('eligibilityRequirement')
        def eligibilityTypes = params.list('eligibilityType')
        
        eligibilityList.eachWithIndex { req, index ->
            if (req?.trim()) {
                resource.addToEligibilityRequirements(
                    new EligibilityRequirement(
                        requirement: req.trim(),
                        type: eligibilityTypes[index]?.toLowerCase() ?: 'other'
                    )
                )
            }
        }
        
        // Set approval status based on user role
        if (isProvider) {
            resource.submittedBy = currentUser
            resource.approvalStatus = 'pending'
        } else {
            resource.approvalStatus = 'approved'
            resource.approvedBy = currentUser
            resource.approvedDate = new Date()
        }

        try {
            resourceService.save(resource)
        } catch (ValidationException e) {
            respond resource.errors, view:'create', model: [categories: Category.findAllByActive(true, [sort: 'displayOrder'])]
            return
        }

        request.withFormat {
            form multipartForm {
                if (isProvider) {
                    flash.message = "Resource submitted for approval. You will be notified once it's reviewed."
                } else {
                    flash.message = message(code: 'default.created.message', args: [message(code: 'resource.label', default: 'Resource'), resource.id])
                }
                redirect resource
            }
            '*' { respond resource, [status: CREATED] }
        }
    }

    def edit(Long id) {
        def resource = resourceService.get(id)
        if (!resource) {
            notFound()
            return
        }
        
        def currentUser = springSecurityService.currentUser as User
        def isProvider = currentUser?.authorities?.any { it.authority == 'ROLE_PROVIDER' }
        
        // Check if provider can edit this resource
        if (isProvider && resource.submittedBy != currentUser) {
            flash.message = "You can only edit resources you have submitted."
            redirect action: 'index'
            return
        }
        
        respond resource, model: [categories: Category.findAllByActive(true, [sort: 'displayOrder'])]
    }

    def update(Resource resource) {
        if (resource == null) {
            notFound()
            return
        }
        
        def currentUser = springSecurityService.currentUser as User
        def isProvider = currentUser?.authorities?.any { it.authority == 'ROLE_PROVIDER' }
        
        // Check if provider can edit this resource
        if (isProvider && resource.submittedBy != currentUser) {
            flash.message = "You can only edit resources you have submitted."
            redirect action: 'index'
            return
        }
        
        // Handle eligibility requirements update
        // Remove all existing eligibility requirements
        def existingRequirements = resource.eligibilityRequirements?.collect { it }
        existingRequirements?.each { req ->
            resource.removeFromEligibilityRequirements(req)
            req.delete()
        }
        
        def eligibilityList = params.list('eligibilityRequirement')
        def eligibilityTypes = params.list('eligibilityType')
        
        eligibilityList.eachWithIndex { req, index ->
            if (req?.trim()) {
                resource.addToEligibilityRequirements(
                    new EligibilityRequirement(
                        requirement: req.trim(),
                        type: eligibilityTypes[index]?.toLowerCase() ?: 'other'
                    )
                )
            }
        }
        
        // Handle approval status for provider updates
        if (isProvider && resource.approvalStatus == 'approved') {
            // If provider edits an approved resource, it goes back to pending
            resource.approvalStatus = 'pending'
            resource.approvedBy = null
            resource.approvedDate = null
        }

        try {
            resourceService.save(resource)
        } catch (ValidationException e) {
            respond resource.errors, view:'edit', model: [categories: Category.findAllByActive(true, [sort: 'displayOrder'])]
            return
        }

        request.withFormat {
            form multipartForm {
                if (isProvider && resource.approvalStatus == 'pending') {
                    flash.message = "Resource updated and submitted for re-approval."
                } else {
                    flash.message = message(code: 'default.updated.message', args: [message(code: 'resource.label', default: 'Resource'), resource.id])
                }
                redirect resource
            }
            '*'{ respond resource, [status: OK] }
        }
    }
    
    def addEligibilityItem() {
        def index = (params.list('eligibilityRequirement')?.size() ?: 0)
        render template: '/resource/eligibilityItem', model: [req: null, index: index]
    }

    def delete(Long id) {
        if (id == null) {
            notFound()
            return
        }
        
        def currentUser = springSecurityService.currentUser as User
        def isProvider = currentUser?.authorities?.any { it.authority == 'ROLE_PROVIDER' }
        
        // Only admins can delete resources
        if (isProvider) {
            flash.message = "You do not have permission to delete resources."
            redirect action: 'index'
            return
        }

        resourceService.delete(id)

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.deleted.message', args: [message(code: 'resource.label', default: 'Resource'), id])
                redirect action:"index", method:"GET"
            }
            '*'{ render status: NO_CONTENT }
        }
    }

    @Secured(['ROLE_ADMIN'])
    def approve(Long id) {
        def resource = resourceService.get(id)
        if (!resource) {
            notFound()
            return
        }
        
        def currentUser = springSecurityService.currentUser as User
        resource.approvalStatus = 'approved'
        resource.approvedBy = currentUser
        resource.approvedDate = new Date()
        resource.rejectionReason = null
        
        resourceService.save(resource)
        
        flash.message = "Resource approved successfully."
        redirect action: 'index'
    }
    
    @Secured(['ROLE_ADMIN'])
    def reject(Long id) {
        def resource = resourceService.get(id)
        if (!resource) {
            notFound()
            return
        }
        
        if (request.method == 'POST') {
            // Process rejection
            def currentUser = springSecurityService.currentUser as User
            resource.approvalStatus = 'rejected'
            resource.approvedBy = currentUser
            resource.approvedDate = new Date()
            resource.rejectionReason = params.rejectionReason
            
            resourceService.save(resource)
            
            flash.message = "Resource rejected successfully."
            redirect action: 'index'
        } else {
            // Show rejection form
            respond resource, model: [resource: resource]
        }
    }

    protected void notFound() {
        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.not.found.message', args: [message(code: 'resource.label', default: 'Resource'), params.id])
                redirect action: "index", method: "GET"
            }
            '*'{ render status: NOT_FOUND }
        }
    }
}
