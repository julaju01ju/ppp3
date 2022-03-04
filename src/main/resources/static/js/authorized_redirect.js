$(document).ready(function () {
    authorizedRedirect();
})

function authorizedRedirect() {
    if (document.cookie) {
        window.location.href = '/main';
    }
}