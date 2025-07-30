<%@ page import="riverCityResources.User" %>

<div class="fieldcontain ${hasErrors(bean: user, field: 'username', 'error')} required">
    <label for="username">
        <g:message code="user.username.label" default="Username" />
        <span class="required-indicator">*</span>
    </label>
    <g:textField name="username" required="" value="${user?.username}" 
                 aria-required="true" aria-describedby="username-help" />
    <small id="username-help" class="form-text text-muted">Must be unique</small>
</div>

<div class="fieldcontain ${hasErrors(bean: user, field: 'email', 'error')} required">
    <label for="email">
        <g:message code="user.email.label" default="Email" />
        <span class="required-indicator">*</span>
    </label>
    <g:field type="email" name="email" required="" value="${user?.email}"
             aria-required="true" aria-describedby="email-help" />
    <small id="email-help" class="form-text text-muted">Must be a valid email address</small>
</div>

<g:if test="${isNew}">
    <div class="fieldcontain ${hasErrors(bean: user, field: 'password', 'error')} required">
        <label for="password">
            <g:message code="user.password.label" default="Password" />
            <span class="required-indicator">*</span>
        </label>
        <g:passwordField name="password" required="" aria-required="true" 
                         aria-describedby="password-help" />
        <small id="password-help" class="form-text text-muted">
            Must be at least 8 characters long with uppercase, lowercase, number, and special character
        </small>
    </div>
</g:if>

<div class="fieldcontain ${hasErrors(bean: user, field: 'firstName', 'error')} required">
    <label for="firstName">
        <g:message code="user.firstName.label" default="First Name" />
        <span class="required-indicator">*</span>
    </label>
    <g:textField name="firstName" required="" value="${user?.firstName}" aria-required="true" />
</div>

<div class="fieldcontain ${hasErrors(bean: user, field: 'lastName', 'error')} required">
    <label for="lastName">
        <g:message code="user.lastName.label" default="Last Name" />
        <span class="required-indicator">*</span>
    </label>
    <g:textField name="lastName" required="" value="${user?.lastName}" aria-required="true" />
</div>

<div class="fieldcontain">
    <label for="roles">
        <g:message code="user.roles.label" default="Roles" />
    </label>
    <g:select name="roles" from="${allRoles}" 
              value="${userRoles*.authority}" 
              optionKey="authority" 
              optionValue="${{it.authority.replace('ROLE_', '')}}"
              multiple="true" 
              class="form-control"
              aria-describedby="roles-help" />
    <small id="roles-help" class="form-text text-muted">Hold Ctrl/Cmd to select multiple roles</small>
</div>

<g:if test="${!isNew}">
    <div class="fieldcontain ${hasErrors(bean: user, field: 'enabled', 'error')}">
        <label for="enabled">
            <g:message code="user.enabled.label" default="Enabled" />
        </label>
        <g:checkBox name="enabled" value="${user?.enabled}" />
    </div>

    <div class="fieldcontain ${hasErrors(bean: user, field: 'accountExpired', 'error')}">
        <label for="accountExpired">
            <g:message code="user.accountExpired.label" default="Account Expired" />
        </label>
        <g:checkBox name="accountExpired" value="${user?.accountExpired}" />
    </div>

    <div class="fieldcontain ${hasErrors(bean: user, field: 'accountLocked', 'error')}">
        <label for="accountLocked">
            <g:message code="user.accountLocked.label" default="Account Locked" />
        </label>
        <g:checkBox name="accountLocked" value="${user?.accountLocked}" />
    </div>

    <div class="fieldcontain ${hasErrors(bean: user, field: 'passwordExpired', 'error')}">
        <label for="passwordExpired">
            <g:message code="user.passwordExpired.label" default="Password Expired" />
        </label>
        <g:checkBox name="passwordExpired" value="${user?.passwordExpired}" />
    </div>
</g:if>