if (document.getElementById("handleFormSubmit")) {
    document.getElementById("handleFormSubmit").addEventListener("click", async e => {
        e.preventDefault();


        const response = await fetch("/api/auth/token/", {
            method: "POST",
            headers: {"Accept": "application/json", "Content-Type": "application/json"},
            body: JSON.stringify({
                username: document.getElementById("inputEmail").value,
                password: document.getElementById("inputPassword").value
            })
        });
        if (response.ok) {
            let getToken = await response.json();
            let token = Object.values(getToken);

            setCookie('token', token[0], {secure: true, 'max-age': 3600});
            window.location.href = '/main';
        }
        if (!response.ok) {
            document.getElementById("errorCode").hidden = false;
        }
    });
}
function setCookie(name, value, options = {}) {
    options = {
        path: '/',
        ...options
    };
    if (options.expires instanceof Date) {
        options.expires = options.expires.toUTCString();
    }
    let updatedCookie = encodeURIComponent(name) + "=" + encodeURIComponent(value);

    for (let optionKey in options) {
        updatedCookie += "; " + optionKey;
        let optionValue = options[optionKey];
        if (optionValue !== true) {
            updatedCookie += "=" + optionValue;
        }
    }
    document.cookie = updatedCookie;
}
function logout() {
    setCookie("token", "", {
        'max-age': -1
    })
}
window.addEventListener('load', () => {
    setGlobalStyles();
    const appHeader = document.getElementById("app-header");
    const appSidebar = document.getElementById("app-side-bar");
    const appFooter = document.getElementById("app-footer");
    const headerProfile = document.getElementById("profile-header");
    if (appHeader != null) {
        appHeader.innerHTML = `${Header.createHeader()}`;

        document.querySelector("#logout").addEventListener("click", logout);
    }
    if (headerProfile != null) {
        headerProfile.innerHTML = `${Header.createHeaderProfile()}`;

    }
    appSidebar.innerHTML = `${SideBar.createSideBar()}`;
    appFooter.innerHTML = `${Footer.createFooter()}`;

    SideBar.activeStyle();
    console.log("Hello from KataAacademy!");
})
function setGlobalStyles() {
    const body = document.body;
    body.style.margin = "0";
    body.style.padding = "0";
    body.style.boxSizing = "border-box";
}


