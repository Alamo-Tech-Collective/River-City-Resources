<!doctype html>
<html lang="${session?.lang ?: 'en'}" class="no-js">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <meta http-equiv="X-UA-Compatible" content="IE=edge"/>
    <title>
        <g:layoutTitle default="${message(code: 'app.name')}"/>
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

<a href="#main-content" class="skip-nav"><g:message code="accessibility.skipToContent"/></a>

<nav class="navbar navbar-expand-lg navbar-dark navbar-static-top" role="navigation" aria-label="Main navigation">
    <div class="container-fluid">
        <g:link controller="directory" action="index" class="navbar-brand">
            <g:message code="app.name"/>
        </g:link>
        <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarContent" aria-controls="navbarContent" aria-expanded="false" aria-label="Toggle navigation menu">
            <span class="navbar-toggler-icon"></span>
        </button>

        <div class="collapse navbar-collapse" aria-expanded="false" style="height: 0.8px;" id="navbarContent">
            <ul class="nav navbar-nav ml-auto">
                <li class="nav-item">
                    <g:link controller="directory" action="index" class="nav-link" aria-current="${controllerName == 'directory' && actionName == 'index' ? 'page' : ''}">
                        <g:message code="nav.home"/>
                    </g:link>
                </li>
                <li class="nav-item">
                    <g:link controller="directory" action="search" class="nav-link" aria-current="${controllerName == 'directory' && actionName == 'search' ? 'page' : ''}">
                        <g:message code="nav.search"/>
                    </g:link>
                </li>
                <!-- Language Switcher -->
                <li class="nav-item dropdown">
                    <a class="nav-link dropdown-toggle" href="#" id="languageDropdown" role="button" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false" aria-label="${message(code: 'accessibility.languageSwitcher')}">
                        <i class="fas fa-globe" aria-hidden="true"></i>
                        <g:message code="nav.language"/>
                    </a>
                    <div class="dropdown-menu dropdown-menu-right" aria-labelledby="languageDropdown" role="menu">
                        <g:link controller="language" action="switchLanguage" 
                                params="[lang: 'en']" class="dropdown-item" role="menuitem">
                            <g:message code="language.english"/>
                            <g:if test="${!session?.lang || session?.lang == 'en'}">
                                <i class="fas fa-check ml-2" aria-hidden="true"></i>
                            </g:if>
                        </g:link>
                        <g:link controller="language" action="switchLanguage" 
                                params="[lang: 'es']" class="dropdown-item" role="menuitem">
                            <g:message code="language.spanish"/>
                            <g:if test="${session?.lang == 'es'}">
                                <i class="fas fa-check ml-2" aria-hidden="true"></i>
                            </g:if>
                        </g:link>
                    </div>
                </li>
                
                <sec:ifLoggedIn>
                    <li class="nav-item dropdown">
                        <a class="nav-link dropdown-toggle" href="#" id="adminDropdown" role="button" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false" aria-label="Admin menu">
                            <g:message code="nav.admin"/>
                        </a>
                        <div class="dropdown-menu dropdown-menu-right" aria-labelledby="adminDropdown" role="menu">
                            <g:link controller="admin" action="index" class="dropdown-item" role="menuitem">
                                <g:message code="admin.dashboard"/>
                            </g:link>
                            <g:link controller="resource" action="index" class="dropdown-item" role="menuitem">
                                <g:message code="admin.manage.resources"/>
                            </g:link>
                            <g:link controller="category" action="index" class="dropdown-item" role="menuitem">
                                <g:message code="admin.manage.categories"/>
                            </g:link>
                            <div class="dropdown-divider" role="separator"></div>
                            <g:link controller="logout" class="dropdown-item" role="menuitem">
                                <g:message code="nav.logout"/>
                            </g:link>
                        </div>
                    </li>
                </sec:ifLoggedIn>
                <sec:ifNotLoggedIn>
                    <li class="nav-item">
                        <g:link controller="login" action="auth" class="nav-link" aria-current="${controllerName == 'login' ? 'page' : ''}">
                            <g:message code="nav.admin.login"/>
                        </g:link>
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
                <h3><g:message code="app.name"/></h3>
                <p><g:message code="footer.tagline"/></p>
            </div>
            <div class="col-md-4">
                <h3><g:message code="footer.quickLinks"/></h3>
                <nav aria-label="Footer navigation">
                    <ul class="list-unstyled">
                        <li><g:link controller="directory" action="index"><g:message code="nav.home"/></g:link></li>
                        <li><g:link controller="directory" action="search"><g:message code="nav.search"/> <g:message code="nav.resources"/></g:link></li>
                        <li><a href="#"><g:message code="nav.about"/></a></li>
                        <li><a href="#"><g:message code="nav.contact"/></a></li>
                    </ul>
                </nav>
            </div>
            <div class="col-md-4">
                <h3><g:message code="footer.accessibility"/></h3>
                <p>This website is designed to be accessible to all users. If you experience any difficulties, please <a href="#" aria-label="Contact us for accessibility support">contact us</a>.</p>
            </div>
        </div>
        <hr>
        <div class="text-center">
            <p>&copy; <g:formatDate date="${new Date()}" format="yyyy"/> <g:message code="app.name"/>. <g:message code="footer.copyright"/></p>
            <p class="mt-2"><g:message code="footer.madeWith"/> <a href="https://alamotechcollective.com" target="_blank" rel="noopener noreferrer" aria-label="Visit Alamo Tech Collective website">Alamo Tech Collective</a></p>
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
