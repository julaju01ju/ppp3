$(document).ready(function () {
    checkAuthorization();
})

function checkAuthorization() {
    const response = fetch("http://localhost:8091/api/user/check_auth/", {
        method: "GET",
        headers: {"Accept": "application/json", "Content-Type": "application/json"},
    });

    if (response.status === 401) {
        clearToken();
        window.location.href = '/login';
    } else if (response.status === 403 || response.status === 491) {
        clearToken();
        window.location.href = '/access_denied';
    }
}

function clearToken() {
    document.cookie = 'token=;expires=Thu, 01 Jan 1970 00:00:01 GMT;';
}


