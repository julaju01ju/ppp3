const myHeaders = new Headers();
myHeaders.append('Content-Type', 'application/json');
myHeaders.append('Authorization','Bearer '+ document.cookie);

function getUsersSortedByReputation(page, itemsOnPage) {

    return fetch(`/api/user/reputation?page=${page}&items=${itemsOnPage}`,
        {method: 'GET',
            headers: myHeaders});
}

function getUsersSortedByNew(page, itemsOnPage) {

    return fetch(`/api/user/new?page=${page}&items=${itemsOnPage}`,
        {method: 'GET',
            headers: myHeaders});
}

function getUsersSortedByVoteSum(page, itemsOnPage) {

    return fetch(`/api/user/vote?page=${page}&items=${itemsOnPage}`,
        {method: 'GET',
            headers: myHeaders});
}

function fillCard(elementItems) {
    $('#usersTable > tbody').empty();
    elementItems.forEach(function (item) {
        let newElement = `<tr>
                                <td> ${item.id} </td>
                                <td> ${item.fullName} </td>
                                <td> ${item.email} </td>
                                <td> ${item.linkImage} </td>
                                <td> ${item.city} </td>
                                <td> ${item.reputation}</td>
                            </tr>`;
        $('#usersTable > tbody').append(newElement);
    });
}
