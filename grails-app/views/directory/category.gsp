<!DOCTYPE html>
<html>
<head>
    <meta name="layout" content="main"/>
    <title>${category.name} - River City Resources</title>
    <style>
        .resource-card {
            margin-bottom: 20px;
            transition: transform 0.2s;
        }
        .resource-card:hover {
            transform: translateY(-3px);
            box-shadow: 0 4px 8px rgba(0,0,0,0.1);
        }
        .back-link {
            margin-bottom: 2rem;
        }
        .no-resources {
            padding: 3rem;
            text-align: center;
            background-color: #f8f9fa;
            border-radius: 8px;
        }
    </style>
</head>
<body>
    <div class="container">
        <nav aria-label="breadcrumb">
            <ol class="breadcrumb">
                <li class="breadcrumb-item">
                    <g:link controller="directory" action="index">Home</g:link>
                </li>
                <li class="breadcrumb-item active" aria-current="page">${category.name}</li>
            </ol>
        </nav>

        <h1 class="mb-3">${category.name}</h1>
        <p class="lead mb-4">${category.description}</p>

        <g:if test="${resources}">
            <div class="row">
                <g:each in="${resources}" var="resource">
                    <div class="col-lg-6">
                        <div class="card resource-card">
                            <div class="card-body">
                                <h2 class="card-title h4">
                                    <g:link controller="directory" action="resource" id="${resource.id}">
                                        ${resource.name}
                                    </g:link>
                                </h2>
                                <p class="card-text">${resource.description.take(200)}...</p>
                                
                                <g:if test="${resource.contact}">
                                    <div class="contact-info mb-3">
                                        <g:if test="${resource.contact.phone}">
                                            <div><i class="fas fa-phone"></i> ${resource.contact.phone}</div>
                                        </g:if>
                                        <g:if test="${resource.contact.email}">
                                            <div><i class="fas fa-envelope"></i> ${resource.contact.email}</div>
                                        </g:if>
                                        <g:if test="${resource.contact.address}">
                                            <div><i class="fas fa-map-marker-alt"></i> 
                                                ${resource.contact.address}, ${resource.contact.city}, ${resource.contact.state}
                                            </div>
                                        </g:if>
                                    </div>
                                </g:if>
                                
                                <div class="d-flex justify-content-between align-items-center">
                                    <small class="text-muted">
                                        Last updated: <g:formatDate date="${resource.lastUpdated}" format="MMM d, yyyy"/>
                                    </small>
                                    <g:link controller="directory" action="resource" id="${resource.id}" 
                                            class="btn btn-primary">
                                        View Details
                                    </g:link>
                                </div>
                            </div>
                        </div>
                    </div>
                </g:each>
            </div>
        </g:if>
        <g:else>
            <div class="no-resources">
                <h2>No Resources Found</h2>
                <p>There are currently no resources available in this category.</p>
                <g:link controller="directory" action="index" class="btn btn-primary">
                    Browse All Categories
                </g:link>
            </div>
        </g:else>
    </div>
</body>
</html>