const form  = document.querySelector('#form');
const title = document.querySelector('#titleInput');
const token = document.cookie;
form.addEventListener("submit", async e => {
    e.preventDefault();
    if (title.validity.valueMissing) {
       return;
    }
    const response = await fetch("/api/user/question/", {
        method: "POST",
        headers: {
            'Authorization': 'Bearer ' + token,
            "Accept": "application/json", "Content-Type": "application/json"},
            body: JSON.stringify({
                title: document.getElementById("titleInput").value,
                description: tinymce.get('questionBodyInput').getContent(),
                tags: [document.getElementById("log").textContent = JSON.parse(tag)]
            })
        }
    );
    if (response.ok) {
        window.location.href = '/question/{id}';

    }
});
