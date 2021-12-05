class App{
    static appBuildLayout(){
        const app = document.getElementById("app");
        app.innerHTML = `${Header.createHeader()}${MainContent.createMainContent()}${Footer.createFooter()}`;
    }
}