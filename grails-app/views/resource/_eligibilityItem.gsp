<div class="eligibility-item card mb-2">
    <div class="card-body">
        <div class="row">
            <div class="col-md-3">
                <g:select name="eligibilityType" 
                         from="${['age', 'income', 'disability', 'residency', 'other']}"
                         value="${req?.type}"
                         class="form-control eligibility-type"
                         noSelection="['': 'Select Type']"/>
            </div>
            <div class="col-md-7">
                <g:textField name="eligibilityRequirement" 
                            value="${req?.requirement}" 
                            class="form-control"
                            placeholder="Describe the requirement"/>
            </div>
            <div class="col-md-2">
                <button type="button" 
                        class="btn btn-danger btn-sm"
                        hx-target="closest .eligibility-item"
                        hx-swap="outerHTML swap:0.3s"
                        hx-confirm="Remove this requirement?"
                        onclick="htmx.remove(this.closest('.eligibility-item'))">
                    <i class="fas fa-trash"></i> Remove
                </button>
            </div>
        </div>
    </div>
</div>