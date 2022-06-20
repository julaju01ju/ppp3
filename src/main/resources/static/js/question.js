const questionId = document.location.href.split("/").slice(-1)[0].replace(/\D/g,'');

// const form  = document.querySelector('#form');
// const title = document.querySelector('#titleInput');
// const token = getCookie('token');

// form.addEventListener("submit", async e => {
//     e.preventDefault();
//     if (title.validity.valueMissing) {
//        return;
//     }
//
//     const response = await fetch("/api/user/question/", {
//         method: "POST",
//         headers: {
//             'Authorization': 'Bearer ' + token,
//             "Accept": "application/json", "Content-Type": "application/json"},
//             body: JSON.stringify({
//                 title: document.getElementById("titleInput").value,
//                 description: tinymce.get('questionBodyInput').getContent(),
//                 tags: document.getElementById("log").textContent = JSON.parse(tag)
//             })
//         }
//     );
//     if (response.ok) {
//         window.location.href = '/questions';
//
//     }
// });

$(document).ready(async function () {

    await questionFilling();
    await answersFilling();
        //Заполнение вопросов
    // await pagination(1, 5, getAnswers);
})

function questionFilling(){

    return fetch(`/api/user/question/` + questionId,
        {
            method: 'GET',
            headers: {
                'Authorization': 'Bearer ' + getCookie('token'),
                'Accept': 'application/json', 'Content-Type': 'application/json'
            },
        })
        .then(response => {
            if (response.status !== 200) {
                document.querySelector('#question-title').textContent =
                    "Ошибка запроса";
                document.querySelector('#question-body p').textContent =
                    "Измените номер вопроса или обновите страницу через 2 минуты.";
                exit(0);
            } else {
                response.json().then(data => {
                    document.querySelector('#question-title').textContent = data.title;
                    document.querySelector('#question-body p').textContent = data.description;
                    document.querySelector('#question-persist-date').textContent =
                        "Дата создания: " + getDateFromDateTime(data.persistDateTime);
                    document.querySelector('#question-update-date').textContent =
                        "Последнее изменение: " + getDateFromDateTime(data.lastUpdateDateTime);
                    document.querySelector('#question-view-count').textContent =
                        "Просмотров: " + data.viewCount;
                    let text = "";
                    data.listTagDto.forEach(tag => {
                        text += '<li className = "text-start list-inline-item">' +
                                    '<a href = "#" style = "font-size: .7rem; background-color:lightblue; '+
                                        'color: blue; padding: .2rem .5rem; border-radius: .2rem; text-decoration: none" >' +
                                    tag.name +
                                    '</a>'+
                                '</li>';
                    })
                    document.querySelector('#tag-list').innerHTML = text;
                    document.querySelector('#users-info').innerHTML =
'                                    <div class="row">\n' +
'                                        <div class="col">\n' +
'                                            <div style="display: inline;">\n' +
'                                                <div><a href="#"><small class="text-secondary">Поделиться</small></a></div>\n' +
'                                                <div><a href="#"><small class="text-secondary">Править</small></a></div>\n' +
'                                                <div><a href="#"><small class="text-secondary">Отслеживать</small></a></div>\n' +
'                                            </div>\n' +
'                                        </div>\n' +
'                                        <div class="col">\n' +
'                                            <div><small>отредактирован: ' + timeDifference(data.lastUpdateDateTime) + '</small></div>\n' +
'                                            <div></div>\n' +
'                                            <ul class="list-unstyled">\n' +
'                                                <li><i class="fa fa-star"></i><small>' + data.authorName + '</small></li>\n' +
'                                            </ul>\n' +
'                                        </div>\n' +
'                                        <div class="col" style="background-color:lightblue; padding: .2rem .5rem; border-radius: .2rem; text-decoration: none">\n' +
'                                            <div><small>задан: ' + timeDifference(data.persistDateTime) + '</small></div>\n' +
'                                            <div></div>\n' +
'                                            <ul class="list-unstyled">\n' +
'                                                <li><i class="fa fa-star"></i><small>' + data.authorName + '</small></li>\n' +
'                                            </ul>\n' +
'                                        </div>\n' +
'                                    </div>\n' +
'                                </div>'
                });
            }
        });
}

