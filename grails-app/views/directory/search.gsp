<!DOCTYPE html>
<html>
<head>
    <meta name="layout" content="main"/>
    <title>Search Results - River City Resources</title>
    <asset:stylesheet src="search.css"/>
</head>
<body>
    <div class="container">
        <div id="search-status" class="live-region" aria-live="polite"></div>
        <nav aria-label="breadcrumb">
            <ol class="breadcrumb">
                <li class="breadcrumb-item">
                    <g:link controller="directory" action="index">Home</g:link>
                </li>
                <li class="breadcrumb-item active" aria-current="page">Search Results</li>
            </ol>
        </nav>

        <h1 class="mb-4">Search Resources</h1>

        <div class="search-form">
            <h2 class="sr-only">Search and Filter Resources</h2>
            <g:form controller="directory" action="search" method="GET" class="form" role="search" aria-label="Resource search form">
                <div class="filter-section">
                    <label for="searchQuery" class="form-label">Search Keywords</label>
                    <input type="text" name="q" id="searchQuery" class="form-control form-control-lg" 
                           placeholder="Enter keywords to search resources..." value="${query}" 
                           aria-describedby="search-help" autocomplete="off">
                    <div id="search-help" class="form-text">Enter keywords to search resource names, descriptions, and services.</div>
                </div>
                
                <div class="filter-section">
                    <label for="sortOrder" class="form-label">Sort By</label>
                    <select name="sort" id="sortOrder" class="form-control" aria-describedby="sort-help">
                        <option value="name" ${params.sort == 'name' ? 'selected' : ''}>Name (A-Z)</option>
                        <option value="lastUpdated" ${params.sort == 'lastUpdated' ? 'selected' : ''}>Recently Updated</option>
                        <option value="viewCount" ${params.sort == 'viewCount' ? 'selected' : ''}>Most Viewed</option>
                        <option value="category.name" ${params.sort == 'category.name' ? 'selected' : ''}>Category</option>
                    </select>
                    <div id="sort-help" class="form-text">Choose how to sort search results.</div>
                </div>
                
                <div class="filter-section">
                    <label for="categoryFilter" class="form-label">Category</label>
                    <select name="categoryId" id="categoryFilter" class="form-control" aria-describedby="categoryHelp">
                        <option value="">All Categories</option>
                        <g:each in="${categories}" var="category">
                            <option value="${category.id}" 
                                    ${selectedCategoryId == category.id ? 'selected' : ''}>
                                ${category.name}
                            </option>
                        </g:each>
                    </select>
                    <div id="category-help" class="form-text">Filter by resource category type.</div>
                </div>
                
                <div class="filter-section">
                    <fieldset>
                        <legend class="form-label">Eligibility Requirements</legend>
                        <div class="eligibility-checkboxes" role="group" aria-labelledby="eligibilityLabel">
                            <div class="form-check">
                                <input class="form-check-input" type="checkbox" name="eligibilityTypes" 
                                       value="age" id="eligibilityAge" 
                                       ${selectedEligibilityTypes?.contains('age') ? 'checked' : ''}>
                                <label class="form-check-label" for="eligibilityAge">Age Requirements</label>
                            </div>
                            <div class="form-check">
                                <input class="form-check-input" type="checkbox" name="eligibilityTypes" 
                                       value="disability" id="eligibilityDisability" 
                                       ${selectedEligibilityTypes?.contains('disability') ? 'checked' : ''}>
                                <label class="form-check-label" for="eligibilityDisability">Disability Requirements</label>
                            </div>
                            <div class="form-check">
                                <input class="form-check-input" type="checkbox" name="eligibilityTypes" 
                                       value="income" id="eligibilityIncome" 
                                       ${selectedEligibilityTypes?.contains('income') ? 'checked' : ''}>
                                <label class="form-check-label" for="eligibilityIncome">Income Requirements</label>
                            </div>
                            <div class="form-check">
                                <input class="form-check-input" type="checkbox" name="eligibilityTypes" 
                                       value="other" id="eligibilityOther" 
                                       ${selectedEligibilityTypes?.contains('other') ? 'checked' : ''}>
                                <label class="form-check-label" for="eligibilityOther">Other Requirements</label>
                            </div>
                        </div>
                        <div class="form-text">Select applicable eligibility types</div>
                        
                        <!-- Hidden field for backward compatibility -->
                        <input type="hidden" name="eligibilityType" value="${selectedEligibilityType}">
                    </fieldset>
                </div>
                
                <div class="row mt-3">
                    <div class="col-md-12">
                        <button type="submit" class="btn btn-primary btn-lg" aria-describedby="search-button-help">
                            <i class="fas fa-search" aria-hidden="true"></i> Search
                        </button>
                        <g:link controller="directory" action="search" class="btn btn-secondary btn-lg" 
                                aria-describedby="clear-button-help">
                            Clear Filters
                        </g:link>
                        <div id="search-button-help" class="sr-only">Search for resources with current filters</div>
                        <div id="clear-button-help" class="sr-only">Clear all search filters and start over</div>
                    </div>
                </div>
            </g:form>
        </div>

        <g:if test="${resources}">
            <h2 class="mb-3" id="results-heading" aria-live="polite">Search Results (${resources.size()} found)</h2>
            
            <div class="row" role="region" aria-labelledby="results-heading">
                <g:each in="${resources}" var="resource" status="i">
                    <div class="col-lg-6">
                        <article class="card resource-card" aria-labelledby="resource-title-${i}">
                            <div class="card-body">
                                <h3 class="card-title h5" id="resource-title-${i}">
                                    <g:link controller="directory" action="resource" id="${resource.id}" 
                                            aria-describedby="resource-desc-${i}">
                                        ${resource.name}
                                    </g:link>
                                </h3>
                                <p class="card-text" id="resource-desc-${i}">${resource.description.take(200)}...</p>
                                
                                <div class="d-flex justify-content-between align-items-center">
                                    <small class="text-muted">
                                        <span class="sr-only">Category:</span> ${resource.category.name}
                                    </small>
                                    <g:link controller="directory" action="resource" id="${resource.id}" 
                                            class="btn btn-sm btn-primary" 
                                            aria-label="View details for ${resource.name}">
                                        View Details
                                    </g:link>
                                </div>
                            </div>
                        </article>
                    </div>
                </g:each>
            </div>
        </g:if>
        <g:elseif test="${query || selectedCategoryId || selectedEligibilityType}">
            <div class="no-results" role="region" aria-labelledby="no-results-heading">
                <h2 id="no-results-heading">No Results Found</h2>
                <p>No resources match your search criteria. Try adjusting your filters or search terms.</p>
                <g:link controller="directory" action="index" class="btn btn-primary" 
                        aria-label="Browse all available resources">
                    Browse All Resources
                </g:link>
            </div>
        </g:elseif>
        <g:else>
            <div class="no-results" role="region" aria-labelledby="start-search-heading">
                <h2 id="start-search-heading">Start Your Search</h2>
                <p>Use the search form above to find disability resources in San Antonio.</p>
            </div>
        </g:else>
    </div>
</body>
</html>