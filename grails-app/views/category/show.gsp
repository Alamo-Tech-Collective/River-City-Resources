<!DOCTYPE html>
<html>
    <head>
        <meta name="layout" content="main" />
        <g:set var="entityName" value="${message(code: 'category.label', default: 'Category')}" />
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
                <div id="show-category" class="col-12 content scaffold-show" role="main">
                    <h1><g:message code="default.show.label" args="[entityName]" /></h1>
                    <g:if test="${flash.message}">
                        <div class="alert alert-success" role="status" aria-live="polite">${flash.message}</div>
                    </g:if>
                    <article aria-labelledby="show-category-heading">
                        <h2 id="show-category-heading" class="sr-only">Category Details</h2>
                        <f:display bean="category" />
                    </article>
                    <g:form resource="${this.category}" method="DELETE" onsubmit="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');">
                        <fieldset class="buttons mt-4">
                            <legend class="sr-only">Category Actions</legend>
                            <g:link class="btn btn-primary" action="edit" resource="${this.category}" aria-label="Edit this category">
                                <i class="fas fa-edit" aria-hidden="true"></i> <g:message code="default.button.edit.label" default="Edit" />
                            </g:link>
                            <input class="btn btn-danger ml-2" type="submit" 
                                   value="${message(code: 'default.button.delete.label', default: 'Delete')}" 
                                   aria-label="Delete this category (requires confirmation)" />
                        </fieldset>
                    </g:form>
                </div>
            </section>
        </div>
    </div>
    </body>
</html>
