<div class="form-group">
    <label class="control-label">Eligibility Requirements</label>
    <div class="eligibility-container" id="eligibility-container">
        <g:each in="${value ?: []}" var="req" status="i">
            <g:render template="/resource/eligibilityItem" model="[req: req, index: i]"/>
        </g:each>
        
        <g:if test="${!value}">
            <g:render template="/resource/eligibilityItem" model="[req: null, index: 0]"/>
        </g:if>
    </div>
    
    <button type="button" 
            class="btn btn-success btn-sm"
            hx-get="/resource/addEligibilityItem"
            hx-target="#eligibility-container"
            hx-swap="beforeend">
        <i class="fas fa-plus"></i> Add Eligibility Requirement
    </button>
    
    <small class="form-text text-muted">
        Specify any eligibility requirements for accessing this resource.
    </small>
</div>