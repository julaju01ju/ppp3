$(document).ready(async function () {

    await fetch("/api/user/check_auth/", {
        method: "GET",
        headers: {
            'Authorization': 'Bearer ' + getCookie('token'),
            "Accept": "application/json", "Content-Type": "application/json"
        },
    }).then(response => {
        if (response.status === 401) {
            clearCookie();
            window.location.href = '/login';
        } else if (response.status === 403 || response.status === 491) {
            clearCookie();
            window.location.href = '/access_denied';
        } else if (response.redirected) {
            window.location.href = response.url;
        }
    });

})

function clearCookie() {
    document.cookie = 'token=;expires=Thu, 01 Jan 1970 00:00:01 GMT;';
}

function getCookie(cookieName) {
    let cookie = {};
    document.cookie.split(';').forEach(function (el) {
        let [key, value] = el.split('=');
        cookie[key.trim()] = value;
    })
    return cookie[cookieName] !== undefined
        ? cookie[cookieName]
        : '';
}


