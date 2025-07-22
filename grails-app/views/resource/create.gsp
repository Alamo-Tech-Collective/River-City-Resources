<!DOCTYPE html>
<html>
    <head>
        <meta name="layout" content="main" />
        <g:set var="entityName" value="${message(code: 'resource.label', default: 'Resource')}" />
        <title><g:message code="default.create.label" args="[entityName]" /></title>
    </head>
    <body>
    <div id="content" role="main">
        <div class="container">
            <nav aria-label="breadcrumb">
                <ol class="breadcrumb">
                    <li class="breadcrumb-item"><a href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></li>
                    <li class="breadcrumb-item"><g:link action="index"><g:message code="default.list.label" args="[entityName]" /></g:link></li>
                    <li class="breadcrumb-item active" aria-current="page"><g:message code="default.create.label" args="[entityName]" /></li>
                </ol>
            </nav>
            <section class="row">
                <div id="create-resource" class="col-12 content scaffold-create" role="main">
                    <div class="content-header">
                        <h1><g:message code="default.create.label" args="[entityName]" /></h1>
                    </div>
                    <g:if test="${flash.message}">
                        <div class="alert alert-success" role="status" aria-live="polite">${flash.message}</div>
                    </g:if>
                    <g:hasErrors bean="${this.resource}">
                        <div class="alert alert-danger" role="alert" aria-live="assertive">
                            <h2 class="alert-heading h5">Please correct the following errors:</h2>
                            <ul class="mb-0">
                                <g:eachError bean="${this.resource}" var="error">
                                    <li <g:if test="${error in org.springframework.validation.FieldError}">data-field-id="${error.field}"</g:if>><g:message error="${error}"/></li>
                                </g:eachError>
                            </ul>
                        </div>
                    </g:hasErrors>
                    <g:form resource="${this.resource}" method="POST" role="form" aria-label="Create resource form">
                        <fieldset class="form">
                            <legend class="sr-only">Resource Information</legend>
                            
                            <!-- Basic Information -->
                            <div class="card mb-4">
                                <div class="card-header">
                                    <h5 class="mb-0">Basic Information</h5>
                                </div>
                                <div class="card-body">
                                    <div class="form-group">
                                        <label for="name" class="required">Name</label>
                                        <g:textField name="name" 
                                                    value="${resource?.name}" 
                                                    class="form-control ${hasErrors(bean: resource, field: 'name', 'is-invalid')}"
                                                    required="required"
                                                    placeholder="Organization Name"/>
                                        <g:renderErrors bean="${resource}" field="name" class="invalid-feedback" />
                                    </div>
                                    
                                    <div class="form-group">
                                        <label for="description" class="required">Description</label>
                                        <g:textArea name="description" 
                                                   value="${resource?.description}" 
                                                   class="form-control ${hasErrors(bean: resource, field: 'description', 'is-invalid')}"
                                                   rows="4"
                                                   required="required"
                                                   placeholder="Provide a detailed description of the organization and its mission"/>
                                        <g:renderErrors bean="${resource}" field="description" class="invalid-feedback" />
                                    </div>
                                    
                                    <div class="form-group">
                                        <label for="category.id" class="required">Category</label>
                                        <g:select name="category.id" 
                                                 from="${riverCityResources.Category.findAllByActive(true, [sort: 'displayOrder'])}"
                                                 optionKey="id" 
                                                 optionValue="name"
                                                 value="${resource?.category?.id}"
                                                 class="form-control ${hasErrors(bean: resource, field: 'category', 'is-invalid')}"
                                                 required="required"
                                                 noSelection="['':'-- Select a Category --']"/>
                                        <g:renderErrors bean="${resource}" field="category" class="invalid-feedback" />
                                    </div>
                                    
                                    <div class="row">
                                        <div class="col-md-6">
                                            <div class="form-group">
                                                <label for="servicesOffered">Services Offered</label>
                                                <g:textArea name="servicesOffered" 
                                                           value="${resource?.servicesOffered}" 
                                                           class="form-control ${hasErrors(bean: resource, field: 'servicesOffered', 'is-invalid')}"
                                                           rows="3"
                                                           placeholder="List the specific services provided"/>
                                                <g:renderErrors bean="${resource}" field="servicesOffered" class="invalid-feedback" />
                                            </div>
                                        </div>
                                        <div class="col-md-6">
                                            <div class="form-group">
                                                <label for="hoursOfOperation">Hours of Operation</label>
                                                <g:textArea name="hoursOfOperation" 
                                                           value="${resource?.hoursOfOperation}" 
                                                           class="form-control ${hasErrors(bean: resource, field: 'hoursOfOperation', 'is-invalid')}"
                                                           rows="3"
                                                           placeholder="Monday-Friday: 9 AM - 5 PM&#10;Saturday: 10 AM - 2 PM"/>
                                                <g:renderErrors bean="${resource}" field="hoursOfOperation" class="invalid-feedback" />
                                            </div>
                                        </div>
                                    </div>
                                    
                                    <div class="form-check">
                                        <g:checkBox name="featured" 
                                                   value="${resource?.featured}" 
                                                   class="form-check-input"/>
                                        <label class="form-check-label" for="featured">
                                            Featured Resource
                                            <small class="text-muted">(Will be highlighted in search results)</small>
                                        </label>
                                    </div>
                                    
                                    <div class="form-check">
                                        <g:checkBox name="active" 
                                                   value="${resource?.active != false}" 
                                                   class="form-check-input"/>
                                        <label class="form-check-label" for="active">
                                            Active
                                            <small class="text-muted">(Uncheck to hide from public directory)</small>
                                        </label>
                                    </div>
                                </div>
                            </div>
                            
                            <!-- Contact Information -->
                            <div class="card mb-4">
                                <div class="card-header">
                                    <h5 class="mb-0">Contact Information</h5>
                                </div>
                                <div class="card-body">
                                    <div class="row">
                                        <div class="col-md-6">
                                            <div class="form-group">
                                                <label for="contact.phone">Phone</label>
                                                <g:textField name="contact.phone" 
                                                            value="${resource?.contact?.phone}" 
                                                            class="form-control"
                                                            placeholder="(210) 555-0123"/>
                                            </div>
                                        </div>
                                        <div class="col-md-6">
                                            <div class="form-group">
                                                <label for="contact.email">Email</label>
                                                <g:field type="email" 
                                                        name="contact.email" 
                                                        value="${resource?.contact?.email}" 
                                                        class="form-control"
                                                        placeholder="contact@organization.org"/>
                                            </div>
                                        </div>
                                    </div>
                                    
                                    <div class="form-group">
                                        <label for="contact.website">Website</label>
                                        <g:field type="url" 
                                                name="contact.website" 
                                                value="${resource?.contact?.website}" 
                                                class="form-control"
                                                placeholder="https://www.organization.org"/>
                                    </div>
                                    
                                    <div class="form-group">
                                        <label for="contact.address">Address</label>
                                        <g:textField name="contact.address" 
                                                   value="${resource?.contact?.address}" 
                                                   class="form-control"
                                                   placeholder="123 Main Street"/>
                                    </div>
                                    
                                    <div class="row">
                                        <div class="col-md-6">
                                            <div class="form-group">
                                                <label for="contact.city">City</label>
                                                <g:textField name="contact.city" 
                                                           value="${resource?.contact?.city}" 
                                                           class="form-control"
                                                           placeholder="San Antonio"/>
                                            </div>
                                        </div>
                                        <div class="col-md-3">
                                            <div class="form-group">
                                                <label for="contact.state">State</label>
                                                <g:textField name="contact.state" 
                                                           value="${resource?.contact?.state}" 
                                                           class="form-control"
                                                           placeholder="TX"
                                                           maxlength="2"/>
                                            </div>
                                        </div>
                                        <div class="col-md-3">
                                            <div class="form-group">
                                                <label for="contact.zipCode">ZIP Code</label>
                                                <g:textField name="contact.zipCode" 
                                                           value="${resource?.contact?.zipCode}" 
                                                           class="form-control"
                                                           placeholder="78201"/>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            
                            <!-- Eligibility Requirements -->
                            <div class="card mb-4">
                                <div class="card-header">
                                    <h5 class="mb-0">Eligibility Requirements</h5>
                                </div>
                                <div class="card-body">
                                    <f:field bean="resource" property="eligibilityRequirements"/>
                                </div>
                            </div>
                        </fieldset>
                        <fieldset class="buttons mt-4">
                            <legend class="sr-only">Form Actions</legend>
                            <g:submitButton name="create" class="btn btn-primary" value="${message(code: 'default.button.create.label', default: 'Create')}" 
                                            aria-describedby="create-help" />
                            <g:link action="index" class="btn btn-secondary ml-2" aria-label="Cancel and return to resource list">Cancel</g:link>
                            <div id="create-help" class="form-text mt-2">Create a new resource with the information provided above.</div>
                        </fieldset>
                    </g:form>
                </div>
            </section>
        </div>
    </div>
    </body>
</html>
