<!DOCTYPE html>
<html>
<head>
    <meta name="layout" content="main" />
    <title>Change My Password</title>
</head>
<body>
    <div class="nav" role="navigation">
        <ul>
            <li><a class="home" href="${createLink(uri: '/admin')}"><g:message code="default.home.label"/></a></li>
        </ul>
    </div>
    <div id="change-own-password" class="content" role="main">
        <h1>Change My Password</h1>
        <g:if test="${flash.message}">
            <div class="message" role="status">${flash.message}</div>
        </g:if>
        <g:if test="${flash.error}">
            <div class="errors" role="alert">${flash.error}</div>
        </g:if>
        
        <div class="card">
            <div class="card-body">
                <p class="text-muted">For security reasons, you must enter your current password to change it.</p>
                
                <g:form action="changeOwnPassword" method="POST">
                    <fieldset class="form">
                        <div class="fieldcontain required">
                            <label for="currentPassword">
                                Current Password
                                <span class="required-indicator">*</span>
                            </label>
                            <g:passwordField name="currentPassword" required="" 
                                           aria-required="true" 
                                           class="form-control" />
                        </div>
                        
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
                                Confirm New Password
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
                        <g:link class="btn btn-secondary" controller="admin" action="index">Cancel</g:link>
                    </fieldset>
                </g:form>
            </div>
        </div>
    </div>
</body>
</html>