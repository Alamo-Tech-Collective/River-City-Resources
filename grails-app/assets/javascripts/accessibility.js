/**
 * Accessibility Enhancement JavaScript for River City Resources
 * WCAG 2.1 AA Compliance Features
 */

document.addEventListener('DOMContentLoaded', function() {
    // Initialize accessibility features
    initializeFocusManagement();
    initializeDropdownKeyboardNavigation();
    initializeSearchResultsAnnouncement();
    initializeFormValidation();
    initializeSkipLinks();
    initializeEscapeKeyHandling();
    initializeAccessibilityControls();
});

/**
 * Focus Management
 */
function initializeFocusManagement() {
    // Store the last focused element before page navigation
    const links = document.querySelectorAll('a[href], button[type="submit"]');
    
    links.forEach(link => {
        link.addEventListener('click', function() {
            sessionStorage.setItem('lastFocusedElement', this.getAttribute('href') || this.name);
        });
    });

    // Restore focus after page load if coming from a form submission
    const lastFocused = sessionStorage.getItem('lastFocusedElement');
    if (lastFocused) {
        setTimeout(() => {
            const element = document.querySelector(`[href="${lastFocused}"], [name="${lastFocused}"]`);
            if (element) {
                element.focus();
            }
            sessionStorage.removeItem('lastFocusedElement');
        }, 100);
    }

    // Ensure focus is visible when navigating with keyboard
    document.addEventListener('keydown', function(e) {
        if (e.key === 'Tab') {
            document.body.classList.add('keyboard-navigation');
        }
    });

    document.addEventListener('mousedown', function() {
        document.body.classList.remove('keyboard-navigation');
    });
}

/**
 * Dropdown Keyboard Navigation
 */
function initializeDropdownKeyboardNavigation() {
    const dropdowns = document.querySelectorAll('.dropdown');
    
    dropdowns.forEach(dropdown => {
        const toggle = dropdown.querySelector('.dropdown-toggle');
        const menu = dropdown.querySelector('.dropdown-menu');
        const items = menu ? menu.querySelectorAll('.dropdown-item') : [];
        
        if (!toggle || !menu) return;

        let currentIndex = -1;

        toggle.addEventListener('keydown', function(e) {
            switch(e.key) {
                case 'ArrowDown':
                case 'ArrowUp':
                    e.preventDefault();
                    if (!menu.classList.contains('show')) {
                        // Open dropdown
                        toggle.click();
                    }
                    // Focus first item
                    currentIndex = 0;
                    items[currentIndex].focus();
                    break;
                case 'Escape':
                    e.preventDefault();
                    if (menu.classList.contains('show')) {
                        toggle.click();
                        toggle.focus();
                    }
                    break;
            }
        });

        items.forEach((item, index) => {
            item.addEventListener('keydown', function(e) {
                switch(e.key) {
                    case 'ArrowDown':
                        e.preventDefault();
                        currentIndex = (index + 1) % items.length;
                        items[currentIndex].focus();
                        break;
                    case 'ArrowUp':
                        e.preventDefault();
                        currentIndex = (index - 1 + items.length) % items.length;
                        items[currentIndex].focus();
                        break;
                    case 'Home':
                        e.preventDefault();
                        currentIndex = 0;
                        items[currentIndex].focus();
                        break;
                    case 'End':
                        e.preventDefault();
                        currentIndex = items.length - 1;
                        items[currentIndex].focus();
                        break;
                    case 'Escape':
                        e.preventDefault();
                        toggle.click();
                        toggle.focus();
                        break;
                    case 'Tab':
                        // Close dropdown when tabbing out
                        if (menu.classList.contains('show')) {
                            toggle.click();
                        }
                        break;
                }
            });
        });
    });
}

/**
 * Search Results Announcement
 */
function initializeSearchResultsAnnouncement() {
    const searchForm = document.querySelector('[role="search"]');
    const resultsHeading = document.getElementById('results-heading');
    const searchStatus = document.getElementById('search-status');
    
    if (!searchForm || !searchStatus) return;

    searchForm.addEventListener('submit', function() {
        searchStatus.textContent = 'Searching resources, please wait...';
        
        // Show loading state
        const submitBtn = searchForm.querySelector('button[type="submit"]');
        if (submitBtn) {
            submitBtn.disabled = true;
            submitBtn.innerHTML = '<i class="fas fa-spinner fa-spin" aria-hidden="true"></i> Searching...';
        }
    });

    // Announce results when page loads with search results
    if (resultsHeading) {
        setTimeout(() => {
            const resultText = resultsHeading.textContent;
            if (searchStatus) {
                searchStatus.textContent = resultText;
            }
        }, 500);
    }
}

