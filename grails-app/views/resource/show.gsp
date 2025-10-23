<!DOCTYPE html>
<html>
    <head>
        <meta name="layout" content="main" />
        <g:set var="entityName" value="${message(code: 'resource.label', default: 'Resource')}" />
        <title><g:message code="default.show.label" args="[entityName]" /></title>
    </head>
    <body>
    <div id="content" role="main">
        <div class="container">
            <nav aria-label="breadcrumb">
                <ol class="breadcrumb">
                    <li class="breadcrumb-item"><a href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></li>
                    <li class="breadcrumb-item"><g:link action="index"><g:message code="default.list.label" args="[entityName]" /></g:link></li>
                    <li class="breadcrumb-item active" aria-current="page"><g:message code="default.show.label" args="[entityName]" /></li>
                </ol>
            </nav>
            <section class="row">
                <div id="show-resource" class="col-12 content scaffold-show" role="main">
                    <h1><g:message code="default.show.label" args="[entityName]" /></h1>
                    <g:if test="${flash.message}">
                        <div class="alert alert-success" role="status" aria-live="polite">${flash.message}</div>
                    </g:if>
                    <article aria-labelledby="show-resource-heading">
                        <h2 id="show-resource-heading" class="sr-only">Resource Details</h2>
                        
                        <!-- Approval Status Banner -->
                        <g:if test="${resource.approvalStatus != 'approved'}">
                            <div class="alert alert-warning mb-4" role="alert" aria-live="polite">
                                <h3 class="alert-heading h5">
                                    <g:if test="${resource.approvalStatus == 'pending'}">
                                        <i class="fas fa-clock" aria-hidden="true"></i> Pending Approval
                                    </g:if>
                                    <g:elseif test="${resource.approvalStatus == 'rejected'}">
                                        <i class="fas fa-times-circle" aria-hidden="true"></i> Rejected
                                    </g:elseif>
                                </h3>
                                <g:if test="${resource.approvalStatus == 'pending'}">
                                    <p class="mb-0">This resource is pending admin approval and will not appear in the public directory until approved.</p>
                                </g:if>
                                <g:elseif test="${resource.approvalStatus == 'rejected'}">
                                    <p class="mb-0">
                                        This resource was rejected. 
                                        <g:if test="${resource.rejectionReason}">
                                            <strong>Reason:</strong> ${resource.rejectionReason}
                                        </g:if>
                                    </p>
                                </g:elseif>
                            </div>
                        </g:if>
                        
                        <!-- Provider Context Information -->
                        <g:if test="${springSecurityService.hasRole('ROLE_PROVIDER') && resource.submittedBy?.id == springSecurityService.currentUser?.id}">
                            <div class="alert alert-info mb-4" role="status" aria-live="polite">
                                <h3 class="alert-heading h5">Your Submitted Resource</h3>
                                <p class="mb-0">This is a resource you submitted. You can edit it, but changes will require re-approval.</p>
                            </div>
                        </g:if>
                        
                        <f:display bean="resource" />
                        
                        <!-- Approval Information for Admins -->
                        <g:if test="${springSecurityService.hasRole('ROLE_ADMIN')}">
                            <div class="card mt-4">
                                <div class="card-header">
                                    <h3 class="h5 mb-0">Approval Information</h3>
                                </div>
                                <div class="card-body">
                                    <dl class="row">
                                        <dt class="col-sm-3">Status:</dt>
                                        <dd class="col-sm-9">
                                            <g:if test="${resource.approvalStatus == 'approved'}">
                                                <span class="badge badge-success">Approved</span>
                                            </g:if>
                                            <g:elseif test="${resource.approvalStatus == 'pending'}">
                                                <span class="badge badge-warning">Pending</span>
                                            </g:elseif>
                                            <g:elseif test="${resource.approvalStatus == 'rejected'}">
                                                <span class="badge badge-danger">Rejected</span>
                                            </g:elseif>
                                        </dd>
                                        
                                        <g:if test="${resource.submittedBy}">
                                            <dt class="col-sm-3">Submitted By:</dt>
                                            <dd class="col-sm-9">${resource.submittedBy.firstName} ${resource.submittedBy.lastName} (${resource.submittedBy.email})</dd>
                                        </g:if>
                                        
                                        <g:if test="${resource.approvedBy}">
                                            <dt class="col-sm-3">Approved By:</dt>
                                            <dd class="col-sm-9">${resource.approvedBy.firstName} ${resource.approvedBy.lastName}</dd>
                                        </g:if>
                                        
                                        <g:if test="${resource.approvedDate}">
                                            <dt class="col-sm-3">Approved Date:</dt>
                                            <dd class="col-sm-9"><g:formatDate date="${resource.approvedDate}" format="MMM dd, yyyy 'at' h:mm a"/></dd>
                                        </g:if>
                                        
                                        <g:if test="${resource.rejectionReason}">
                                            <dt class="col-sm-3">Rejection Reason:</dt>
                                            <dd class="col-sm-9">${resource.rejectionReason}</dd>
                                        </g:if>
                                    </dl>
                                </div>
                            </div>
                        </g:if>
                    </article>
                    <div class="mt-4">
                        <fieldset class="buttons">
                            <legend class="sr-only">Resource Actions</legend>
                            <div class="btn-group" role="group" aria-label="Resource actions">
                                <!-- Edit button - only for admins or resource submitter -->
                                <g:if test="${!springSecurityService.hasRole('ROLE_PROVIDER') || resource.submittedBy?.id == springSecurityService.currentUser?.id}">
                                    <g:link class="btn btn-primary" action="edit" resource="${this.resource}" aria-label="Edit this resource">
                                        <i class="fas fa-edit" aria-hidden="true"></i> <g:message code="default.button.edit.label" default="Edit" />
                                    </g:link>
                                </g:if>
                                
                                <!-- Admin approval actions -->
                                <g:if test="${springSecurityService.hasRole('ROLE_ADMIN') && resource.approvalStatus == 'pending'}">
                                    <g:link class="btn btn-success" action="approve" id="${resource.id}"
                                            onclick="return confirm('Approve this resource?')"
                                            aria-label="Approve this resource">
                                        <i class="fas fa-check" aria-hidden="true"></i> Approve
                                    </g:link>
                                    <g:link class="btn btn-warning" action="reject" id="${resource.id}"
                                            aria-label="Reject this resource">
                                        <i class="fas fa-times" aria-hidden="true"></i> Reject
                                    </g:link>
                                </g:if>
                                
                                <!-- Delete button - only for admins -->
                                <g:if test="${springSecurityService.hasRole('ROLE_ADMIN')}">
                                    <g:form resource="${this.resource}" method="DELETE" style="display: inline;"
                                            onsubmit="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');">
                                        <button type="submit" class="btn btn-danger"
                                                aria-label="Delete this resource (requires confirmation)">
                                            <i class="fas fa-trash" aria-hidden="true"></i> <g:message code="default.button.delete.label" default="Delete" />
                                        </button>
                                    </g:form>
                                </g:if>
                                
                                <!-- Back to list -->
                                <g:link action="index" class="btn btn-secondary" aria-label="Return to resource list">
                                    <i class="fas fa-list" aria-hidden="true"></i> Back to List
                                </g:link>
                            </div>
                        </fieldset>
                    </div>
                </div>
            </section>
        </div>
    </div>
    </body>
</html>
