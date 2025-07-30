<!DOCTYPE html>
<html>
<head>
    <meta name="layout" content="main" />
    <g:set var="entityName" value="${message(code: 'user.label', default: 'User')}" />
    <title><g:message code="default.show.label" args="[entityName]" /></title>
</head>
<body>
    <div class="nav" role="navigation">
        <ul>
            <li><a class="home" href="${createLink(uri: '/admin')}"><g:message code="default.home.label"/></a></li>
            <li><g:link class="list" action="index"><g:message code="default.list.label" args="[entityName]" /></g:link></li>
            <li><g:link class="create" action="create"><g:message code="default.new.label" args="[entityName]" /></g:link></li>
        </ul>
    </div>
    <div id="show-user" class="content scaffold-show" role="main">
        <h1><g:message code="default.show.label" args="[entityName]" /></h1>
        <g:if test="${flash.message}">
            <div class="message" role="status">${flash.message}</div>
        </g:if>
        <g:if test="${flash.error}">
            <div class="errors" role="alert">${flash.error}</div>
        </g:if>
        
        <div class="card">
            <div class="card-body">
                <h2 class="card-title">${this.user.firstName} ${this.user.lastName}</h2>
                
                <dl class="row">
                    <dt class="col-sm-3">Username</dt>
                    <dd class="col-sm-9">${this.user.username}</dd>
                    
                    <dt class="col-sm-3">Email</dt>
                    <dd class="col-sm-9">
                        <a href="mailto:${this.user.email}">${this.user.email}</a>
                    </dd>
                    
                    <dt class="col-sm-3">First Name</dt>
                    <dd class="col-sm-9">${this.user.firstName}</dd>
                    
                    <dt class="col-sm-3">Last Name</dt>
                    <dd class="col-sm-9">${this.user.lastName}</dd>
                    
                    <dt class="col-sm-3">Roles</dt>
                    <dd class="col-sm-9">
                        <g:each in="${userRoles}" var="role">
                            <span class="badge badge-info">${role.authority.replace('ROLE_', '')}</span>
                        </g:each>
                    </dd>
                    
                    <dt class="col-sm-3">Account Status</dt>
                    <dd class="col-sm-9">
                        <g:if test="${this.user.enabled}">
                            <span class="badge badge-success">Active</span>
                        </g:if>
                        <g:else>
                            <span class="badge badge-danger">Disabled</span>
                        </g:else>
                        
                        <g:if test="${this.user.accountExpired}">
                            <span class="badge badge-warning">Account Expired</span>
                        </g:if>
                        
                        <g:if test="${this.user.accountLocked}">
                            <span class="badge badge-danger">Account Locked</span>
                        </g:if>
                        
                        <g:if test="${this.user.passwordExpired}">
                            <span class="badge badge-warning">Password Expired</span>
                        </g:if>
                    </dd>
                </dl>
            </div>
        </div>
        
        <g:form resource="${this.user}" method="DELETE">
            <fieldset class="buttons">
                <g:link class="edit btn btn-primary" action="edit" resource="${this.user}">
                    <g:message code="default.button.edit.label" default="Edit" />
                </g:link>
                <g:link class="btn btn-warning" action="changePassword" id="${this.user.id}">
                    Change Password
                </g:link>
                <input class="delete btn btn-danger" type="submit" 
                       value="${message(code: 'default.button.delete.label', default: 'Delete')}" 
                       onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" />
            </fieldset>
        </g:form>
    </div>
</body>
</html>