/**
 * Form Validation Enhancement
 */
function initializeFormValidation() {
    const forms = document.querySelectorAll('form');
    
    forms.forEach(form => {
        const inputs = form.querySelectorAll('input, select, textarea');
        
        inputs.forEach(input => {
            // Add real-time validation feedback
            input.addEventListener('blur', function() {
                validateField(input);
            });
            
            // Clear error state when user starts typing
            input.addEventListener('input', function() {
                clearFieldError(input);
            });
        });

        // Enhanced form submission
        form.addEventListener('submit', function(e) {
            let hasErrors = false;
            
            inputs.forEach(input => {
                if (!validateField(input)) {
                    hasErrors = true;
                }
            });
            
            if (hasErrors) {
                e.preventDefault();
                
                // Focus first error field
                const firstError = form.querySelector('.form-control.error');
                if (firstError) {
                    firstError.focus();
                }
                
                // Announce error
                announceFormErrors(form);
            }
        });
    });
}

function validateField(field) {
    const isRequired = field.hasAttribute('required');
    const value = field.value.trim();
    
    if (isRequired && !value) {
        showFieldError(field, 'This field is required.');
        return false;
    }
    
    // Email validation
    if (field.type === 'email' && value) {
        const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
        if (!emailRegex.test(value)) {
            showFieldError(field, 'Please enter a valid email address.');
            return false;
        }
    }
    
    // URL validation
    if (field.type === 'url' && value) {
        try {
            new URL(value);
        } catch {
            showFieldError(field, 'Please enter a valid URL.');
            return false;
        }
    }
    
    clearFieldError(field);
    return true;
}

function showFieldError(field, message) {
    field.classList.add('error');
    field.setAttribute('aria-invalid', 'true');
    
    let errorElement = field.parentNode.querySelector('.field-error');
    if (!errorElement) {
        errorElement = document.createElement('div');
        errorElement.className = 'field-error';
        errorElement.setAttribute('role', 'alert');
        field.parentNode.appendChild(errorElement);
    }
    
    errorElement.textContent = message;
    
    // Associate error with field
    const errorId = field.id + '-error';
    errorElement.id = errorId;
    
    const describedBy = field.getAttribute('aria-describedby') || '';
    if (!describedBy.includes(errorId)) {
        field.setAttribute('aria-describedby', (describedBy + ' ' + errorId).trim());
    }
}

function clearFieldError(field) {
    field.classList.remove('error');
    field.removeAttribute('aria-invalid');
    
    const errorElement = field.parentNode.querySelector('.field-error');
    if (errorElement) {
        errorElement.remove();
    }
    
    // Remove error ID from aria-describedby
    const describedBy = field.getAttribute('aria-describedby');
    if (describedBy) {
        const errorId = field.id + '-error';
        const updatedDescribedBy = describedBy.replace(errorId, '').trim();
        if (updatedDescribedBy) {
            field.setAttribute('aria-describedby', updatedDescribedBy);
        } else {
            field.removeAttribute('aria-describedby');
        }
    }
}

function announceFormErrors(form) {
    const errors = form.querySelectorAll('.field-error');
    if (errors.length === 0) return;
    
    const announcement = `Form has ${errors.length} error${errors.length > 1 ? 's' : ''}. Please review and correct.`;
    
    // Create or update live region for form errors
    let liveRegion = document.getElementById('form-errors-live');
    if (!liveRegion) {
        liveRegion = document.createElement('div');
        liveRegion.id = 'form-errors-live';
        liveRegion.className = 'live-region';
        liveRegion.setAttribute('aria-live', 'assertive');
        document.body.appendChild(liveRegion);
    }
    
    liveRegion.textContent = announcement;
}

/**
 * Skip Links Enhancement
 */
