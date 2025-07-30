<!DOCTYPE html>
<html>
<head>
    <meta name="layout" content="main" />
    <title>Change Password for ${user.username}</title>
</head>
<body>
    <div class="nav" role="navigation">
        <ul>
            <li><a class="home" href="${createLink(uri: '/admin')}"><g:message code="default.home.label"/></a></li>
            <li><g:link class="list" action="index">User List</g:link></li>
            <li><g:link class="show" action="show" id="${user.id}">Back to User</g:link></li>
        </ul>
    </div>
    <div id="change-password" class="content" role="main">
        <h1>Change Password for ${user.username}</h1>
        <g:if test="${flash.message}">
            <div class="message" role="status">${flash.message}</div>
        </g:if>
        <g:if test="${flash.error}">
            <div class="errors" role="alert">${flash.error}</div>
        </g:if>
        
        <div class="card">
            <div class="card-body">
                <p class="text-muted">Changing password for user: <strong>${user.firstName} ${user.lastName} (${user.username})</strong></p>
                
                <g:form action="changePassword" method="POST">
                    <g:hiddenField name="id" value="${user.id}" />
                    <fieldset class="form">
                        <div class="fieldcontain required">
                            <label for="newPassword">
                                New Password
                                <span class="required-indicator">*</span>
                            </label>
                            <g:passwordField name="newPassword" required="" 
                                           aria-required="true" 
                                           aria-describedby="newPassword-help" 
                                           class="form-control" />
                            <small id="newPassword-help" class="form-text text-muted">
                                Must be at least 8 characters long with uppercase, lowercase, number, and special character
                            </small>
                        </div>
                        
                        <div class="fieldcontain required">
                            <label for="confirmPassword">
                                Confirm Password
                                <span class="required-indicator">*</span>
                            </label>
                            <g:passwordField name="confirmPassword" required="" 
                                           aria-required="true" 
                                           aria-describedby="confirmPassword-help" 
                                           class="form-control" />
                            <small id="confirmPassword-help" class="form-text text-muted">
                                Must match the new password
                            </small>
                        </div>
                    </fieldset>
                    <fieldset class="buttons">
                        <g:submitButton name="changePassword" class="save btn btn-primary" 
                                      value="Change Password" />
                        <g:link class="btn btn-secondary" action="show" id="${user.id}">Cancel</g:link>
                    </fieldset>
                </g:form>
            </div>
        </div>
    </div>
</body>
</html>