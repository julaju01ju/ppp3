window.addEventListener('load', () => {
    setGlobalStyles();
    App.appBuildLayout();
    SideBar.activeStyle();
    console.log("hello!");
})

function setGlobalStyles() {
    const body = document.body;
    body.style.margin = "0";
    body.style.padding = "0";
    body.style.boxSizing = "border-box";
}