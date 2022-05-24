const header = new Headers();
header.append('Content-Type', 'application/json');
header.append('Authorization', 'Bearer ' + getCookie('token'));
const trackedTags = document.querySelector('#trackedTags');
const ignoredTags = document.querySelector('#ignoredTags');
const searchTagTracked = document.querySelector('#searchTrackedTag')
const searchTagIgnored = document.querySelector('#searchIgnoreTag')

const trackUrl = '/api/user/tag/tracked';
const ignoreUrl = '/api/user/tag/ignored';

////////////////////////////
// Методы для вывода данных

function renderTags(element, inner) {
    let output = '';
    element.forEach(tag => {
        output += `
                    <div><a href="#" style="  background-color:lightblue; font-size: .8rem;
                    color: darkblue; padding: .3rem .5rem; border-radius: .2rem;
                    text-decoration: none" >${tag.name}</a></div>`;
    });
    inner.innerHTML = output;
}

function renderSearch(element, inner) {
    let output = '';
        element.forEach(tag => {
            output += `
                     <option value="${tag.id}" >${tag.name}</option>`;
        });
        inner.innerHTML = output;
}

//////////////////////
// Для задержки вывода

document.getElementById('trackedTagId').oninput = debounce(getLatterTracked, 500);
document.getElementById('ignoredTagId').oninput = debounce(getLatterIgnored, 500);

function debounce(call, timeout) {
    return function perform(...args) {
        let previousCall = this.lastCall;
        this.lastCall = Date.now();
        if (previousCall && this.lastCall - previousCall <= timeout) {
            clearTimeout(this.lastCallTimer);
        }
        this.lastCallTimer = setTimeout(() => call(...args), timeout);
    }
}

function getLatterTracked() {
    let param = document.getElementById('trackedTagId').value
    fetch(`/api/user/tag/latter?searchString=${param}`, {
        method: 'GET',
        headers: header
    }).then(res => res.json()).then(data => {
        renderSearch(data, searchTagTracked);
    })
}

function getLatterIgnored() {
    let param = document.getElementById('ignoredTagId').value
    fetch(`/api/user/tag/latter?searchString=${param}`, {
        method: 'GET',
        headers: header
    }).then(res => res.json()).then(data => {
        renderSearch(data, searchTagIgnored);
    })
}

//////////////////////////////////
// Запросы на получения всех тэгов

function getTrackedTags() {
    return fetch(trackUrl, {
        method: 'GET',
        headers: header
    })
        .then(res => res.json())
        .then(data => renderTags(data, trackedTags))
}

function getIgnoredTags() {
    return fetch(ignoreUrl, {
        method: 'GET',
        headers: header
    })
        .then(res => res.json())
        .then(data => renderTags(data, ignoredTags))
}

///////////////////////////////////////
// Для вывода поиска по клику на кнопку

function showAdd(id) {
    let div = document.getElementById(id);
    if (div.style.display === '') {
        div.style.display = 'none';
    } else
        div.style.display = '';
}

let checkIdTrackedTagsIfExists = "";
let checkIdIgnoredTagsIfExists = "";

/////////////////////
// Очистка поля ввода

function clearInput() {
    let clearInputTr = document.getElementById('trackedTagId').value = "";
    let clearInputIgn = document.getElementById('ignoredTagId').value = "";
}

//////////////////////////////
// Запросы на добавление тэгов

function addIgnoredTag() {
    let id = document.getElementById('ignoredTagId').value
    if (checkIdIgnoredTagsIfExists === id) {
        console.log('Поле add tag не заполнено или метка уже добавлена')
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
    console.log(id)
    if (checkIdTrackedTagsIfExists === id) {
        console.log('Поле add tag не заполнено или метка уже добавлена')
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






