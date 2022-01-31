
document.getElementById("handleFormSubmit").addEventListener("click", async e => {
    e.preventDefault();

    const response = await fetch("/api/auth/token/" , {
        method: "POST",
        headers: { "Accept": "application/json", "Content-Type": "application/json" },
        body: JSON.stringify({
            username: document.getElementById("inputEmail").value,
            password: document.getElementById("inputPassword").value
        })
    });
    if (response.ok) {
        const token = await response.json();
        if (document.cookie.split(';').filter((item) => item.trim().startsWith('token=')).length) {
            document.cookie = "token=;max-age=-1";
        }
        document.cookie = `token = ${token}`;

        window.location.href = '/main';
    }
    if(!response.ok) {
        document.getElementById("errorCode").hidden=false;
    }
});
