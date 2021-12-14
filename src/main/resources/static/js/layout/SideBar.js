class SideBar{
    static sidebar = `<div>
            <ul class="nav flex-column" style="width: 100%">
                <li class="nav-item" style="border-bottom: 1px solid orange">
                    <a class="nav-link sidebar-menu-item active" aria-current="page" href="#" style="color: #333333">Home</a>
                </li>
                <li class="nav-item">
                    <p style="font-size: 1.2rem; margin-left: 1rem; font-weight: 100;">
                        <img src="../../images/header-images/icon/icon_svg/world.svg" alt="world"
                             style="height: 1.2rem">
                        Public</p>
                </li>
                <li>
                    <ul class="nav flex-column" style="margin-left: 1rem;">
                        <li class="nav-item">
                         <a class="nav-link sidebar-menu-item" href="#"  style="color: #333333">
                            Questions</a>
                        </li>
                        <li class="nav-item">
                            <a class="nav-link sidebar-menu-item" href="#" style="color: #333333">Users</a>
                        </li>
                    </ul>
                </li>

            </ul>
        </div>`;

    static  createSideBar(){
        return this.sidebar;
    }

    static activeStyle(){
        const arrElement = document.querySelectorAll(".sidebar-menu-item");
        arrElement.forEach(el =>{
            if(el.classList.contains("active")){
                const parent = el.parentElement;
                parent.style.borderBottom = "1px solid orange";
            }
        })
    }
}