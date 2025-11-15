<!DOCTYPE html>
<html>
<head>
    <meta name="layout" content="main"/>
    <title>Login - River City Resources</title>
    <meta name="description" content="Login to manage disability resources in the River City Resources directory. Admin access only."/>
    <meta name="robots" content="noindex, nofollow"/>
    <style>
        .login-container {
            max-width: 400px;
            margin: 50px auto;
            padding: 20px;
            background-color: #f8f9fa;
            border-radius: 8px;
            box-shadow: 0 0 10px rgba(0,0,0,0.1);
        }
        .login-form {
            margin-top: 20px;
        }
        .form-group {
            margin-bottom: 15px;
        }
        .form-control {
            width: 100%;
            padding: 10px;
            border: 1px solid #ddd;
            border-radius: 4px;
            font-size: 16px;
        }
        .btn-login {
            width: 100%;
            padding: 10px;
            background-color: #007bff;
            color: white;
            border: none;
            border-radius: 4px;
            font-size: 16px;
            cursor: pointer;
        }
        .btn-login:hover {
            background-color: #0056b3;
        }
        .error-message {
            color: #dc3545;
            margin-bottom: 15px;
            padding: 10px;
            background-color: #f8d7da;
            border: 1px solid #f5c6cb;
            border-radius: 4px;
        }
        .info-message {
            color: #004085;
            margin-bottom: 15px;
            padding: 10px;
            background-color: #cce5ff;
            border: 1px solid #b8daff;
            border-radius: 4px;
        }
        .checkbox-group {
            display: flex;
            align-items: center;
            gap: 8px;
        }
        .checkbox-group input[type="checkbox"] {
            width: auto;
        }
    </style>
</head>
<body>
    <div class="login-container">
        <h1>Login</h1>
        
        <g:if test='${flash.message}'>
            <div class="info-message" role="status" aria-live="polite">${flash.message}</div>
        </g:if>
        
        <g:if test='${params.login_error == "1"}'>
            <div class="error-message" role="alert" aria-live="assertive">Invalid username or password. Please try again.</div>
        </g:if>
        
        <form action="${postUrl ?: '/login/authenticate'}" method="POST" class="login-form" 
              role="form" aria-label="Login form" novalidate>
            <fieldset>
                <legend class="sr-only">Login Credentials</legend>
                
                <div class="form-group">
                    <label for="username" class="required">Username:</label>
                    <input type="text" class="form-control" name="${usernameParameter ?: 'username'}" 
                           id="username" required autofocus autocomplete="username"
                           aria-describedby="username-help"
                           aria-invalid="${params.login_error == '1' ? 'true' : 'false'}"/>
                    <div id="username-help" class="form-text">Enter your admin username</div>
                </div>
                
                <div class="form-group">
                    <label for="password" class="required">Password:</label>
                    <input type="password" class="form-control" name="${passwordParameter ?: 'password'}" 
                           id="password" required autocomplete="current-password"
                           aria-describedby="password-help"
                           aria-invalid="${params.login_error == '1' ? 'true' : 'false'}"/>
                    <div id="password-help" class="form-text">Enter your admin password</div>
                </div>
                
                <div class="form-group checkbox-group">
                    <input type="checkbox" name="${rememberMeParameter ?: 'remember-me'}" 
                           id="remember_me" aria-describedby="remember-help"/>
                    <label for="remember_me">Remember me</label>
                    <div id="remember-help" class="sr-only">Keep me logged in for future visits</div>
                </div>
                
                <button type="submit" class="btn-login" aria-describedby="login-help">
                    Login
                </button>
                <div id="login-help" class="sr-only">Submit the form to log in to the admin area</div>
            </fieldset>
        </form>
        
        <nav aria-label="Login page navigation" style="margin-top: 20px; text-align: center;">
            <a href="${createLink(controller: 'directory', action: 'index')}" 
               aria-label="Return to main directory">Back to Directory</a>
        </nav>
    </div>
</body>
</html>