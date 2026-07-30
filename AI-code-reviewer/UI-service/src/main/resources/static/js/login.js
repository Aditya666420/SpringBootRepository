document.addEventListener("DOMContentLoaded", () => {

    const form = document.getElementById("loginForm");

    form.addEventListener("submit", async function (event) {

        event.preventDefault();

        const username = document.getElementById("username").value;
        const password = document.getElementById("password").value;

        const response = await fetch(window.loginUrl, {

            method: "POST",

            headers: {
                "Content-Type": "application/json"
            },

            body: JSON.stringify({
                username: username,
                password: password
            })

        });

        if (response.ok) {

            const data = await response.json();
			console.log(data);
			console.log(data.token);
            // Save the JWT
            localStorage.setItem("JWT", data.token);

            // Redirect to the home page
			window.location.href = window.homeUrl;

        } else {

            alert("Invalid username or password");

        }

    });

});