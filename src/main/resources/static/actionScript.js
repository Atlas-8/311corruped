
let adminRole = {id:'1', name:'ROLE_ADMIN'};
let userRole = {id:'2', name:'ROLE_USER'};

function saveUser() {

    console.log('button clicked');

    let user = {

        username: $('#newUserForm #username').val(),
        password: $('#newUserForm #password').val(),
        name:     $('#newUserForm #name').val(),
        email:    $('#newUserForm #email').val(),
        adress:   $('#newUserForm #adress').val(),
        roles:    [],

    };

    if ( $('#userRoles').val().length === 2) {
        user.roles = [adminRole, userRole];
    } else if ($('#newUserForm #userRoles').val().indexOf('Admin') !== -1) {
        user.roles = [adminRole];
    } else {
        user.roles = [userRole];
    }

    console.log(user);

    fetch('http://localhost:8080/saveUser', {
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json'
        },
        method: 'POST',
        body: JSON.stringify(user),
    })
        .catch(function (error) {
            console.log('Request failed', error)
        });

    setTimeout(table, 500);
    alert('New user accepted')
}

function updateUser(){

    console.log('button clicked');

    let user = {

        id:       $('#editModal #modalEdit_id').val(),
        username: $('#editModal #modalEdit_username').val(),
        password: $('#editModal #modalEdit_password').val(),
        name:     $('#editModal #modalEdit_name').val(),
        email:    $('#editModal #modalEdit_email').val(),
        adress:   $('#editModal #modalEdit_adress').val(),
        roles:    [],

    };

    if ( $('#modalEdit_role').val().length === 2 ) {
        user.roles = [adminRole, userRole];
    } else if ($('#editModal #modalEdit_role').val().indexOf('Admin') !== -1) {
        user.roles = [adminRole];
    } else if ($('#editModal #modalEdit_role').val().indexOf('User') !== -1) {
        user.roles = [userRole];
    }

    fetch('http://localhost:8080/editUserAction/', {
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json'
        },
        method: 'POST',
        body: JSON.stringify(user),
    })
        .then(function (response) {
            return response.json();
        })
        .then(function (json) {
            console.log('Request successful');
            console.log(json);
        })
        .catch(function (error) {
            console.log('Request failed', error)
        });

}