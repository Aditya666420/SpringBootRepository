document.addEventListener("DOMContentLoaded", () => {

    document.getElementById("homeBtn")
        .addEventListener("click", () => {

            window.location.href = window.homeUrl;

        });

    document.getElementById("reviewPrBtn")
        .addEventListener("click", async () => {

            const url = document.getElementById("prUrl").value.trim();

            if (!url) {
                alert("Please enter a Pull Request URL.");
                return;
            }

            const token = localStorage.getItem("JWT");

            try {

                document.getElementById("loading").style.display = "block";
				console.log(`${window.reviewPrUrl}?url=${encodeURIComponent(url)}`);
                const response = await fetch(
                    `${window.reviewPrUrl}?url=${encodeURIComponent(url)}`,
                    {
                        headers: {
                            Authorization: "Bearer " + token
                        }
                    });

                if (!response.ok) {
                    throw new Error("Unable to review PR");
                }

                const reviews = await response.json();
				console.log("Review for PR : ",reviews);
                const container =
                    document.getElementById("reviewContainer");

                container.innerHTML = "";

                reviews.forEach(review => {

                    container.innerHTML += `
                        <div class="review-box">

                            <h3>${review.filename}</h3>

                            <pre class="review-content">
										${review.review}
                            </pre>

                        </div>
                    `;
                });

            } catch (e) {

                console.error(e);

                alert("Failed to review Pull Request.");

            } finally {

                document.getElementById("loading").style.display = "none";

            }

        });

});