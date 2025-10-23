<!DOCTYPE html>
<html>
    <head>
        <meta name="layout" content="main" />
        <g:set var="entityName" value="${message(code: 'resource.label', default: 'Resource')}" />
        <title>Approve Resource - ${resource.name}</title>
    </head>
    <body>
    <div id="content" role="main">
        <div class="container">
            <nav aria-label="breadcrumb">
                <ol class="breadcrumb">
                    <li class="breadcrumb-item"><a href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></li>
                    <li class="breadcrumb-item"><g:link action="index"><g:message code="default.list.label" args="[entityName]" /></g:link></li>
                    <li class="breadcrumb-item"><g:link action="show" id="${resource.id}">Show</g:link></li>
                    <li class="breadcrumb-item active" aria-current="page">Approve Resource</li>
                </ol>
            </nav>
            <section class="row">
                <div id="approve-resource" class="col-12 content" role="main">
                    <h1>Approve Resource: ${resource.name}</h1>
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
                                
                                <g:if test="${resource.servicesOffered}">
                                    <dt class="col-sm-3">Services Offered:</dt>
                                    <dd class="col-sm-9">${resource.servicesOffered}</dd>
                                </g:if>
                                
                                <g:if test="${resource.hoursOfOperation}">
                                    <dt class="col-sm-3">Hours of Operation:</dt>
                                    <dd class="col-sm-9">${resource.hoursOfOperation}</dd>
                                </g:if>
                                
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
                    
                    <!-- Contact Information -->
                    <g:if test="${resource.contact}">
                        <div class="card mb-4">
                            <div class="card-header">
                                <h2 class="h5 mb-0">Contact Information</h2>
                            </div>
                            <div class="card-body">
                                <dl class="row">
                                    <g:if test="${resource.contact.phone}">
                                        <dt class="col-sm-3">Phone:</dt>
                                        <dd class="col-sm-9">${resource.contact.phone}</dd>
                                    </g:if>
                                    
                                    <g:if test="${resource.contact.email}">
                                        <dt class="col-sm-3">Email:</dt>
                                        <dd class="col-sm-9">${resource.contact.email}</dd>
                                    </g:if>
                                    
                                    <g:if test="${resource.contact.website}">
                                        <dt class="col-sm-3">Website:</dt>
                                        <dd class="col-sm-9">
                                            <a href="${resource.contact.website}" target="_blank" rel="noopener noreferrer" 
                                               aria-label="Visit website: ${resource.contact.website}">
                                                ${resource.contact.website}
                                            </a>
                                        </dd>
                                    </g:if>
                                    
                                    <g:if test="${resource.contact.address}">
                                        <dt class="col-sm-3">Address:</dt>
                                        <dd class="col-sm-9">
                                            ${resource.contact.address}<br/>
                                            <g:if test="${resource.contact.city}">${resource.contact.city}, </g:if>
                                            <g:if test="${resource.contact.state}">${resource.contact.state} </g:if>
                                            <g:if test="${resource.contact.zipCode}">${resource.contact.zipCode}</g:if>
                                        </dd>
                                    </g:if>
                                </dl>
                            </div>
                        </div>
                    </g:if>
                    
                    <!-- Eligibility Requirements -->
                    <g:if test="${resource.eligibilityRequirements}">
                        <div class="card mb-4">
                            <div class="card-header">
                                <h2 class="h5 mb-0">Eligibility Requirements</h2>
                            </div>
                            <div class="card-body">
                                <ul class="list-unstyled">
                                    <g:each in="${resource.eligibilityRequirements}" var="req">
                                        <li class="mb-2">
                                            <span class="badge badge-secondary">${req.type}</span>
                                            ${req.requirement}
                                        </li>
                                    </g:each>
                                </ul>
                            </div>
                        </div>
                    </g:if>
                    
                    <!-- Approval Actions -->
                    <div class="card">
                        <div class="card-header">
                            <h2 class="h5 mb-0">Approval Actions</h2>
                        </div>
                        <div class="card-body">
                            <div class="row">
                                <div class="col-md-6">
                                    <h3 class="h6">Approve Resource</h3>
                                    <p class="text-muted">Approve this resource to make it visible in the public directory.</p>
                                    <g:link class="btn btn-success btn-lg" action="approve" id="${resource.id}"
                                            onclick="return confirm('Approve this resource?')"
                                            aria-label="Approve ${resource.name}">
                                        <i class="fas fa-check" aria-hidden="true"></i> Approve Resource
                                    </g:link>
                                </div>
                                <div class="col-md-6">
                                    <h3 class="h6">Reject Resource</h3>
                                    <p class="text-muted">Reject this resource and provide a reason for rejection.</p>
                                    <g:link class="btn btn-danger btn-lg" action="reject" id="${resource.id}"
                                            aria-label="Reject ${resource.name}">
                                        <i class="fas fa-times" aria-hidden="true"></i> Reject Resource
                                    </g:link>
                                </div>
                            </div>
                        </div>
                    </div>
                    
                    <!-- Navigation -->
                    <div class="mt-4">
                        <g:link action="show" id="${resource.id}" class="btn btn-secondary" aria-label="View resource details">
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
