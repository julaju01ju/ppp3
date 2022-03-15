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
    $('#questions_list').empty();

    elementItems.forEach(function (item) {
        let newElement =
            $('<div class="question-card d-flex">')
                .attr("style", "height: 9rem; padding-left: 1.5rem; padding-top: .7rem; border-bottom: 1px solid lightgrey")
                .append($('<div class="votes-list d-flex flex-column">')
                    .append($('<div class="d-flex flex-column">')
                        .append($('<span class="count_question text-center">')
                            .attr("style", "color: #7b8185").text(item.countValuable))
                        .append($('<span class="label">')
                            .attr("style", "font-size: .7rem").text('голосов'))
                    )
                    .append($('<div class="d-flex flex-column">')
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
                        .attr("style", "margin-left: 2rem; overflow: hidden; height: 5rem; margin-bottom: .7rem")
                        .append($('<a href="#" class="question-heading">')
                            .attr("style", "text-decoration: none").text(item.title))
                        .append($('<div class="question-content">')
                            .attr("style", "font-size: .8rem; margin-top: 1rem")
                            .append($('<p>').text(item.description))))
                    .append($('<div class="tag-list" id="tag-list">')
                        .attr("style", "height: 2rem")
                        .append($('<ul class="d-flex">')
                            .attr("style", "list-style: none")
                            .append(
                                $.map(item.listTagDto, function (itemTag) {
                                        return $('<li class="list-item">')
                                            .append('<a href="#">')
                                            .attr("style", "margin-left: 1rem; font-size: .7rem; background-color: lightblue; color: blue; padding: .3rem .5rem; border-radius: .2rem; text-decoration: none")
                                            .text(itemTag.name)
                                    }
                                ))
                        ))
                )

        $('#questions_list').prepend(newElement);

    });
}