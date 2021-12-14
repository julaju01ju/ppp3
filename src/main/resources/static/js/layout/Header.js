class Header{
   static header = `<div class="row header" style="border-bottom: 1px solid lightgrey">
        <div class="col">
            <nav class="navbar navbar-expand-lg navbar-light bg-light">
                <div class="container-fluid">
                    <a class="navbar-brand" href="#" style="border-bottom: 2px solid orange">
                        <img src="../../images/header-images/icon/kata_academy.jpeg" alt="KataAcademy"
                             style="height: 1.5rem">
                        KataAcademy
                    </a>
                    <button class="navbar-toggler" type="button" data-bs-toggle="collapse"
                            data-bs-target="#navbarSupportedContent" aria-controls="navbarSupportedContent"
                            aria-expanded="false" aria-label="Toggle navigation">
                        <span class="navbar-toggler-icon"></span>
                    </button>
                    <div class="collapse navbar-collapse" id="navbarSupportedContent">
                        <form class="d-flex input-group" style="width: 65%">
                            <button class="nput-group-text" type="submit" style="border:none">
                                <img src="../../images/header-images/icon/search-icon.png" alt="search"
                                     style="height: 1rem">
                            </button>
                            <input class="form-control me-2" type="search" placeholder="Search" aria-label="Search">
                        </form>
                        <ul class="navbar-nav mb-2 mb-lg-0">
                            <li class="nav-item">
                                <a class="nav-link" aria-current="page" href="#">
                                    <img src="../../images/header-images/icon/message.png" alt="message"
                                         style="height: 1.5rem">
                                </a>
                            </li>
                            <li class="nav-item">
                                <a class="nav-link" href="#">
                                    <img src="../../images/header-images/icon/prize.png" alt="prize" style="height:
                                     1.5rem">
                                </a>
                            </li>
                            <li class="nav-item">
                                <a class="nav-link" href="#">
                                    <img src="../../images/header-images/icon/question.png" alt="prize" style="height:
                                     1.5rem">
                                </a>
                            </li>
                            <li class="nav-item">
                                <a class="nav-link" href="#">
                                    <img src="../../images/header-images/icon/lists.png" alt="prize" style="height:
                                     1.5rem">
                                </a>
                            </li>

                        </ul>
                        <div class="d-flex justify-between align-content-center">
                            <button type="button" class="btn btn-outline-info">Log In</button>
                            <button type="button" class="btn btn-info"
                                    style="color: white; margin-left: .5rem">Registration
                            </button>
                        </div>

                    </div>
                </div>
            </nav>
        </div>
    </div>`;

   static createHeader(){
       return this.header;
   }
}