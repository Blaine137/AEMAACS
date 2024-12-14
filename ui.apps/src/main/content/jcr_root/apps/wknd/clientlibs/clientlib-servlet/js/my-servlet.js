document.addEventListener("DOMContentLoaded", () => {
    const container = document.getElementById("servlet-results");

    // Fetch data from the resource-based servlet
    fetch("http://localhost:4502/bin/test")
        .then((response) => response.json())
        .then((data) => {
            container.innerHTML = `
                <p>${data.response}</p>
            `;
        })
        .catch((error) => {
            console.error("Error fetching servlet data:", error);
            container.innerHTML = `<p>Error loading content.</p>`;
        });
});