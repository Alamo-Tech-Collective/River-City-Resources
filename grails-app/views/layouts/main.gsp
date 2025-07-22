<!doctype html>
<html lang="en" class="no-js">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <meta http-equiv="X-UA-Compatible" content="IE=edge"/>
    <title>
        <g:layoutTitle default="Grails"/>
    </title>
    <meta name="viewport" content="width=device-width, initial-scale=1"/>
    <asset:link rel="icon" href="favicon.ico" type="image/x-ico"/>

    <asset:stylesheet src="application.css"/>
    <asset:stylesheet src="accessibility.css"/>
    <asset:stylesheet src="admin-forms.css"/>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css">

    <g:layoutHead/>
</head>

<body>

<a href="#main-content" class="skip-nav">Skip to main content</a>

<nav class="navbar navbar-expand-lg navbar-dark navbar-static-top" role="navigation" aria-label="Main navigation">
    <div class="container-fluid">
        <g:link controller="directory" action="index" class="navbar-brand">
            River City Resources
        </g:link>
        <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarContent" aria-controls="navbarContent" aria-expanded="false" aria-label="Toggle navigation menu">
            <span class="navbar-toggler-icon"></span>
        </button>

        <div class="collapse navbar-collapse" aria-expanded="false" style="height: 0.8px;" id="navbarContent">
            <ul class="nav navbar-nav ml-auto">
                <li class="nav-item">
                    <g:link controller="directory" action="index" class="nav-link" aria-current="${controllerName == 'directory' && actionName == 'index' ? 'page' : ''}">Home</g:link>
                </li>
                <li class="nav-item">
                    <g:link controller="directory" action="search" class="nav-link" aria-current="${controllerName == 'directory' && actionName == 'search' ? 'page' : ''}">Search</g:link>
                </li>
                <sec:ifLoggedIn>
                    <li class="nav-item dropdown">
                        <a class="nav-link dropdown-toggle" href="#" id="adminDropdown" role="button" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false" aria-label="Admin menu">
                            Admin
                        </a>
                        <div class="dropdown-menu" aria-labelledby="adminDropdown" role="menu">
                            <g:link controller="admin" action="index" class="dropdown-item" role="menuitem">Dashboard</g:link>
                            <g:link controller="resource" action="index" class="dropdown-item" role="menuitem">Manage Resources</g:link>
                            <g:link controller="category" action="index" class="dropdown-item" role="menuitem">Manage Categories</g:link>
                            <div class="dropdown-divider" role="separator"></div>
                            <g:link controller="logout" class="dropdown-item" role="menuitem">Logout</g:link>
                        </div>
                    </li>
                </sec:ifLoggedIn>
                <sec:ifNotLoggedIn>
                    <li class="nav-item">
                        <g:link controller="login" action="auth" class="nav-link" aria-current="${controllerName == 'login' ? 'page' : ''}">Admin Login</g:link>
                    </li>
                </sec:ifNotLoggedIn>
                <g:pageProperty name="page.nav"/>
            </ul>
        </div>
    </div>
</nav>

<main id="main-content" role="main">
    <g:layoutBody/>
</main>

<footer class="footer mt-5" role="contentinfo">
    <div class="container">
        <div class="row">
            <div class="col-md-4">
                <h3>River City Resources</h3>
                <p>Connecting San Antonio residents with disability resources and services.</p>
            </div>
            <div class="col-md-4">
                <h3>Quick Links</h3>
                <nav aria-label="Footer navigation">
                    <ul class="list-unstyled">
                        <li><g:link controller="directory" action="index">Home</g:link></li>
                        <li><g:link controller="directory" action="search">Search Resources</g:link></li>
                        <li><a href="#">About Us</a></li>
                        <li><a href="#">Contact</a></li>
                    </ul>
                </nav>
            </div>
            <div class="col-md-4">
                <h3>Accessibility</h3>
                <p>This website is designed to be accessible to all users. If you experience any difficulties, please <a href="#" aria-label="Contact us for accessibility support">contact us</a>.</p>
            </div>
        </div>
        <hr>
        <div class="text-center">
            <p>&copy; <g:formatDate date="${new Date()}" format="yyyy"/> River City Resources. All rights reserved.</p>
            <p class="mt-2">Made with ❤️ by <a href="https://alamotechcollective.com" target="_blank" rel="noopener noreferrer" aria-label="Visit Alamo Tech Collective website">Alamo Tech Collective</a></p>
        </div>
    </div>
</footer>

<!-- Accessibility Controls -->
<div class="btn-group-vertical btn-accessibility" role="group" aria-label="Accessibility controls">
    <button type="button" class="btn btn-secondary" onclick="adjustFontSize(1)" 
            aria-label="Increase font size" title="Increase font size">
        A+
    </button>
    <button type="button" class="btn btn-secondary" onclick="adjustFontSize(-1)" 
            aria-label="Decrease font size" title="Decrease font size">
        A-
    </button>
    <button type="button" class="btn btn-secondary" onclick="toggleHighContrast()" 
            aria-label="Toggle high contrast" title="Toggle high contrast">
        <i class="fas fa-adjust" aria-hidden="true"></i>
    </button>
</div>

<div id="spinner" class="spinner" style="display:none;" role="status" aria-live="polite" aria-label="Loading content">
    <span class="sr-only"><g:message code="spinner.alt" default="Loading content, please wait..."/></span>
    <g:message code="spinner.alt" default="Loading&hellip;"/>
</div>

<asset:javascript src="application.js"/>
<asset:javascript src="htmx.min.js"/>
<asset:javascript src="accessibility.js"/>

</body>
</html>
