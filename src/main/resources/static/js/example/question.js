$(document).ready(async function () {

    await pagination(1, 50, getMostPopularQuestion);
})


function getMostPopularQuestion(page, itemsOnPage) {

    return fetch(`/api/user/question/sortedQuestions?page=${page}&items=${itemsOnPage}`,
        {
            method: 'GET',
            headers: {
                'Authorization': 'Bearer ' + getCookie('token'),
                'Accept': 'application/json', 'Content-Type': 'application/json'
            },
        });
}

function fillCard(elementItems) {
    $('#questions-browser').empty();

    elementItems.forEach(function (item) {
        let newElement =
            `<div class="question-card d-flex" style="height: 9rem; padding-left: 1.5rem; padding-top: .7rem; border-bottom: 1px solid lightgrey">
                <div class="votes-list d-flex flex-column">
                    <div class="d-flex flex-column">
                        <span class="count_question text-center" style="color: #7b8185">${item.countValuable}</span>
                        <span class="label" style="font-size: .7rem">голосов</span>
                    </div>
                    <div class="d-flex flex-column">
                        <span class="count_question text-center" style="color: #7b8185">${item.countAnswer}</span>
                        <span class="label" style="font-size: .7rem">ответов</span>
                    </div>
                    <div class="d-flex flex-column">
                        <span class="count_question text-center" style="color: #7b8185">${item.viewCount}</span>
                        <span class="label" style="font-size: .7rem">показов</span>
                    </div>
                </div>
                <div class="d-flex flex-column">
                    <div class="question-card-body" style="margin-left: 2rem; overflow: hidden; height: 5rem;
                                margin-bottom: .7rem">
                        <a href="#" class="question-heading" style="text-decoration: none">${item.title}</a>
                        <div class="question-content" style="font-size: .8rem; margin-top: 1rem">
                            <p>${item.description}</p>
                        </div>
                    </div>
                    <div class="tag-list " style="height: 2rem">
                    
                        <ul class="d-flex" style="list-style: none">
                            <li class="list-item">
                                <a href="#" style="margin-left: 1rem; font-size: .7rem; background-color:
                                        lightblue; color: blue; padding: .3rem .5rem; border-radius: .2rem; text-decoration: none">
                                    Java</a>
                            </li>
                            <li class="list-item">
                                <a href="#" style="margin-left: 1rem; font-size: .7rem; background-color:
                                        lightblue; color: blue; padding: .3rem .5rem; border-radius: .2rem; text-decoration: none">
                                    Hibernate</a>
                            </li>
                            <li class="light-item">
                                <a href="#" style="margin-left: 1rem; font-size: .7rem; background-color:
                                        lightblue; color: blue; padding: .3rem .5rem; border-radius: .2rem; text-decoration: none">
                                    Spring</a>
                            </li>
                        </ul>
                    </div>
                </div>
            </div>`;

        $('#questions-browser').append(newElement);
    });
}


