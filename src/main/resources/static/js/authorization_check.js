const butCurMain = document.getElementById(`currentQuestionsMain`)
const butNewMain = document.getElementById(`newQuestionsMain`)
const butRepMain = document.getElementById(`reputationMain`)


butNewMain.addEventListener (`click`, () => {
    butNewMain.className = `btn btn-outline-secondary active`
    butCurMain.className = `btn btn-outline-secondary`
    butRepMain.className = `btn btn-outline-secondary`
})

butCurMain.addEventListener (`click`, () => {
    butCurMain.className = `btn btn-outline-secondary active`
    butNewMain.className = `btn btn-outline-secondary`
    butRepMain.className = `btn btn-outline-secondary`
})




$(document).ready(async function () {

    await getAllQuestionsCountMain();
    await pagination(1, 50, getAllQuestionsMain);
})


function getAllQuestionsMain(page, itemsOnPage) {

    return fetch(`/api/user/question?page=${page}&items=${itemsOnPage}`,
        {
            method: 'GET',
            headers: {
                'Authorization': 'Bearer ' + getCookie('token'),
                'Accept': 'application/json', 'Content-Type': 'application/json'
            },
        });
}

function getNewQuestionsMain(page, itemsOnPage) {

    return fetch(`/api/user/question/new?page=${page}&items=${itemsOnPage}`,
        {
            method: 'GET',
            headers: {
                'Authorization': 'Bearer ' + getCookie('token'),
                'Accept': 'application/json', 'Content-Type': 'application/json'
            },
        });
}

function getAllQuestionsCountMain() {
    return fetch(`/api/user/question/count`,
        {
            method: 'GET',
            headers: {
                'Authorization': 'Bearer ' + getCookie('token'),
                'Accept': 'application/json', 'Content-Type': 'application/json'
            },
        }).then(response => {
        response.json().then(questionsCount => {

            const questionsCountDecl = [" вопрос", " вопроса", " вопросов"];
            let decl = declOfNum(questionsCount, questionsCountDecl);
            $(".counter-questions").text(questionsCount + decl);

        })
    });
}


function fillCard(elementItems) {
    $('#questions_list').empty();


    elementItems.forEach(function (item) {
        let newElement2 =
            $(`<div class="question-card d-flex">`)
                .attr("style", "height: 5rem; padding-left: 1.5rem; padding-top: .7rem; border-bottom: 1px solid lightgrey")
                .append($('<div class="votes-list d-flex ">')
                    .append($('<div class="d-flex flex-column">')
                        .attr("style", "margin-right: .7rem")
                        .append($('<span class="count_question text-center">')
                            .attr("style", "color: #7b8185").text(item.countValuable))
                        .append($('<span class="label">')
                            .attr("style", "font-size: .7rem").text('голосов'))
                    )

                    .append($('<div class="d-flex flex-column">')
                        .attr("style", "margin-right: .7rem; border: 1px solid green; border-radius: .5rem; margin-bottom: .5rem; padding: 0 .3rem")
                        .append($('<span class="count_question text-center">')
                            .attr("style", "color: #7b8185").text(item.countAnswer))
                        .append($('<span class="label">')
                            .attr("style", "font-size: .7rem").text('ответов'))
                    )
                    .append($('<div class="d-flex flex-column">')
                        .append($('<span class="count_question text-center">')
                            .attr("style", "color: #7b8185").text(item.viewCount))
                        .append($('<span class="label">')
                            .attr("style", "font-size: .7rem").text('показов'))
                    )
                )

                .append($('<div class="d-flex flex-column">')
                    .append($('<div class="question-card-body">')
                        .attr("style", "margin-left: 2rem; overflow: hidden; height: 3rem; margin-bottom: .7rem")
                        .append($('<a href="#" class="question-heading">'))
                            .attr("style", "color: #0080FF; margin-left: 30px; text-decoration: none").text(item.title))
                    .append($('<div class="tag-list">')
                        .attr("style", "height: 2rem")
                        .append($('<ul class="d-flex">')
                            .attr("style", "list-style: none")
                            .append(
                                $.map(item.listTagDto, function (itemTag) {
                                        return $('<li class="list-item">')
                                            .append($('<a href="#">')
                                                .attr("style", "margin-left: 1rem; font-size: .7rem; background-color: lightblue; color: blue; padding: .3rem .5rem; border-radius: .2rem; text-decoration: none")
                                                .text(itemTag.name))
                                    }
                                )
                            )
                        )
                    )
                )
        $('#questions_list').append(newElement2);
    });
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






function clearCookie() {
    document.cookie = 'token=;expires=Thu, 01 Jan 1970 00:00:01 GMT;';
}

function getCookie(cookieName) {
    let cookie = {};
    document.cookie.split(';').forEach(function (el) {
        let [key, value] = el.split('=');
        cookie[key.trim()] = value;
    })
    return cookie[cookieName] !== undefined
        ? cookie[cookieName]
        : '';
}



