// Admin form enhancements
//= require jquery-3.5.1.min

$(document).ready(function() {
    
    // Eligibility Requirements are now handled by HTMX
    
    // Form validation enhancements
    function initializeFormValidation() {
        $('form').on('submit', function(e) {
            var isValid = true;
            var errors = [];
            
            // Validate required fields
            $(this).find('[required]').each(function() {
                if (!$(this).val()) {
                    isValid = false;
                    $(this).addClass('is-invalid');
                    errors.push($(this).attr('name') + ' is required');
                } else {
                    $(this).removeClass('is-invalid');
                }
            });
            
            // Validate email format
            $(this).find('input[type="email"]').each(function() {
                var email = $(this).val();
                if (email && !isValidEmail(email)) {
                    isValid = false;
                    $(this).addClass('is-invalid');
                    errors.push('Please enter a valid email address');
                }
            });
            
            // Validate URL format
            $(this).find('input[type="url"]').each(function() {
                var url = $(this).val();
                if (url && !isValidUrl(url)) {
                    isValid = false;
                    $(this).addClass('is-invalid');
                    errors.push('Please enter a valid URL');
                }
            });
            
            if (!isValid) {
                e.preventDefault();
                showErrors(errors);
            }
        });
        
        // Clear validation errors on input
        $('input, select, textarea').on('input change', function() {
            $(this).removeClass('is-invalid');
        });
    }
    
    // Helper functions
    function isValidEmail(email) {
        var re = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
        return re.test(email);
    }
    
    function isValidUrl(url) {
        try {
            new URL(url);
            return true;
        } catch (_) {
            return false;
        }
    }
    
    function showErrors(errors) {
        var errorHtml = '<div class="alert alert-danger alert-dismissible fade show" role="alert">';
        errorHtml += '<strong>Please fix the following errors:</strong><ul>';
        errors.forEach(function(error) {
            errorHtml += '<li>' + error + '</li>';
        });
        errorHtml += '</ul>';
        errorHtml += '<button type="button" class="close" data-dismiss="alert" aria-label="Close">';
        errorHtml += '<span aria-hidden="true">&times;</span></button></div>';
        
        $('.content-header').after(errorHtml);
        
        // Auto-dismiss after 5 seconds
        setTimeout(function() {
            $('.alert').fadeOut();
        }, 5000);
    }
    
    // Rich text editor for descriptions
    function initializeRichTextEditors() {
        $('textarea[name="description"], textarea[name="servicesOffered"]').each(function() {
            $(this).addClass('form-control-lg');
        });
    }
    
    // Tooltips and help text
    function initializeTooltips() {
        $('[data-toggle="tooltip"]').tooltip();
    }
    
    // Auto-save draft functionality
    function initializeAutoSave() {
        var saveTimer;
        var formData = {};
        
        $('input, textarea, select').on('input change', function() {
            clearTimeout(saveTimer);
            saveTimer = setTimeout(function() {
                saveDraft();
            }, 2000); // Save after 2 seconds of inactivity
        });
        
        function saveDraft() {
            $('form').find('input, textarea, select').each(function() {
                formData[$(this).attr('name')] = $(this).val();
            });
            
            localStorage.setItem('resourceFormDraft', JSON.stringify(formData));
            showSaveIndicator();
        }
        
        function showSaveIndicator() {
            var indicator = $('<span class="save-indicator text-success ml-2">Draft saved</span>');
            $('.content-header h1').append(indicator);
            setTimeout(function() {
                indicator.fadeOut(function() {
                    $(this).remove();
                });
            }, 2000);
        }
        
        // Restore draft on page load
        var savedDraft = localStorage.getItem('resourceFormDraft');
        if (savedDraft && window.location.pathname.includes('/create')) {
            var draft = JSON.parse(savedDraft);
            if (confirm('Would you like to restore your previous draft?')) {
                for (var key in draft) {
                    $('[name="' + key + '"]').val(draft[key]);
                }
            }
        }
    }
    
    // Initialize all enhancements
    initializeFormValidation();
    initializeRichTextEditors();
    initializeTooltips();
    initializeAutoSave();
});