<!doctype html>
<html>
    <head>
        <meta name="layout" content="main">
        <title>Page Not Found - River City Resources</title>
        <meta name="description" content="The page you're looking for doesn't exist. Browse our disability resources directory or search for specific services."/>
        <meta name="robots" content="noindex, follow"/>
        <g:if env="development"><asset:stylesheet src="errors.css"/></g:if>
    </head>
    <body>
        <div class="container">
            <div class="row justify-content-center">
                <div class="col-md-8 text-center">
                    <h1 class="display-1">404</h1>
                    <h2>Page Not Found</h2>
                    <p class="lead">Sorry, the page you're looking for doesn't exist or has been moved.</p>
                    
                    <g:if env="development">
                        <div class="alert alert-info">
                            <p class="mb-0">Path: ${request.forwardURI}</p>
                        </div>
                    </g:if>
                    
                    <div class="mt-4">
                        <h3>Here are some helpful links:</h3>
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
    </body>
</html>
