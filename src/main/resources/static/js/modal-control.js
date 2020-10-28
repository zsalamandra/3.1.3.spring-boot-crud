$('document').ready(function () {
    $("#showModal").on('show.bs.modal', function (event) {
        let userId = $(event.relatedTarget).data('user-id');
        let method = $(event.relatedTarget).data('method');
        const href = "/adm/users/" + userId + "/" + method;
        $.get(href, function (data) {
            $('#showModal').empty();
            $('#showModal').html(data);
        });
    });
})