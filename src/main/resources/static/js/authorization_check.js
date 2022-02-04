$(document).ready(function() {
    checkAuthorization();
})

function checkAuthorization() {
    const response = fetch("http://localhost:8091/api/user/check_auth/", {
        method: "GET",
        headers: { "Accept": "application/json", "Content-Type": "application/json" },
    });

    if (response.status === 401) {
        window.location.href = '/login';
    } else if (response.status === 403) {
        window.location.href = '/access_denied';
    }
}

