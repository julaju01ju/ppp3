
document.getElementById("handleFormSubmit").addEventListener("click", async e => {
    e.preventDefault();

    const response = await fetch("http://localhost:8091/api/auth/token/", {
        method: "POST",
        headers: { "Accept": "application/json", "Content-Type": "application/json" },
        body: JSON.stringify({
            username: document.getElementById("inputEmail").value,
            password: document.getElementById("inputPassword").value
        })
    });
    if (response.ok === true) {
        const token = await response.json();
        document.cookie = "token=;max-age=-1";
        document.cookie = `token = ${token}`;
        window.location.href = '/main';
    }
    document.getElementById("errorCode").hidden=false;
});
