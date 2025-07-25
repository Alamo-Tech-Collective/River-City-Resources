<!DOCTYPE html>
<html>
<head>
    <meta name="layout" content="main"/>
    <title><g:message code="app.name"/> - <g:message code="app.tagline"/></title>
    <style>
        .category-card {
            margin-bottom: 20px;
            cursor: pointer;
            transition: transform 0.2s;
            min-height: 150px;
        }
        .category-card:hover {
            transform: translateY(-5px);
            box-shadow: 0 4px 8px rgba(0,0,0,0.1);
        }
        .category-card .card-body {
            padding: 2rem;
        }
        .hero-section {
            background-color: #f8f9fa;
            padding: 3rem 0;
            margin-bottom: 2rem;
        }
        .search-box {
            max-width: 600px;
            margin: 0 auto;
        }
        .featured-resources {
            margin-top: 3rem;
        }
        .resource-card {
            margin-bottom: 1rem;
        }
    </style>
</head>
<body>
    <div class="hero-section">
        <div class="container">
            <h1 class="text-center mb-4"><g:message code="app.name"/></h1>
            <p class="text-center lead mb-4"><g:message code="app.description"/></p>
            
            <div class="search-box">
                <g:form controller="directory" action="search" method="GET" class="form" role="search" aria-label="${message(code: 'search.title')}">
                    <div class="input-group input-group-lg">
                        <input type="text" name="q" class="form-control" placeholder="${message(code: 'search.placeholder')}" 
                               aria-label="${message(code: 'search.title')}" value="${params.q}">
                        <div class="input-group-append">
                            <button class="btn btn-primary" type="submit" aria-label="${message(code: 'button.search')}">
                                <i class="fas fa-search" aria-hidden="true"></i> <g:message code="button.search"/>
                            </button>
                        </div>
                    </div>
                </g:form>
            </div>
        </div>
    </div>

    <div class="container">
        <section aria-label="${message(code: 'category.all')}">
            <h2 class="mb-4"><g:message code="nav.search"/> <g:message code="label.category"/></h2>
            <div class="row">
                <g:each in="${categories}" var="category">
                    <div class="col-md-4">
                        <g:link controller="directory" action="category" id="${category.id}" 
                                class="text-decoration-none" aria-describedby="category-desc-${category.id}">
                            <div class="card category-card">
                                <div class="card-body">
                                    <h3 class="card-title h4">${category.name}</h3>
                                    <p class="card-text" id="category-desc-${category.id}">${category.description}</p>
                                    <span class="text-primary" aria-hidden="true">Browse Resources →</span>
                                </div>
                            </div>
                        </g:link>
                    </div>
                </g:each>
            </div>
        </section>

        <g:if test="${featuredResources}">
            <section class="featured-resources" aria-label="Featured Resources">
                <h2 class="mb-4">Featured Resources</h2>
                <div class="row">
                    <g:each in="${featuredResources}" var="resource">
                        <div class="col-md-6">
                            <article class="card resource-card" aria-labelledby="featured-resource-${resource.id}">
                                <div class="card-body">
                                    <h3 class="card-title h5" id="featured-resource-${resource.id}">
                                        <g:link controller="directory" action="resource" id="${resource.id}" 
                                                aria-describedby="featured-desc-${resource.id}">
                                            ${resource.name}
                                        </g:link>
                                    </h3>
                                    <p class="card-text" id="featured-desc-${resource.id}">${resource.description.take(150)}...</p>
                                    <div class="d-flex justify-content-between align-items-center">
                                        <small class="text-muted">
                                            <span class="sr-only">Category:</span> ${resource.category.name}
                                        </small>
                                        <g:link controller="directory" action="resource" id="${resource.id}" 
                                                class="btn btn-sm btn-outline-primary"
                                                aria-label="View details for ${resource.name}">
                                            View Details
                                        </g:link>
                                    </div>
                                </div>
                            </article>
                        </div>
                    </g:each>
                </div>
            </section>
        </g:if>
    </div>
</body>
</html>