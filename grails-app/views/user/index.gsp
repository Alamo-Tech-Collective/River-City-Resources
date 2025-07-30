<!DOCTYPE html>
<html>
<head>
    <meta name="layout" content="main" />
    <g:set var="entityName" value="${message(code: 'user.label', default: 'User')}" />
    <title><g:message code="default.list.label" args="[entityName]" /></title>
</head>
<body>
    <div class="nav" role="navigation">
        <ul>
            <li><a class="home" href="${createLink(uri: '/admin')}"><g:message code="default.home.label"/></a></li>
            <li><g:link class="create" action="create"><g:message code="default.new.label" args="[entityName]" /></g:link></li>
        </ul>
    </div>
    <div id="list-user" class="content scaffold-list" role="main">
        <h1><g:message code="default.list.label" args="[entityName]" /></h1>
        <g:if test="${flash.message}">
            <div class="message" role="status">${flash.message}</div>
        </g:if>
        <g:if test="${flash.error}">
            <div class="errors" role="alert">${flash.error}</div>
        </g:if>
        
        <div class="search-bar">
            <g:form method="GET" action="index">
                <fieldset class="form">
                    <div class="fieldcontain">
                        <label for="search">Search Users:</label>
                        <g:textField name="search" value="${params.search}" placeholder="Username, email, or name..." />
                        <g:select name="enabled" from="['':'All Users', 'true':'Active Only', 'false':'Disabled Only']" 
                                  optionKey="key" optionValue="value" value="${params.enabled}" />
                        <g:submitButton name="searchButton" value="Search" class="btn btn-primary" />
                        <g:link action="index" class="btn btn-secondary">Clear</g:link>
                    </div>
                </fieldset>
            </g:form>
        </div>
        
        <div class="table-responsive">
            <table class="table table-striped">
                <thead>
                    <tr>
                        <g:sortableColumn property="username" title="${message(code: 'user.username.label', default: 'Username')}" />
                        <g:sortableColumn property="email" title="${message(code: 'user.email.label', default: 'Email')}" />
                        <g:sortableColumn property="firstName" title="${message(code: 'user.firstName.label', default: 'First Name')}" />
                        <g:sortableColumn property="lastName" title="${message(code: 'user.lastName.label', default: 'Last Name')}" />
                        <th>Roles</th>
                        <g:sortableColumn property="enabled" title="${message(code: 'user.enabled.label', default: 'Status')}" />
                        <th>Actions</th>
                    </tr>
                </thead>
                <tbody>
                <g:each in="${userList}" status="i" var="userInstance">
                    <tr class="${(i % 2) == 0 ? 'even' : 'odd'}">
                        <td>
                            <g:link action="show" id="${userInstance.id}">${fieldValue(bean: userInstance, field: "username")}</g:link>
                        </td>
                        <td>${fieldValue(bean: userInstance, field: "email")}</td>
                        <td>${fieldValue(bean: userInstance, field: "firstName")}</td>
                        <td>${fieldValue(bean: userInstance, field: "lastName")}</td>
                        <td>
                            <g:each in="${userInstance.authorities}" var="authority">
                                <span class="badge badge-info">${authority.authority.replace('ROLE_', '')}</span>
                            </g:each>
                        </td>
                        <td>
                            <g:if test="${userInstance.enabled}">
                                <span class="badge badge-success">Active</span>
                            </g:if>
                            <g:else>
                                <span class="badge badge-danger">Disabled</span>
                            </g:else>
                        </td>
                        <td>
                            <div class="btn-group btn-group-sm" role="group" aria-label="User actions">
                                <g:link action="edit" id="${userInstance.id}" class="btn btn-sm btn-primary" 
                                        aria-label="Edit ${userInstance.username}">
                                    <i class="fas fa-edit"></i> Edit
                                </g:link>
                                <g:link action="changePassword" id="${userInstance.id}" class="btn btn-sm btn-warning"
                                        aria-label="Change password for ${userInstance.username}">
                                    <i class="fas fa-key"></i> Password
                                </g:link>
                                <g:form action="toggleStatus" method="POST" style="display:inline;">
                                    <g:hiddenField name="id" value="${userInstance.id}" />
                                    <button type="submit" class="btn btn-sm ${userInstance.enabled ? 'btn-secondary' : 'btn-success'}"
                                            aria-label="${userInstance.enabled ? 'Disable' : 'Enable'} ${userInstance.username}">
                                        <i class="fas ${userInstance.enabled ? 'fa-ban' : 'fa-check'}"></i>
                                        ${userInstance.enabled ? 'Disable' : 'Enable'}
                                    </button>
                                </g:form>
                            </div>
                        </td>
                    </tr>
                </g:each>
                </tbody>
            </table>
        </div>
        
        <div class="pagination">
            <g:paginate total="${userCount ?: 0}" params="${params}" />
        </div>
    </div>
</body>
</html>