function initializeSkipLinks() {
    const skipLinks = document.querySelectorAll('.skip-nav, .skip');
    
    skipLinks.forEach(link => {
        link.addEventListener('click', function(e) {
            e.preventDefault();
            
            const targetId = this.getAttribute('href').substring(1);
            const target = document.getElementById(targetId);
            
            if (target) {
                target.focus();
                target.scrollIntoView({
                    behavior: 'smooth',
                    block: 'start'
                });
            }
        });
    });
}

/**
 * Escape Key Handling
 */
function initializeEscapeKeyHandling() {
    document.addEventListener('keydown', function(e) {
        if (e.key === 'Escape') {
            // Close any open dropdowns
            const openDropdowns = document.querySelectorAll('.dropdown-menu.show');
            openDropdowns.forEach(dropdown => {
                const toggle = dropdown.parentNode.querySelector('.dropdown-toggle');
                if (toggle) {
                    toggle.click();
                    toggle.focus();
                }
            });
            
            // Close any open modals (if implemented)
            const openModals = document.querySelectorAll('.modal.show');
            openModals.forEach(modal => {
                const closeBtn = modal.querySelector('.btn-close, .close');
                if (closeBtn) {
                    closeBtn.click();
                }
            });
        }
    });
}

/**
 * Utility Functions
 */

// Trap focus within an element (useful for modals)
function trapFocus(element) {
    const focusableElements = element.querySelectorAll(
        'a[href], button, textarea, input[type="text"], input[type="radio"], input[type="checkbox"], select'
    );
    
    const firstFocusableElement = focusableElements[0];
    const lastFocusableElement = focusableElements[focusableElements.length - 1];
    
    element.addEventListener('keydown', function(e) {
        if (e.key === 'Tab') {
            if (e.shiftKey) {
                if (document.activeElement === firstFocusableElement) {
                    lastFocusableElement.focus();
                    e.preventDefault();
                }
            } else {
                if (document.activeElement === lastFocusableElement) {
                    firstFocusableElement.focus();
                    e.preventDefault();
                }
            }
        }
    });
}

// Announce content changes to screen readers
function announceToScreenReader(message, priority = 'polite') {
    const announcement = document.createElement('div');
    announcement.setAttribute('aria-live', priority);
    announcement.setAttribute('aria-atomic', 'true');
    announcement.className = 'sr-only';
    announcement.textContent = message;
    
    document.body.appendChild(announcement);
    
    setTimeout(() => {
        document.body.removeChild(announcement);
    }, 1000);
}

/**
 * Initialize Accessibility Controls
 */
function initializeAccessibilityControls() {
    // Load saved preferences
    loadAccessibilityPreferences();
}

/**
 * Adjust Font Size
 */
function adjustFontSize(change) {
    const body = document.body;
    const currentSize = parseInt(window.getComputedStyle(body).fontSize);
    const newSize = Math.max(12, Math.min(24, currentSize + change)); // Limit between 12px and 24px
    body.style.fontSize = newSize + 'px';
    localStorage.setItem('fontSize', newSize + 'px');
    
    // Announce the change
    announceToScreenReader(`Font size ${change > 0 ? 'increased' : 'decreased'} to ${newSize} pixels`);
}

/**
 * Toggle High Contrast
 */
function toggleHighContrast() {
    const body = document.body;
    const isHighContrast = body.classList.contains('high-contrast');
    
    if (isHighContrast) {
        body.classList.remove('high-contrast');
        localStorage.setItem('highContrast', 'false');
        announceToScreenReader('High contrast mode disabled');
    } else {
        body.classList.add('high-contrast');
        localStorage.setItem('highContrast', 'true');
        announceToScreenReader('High contrast mode enabled');
    }
}

/**
 * Load Accessibility Preferences
 */
function loadAccessibilityPreferences() {
    // Load saved font size
    const savedFontSize = localStorage.getItem('fontSize');
    if (savedFontSize) {
        document.body.style.fontSize = savedFontSize;
    }
    
    // Load saved high contrast setting
    const highContrast = localStorage.getItem('highContrast');
    if (highContrast === 'true') {
        document.body.classList.add('high-contrast');
    }
}

// Export functions for use in other scripts
window.AccessibilityUtils = {
    trapFocus,
    announceToScreenReader,
    validateField,
    showFieldError,
    clearFieldError,
    adjustFontSize,
    toggleHighContrast
};

// Make functions globally available for inline event handlers
window.adjustFontSize = adjustFontSize;
window.toggleHighContrast = toggleHighContrast;