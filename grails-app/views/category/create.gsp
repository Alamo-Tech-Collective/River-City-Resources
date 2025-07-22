<!DOCTYPE html>
<html>
    <head>
        <meta name="layout" content="main" />
        <g:set var="entityName" value="${message(code: 'category.label', default: 'Category')}" />
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
                <div id="create-category" class="col-12 content scaffold-create" role="main">
                    <h1><g:message code="default.create.label" args="[entityName]" /></h1>
                    <g:if test="${flash.message}">
                        <div class="alert alert-success" role="status" aria-live="polite">${flash.message}</div>
                    </g:if>
                    <g:hasErrors bean="${this.category}">
                        <div class="alert alert-danger" role="alert" aria-live="assertive">
                            <h2 class="alert-heading h5">Please correct the following errors:</h2>
                            <ul class="mb-0">
                                <g:eachError bean="${this.category}" var="error">
                                    <li <g:if test="${error in org.springframework.validation.FieldError}">data-field-id="${error.field}"</g:if>><g:message error="${error}"/></li>
                                </g:eachError>
                            </ul>
                        </div>
                    </g:hasErrors>
                    <g:form resource="${this.category}" method="POST" role="form" aria-label="Create category form">
                        <fieldset class="form">
                            <legend class="sr-only">Category Information</legend>
                            
                            <div class="card">
                                <div class="card-body">
                                    <div class="form-group">
                                        <label for="name" class="required">Name</label>
                                        <g:textField name="name" 
                                                    value="${category?.name}" 
                                                    class="form-control ${hasErrors(bean: category, field: 'name', 'is-invalid')}"
                                                    required="required"
                                                    placeholder="Category Name (e.g., Transportation, Employment)"
                                                    maxlength="100"/>
                                        <g:renderErrors bean="${category}" field="name" class="invalid-feedback" />
                                    </div>
                                    
                                    <div class="form-group">
                                        <label for="description">Description</label>
                                        <g:textArea name="description" 
                                                   value="${category?.description}" 
                                                   class="form-control ${hasErrors(bean: category, field: 'description', 'is-invalid')}"
                                                   rows="3"
                                                   placeholder="Brief description of this category"/>
                                        <g:renderErrors bean="${category}" field="description" class="invalid-feedback" />
                                    </div>
                                    
                                    <div class="form-group">
                                        <label for="displayOrder" class="required">Display Order</label>
                                        <g:field type="number" 
                                                name="displayOrder" 
                                                value="${category?.displayOrder}" 
                                                class="form-control ${hasErrors(bean: category, field: 'displayOrder', 'is-invalid')}"
                                                required="required"
                                                placeholder="1"
                                                min="1"/>
                                        <g:renderErrors bean="${category}" field="displayOrder" class="invalid-feedback" />
                                        <small class="form-text text-muted">Lower numbers appear first in lists</small>
                                    </div>
                                    
                                    <div class="form-check">
                                        <g:checkBox name="active" 
                                                   value="${category?.active != false}" 
                                                   class="form-check-input"/>
                                        <label class="form-check-label" for="active">
                                            Active
                                            <small class="text-muted">(Uncheck to hide this category)</small>
                                        </label>
                                    </div>
                                </div>
                            </div>
                        </fieldset>
                        <fieldset class="buttons mt-4">
                            <legend class="sr-only">Form Actions</legend>
                            <g:submitButton name="create" class="btn btn-primary" value="${message(code: 'default.button.create.label', default: 'Create')}" 
                                            aria-describedby="create-help" />
                            <g:link action="index" class="btn btn-secondary ml-2" aria-label="Cancel and return to category list">Cancel</g:link>
                            <div id="create-help" class="form-text mt-2">Create a new category with the information provided above.</div>
                        </fieldset>
                    </g:form>
                </div>
            </section>
        </div>
    </div>
    <asset:javascript src="admin-forms.js"/>
    </body>
</html>
