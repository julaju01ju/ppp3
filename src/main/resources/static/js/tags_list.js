const header = new Headers();
header.append('Content-Type', 'application/json');
header.append('Authorization', 'Bearer ' + getCookie('token'));
let listOfTags = document.querySelector('#tagList');

let fillCard = (element) => {
    let output = '';
    element.forEach(tag => {
        output += `<div class="col-sm-3">
                   <div class="card">
                        <div class="card-body">
                           <div><a href="#" class="btn btn-outline-secondary" style="background-color: gainsboro">${tag.name}</a></div>
                          <p class="card-text">${tag.description}</p>
                       </div>
                    </div>
                </div>`;
    });
    listOfTags.innerHTML = output;
}

function getTagByItem(page, itemNumber) {
    return fetch(`/api/user/tag/name?page=${page}&items=${itemNumber}`, {
        method: 'GET',
        headers: header
    })
}





