"use strict";

// Ensure DOM is fully loaded before executing script
document.addEventListener('DOMContentLoaded', function () {
    // Handle the registration form submission
    const registerForm = document.querySelector('form');
    if (registerForm) {
        registerForm.addEventListener('submit', function (event) {
            event.preventDefault();

            // Collect all input fields
            const inputs = registerForm.querySelectorAll('input');
            let isValid = true;
            let formData = {};

            // Loop through each input field to validate
            inputs.forEach(function (input) {
                const value = input.value.trim();
                const name = input.getAttribute('name');

                // Store form data
                formData[name] = value;

                // Basic validation: Check if the field is not empty
                if (!value) {
                    alert(`Please fill out the ${name} field.`);
                    isValid = false;
                    return;
                }

                // Additional validation for email field
                if (name === 'email' && !validateEmail(value)) {
                    alert('Please enter a valid email address.');
                    isValid = false;
                    return;
                }

                // Additional validation for password field
                if (name === 'password' && !validatePassword(value)) {
                    alert('Password must be at least 8 characters long, and include at least one uppercase letter, one lowercase letter, and one number.');
                    isValid = false;
                    return;
                }
            });

            // If all fields are valid, proceed
            if (isValid) {
                alert(`Thank you for registering, ${formData.firstName}!`);
                registerForm.submit(); // Optionally submit the form
            }
        });
    }

});

// Utility function to validate email format
function validateEmail(email) {
    const re = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/;
    return re.test(String(email).toLowerCase());
}

// Utility function to validate password strength
function validatePassword(password) {
    const re = /^(?=.*\d)(?=.*[a-z])(?=.*[A-Z]).{8,}$/;
    return re.test(password);
}