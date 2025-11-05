<!DOCTYPE html>
<html>
    <head>
        <meta name="layout" content="main" />
        <g:set var="entityName" value="${message(code: 'resource.label', default: 'Resource')}" />
        <title>Reject Resource - ${resource.name}</title>
    </head>
    <body>
    <div id="content" role="main">
        <div class="container">
            <nav aria-label="breadcrumb">
                <ol class="breadcrumb">
                    <li class="breadcrumb-item"><a href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></li>
                    <li class="breadcrumb-item"><g:link action="index"><g:message code="default.list.label" args="[entityName]" /></g:link></li>
                    <li class="breadcrumb-item"><g:link action="show" id="${resource.id}">Show</g:link></li>
                    <li class="breadcrumb-item active" aria-current="page">Reject Resource</li>
                </ol>
            </nav>
            <section class="row">
                <div id="reject-resource" class="col-12 content" role="main">
                    <h1>Reject Resource: ${resource.name}</h1>
                    <g:if test="${flash.message}">
                        <div class="alert alert-success" role="status" aria-live="polite">${flash.message}</div>
                    </g:if>
                    
                    <!-- Resource Information -->
                    <div class="card mb-4">
                        <div class="card-header">
                            <h2 class="h5 mb-0">Resource Information</h2>
                        </div>
                        <div class="card-body">
                            <dl class="row">
                                <dt class="col-sm-3">Name:</dt>
                                <dd class="col-sm-9">${resource.name}</dd>
                                
                                <dt class="col-sm-3">Category:</dt>
                                <dd class="col-sm-9">${resource.category?.name}</dd>
                                
                                <dt class="col-sm-3">Description:</dt>
                                <dd class="col-sm-9">${resource.description}</dd>
                                
                                <dt class="col-sm-3">Submitted By:</dt>
                                <dd class="col-sm-9">
                                    <g:if test="${resource.submittedBy}">
                                        ${resource.submittedBy.firstName} ${resource.submittedBy.lastName} (${resource.submittedBy.email})
                                    </g:if>
                                    <g:else>
                                        <span class="text-muted">System</span>
                                    </g:else>
                                </dd>
                                
                                <dt class="col-sm-3">Submitted Date:</dt>
                                <dd class="col-sm-9"><g:formatDate date="${resource.dateCreated}" format="MMM dd, yyyy 'at' h:mm a"/></dd>
                            </dl>
                        </div>
                    </div>
                    
                    <!-- Rejection Form -->
                    <div class="card">
                        <div class="card-header">
                            <h2 class="h5 mb-0">Rejection Reason</h2>
                        </div>
                        <div class="card-body">
                            <g:form action="reject" method="POST" role="form" aria-label="Reject resource form">
                                <g:hiddenField name="id" value="${resource.id}" />
                                
                                <div class="form-group">
                                    <label for="rejectionReason" class="required">Reason for Rejection</label>
                                    <g:textArea name="rejectionReason" 
                                               class="form-control"
                                               rows="4"
                                               required="required"
                                               placeholder="Please provide a clear reason for rejecting this resource. This will be shared with the submitter."
                                               aria-describedby="rejectionHelp"/>
                                    <div id="rejectionHelp" class="form-text">
                                        This reason will be shared with the resource submitter to help them understand why the resource was rejected.
                                    </div>
                                </div>
                                
                                <div class="alert alert-warning" role="alert">
                                    <h3 class="alert-heading h6">Warning</h3>
                                    <p class="mb-0">Rejecting this resource will prevent it from appearing in the public directory. The submitter will be notified of the rejection with the reason you provide.</p>
                                </div>
                                
                                <fieldset class="buttons mt-4">
                                    <legend class="sr-only">Rejection Actions</legend>
                                    <button type="submit" class="btn btn-danger btn-lg" 
                                            onclick="return confirm('Are you sure you want to reject this resource?')"
                                            aria-describedby="reject-help">
                                        <i class="fas fa-times" aria-hidden="true"></i> Reject Resource
                                    </button>
                                    <g:link action="show" id="${resource.id}" class="btn btn-secondary ml-2" 
                                            aria-label="Cancel and view resource details">
                                        <i class="fas fa-eye" aria-hidden="true"></i> Cancel
                                    </g:link>
                                    <div id="reject-help" class="form-text mt-2">
                                        This action will reject the resource and notify the submitter.
                                    </div>
                                </fieldset>
                            </g:form>
                        </div>
                    </div>
                    
                    <!-- Navigation -->
                    <div class="mt-4">
                        <g:link action="show" id="${resource.id}" class="btn btn-outline-primary" aria-label="View resource details">
                            <i class="fas fa-eye" aria-hidden="true"></i> View Details
                        </g:link>
                        <g:link action="index" class="btn btn-outline-secondary ml-2" aria-label="Return to resource list">
                            <i class="fas fa-list" aria-hidden="true"></i> Back to List
                        </g:link>
                    </div>
                </div>
            </section>
        </div>
    </div>
    </body>
</html>
