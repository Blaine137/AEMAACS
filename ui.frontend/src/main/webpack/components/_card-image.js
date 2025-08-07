document.addEventListener('DOMContentLoaded', function () {
  // Select the element with the specific data attribute
  const headlineEl = document.querySelector('[data-cmp-is="card-image_headline"]');

  if (headlineEl) {
    // Add a click event listener
    headlineEl.addEventListener('click', function () {
      alert('Card headline was clicked!');
    });
  }
});
