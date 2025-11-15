# Implementation Plan: Provider Empty Resources Message

## Overview
When a provider signs in and has no approved resources, they currently see an empty table. We need to display an instructive message instead.

## Current State Analysis

### Controller (`ResourceController.groovy`)
- The `index` action (lines 16-29) already:
  - Detects if user is a provider
  - Calls `resourceService.listForProvider(currentUser, params)` for providers
  - Passes `resourceCount: resourceService.countForProvider(currentUser)` to the model
  - **No changes needed in controller** ✅

### View (`resource/index.gsp`)
- Line 30-35: Provider dashboard info section
- Line 37-127: Resource table (currently shows empty table if no resources)
- **Changes needed**: Add conditional message between provider dashboard and table

### Service (`ResourceService.groovy`)
- `listForProvider()` returns only approved resources for provider
- `countForProvider()` counts approved resources
- **No changes needed in service** ✅

## Implementation Steps

### Step 1: Modify the View (`grails-app/views/resource/index.gsp`)

**Location**: After the provider dashboard section (line 35), before the table (line 37)

**What to add**:
- A conditional check: if user is a provider AND `resourceCount == 0` (or `resourceList` is empty)
- Display an informative message in an accessible format
- Use Bootstrap alert styling consistent with the existing provider dashboard

**Message text**:
"You do not have any approved resources. Please submit a new resource or come back later after an admin has had a chance to review your submission(s)."

**Accessibility requirements**:
- Use semantic HTML
- Include proper ARIA attributes (`role="status"`, `aria-live="polite"`)
- Ensure keyboard navigation works
- Maintain color contrast (WCAG 2.1 AA)

### Step 2: Implementation Details

**Code structure**:
```gsp
<!-- After line 35 (Provider Context Information section) -->
<g:if test="${springSecurityService?.currentUser?.authorities?.any { it.authority == 'ROLE_PROVIDER' } && (resourceCount == 0 || !resourceList)}">
    <div class="alert alert-info" role="status" aria-live="polite">
        <p class="mb-0">
            <i class="fas fa-info-circle" aria-hidden="true"></i>
            You do not have any approved resources. Please submit a new resource or come back later after an admin has had a chance to review your submission(s).
        </p>
    </div>
</g:if>
```

**Placement**: Insert between lines 35 and 37 (after provider dashboard, before table)

### Step 3: Testing Checklist

- [ ] Test as provider with 0 approved resources → should see message
- [ ] Test as provider with 1+ approved resources → should NOT see message, should see table
- [ ] Test as admin → should NOT see message (only providers see it)
- [ ] Verify accessibility:
  - [ ] Screen reader announces the message
  - [ ] Keyboard navigation works
  - [ ] Color contrast meets WCAG 2.1 AA
  - [ ] Message is properly associated with context
- [ ] Verify styling matches existing provider dashboard alert
- [ ] Test edge cases:
  - [ ] Provider who just signed up (no resources at all)
  - [ ] Provider with only pending resources
  - [ ] Provider with only rejected resources

## Files to Modify

1. **`grails-app/views/resource/index.gsp`**
   - Add conditional message block after line 35
   - No other changes needed

## Design Considerations

1. **Consistency**: Use the same `alert alert-info` styling as the provider dashboard section above it
2. **Accessibility**: Follow WCAG 2.1 AA standards (already established in the codebase)
3. **User Experience**: Message should be clear, actionable, and encouraging
4. **Placement**: Message appears after the header/context but before the empty table

## Alternative Approaches Considered

1. **Hide the table entirely when empty**: Rejected - table structure should remain for consistency
2. **Show message in table body**: Rejected - less prominent, harder to notice
3. **Use flash message**: Rejected - flash messages are for temporary notifications, not persistent state

## Success Criteria

✅ Provider with no approved resources sees the instructive message  
✅ Message appears in the correct location (after dashboard, before table)  
✅ Message is accessible (screen reader friendly)  
✅ Message styling is consistent with existing UI  
✅ No impact on providers with approved resources  
✅ No impact on admin users  

