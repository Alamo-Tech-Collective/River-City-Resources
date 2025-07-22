<!DOCTYPE html>
<html>
<head>
    <meta name="layout" content="main"/>
    <title>${resource.name} - River City Resources</title>
    <style>
        .resource-header {
            background-color: #f8f9fa;
            padding: 2rem 0;
            margin-bottom: 2rem;
        }
        .contact-card {
            background-color: #f8f9fa;
            padding: 1.5rem;
            border-radius: 8px;
            margin-bottom: 1.5rem;
        }
        .services-list {
            list-style-type: none;
            padding-left: 0;
        }
        .services-list li {
            padding: 0.5rem 0;
            border-bottom: 1px solid #dee2e6;
        }
        .services-list li:last-child {
            border-bottom: none;
        }
        .services-list li:before {
            content: "✓";
            color: #28a745;
            font-weight: bold;
            margin-right: 0.5rem;
        }
        .eligibility-item {
            background-color: #e9ecef;
            padding: 0.5rem 1rem;
            border-radius: 4px;
            margin-bottom: 0.5rem;
        }
        .print-button {
            margin-right: 1rem;
        }
        @media print {
            .btn-group, .breadcrumb, nav {
                display: none !important;
            }
        }
    </style>
</head>
<body>
    <div class="resource-header">
        <div class="container">
            <nav aria-label="breadcrumb">
                <ol class="breadcrumb bg-transparent p-0">
                    <li class="breadcrumb-item">
                        <g:link controller="directory" action="index">Home</g:link>
                    </li>
                    <li class="breadcrumb-item">
                        <g:link controller="directory" action="category" id="${resource.category.id}">
                            ${resource.category.name}
                        </g:link>
                    </li>
                    <li class="breadcrumb-item active" aria-current="page">${resource.name}</li>
                </ol>
            </nav>
            
            <h1>${resource.name}</h1>
            <p class="text-muted mb-0">Category: ${resource.category.name}</p>
        </div>
    </div>

    <div class="container">
        <div class="row">
            <div class="col-lg-8">
                <section aria-label="Resource Description">
                    <h2>About This Resource</h2>
                    <p class="lead">${resource.description}</p>
                </section>

                <g:if test="${resource.servicesOffered}">
                    <section aria-label="Services Offered" class="mt-4">
                        <h2>Services Offered</h2>
                        <ul class="services-list">
                            <g:each in="${resource.servicesOffered.split('\n')}" var="service">
                                <g:if test="${service.trim()}">
                                    <li>${service.trim()}</li>
                                </g:if>
                            </g:each>
                        </ul>
                    </section>
                </g:if>

                <g:if test="${resource.eligibilityRequirements}">
                    <section aria-label="Eligibility Requirements" class="mt-4">
                        <h2>Eligibility Requirements</h2>
                        <g:each in="${resource.eligibilityRequirements}" var="requirement">
                            <div class="eligibility-item">
                                <strong>${requirement.type.capitalize()}:</strong> ${requirement.requirement}
                            </div>
                        </g:each>
                    </section>
                </g:if>
            </div>

            <div class="col-lg-4">
                <g:if test="${resource.contact}">
                    <div class="contact-card">
                        <h3>Contact Information</h3>
                        
                        <g:if test="${resource.contact.phone}">
                            <div class="mb-2">
                                <i class="fas fa-phone"></i> 
                                <a href="tel:${resource.contact.phone}">${resource.contact.phone}</a>
                            </div>
                        </g:if>
                        
                        <g:if test="${resource.contact.email}">
                            <div class="mb-2">
                                <i class="fas fa-envelope"></i> 
                                <a href="mailto:${resource.contact.email}">${resource.contact.email}</a>
                            </div>
                        </g:if>
                        
                        <g:if test="${resource.contact.website}">
                            <div class="mb-2">
                                <i class="fas fa-globe"></i> 
                                <a href="${resource.contact.website}" target="_blank" rel="noopener noreferrer">
                                    Visit Website
                                </a>
                            </div>
                        </g:if>
                        
                        <g:if test="${resource.contact.address}">
                            <div class="mb-2">
                                <i class="fas fa-map-marker-alt"></i> 
                                ${resource.contact.address}<br>
                                ${resource.contact.city}, ${resource.contact.state} ${resource.contact.zipCode}
                            </div>
                        </g:if>
                    </div>
                </g:if>

                <g:if test="${resource.hoursOfOperation}">
                    <div class="contact-card">
                        <h3>Hours of Operation</h3>
                        <p>${resource.hoursOfOperation}</p>
                    </div>
                </g:if>

                <div class="btn-group" role="group" aria-label="Resource actions">
                    <button onclick="window.print()" class="btn btn-outline-secondary print-button">
                        <i class="fas fa-print"></i> Print
                    </button>
                    <button onclick="saveToPDF()" class="btn btn-outline-secondary">
                        <i class="fas fa-file-pdf"></i> Save as PDF
                    </button>
                </div>

                <div class="mt-3">
                    <small class="text-muted">
                        Last updated: <g:formatDate date="${resource.lastUpdated}" format="MMMM d, yyyy"/>
                    </small>
                </div>
            </div>
        </div>

        <div class="mt-4">
            <g:link controller="directory" action="category" id="${resource.category.id}" 
                    class="btn btn-secondary">
                <i class="fas fa-arrow-left"></i> Back to ${resource.category.name}
            </g:link>
        </div>
    </div>

    <script>
        function saveToPDF() {
            // For now, just trigger print dialog
            // In production, you might want to use a library like jsPDF
            window.print();
        }
    </script>
</body>
</html>