function answersFilling() {

    return fetch(`/api/user/question/` + questionId + `/answer`,
        {
            method: 'GET',
            headers: {
                'Authorization': 'Bearer ' + getCookie('token'),
                'Accept': 'application/json', 'Content-Type': 'application/json'
            },
        })
        .then(response => {
            console.log(response);
            if (response.status !== 200) {

                document.querySelector('#answer-row').innerHTML = "";
                document.querySelector('#count-answers').textContent =
                    data.length + declOfNum(data.length, [" ответ", " ответа", " ответов"]);
                console.log(data);
                let text = "";
                data.forEach(function (item) {
                    text +=
                        '                <div class="flex-row">\n' +
                        '                    <div class="col">\n' +
                        '                        <div class="row">\n' +
                        '                            <div class="col-1 text-center" id="voteplace-1">\n' +
                        '                                <ul class="list-unstyled">\n' +
                        '                                    <li><a id="voteUp-1" href="#"><i class="fas fa-arrow-circle-up" style="color: rgb(121,135,155);"></i></a></li>\n' +
                        '                                    <li id="countVote-1">0</li>\n' +
                        '                                    <li id="voteDown-1"><a href="#"><i class="fa fa-arrow-circle-down" style="color: rgb(121,135,155);"></i></a></li>\n' +
                        '                                    <li><a href="#"><i class="fa fa-rotate-right" style="color: rgb(121,135,155);"></i></a></li>\n' +
                        '                                </ul>\n' +
                        '                            </div>\n' +
                        '                            <div class="col" id="questionplace-1">\n' +
                        '                                <div id="answer-body">\n' +
                        '                                    <p>' + item.body + '<br></p>\n' +
                        '                                </div>\n' +
                        '                                <div id="users-info-1">\n' +
                        '                                    <div class="row">\n' +
                        '                                        <div class="col">\n' +
                        '                                            <div style="display: inline;">\n' +
                        '                                                <div><a href="#"><small class="text-secondary">Поделиться</small></a></div>\n' +
                        '                                                <div><a href="#"><small class="text-secondary">Править</small></a></div>\n' +
                        '                                                <div><a href="#"><small class="text-secondary">Отслеживать</small></a></div>\n' +
                        '                                            </div>\n' +
                        '                                        </div>\n' +
                        '                                        <div class="col"></div>\n' +
                        '                                        <div class="col" style="background-color:lightblue; padding: .2rem .5rem; border-radius: .2rem; text-decoration: none">\n' +
                        '                                            <div><small>Ответ: ' + timeDifference(item.persistDate) + '</small></div>\n' +
                        '                                            <div></div>\n' +
                        '                                            <ul class="list-unstyled">\n' +
                        '                                                <li><i class="fa fa-star"></i><small>' + item.nickName + '</small></li>\n' +
                        '                                            </ul>\n' +
                        '                                        </div>\n' +
                        '                                    </div>\n' +
                        '                                </div>\n' +
                        '                                <div id="question-comments-1">\n' +
                        '                                    <hr>\n' +
                        '                                    <p style="font-size: 11px;margin: 9px;">Не могу перезагрузить, потому что это окно появляется только один раз — после получения игры (в том числе бесплатной). Где-то же должен быть в Windows (может, в реестре) этот протокол.</p>\n' +
                        '                                    <hr>\n' +
                        '                                </div>\n' +
                        '                                <div><a href="#"><small class="text-secondary">Добавить комментарий</small></a></div>\n' +
                        '                            </div>\n' +
                        '                        </div>\n' +
                        '                    </div>' +
                        '                </div>';

                //------------

                //Реализовать "Ответы не обнаружены" и "Дообавить первый ответ"

                //-------------
                exit(0);
            } else {
                response.json().then(data => {
                    let text = "";
                    if (data.length === 0) {
                        document.querySelector('#count-answers').textContent =
                            "Ответы пока отсутствуют"
                        document.querySelector('#handleQuestion').textContent =
                            "Опубликовать первый ответ"
                    } else {
                        document.querySelector('#count-answers').textContent =
                            data.length + declOfNum(data.length, [" ответ", " ответа", " ответов"]);
                        document.querySelector('#handleQuestion').textContent =
                            "Опубликовать ответ"
                        console.log(data);
                        data.forEach(function (item) {
                            text +=
                '                <div class="flex-row">\n' +
                '                    <div class="col">\n' +
                '                        <div class="row">\n' +
                '                            <div class="col-1 text-center" id="voteplace-1">\n' +
                '                                <ul class="list-unstyled">\n' +
                '                                    <li><a id="voteUp-1" href="#"><i class="fas fa-arrow-circle-up" style="color: rgb(121,135,155);"></i></a></li>\n' +
                '                                    <li id="countVote-1">0</li>\n' +
                '                                    <li id="voteDown-1"><a href="#"><i class="fa fa-arrow-circle-down" style="color: rgb(121,135,155);"></i></a></li>\n' +
                '                                    <li><a href="#"><i class="fa fa-rotate-right" style="color: rgb(121,135,155);"></i></a></li>\n' +
                '                                </ul>\n' +
                '                            </div>\n' +
                '                            <div class="col" id="questionplace-1">\n' +
                '                                <div id="answer-body">\n' +
                '                                    <p>' + item.body + '<br></p>\n' +
                '                                </div>\n' +
                '                                <div id="users-info-1">\n' +
                '                                    <div class="row">\n' +
                '                                        <div class="col">\n' +
                '                                            <div style="display: inline;">\n' +
                '                                                <div><a href="#"><small class="text-secondary">Поделиться</small></a></div>\n' +
                '                                                <div><a href="#"><small class="text-secondary">Править</small></a></div>\n' +
                '                                                <div><a href="#"><small class="text-secondary">Отслеживать</small></a></div>\n' +
                '                                            </div>\n' +
                '                                        </div>\n' +
                '                                        <div class="col"></div>\n' +
                '                                        <div class="col" style="background-color:lightblue; padding: .2rem .5rem; border-radius: .2rem; text-decoration: none">\n' +
                '                                            <div><small>Ответ: ' + timeDifference(item.persistDate) + '</small></div>\n' +
                '                                            <div></div>\n' +
                '                                            <ul class="list-unstyled">\n' +
                '                                                <li><i class="fa fa-star"></i><small>' + item.nickName + '</small></li>\n' +
                '                                            </ul>\n' +
                '                                        </div>\n' +
                '                                    </div>\n' +
                '                                </div>\n' +
                '                                <div id="question-comments-1">\n' +
                '                                    <hr>\n' +
                '                                    <p style="font-size: 11px;margin: 9px;">Не могу перезагрузить, потому что это окно появляется только один раз — после получения игры (в том числе бесплатной). Где-то же должен быть в Windows (может, в реестре) этот протокол.</p>\n' +
                '                                    <hr>\n' +
                '                                </div>\n' +
                '                                <div><a href="#"><small class="text-secondary">Добавить комментарий</small></a></div>\n' +
                '                            </div>\n' +
                '                        </div>\n' +
                '                    </div>' +
                '                </div>';
                            });
                        }
                        document.querySelector('#answer-row').innerHTML = text;
                    })
            }
    });
}




function getDateFromDateTime(dateTime) {
    return dateTime.substr(0, 10)
}

function timeDifference(previous) {

    let msPerMinute = 60 * 1000;
    let msPerHour = msPerMinute * 60;
    let msPerDay = msPerHour * 24;
    let msPerMonth = msPerDay * 30;
    let msPerYear = msPerDay * 365;

    previous = new Date(previous);
    let current = new Date();
    let elapsed = current.getTime() - previous.getTime();

    if (elapsed < msPerMinute) {
        return Math.round(elapsed / 1000) + ' seconds ago';
    }
    if (elapsed < msPerHour) {
        return Math.round(elapsed / msPerMinute) + ' minutes ago';
    }
    if (elapsed < msPerDay) {
        return Math.round(elapsed / msPerHour) + ' hours ago';
    }
    if (elapsed < msPerMonth) {
        return Math.round(elapsed / msPerDay) + ' days ago';
    }
    if (elapsed < msPerYear) {
        return Math.round(elapsed / msPerMonth) + ' months ago';
    } else {
        return Math.round(elapsed / msPerYear) + ' years ago';
    }
}

function declOfNum(n, text_forms) {
    n = Math.abs(n) % 100;
    let n1 = n % 10;
    if (n > 10 && n < 20) {
        return text_forms[2];
    }
    if (n1 > 1 && n1 < 5) {
        return text_forms[1];
    }
    if (n1 === 1) {
        return text_forms[0];
    }
    return text_forms[2];
}

//     Примечание - сделать вопрос просмотренным
// @PostMapping("/{questionId}/view")
// @ApiOperation("При переходе на вопрос c questionId=* авторизованного пользователя, вопрос добавляется в QuestionViewed")
