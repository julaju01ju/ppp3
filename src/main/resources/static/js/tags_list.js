const header = new Headers();
header.append('Content-Type', 'application/json');
header.append('Authorization', 'Bearer ' + getCookie('token'));
let listOfTags = document.querySelector('#tagList');

let getTagList = (element) => {
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

async function pagination(page, itemsOnPage, funcForURL) {
    $('#paginationButtons').empty();

    await funcForURL(page, itemsOnPage).then(response => {
        response.json().then(element => {
            for (let i = 1; i < element.totalPageCount + 1; i++) {
                if (element.currentPageNumber === i) {
                    let newPage = `<button type="button" class="btn btn-outline-primary active" onclick="pagination(${i}, ${itemsOnPage}, ${funcForURL})">${i}</button>`;
                    $('#paginationButtons').append(newPage);
                } else {
                    let newPage = `<button type="button" class="btn btn-outline-primary" onclick="pagination(${i}, ${itemsOnPage}, ${funcForURL})">${i}</button>`;
                    $('#paginationButtons').append(newPage);
                }
            }
            getTagList(element.items);
        });
    });
}




