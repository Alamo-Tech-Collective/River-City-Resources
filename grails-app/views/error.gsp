<!doctype html>
<html>
    <head>
        <meta name="layout" content="main">
        <title>Error - River City Resources</title>
        <meta name="description" content="An error occurred. Please try again or contact support."/>
        <meta name="robots" content="noindex, nofollow"/>
        <g:if env="development"><asset:stylesheet src="errors.css"/></g:if>
    </head>
    <body>
        <g:if env="development">
            <g:if test="${Throwable.isInstance(exception)}">
                <g:renderException exception="${exception}" />
            </g:if>
            <g:elseif test="${request.getAttribute('javax.servlet.error.exception')}">
                <g:renderException exception="${request.getAttribute('javax.servlet.error.exception')}" />
            </g:elseif>
            <g:else>
                <ul class="errors">
                    <li>An error has occurred</li>
                    <li>Exception: ${exception}</li>
                    <li>Message: ${message}</li>
                    <li>Path: ${path}</li>
                </ul>
            </g:else>
        </g:if>
        <g:else>
            <div class="container">
                <div class="row justify-content-center">
                    <div class="col-md-8 text-center">
                        <h1 class="display-1">500</h1>
                        <h2>Oops! Something went wrong</h2>
                        <p class="lead">We're sorry, but an error occurred while processing your request.</p>
                        
                        <div class="mt-4">
                            <h3>What can you do?</h3>
                            <ul class="list-unstyled">
                                <li class="mb-2">
                                    <a href="${createLink(uri: '/')}" class="btn btn-primary">Go to Homepage</a>
                                </li>
                                <li class="mb-2">
                                    <a href="${createLink(controller: 'directory', action: 'search')}" class="btn btn-outline-primary">Search Resources</a>
                                </li>
                            </ul>
                        </div>
                    </div>
                </div>
            </div>
        </g:else>
    </body>
</html>
