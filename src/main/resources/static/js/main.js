/*Читаем пользователя вошедшего в систему,
    возвращенный результат (Boolean):
0: обычный юзер
1: ADMIN*/
function readNavBarData() {
    let urlFoFetch = '/api/v1/current-user';
    let userRoles = '';
    fetch(urlFoFetch)
        .then(response => response.json())
        .then(user => {
            $('#user-name-on-navbar').html(user.name);
            userRoles = user.roles.map(item => item.name.substring(5)).join(', ');
            $('#user-roles-on-navbar').html(userRoles);

            let rowStr = $('<tr>' +
                '<td>' + user.id + '</td>' +
                '<td>' + user.name + '</td>' +
                '<td>' + userRoles + '</td>'
            );
            $('#user-info_table').append(rowStr);
        });

    let result = userRoles.indexOf('ADMIN') > -1 ;
    console.log(userRoles);
    console.log(result);
    return true;
}


async function readToTable(){
    document.getElementById("row-data").innerText = "";
    await fetch('/api/v1/users')
        .then(response => response.json())
        .then(users => {
            users.forEach(
                user =>
                {
                    let roles = Array.from(user.roles)
                        .map(role => role.name.substring(5)).join(', ');

                    let rowStr = $(`<tr>
                        <td> ${user.id} </td>
                        <td> ${user.name} </td>
                        <td> ${user.password} </td>
                        <td> ${roles}         </td>
                        <td> <a class="btn btn-outline-info" data-user-id=${user.id} data-method="edit" data-toggle="modal" data-target="#showModal">Edit </a></td>
                        <td> <a class="btn btn-outline-danger" data-user-id=${user.id} data-method="delete"  data-toggle="modal" data-target="#showModal">Delete</a> </td>`
                    );
                    $('#row-data').append(rowStr);
                }
            )
        });
}


async function readAllRolesToSelect(){
    $('#editRoleSelector').innerText = '';

    let urlFetch = '/api/v1/roles';
    await fetch(urlFetch)
        .then(responce => responce.json())
        .then(roles => {
            roles.forEach(
                role => {
                    let optionForRoles = $(`<option id="${role.id}" value="${role.name}">${role.name.substring(5)}</option>`);
                    $('#editRoleSelector').append(optionForRoles);
                }
            )
        })
}

async function readToModal(id){
    await readAllRolesToSelect();

    $('#editID').val(id);

    let urlFoFetch = '/api/v1/users/' + id;
    fetch(urlFoFetch)
        .then(response => response.json())
        .then(user => {
            $('#editUserName').val(user.name);
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

    let urlFoFetch = '/api/v1/users';
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
    console.log(roles);

    // Подготовка JSON запроса
    var jsonString = JSON.stringify({
        id: id,
        name: username,
        password: password,
        roles: roles

    });
    console.log(jsonString);
    let urlFoFetch = '/api/v1/users/' + id;
    await fetch(urlFoFetch, {
        method: 'PUT',
        headers: {
            'Content-Type': 'application/json;charset=utf-8'
        },
        body: jsonString
    });
}




function deleteUser(id){
    let urlFoFetch = '/api/v1/users/' + id;
    fetch(urlFoFetch, {
        method: 'DELETE'
    });
}


function modalCmdButtonHandler(){
    const cmdType = $("#modal-cmd-button").data('method');
    const userId = $("#editID").val();
    (cmdType == 'edit') ? editUser(userId) : deleteUser(userId);
}


jQuery(function () {

    let isAdmin = readNavBarData();

    if (isAdmin) {
        readToTable();
        console.log('Current user roles include role - ADMIN');
    }



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
        .map(item => ({id: item.id,
                       name: 'ROLE_' + item.value}))
}