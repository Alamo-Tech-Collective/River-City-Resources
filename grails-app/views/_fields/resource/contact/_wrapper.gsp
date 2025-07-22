<div class="contact-section card">
    <div class="card-header">
        <h5 class="mb-0">Contact Information</h5>
    </div>
    <div class="card-body">
        <div class="row">
            <div class="col-md-6">
                <div class="form-group">
                    <label for="contact.phone">Phone</label>
                    <g:textField name="contact.phone" 
                                value="${value?.phone}" 
                                class="form-control"
                                placeholder="(210) 555-0123"/>
                </div>
            </div>
            <div class="col-md-6">
                <div class="form-group">
                    <label for="contact.email">Email</label>
                    <g:field type="email" 
                            name="contact.email" 
                            value="${value?.email}" 
                            class="form-control"
                            placeholder="contact@organization.org"/>
                </div>
            </div>
        </div>
        
        <div class="form-group">
            <label for="contact.website">Website</label>
            <g:field type="url" 
                    name="contact.website" 
                    value="${value?.website}" 
                    class="form-control"
                    placeholder="https://www.organization.org"/>
        </div>
        
        <div class="form-group">
            <label for="contact.address">Address</label>
            <g:textArea name="contact.address" 
                       value="${value?.address}" 
                       class="form-control" 
                       rows="3"
                       placeholder="123 Main Street&#10;San Antonio, TX 78201"/>
        </div>
    </div>
</div>