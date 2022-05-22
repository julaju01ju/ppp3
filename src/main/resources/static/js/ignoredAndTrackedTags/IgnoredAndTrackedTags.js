const header = new Headers();
header.append('Content-Type', 'application/json');
header.append('Authorization', 'Bearer ' + getCookie('token'));
const trackedTags = document.querySelector('#trackedTags');
const ignoredTags = document.querySelector('#ignoredTags');
const trackUrl = '/api/user/tag/tracked';
const ignoreUrl = '/api/user/tag/ignored';

let renderTrackedTags = (element) => {
    let output = '';
    element.forEach(tag => {
        output += `
                    <div><a href="#" style="  background-color:lightblue; font-size: .8rem;
                    color: darkblue; padding: .3rem .5rem; border-radius: .2rem;
                    text-decoration: none" id="selectTagsFromTrackedTags">${tag.name}</a></div>`;
    });
    trackedTags.innerHTML = output;
}

let renderIgnoredTags = (element) => {
    let output = '';
    element.forEach(tag => {
        output += `
                    <div><a href="#" style="  background-color:lightblue; font-size: .8rem;
                    color: darkblue; padding: .3rem .5rem; border-radius: .2rem;
                    text-decoration: none">${tag.name}</a></div>`;
    });
    ignoredTags.innerHTML = output;
}

function getTrackedTags() {
    return fetch(trackUrl, {
        method: 'GET',
        headers: header
    })
        .then(res => res.json())
        .then(data => renderTrackedTags(data))
}

function getIgnoredTags() {
    return fetch(ignoreUrl, {
        method: 'GET',
        headers: header
    })
        .then(res => res.json())
        .then(data => renderIgnoredTags(data))
}

function showAdd(id) {
    let div = document.getElementById(id);
    if (div.style.display === '') {
        div.style.display = 'none';
    } else
        div.style.display = '';
}

let checkIdTrackedTagsIfExists = "";
let checkIdIgnoredTagsIfExists = "";

function clearInput(){
    let clearInputTr = document.getElementById('trackedTagId').value = "";
    let clearInputIgn = document.getElementById('ignoredTagId').value = "";

}

function addIgnoredTag() {
    let id = document.getElementById('ignoredTagId').value
    if (checkIdIgnoredTagsIfExists === id){
        console.log('Tэг с id: ' + id + ' уже добавлен')
        return clearInput();
    } else {
        checkIdIgnoredTagsIfExists = id;
        fetch(`/api/user/tag/${id}/ignored`, {
            method: 'POST',
            headers: header
        })
            .then(res => res.json())
            .then(data => getIgnoredTags(data));
        clearInput()
    }
}

function addTrackedTag() {
    let id = document.getElementById('trackedTagId').value
    if (checkIdTrackedTagsIfExists === id){
        console.log('Tэг с id: ' + id + ' уже добавлен')
        return clearInput();
    } else {
        checkIdTrackedTagsIfExists = id;
    fetch(`/api/user/tag/${id}/tracked`, {
        method: 'POST',
        headers: header
    })
        .then(res => res.json())
        .then(data => getTrackedTags(data));
        clearInput();
    }
}

function showTags() {
    getTrackedTags();
    getIgnoredTags();
}




