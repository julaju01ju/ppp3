document.getElementById("handleFormSubmit").addEventListener("click", async e => {
    e.preventDefault();

    let checkbox = document.getElementById("rememberMe")
    if (checkbox.checked) {
        checkbox.value=true
    } else {
        checkbox.value=false
    }
    const response = await fetch("/api/auth/token/", {
        method: "POST",
        headers: {"Accept": "application/json", "Content-Type": "application/json"},
        body: JSON.stringify({
            username: document.getElementById("inputEmail").value,
            password: document.getElementById("inputPassword").value,
            isRemember: checkbox.value
        })
    });
    if (response.ok) {
        let getToken = await response.json();
        if (document.cookie.split(';').filter((item) => item.trim().startsWith('token=')).length) {
            document.cookie = "token=;max-age=-1"; // отключает кеширование токена
        }
        let token = Object.values(getToken);

        document.cookie = token;
        window.location.href = '/main';
    }
    if (!response.ok) {
        document.getElementById("errorCode").hidden = false;
    }
});
