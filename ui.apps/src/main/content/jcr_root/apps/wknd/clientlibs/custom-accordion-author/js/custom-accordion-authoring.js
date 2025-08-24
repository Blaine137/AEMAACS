(function(document, $) {
    "use strict";

    var ENABLE_CUSTOM_HEADLINE_SELECTOR = "[data-cmp-accordion-v1-dialog-edit-hook='enableCustomHeadline']";
    var CUSTOM_HEADLINE_FIELD_SELECTOR = "[data-cmp-accordion-v1-dialog-field='customHeadline']";

    /**
     * Toggle the custom headline field visibility based on the dropdown selection
     */
    function toggleCustomHeadlineField($enableDropdown, $customHeadlineField) {
        var enableValue = $enableDropdown.val();
        var $fieldContainer = $customHeadlineField.closest('.coral-Form-fieldwrapper');

        if (enableValue === 'yes') {
            $fieldContainer.show();
            $customHeadlineField.attr('aria-required', 'true');
        } else {
            $fieldContainer.hide();
            $customHeadlineField.attr('aria-required', 'false');
            $customHeadlineField.val(''); // Clear the field when hidden
        }
    }

    /**
     * Initialize the dialog behavior
     */
    function initializeDialog($dialog) {
        var $enableDropdown = $dialog.find(ENABLE_CUSTOM_HEADLINE_SELECTOR);
        var $customHeadlineField = $dialog.find(CUSTOM_HEADLINE_FIELD_SELECTOR);

        if ($enableDropdown.length && $customHeadlineField.length) {
            // Set initial state
            toggleCustomHeadlineField($enableDropdown, $customHeadlineField);

            // Listen for changes to the dropdown
            $enableDropdown.on('change', function() {
                toggleCustomHeadlineField($enableDropdown, $customHeadlineField);
            });

            // Handle Coral UI select change event
            $enableDropdown.on('coral-select:change', function() {
                toggleCustomHeadlineField($enableDropdown, $customHeadlineField);
            });
        }
    }

    /**
     * Document ready handler
     */
    $(document).on('dialog-ready', function(e) {
        var $dialog = $(e.target);

        // Check if this is our accordion dialog
        if ($dialog.find(ENABLE_CUSTOM_HEADLINE_SELECTOR).length) {
            initializeDialog($dialog);
        }
    });

    /**
     * Handle foundation-contentloaded event for dynamic content
     */
    $(document).on('foundation-contentloaded', function(e) {
        var $container = $(e.target);
        var $dialog = $container.closest('.cq-dialog');

        if ($dialog.length && $container.find(ENABLE_CUSTOM_HEADLINE_SELECTOR).length) {
            // Small delay to ensure Coral UI components are fully initialized
            setTimeout(function() {
                initializeDialog($dialog);
            }, 100);
        }
    });

})(document, Granite.$);