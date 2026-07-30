document.addEventListener("DOMContentLoaded", function () {

	function loadMonaco() {

	    return new Promise((resolve, reject) => {

	        const script = document.createElement("script");

	        script.src =
	            "https://cdnjs.cloudflare.com/ajax/libs/monaco-editor/0.52.2/min/vs/loader.min.js";

	        script.onload = function () {

	            require.config({
	                paths: {
	                    vs: "https://cdnjs.cloudflare.com/ajax/libs/monaco-editor/0.52.2/min/vs"
	                }
	            });

	            require(["vs/editor/editor.main"], function () {
	                resolve();
	            });

	        };

	        script.onerror = reject;

	        document.head.appendChild(script);

	    });

	}
	console.log("Previous code :"+previousCode)
	loadMonaco()
	    .then(async() => {

        editor = monaco.editor.create(
            document.getElementById('editor'),
            {
                value: previousCode || '',
                language: 'java',
                automaticLayout: true,
                minimap: {
                    enabled: false
                }
            }
        );
		monaco.editor.setTheme("vs-dark");
		const id = new URLSearchParams(window.location.search).get("id");
		console.log("Id="+ id);
		   if (id) {

		       const token = localStorage.getItem("JWT");

		       try {
					console.log("API call : "+ window.reviewByIdUrl + "?id=" + id)
		           const response = await fetch(window.reviewByIdUrl + "?id=" + id, {
		               headers: {
		                   "Authorization": "Bearer " + token
		               }
		           });

		           if (!response.ok) {
		               throw new Error("Unable to load review");
		           }

		           const review = await response.json();

		           editor.setValue(review.code);

		           document.querySelector(".review-content").textContent =
		               review.reviewResult;

		           document.querySelector(".review-box").style.display = "block";

		       } catch (e) {
		           console.error(e);
		       }
		   }

        document.getElementById("language").addEventListener("change", function () {
            monaco.editor.setModelLanguage(
                editor.getModel(),
                this.value
            );
        });

        document.getElementById("reviewForm").addEventListener("submit", async function (event) {

            event.preventDefault();

            const token = localStorage.getItem("JWT");

            // Copy Monaco editor content to hidden input
            document.getElementById("codeInput").value = editor.getValue();

            const code = document.getElementById("codeInput").value;
            const language = document.getElementById("language").value;

            console.log("Code:", code);
            console.log("Language:", language);
			

            const request = {
                code: code,
                language: language
            };

            try {

                const response = await fetch(window.reviewUrl, {
                    method: "POST",
                    headers: {
                        "Content-Type": "application/json",
                        "Authorization": "Bearer " + token
                    },
                    body: JSON.stringify(request)
                });

                if (!response.ok) {
                    throw new Error("Request Failed");
                }

                const result = await response.json();
				console.log("Output : "+result.review);
                document.querySelector(".review-content").innerText =
                    result.review;
				document.querySelector(".review-box").style.display = "block";

            } catch (error) {

                console.error(error);

                document.getElementById("errorMessage").innerText = error.message;
                document.getElementById("errorBox").style.display = "block";

                alert("Unable to review code");
            }

        });

    })
	.catch(err => {
	       console.error("Failed to load Monaco:", err);
	   });
	   
	 

});