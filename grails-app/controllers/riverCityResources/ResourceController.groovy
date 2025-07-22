package riverCityResources

import grails.validation.ValidationException
import grails.plugin.springsecurity.annotation.Secured
import static org.springframework.http.HttpStatus.*

@Secured(['ROLE_ADMIN'])
class ResourceController {

    ResourceService resourceService

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        respond resourceService.list(params), model:[resourceCount: resourceService.count()]
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

        try {
            resourceService.save(resource)
        } catch (ValidationException e) {
            respond resource.errors, view:'create', model: [categories: Category.findAllByActive(true, [sort: 'displayOrder'])]
            return
        }

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.created.message', args: [message(code: 'resource.label', default: 'Resource'), resource.id])
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
        respond resource, model: [categories: Category.findAllByActive(true, [sort: 'displayOrder'])]
    }

    def update(Resource resource) {
        if (resource == null) {
            notFound()
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

        try {
            resourceService.save(resource)
        } catch (ValidationException e) {
            respond resource.errors, view:'edit', model: [categories: Category.findAllByActive(true, [sort: 'displayOrder'])]
            return
        }

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.updated.message', args: [message(code: 'resource.label', default: 'Resource'), resource.id])
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

        resourceService.delete(id)

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.deleted.message', args: [message(code: 'resource.label', default: 'Resource'), id])
                redirect action:"index", method:"GET"
            }
            '*'{ render status: NO_CONTENT }
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
