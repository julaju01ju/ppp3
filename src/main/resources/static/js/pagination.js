class Pagination {
    quantityPage = 0;
    currentPage = 1;
    constructor(wrap) {
        this.wrap = wrap;
        this.init();
    }
    init() {
        $(document).on('click', this.wrap + ' .pagePag a', this.clickPage.bind(this));
        $(document).on('click', this.wrap + ' ul li:first-child a', this.clickPrev.bind(this));
        $(document).on('click', this.wrap + ' ul li:last-child a', this.clickNext.bind(this));
    }
    setQuantityPage(val) {
        this.quantityPage = val;
        this.currentPage = 1;
        this.renderPag();
    }
    clickPrev(e) {
        e.preventDefault();
        if (this.currentPage > 1) {
            this.currentPage--;
            $(this.wrap + ' .pagePag').removeClass('active');
            $(this.wrap + ' .pagePag a[data-page="' + this.currentPage + '"]').parent().addClass('active');
            this.checkCurrentPage();
            this.sendServer();
        }
    }
    clickNext(e) {
        e.preventDefault();
        if (this.currentPage != this.quantityPage) {
            this.currentPage++;
            $(this.wrap + ' .pagePag').removeClass('active');
            $(this.wrap + ' .pagePag a[data-page="' + this.currentPage + '"]').parent().addClass('active');
            this.checkCurrentPage();
            this.sendServer();
        }
    }
    clickPage(e) {
        e.preventDefault();
        const dataPage = $(e.target).attr('data-page');
        if (this.currentPage != dataPage) {
            $(this.wrap + ' .pagePag').removeClass('active');
            $(e.target).parent().addClass('active');
            this.currentPage = dataPage;
            this.checkCurrentPage();
            this.sendServer();
        }
    }
    checkCurrentPage() {
        if (this.currentPage == 1) {
            $(this.wrap + ' ul li:first-child').addClass('disabled');
        } else {
            $(this.wrap + ' ul li:first-child').removeClass('disabled');
        }
        if (this.currentPage == this.quantityPage) {
            $(this.wrap + ' ul li:last-child').addClass('disabled');
        } else {
            $(this.wrap + ' ul li:last-child').removeClass('disabled');
        }
    }
    sendServer() {
        console.log('qq', this.currentPage);
    }
    renderPag() {
        if (this.quantityPage) {
            let html = `<li class="page-item">
                                      <a class="page-link" href="#" tabindex="-1" aria-disabled="true">Назад</a>
                              </li>`;
            for (let i = 0; i < this.quantityPage; i++) {
                html += `<li class="page-item pagePag ${i < 1 ? 'active' : ''}"><a class="page-link" href="#" data-page="${i + 1}">${i + 1}</a></li>`;
            }
            let disable = this.quantityPage < 2 ? 'disabled' : '';
            html += `<li class="page-item ${disable}">
                                  <a class="page-link" href="#">Вперед</a>
                          </li>`;
            $(this.wrap + ' ul').html(html).css('display', 'block');
        } else {
            $(this.wrap).css('display', 'none');
        }
    }
}
const pagination = new Pagination('#pageNavigation');
pagination.setQuantityPage(3);
$('.start').click(function () {
    pagination.setQuantityPage(3);
});