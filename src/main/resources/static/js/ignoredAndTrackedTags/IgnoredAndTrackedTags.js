const header = new Headers();
header.append('Content-Type', 'application/json');
header.append('Authorization', 'Bearer ' + getCookie('token'));

// Переменные для GET запросов
const trackedTags = document.querySelector('#trackedTags');
const ignoredTags = document.querySelector('#ignoredTags');

const searchTagIgnored = document.querySelector('#searchIgnoreTag')

const trackUrl = '/api/user/tag/tracked';
const ignoreUrl = '/api/user/tag/ignored';

const inputSearchTrackedTag = document.getElementById('inputSearchTrackedTag');
const inputSearchIgnoredTag = document.getElementById('inputSearchIgnoredTag');

let resultSearchTrackedTag = document.getElementById('resultSearchTrackedTag')
let resultsWrapper = document.querySelector('.resultSearch')


function startInput(tableInput){
    tableInput.addEventListener('keyup', (e) => {
    let input = tableInput.value
    console.log(input)
    if (input.length > 0){
        resultsWrapper.style.display = "block"
        resultsWrapper.style.opacity = 1
        document.getElementById('inputSearchTrackedTag').oninput = debounce(getLatterTracked, 500);
        document.getElementById('inputSearchIgnoredTag').oninput = debounce(getLatterIgnored, 500);
    } else {
        resultsWrapper.style.display = "none"
        resultsWrapper.innerHTML =""
    }
})
}

let selectIdTag = 0;
function select(element){
    let selectData = element.textContent
    if (inputSearchTrackedTag.value !== ""){
        inputSearchTrackedTag.value = selectData
        selectIdTag = element.value
        resultsWrapper.style.display = "none"
        resultsWrapper.innerHTML =""
    } else {
        inputSearchIgnoredTag.value = selectData
        selectIdTag = element.value
        resultsWrapper.style.display = "none"
        resultsWrapper.innerHTML =""
    }


}

function getLatterTracked() {
    let param = document.getElementById('inputSearchTrackedTag').value
    fetch(`/api/user/tag/latter?searchString=${param}`, {
        method: 'GET',
        headers: header
    }).then(res => res.json()).then(data => {
        renderSearch(data, resultsWrapper)
    })
}

function getLatterIgnored() {
    let param = document.getElementById('inputSearchIgnoredTag').value
    fetch(`/api/user/tag/latter?searchString=${param}`, {
        method: 'GET',
        headers: header
    }).then(res => res.json()).then(data => {
        renderSearch(data, resultsWrapper);
    })
}

function renderSearch(element, inner) {
    let output = '';
    element.forEach(tag => {
        output += `
                     <li onclick="select(this)" value="${tag.id}" class="${tag.name}" style="padding: 3px">${tag.name}</li>`;
    });
    inner.innerHTML = output;
}

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

//////////////////////
// Для задержки вывода


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
/////////////////////
// Очистка поля ввода

function clearInput() {
    let clearInputTr = document.getElementById('inputSearchTrackedTag').value = "";
    let clearInputIgn = document.getElementById('inputSearchIgnoredTag').value = "";
}

//////////////////////////////
// Запросы на добавление тэгов

function addIgnoredTag() {
        fetch(`/api/user/tag/${selectIdTag}/ignored`, {
            method: 'POST',
            headers: header
        })
            .then(res => res.json())
            .then(data => getIgnoredTags(data));
        clearInput()
}

function addTrackedTag() {
        fetch(`/api/user/tag/${selectIdTag}/tracked`, {
            method: 'POST',
            headers: header
        })
            .then(res => res.json())
            .then(data => getTrackedTags(data));
        clearInput();

}

function showTags() {
    getTrackedTags();
    getIgnoredTags();
}






