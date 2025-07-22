<div class="form-group">
    <label for="${property}" class="control-label">${label}</label>
    <div class="services-container">
        <g:textArea name="${property}" 
                    value="${value}" 
                    class="form-control" 
                    rows="5"
                    placeholder="Enter services offered, one per line. For example:
- Job placement assistance
- Resume writing workshops
- Interview preparation
- Career counseling"/>
        <small class="form-text text-muted">
            List each service on a new line. Be specific about what is offered.
        </small>
    </div>
</div>