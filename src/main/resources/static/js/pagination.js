function getResponce() {
    var responce = fetch('http://localhost:8091/api/user/reputation', {
        method: 'GET',
        headers: new Headers({
            'Authorization': 'Bearer ' + token,
            'Content-Type': 'application/json'
        }),
    });
}

var tableData = [
    {
        'id': '14',
        'email': '13@mail.com',
        'fullName': 'fullName13',
        'linkImage': 'imageLink13',
        'city': 'City13',
        'reputation': '49'
    },
    {
        'id': '32',
        'email': '31@mail.com',
        'fullName': 'fullName31',
        'linkImage': 'imageLink31',
        'city': 'City31',
        'reputation': '45'
    },
    {
        'id': '43',
        'email': '42@mail.com',
        'fullName': 'fullName42',
        'linkImage': 'imageLink42',
        'city': 'City42',
        'reputation': '45'
    },
    {
        'id': '9',
        'email': '8@mail.com',
        'fullName': 'fullName8',
        'linkImage': 'imageLink8',
        'city': 'City8',
        'reputation': '44'
    },
    {
        'id': '38',
        'email': '37@mail.com',
        'fullName': 'fullName37',
        'linkImage': 'imageLink37',
        'city': 'City37',
        'reputation': '44'
    },
    {
        'id': '33',
        'email': '32@mail.com',
        'fullName': 'fullName32',
        'linkImage': 'imageLink32',
        'city': 'City32',
        'reputation': '43'
    },
    {
        'id': '45',
        'email': '44@mail.com',
        'fullName': 'fullName44',
        'linkImage': 'imageLink44',
        'city': 'City44',
        'reputation': '42'
    },
    {
        'id': '22',
        'email': '21@mail.com',
        'fullName': 'fullName21',
        'linkImage': 'imageLink21',
        'city': 'City21',
        'reputation': '41'
    },
    {
        'id': '6',
        'email': '5@mail.com',
        'fullName': 'fullName5',
        'linkImage': 'imageLink5',
        'city': 'City5',
        'reputation': '40'
    },
    {
        'id': '50',
        'email': '49@mail.com',
        'fullName': 'fullName49',
        'linkImage': 'imageLink49',
        'city': 'City49',
        'reputation': '38'
    }
]

var state = {
    'querySet': tableData,

    'page': 1,
    'rows': 5,
    'window': 5,
}

buildTable()

function pagination(querySet, page, rows) {

    var trimStart = (page - 1) * rows
    var trimEnd = trimStart + rows

    var trimmedData = querySet.slice(trimStart, trimEnd)

    var pages = Math.round(querySet.length / rows);

    return {
        'querySet': trimmedData,
        'pages': pages,
    }
}

function pageButtons(pages) {
    var wrapper = document.getElementById('pagination-wrapper')

    wrapper.innerHTML = ``
    console.log('Pages:', pages)

    var maxLeft = (state.page - Math.floor(state.window / 2))
    var maxRight = (state.page + Math.floor(state.window / 2))

    if (maxLeft < 1) {
        maxLeft = 1
        maxRight = state.window
    }

    if (maxRight > pages) {
        maxLeft = pages - (state.window - 1)

        if (maxLeft < 1) {
            maxLeft = 1
        }
        maxRight = pages
    }


    for (var page = maxLeft; page <= maxRight; page++) {
        wrapper.innerHTML += `<button value=${page} class="page btn btn-sm btn-info">${page}</button>`
    }

    if (state.page != 1) {
        wrapper.innerHTML = `<button value=${1} class="page btn btn-sm btn-info">&#171; prev</button>` + wrapper.innerHTML
    }

    if (state.page != pages) {
        wrapper.innerHTML += `<button value=${pages} class="page btn btn-sm btn-info">next &#187;</button>`
    }

    $('.page').on('click', function () {
        $('#table-body').empty()

        state.page = Number($(this).val())

        buildTable()
    })

}

function buildTable() {
    var table = $('#table-body')
    var data = pagination(state.querySet, state.page, state.rows)
    var myList = data.querySet

    for (var i = 1 in myList) {
        var row = `<tr>
            <td>${myList[i].id}</td>
            <td>${myList[i].email}</td>
            <td>${myList[i].fullName}</td>
            <td>${myList[i].linkImage}</td>
            <td>${myList[i].city}</td>
            <td>${myList[i].reputation}</td>
            `
        table.append(row)
    }
    pageButtons(data.pages)
}
