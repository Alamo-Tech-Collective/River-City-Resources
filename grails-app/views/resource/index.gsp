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
                    
                    <div class="table-responsive">
                        <table class="table table-striped table-hover" role="table" aria-label="List of resources">
                            <caption class="sr-only">
                                Table showing ${resourceList?.size() ?: 0} resources. 
                                Use arrow keys to navigate table cells and tab to access action buttons.
                            </caption>
                            <f:table collection="${resourceList}" />
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