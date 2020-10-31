async function readToTable(){
    document.getElementById("row-data").innerText = "";
    await fetch('/adm/restapi/users')
        .then(response => response.json())
        .then(users => {
            users.forEach(
                user =>
                {
                    let roles = Array.from(user.roles)
                        .map(role => role.name.substring(5)).join(', ');

                    let rowStr = $('<tr>' +
                        '<td>' + user.id +       '</td>' +
                        '<td>' + user.username + '</td>' +
                        '<td>' + user.password + '</td>' +
                        '<td>' + roles +         '</td>' +
                        '<td> <a class="btn btn-outline-info" data-user-id=' + user.id + ' data-method="edit" data-toggle="modal" data-target="#showModal">Edit </a></td>' +
                        '<td> <a class="btn btn-outline-danger" data-user-id=' + user.id + ' data-method="delete"  data-toggle="modal" data-target="#showModal">Delete</a> </td>'
                    );
                    $('#row-data').append(rowStr);
                }
            )
        });
}




function readToModal(id){
    $('#editID').val(id);

    let urlFoFetch = '/adm/restapi/users/' + id;
    fetch(urlFoFetch)
        .then(response => response.json())
        .then(user => {
            $('#editUserName').val(user.username);
            $('#editPassword').val(user.password);
            user.roles.forEach(
                role =>
                {
                    let roleName = role.name.substring(5);
                    console.log(roleName);
                    let selectorQuery = '#editRoleSelector option:contains(' + roleName + ')';
                    $(selectorQuery).prop('selected', true);
                })

        })
}




async function addUser(){
    const username = $('#nu-name').val();
    const password = $('#nu-password').val();
    const roles = getSelectedItems(document.getElementById("nu-select"));

    // Подготовка JSON запроса
    const jsonString = JSON.stringify({
        name: username,
        password: password,
        roles: roles
    });
    console.log(jsonString);

    let urlFoFetch = '/adm/restapi/users';
    await fetch(urlFoFetch, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json;charset=utf-8'
        },
        body: jsonString
    });
    await readToTable();
}







async function editUser(id) {
    const username = $('#editUserName').val();
    const password = $('#editPassword').val();
    const roles = getSelectedItems(document.getElementById("editRoleSelector"));

    // Подготовка JSON запроса
    var jsonString = JSON.stringify({
        name: username,
        password: password,
        roles: roles

    });
    console.log(jsonString);
    let urlFoFetch = '/adm/restapi/users/' + id;
    await fetch(urlFoFetch, {
        method: 'PUT',
        headers: {
            'Content-Type': 'application/json;charset=utf-8'
        },
        body: jsonString
    });
}

function deleteUser(id){
    let urlFoFetch = '/adm/restapi/users/' + id;
    fetch(urlFoFetch, {
        method: 'DELETE'
    });
}

function modalCmdButtonHandler(){
    const cmdType = $("#modal-cmd-button").data('method');
    const userId = $("#editID").val();
    (cmdType == 'edit') ? editUser(userId) : deleteUser(userId);
}




function readNavBarData() {
    let urlFoFetch = '/adm/restapi/current-user';
    fetch(urlFoFetch)
        .then(response => response.json())
        .then(user => {
            console.log(user);
            $('#user-name-on-navbar').html(user.name);
            let roles = user.roles.map(item => item.name.substring(5)).join(', ');
            $('#user-roles-on-navbar').html(roles);

            let rowStr = $('<tr>' +
                '<td>' + user.id + '</td>' +
                '<td>' + user.name + '</td>' +
                '<td>' + roles + '</td>'
            );
            $('#user-info_table').append(rowStr);
        })
}




jQuery(function () {
    // чтение всей таблицы
    readToTable();
    readNavBarData();


    // при закрытии модалки читаем данные в таблицу
    $("#showModal").on('hidden.bs.modal', function (event) {
        readToTable();
    })

    // при открытии модалки заполняем данными прокликанного юзера
    $("#showModal").on('show.bs.modal', function (event) {
        let userId = $(event.relatedTarget).data('user-id');
        let method = $(event.relatedTarget).data('method');
        const href = "/adm/users/" + userId + "/" + method;
        $.get(href, function (data) {
            $('#showModal').empty();
            $('#showModal').html(data);
            (readToModal(userId));
        });
    });
})

function getSelectedItems(select){
    return Array.from(select.options)
        .filter(item => item.selected)
        .map(item => ({name: 'ROLE_' + item.value}))
}