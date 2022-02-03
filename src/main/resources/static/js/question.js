const form  = document.querySelector('#form');
const title = document.querySelector('#titleInput');

form.addEventListener("submit", async e => {
    e.preventDefault();
    if(title.validity.valueMissing) {
        return;
    }

    const response = await fetch("/api/user/question", {
        method: "POST",
        headers: {"Accept": "application/json", "Content-Type": "application/json"},
        body: JSON.stringify({
            title: document.getElementById("titleInput").value,
            questionBody: tinymce.get('questionBodyInput').getContent(),
            tag: document.getElementById("tagInput").value
        })
    });
    if (response.ok) {
        window.location.href = '/question/{id}';
    }
});