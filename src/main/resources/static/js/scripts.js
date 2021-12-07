window.addEventListener('load', () => {
    setGlobalStyles();
    const appHeader = document.getElementById("app-header");
    const appSidebar = document.getElementById("app-side-bar");
    const appFooter = document.getElementById("app-footer");
    appHeader.innerHTML = `${Header.createHeader()}`;
    appSidebar.innerHTML = `${SideBar.createSideBar()}`;
    appFooter.innerHTML = `${Footer.createFooter()}`;
    SideBar.activeStyle();
    console.log("hello!");
})

function setGlobalStyles() {
    const body = document.body;
    body.style.margin = "0";
    body.style.padding = "0";
    body.style.boxSizing = "border-box";
}