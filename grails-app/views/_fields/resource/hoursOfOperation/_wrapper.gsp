<div class="form-group">
    <label for="${property}" class="control-label">${label}</label>
    <div class="hours-container">
        <g:textArea name="${property}" 
                    value="${value}" 
                    class="form-control" 
                    rows="4"
                    placeholder="Example:
Monday - Friday: 9:00 AM - 5:00 PM
Saturday: 10:00 AM - 2:00 PM
Sunday: Closed
Emergency services available 24/7"/>
        <small class="form-text text-muted">
            Specify regular hours and any special availability.
        </small>
    </div>
</div>