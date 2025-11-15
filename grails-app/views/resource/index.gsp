<!DOCTYPE html>
<html>
    <head>
        <meta name="layout" content="main" />
        <g:set var="entityName" value="${message(code: 'resource.label', default: 'Resource')}" />
        <title><g:message code="default.list.label" args="[entityName]" /></title>
    </head>
    <body>
    <div id="content" role="main">
        <div class="container">
            <nav aria-label="breadcrumb">
                <ol class="breadcrumb">
                    <li class="breadcrumb-item"><a href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></li>
                    <li class="breadcrumb-item active" aria-current="page"><g:message code="default.list.label" args="[entityName]" /></li>
                </ol>
            </nav>
            <section class="row">
                <div id="list-resource" class="col-12 content scaffold-list" role="main">
                    <div class="d-flex justify-content-between align-items-center mb-4">
                        <h1><g:message code="default.list.label" args="[entityName]" /></h1>
                        <g:link class="btn btn-primary" action="create" aria-label="Create a new resource">
                            <i class="fas fa-plus" aria-hidden="true"></i> <g:message code="default.new.label" args="[entityName]" />
                        </g:link>
                    </div>
                    <g:if test="${flash.message}">
                        <div class="alert alert-success" role="status" aria-live="polite">${flash.message}</div>
                    </g:if>
                    
                    <!-- Provider Context Information -->
                    <g:if test="${isProvider}">
                        <div class="alert alert-info" role="status" aria-live="polite">
                            <h2 class="alert-heading h5">Provider Dashboard</h2>
                            <p class="mb-0">As a service provider, you can view and edit only the resources you have submitted that have been approved. Your submitted resources require admin approval before they appear in the public directory.</p>
                        </div>
                    </g:if>
                    
                    <div class="table-responsive">
                        <table class="table table-striped table-hover" role="table" aria-label="List of resources">
                            <caption class="sr-only">
                                Table showing ${resourceList?.size() ?: 0} resources. 
                                Use arrow keys to navigate table cells and tab to access action buttons.
                            </caption>
                            <thead>
                                <tr>
                                    <th scope="col">Name</th>
                                    <th scope="col">Category</th>
                                    <th scope="col">Status</th>
                                    <th scope="col">Submitted By</th>
                                    <th scope="col">Actions</th>
                                </tr>
                            </thead>
                            <tbody>
                                <!-- Provider Empty Resources Message -->
                                <g:if test="${isProvider && (isEmpty || resourceCount == 0)}">
                                    <tr>
                                        <td colspan="5">
                                            <div class="alert alert-info m-3" role="status" aria-live="polite">
                                                <p class="mb-0">
                                                    <i class="fas fa-info-circle" aria-hidden="true"></i>
                                                    You do not have any approved resources. Please submit a new resource or come back later after an admin has had a chance to review your submission(s).
                                                </p>
                                            </div>
                                        </td>
                                    </tr>
                                </g:if>
                                <g:each in="${resourceList}" var="resource" status="i">
                                    <tr>
                                        <td>
                                            <g:link action="show" id="${resource.id}" aria-label="View details for ${resource.name}">
                                                ${resource.name}
                                            </g:link>
                                            <g:if test="${resource.featured}">
                                                <span class="badge badge-warning ml-2" aria-label="Featured resource">Featured</span>
                                            </g:if>
                                        </td>
                                        <td>${resource.category?.name}</td>
                                        <td>
                                            <g:if test="${resource.approvalStatus == 'approved'}">
                                                <span class="badge badge-success" aria-label="Approved resource">Approved</span>
                                            </g:if>
                                            <g:elseif test="${resource.approvalStatus == 'pending'}">
                                                <span class="badge badge-warning" aria-label="Pending approval">Pending</span>
                                            </g:elseif>
                                            <g:elseif test="${resource.approvalStatus == 'rejected'}">
                                                <span class="badge badge-danger" aria-label="Rejected resource">Rejected</span>
                                            </g:elseif>
                                        </td>
                                        <td>
                                            <g:if test="${resource.submittedBy}">
                                                ${resource.submittedBy.firstName} ${resource.submittedBy.lastName}
                                            </g:if>
                                            <g:else>
                                                <span class="text-muted">System</span>
                                            </g:else>
                                        </td>
                                        <td>
                                            <div class="btn-group" role="group" aria-label="Actions for ${resource.name}">
                                                <g:link class="btn btn-sm btn-outline-primary" action="show" id="${resource.id}" 
                                                        aria-label="View details for ${resource.name}">
                                                    <i class="fas fa-eye" aria-hidden="true"></i> View
                                                </g:link>
                                                
                                                <!-- Edit button - only for admins or resource submitter -->
                                                <g:if test="${!springSecurityService?.currentUser?.authorities?.any { it.authority == 'ROLE_PROVIDER' } || resource.submittedBy?.id == springSecurityService.currentUser?.id}">
                                                    <g:link class="btn btn-sm btn-outline-secondary" action="edit" id="${resource.id}"
                                                            aria-label="Edit ${resource.name}">
                                                        <i class="fas fa-edit" aria-hidden="true"></i> Edit
                                                    </g:link>
                                                </g:if>
                                                
                                                <!-- Admin approval actions -->
                                                <g:if test="${springSecurityService?.currentUser?.authorities?.any { it.authority == 'ROLE_ADMIN' } && resource.approvalStatus == 'pending'}">
                                                    <g:link class="btn btn-sm btn-success" action="approve" id="${resource.id}"
                                                            onclick="return confirm('Approve this resource?')"
                                                            aria-label="Approve ${resource.name}">
                                                        <i class="fas fa-check" aria-hidden="true"></i> Approve
                                                    </g:link>
                                                    <g:link class="btn btn-sm btn-danger" action="reject" id="${resource.id}"
                                                            aria-label="Reject ${resource.name}">
                                                        <i class="fas fa-times" aria-hidden="true"></i> Reject
                                                    </g:link>
                                                </g:if>
                                                
                                                <!-- Delete button - only for admins -->
                                                <g:if test="${springSecurityService?.currentUser?.authorities?.any { it.authority == 'ROLE_ADMIN' }}">
                                                    <g:form resource="${resource}" method="DELETE" style="display: inline;"
                                                            onsubmit="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');">
                                                        <button type="submit" class="btn btn-sm btn-outline-danger"
                                                                aria-label="Delete ${resource.name}">
                                                            <i class="fas fa-trash" aria-hidden="true"></i> Delete
                                                        </button>
                                                    </g:form>
                                                </g:if>
                                            </div>
                                        </td>
                                    </tr>
                                </g:each>
                            </tbody>
                        </table>
                    </div>

                    <g:if test="${resourceCount > params.int('max')}">
                        <nav aria-label="Resource pagination" class="mt-4">
                            <div class="pagination">
                                <g:paginate total="${resourceCount ?: 0}" />
                            </div>
                        </nav>
                    </g:if>
                </div>
            </section>
        </div>
    </div>
    </body>
</html>