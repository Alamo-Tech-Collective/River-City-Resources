<!DOCTYPE html>
<html>
<head>
    <meta name="layout" content="main"/>
    <title>Admin Dashboard - River City Resources</title>
    <style>
        .dashboard-container {
            padding: 20px;
        }
        .stats-grid {
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
            gap: 20px;
            margin-bottom: 30px;
        }
        .stat-card {
            background-color: #f8f9fa;
            padding: 20px;
            border-radius: 8px;
            box-shadow: 0 2px 4px rgba(0,0,0,0.1);
            text-align: center;
        }
        .stat-number {
            font-size: 36px;
            font-weight: bold;
            color: #007bff;
        }
        .stat-label {
            color: #6c757d;
            margin-top: 10px;
        }
        .admin-actions {
            margin-top: 30px;
        }
        .action-button {
            display: inline-block;
            padding: 10px 20px;
            margin: 5px;
            background-color: #007bff;
            color: white;
            text-decoration: none;
            border-radius: 4px;
        }
        .action-button:hover {
            background-color: #0056b3;
            color: white;
            text-decoration: none;
        }
        .featured-resources {
            margin-top: 30px;
        }
        .resource-list {
            list-style: none;
            padding: 0;
        }
        .resource-item {
            padding: 10px;
            border-bottom: 1px solid #dee2e6;
        }
        .logout-link {
            float: right;
            color: #dc3545;
        }
        .dashboard-header {
            display: flex;
            justify-content: space-between;
            align-items: center;
            margin-bottom: 20px;
        }
    </style>
</head>
<body>
    <div class="dashboard-container">
        <header class="dashboard-header">
            <h1>Admin Dashboard</h1>
            <g:link controller="logout" class="logout-link btn btn-outline-danger" 
                    aria-label="Logout from admin dashboard">
                <i class="fas fa-sign-out-alt" aria-hidden="true"></i> Logout
            </g:link>
        </header>
        
        <section aria-label="Dashboard Statistics">
            <h2 class="sr-only">System Statistics</h2>
            <div class="stats-grid">
                <div class="stat-card" role="region" aria-labelledby="resource-count-label">
                    <div class="stat-number" aria-label="${resourceCount} total resources">${resourceCount}</div>
                    <div class="stat-label" id="resource-count-label">Total Resources</div>
                </div>
                <div class="stat-card" role="region" aria-labelledby="category-count-label">
                    <div class="stat-number" aria-label="${categoryCount} categories">${categoryCount}</div>
                    <div class="stat-label" id="category-count-label">Categories</div>
                </div>
                <div class="stat-card" role="region" aria-labelledby="user-count-label">
                    <div class="stat-number" aria-label="${userCount} admin users">${userCount}</div>
                    <div class="stat-label" id="user-count-label">Admin Users</div>
                </div>
            </div>
        </section>
        
        <section class="admin-actions" aria-label="Quick Actions">
            <h2>Quick Actions</h2>
            <div class="row">
                <div class="col-md-6 col-lg-3 mb-3">
                    <div class="card h-100">
                        <div class="card-body text-center">
                            <i class="fas fa-list fa-3x text-primary mb-3"></i>
                            <h5 class="card-title">Resources</h5>
                            <p class="card-text">View and manage all disability resources</p>
                            <g:link controller="resource" action="index" class="btn btn-primary btn-block">
                                Manage Resources
                            </g:link>
                        </div>
                    </div>
                </div>
                <div class="col-md-6 col-lg-3 mb-3">
                    <div class="card h-100">
                        <div class="card-body text-center">
                            <i class="fas fa-plus fa-3x text-success mb-3"></i>
                            <h5 class="card-title">Add Resource</h5>
                            <p class="card-text">Create a new resource entry</p>
                            <g:link controller="resource" action="create" class="btn btn-success btn-block">
                                Add New Resource
                            </g:link>
                        </div>
                    </div>
                </div>
                <div class="col-md-6 col-lg-3 mb-3">
                    <div class="card h-100">
                        <div class="card-body text-center">
                            <i class="fas fa-tags fa-3x text-info mb-3"></i>
                            <h5 class="card-title">Categories</h5>
                            <p class="card-text">Organize resources by category</p>
                            <g:link controller="category" action="index" class="btn btn-info btn-block">
                                Manage Categories
                            </g:link>
                        </div>
                    </div>
                </div>
                <div class="col-md-6 col-lg-3 mb-3">
                    <div class="card h-100">
                        <div class="card-body text-center">
                            <i class="fas fa-users fa-3x text-danger mb-3"></i>
                            <h5 class="card-title">Users</h5>
                            <p class="card-text">Manage admin users and passwords</p>
                            <g:link controller="user" action="index" class="btn btn-danger btn-block">
                                Manage Users
                            </g:link>
                        </div>
                    </div>
                </div>
                <div class="col-md-6 col-lg-3 mb-3">
                    <div class="card h-100">
                        <div class="card-body text-center">
                            <i class="fas fa-key fa-3x text-secondary mb-3"></i>
                            <h5 class="card-title">My Password</h5>
                            <p class="card-text">Change your own password</p>
                            <g:link controller="user" action="changeOwnPassword" class="btn btn-secondary btn-block">
                                Change Password
                            </g:link>
                        </div>
                    </div>
                </div>
                <div class="col-md-6 col-lg-3 mb-3">
                    <div class="card h-100">
                        <div class="card-body text-center">
                            <i class="fas fa-chart-line fa-3x text-warning mb-3"></i>
                            <h5 class="card-title">Reports</h5>
                            <p class="card-text">View usage statistics and reports</p>
                            <button class="btn btn-warning btn-block" disabled>
                                Coming Soon
                            </button>
                        </div>
                    </div>
                </div>
            </div>
        </section>
        
        <g:if test="${featuredResources}">
            <section class="featured-resources" aria-label="Featured Resources">
                <h2>Featured Resources</h2>
                <ul class="resource-list" role="list" aria-label="List of featured resources">
                    <g:each in="${featuredResources}" var="resource" status="i">
                        <li class="resource-item" role="listitem">
                            <g:link controller="resource" action="show" id="${resource.id}" 
                                    aria-label="View details for ${resource.name} in ${resource.category.name} category">
                                ${resource.name}
                            </g:link>
                            <span class="text-muted"> - ${resource.category.name}</span>
                        </li>
                    </g:each>
                </ul>
            </section>
        </g:if>
    </div>
<asset:javascript src="admin-forms.js"/>
</body>
</html>