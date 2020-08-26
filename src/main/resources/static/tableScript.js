let url = "/getUsers";

function renderTable(arrObj) {
    arrObj.forEach((row) => {

        let currentRole = '';
        if (row.roles.length === 2) {
            currentRole = 'user, admin';
        } else if (row.roles[0].name === 'ROLE_ADMIN') {
            currentRole = 'admin';
        } else {
            currentRole = 'user';
        }

        let editButton = '<button type="button" class="btn btn-info" data-toggle="modal" ' +
            'data-target="#editModal" value="' + row.id + '"> Edit </button>';

        let deleteButton = '<button type="button" class="btn btn-danger" data-toggle="modal" ' +
            'data-target="#deleteModal" value="' + row.id + '"> Delete </button>';

        $('#testTable').append('<tr><td>' + row.id + '</td><td>'
                                          + row.name + '</td><td>'
                                          + row.email + '</td><td>'
                                          + row.adress + '</td><td>'
                                          + row.username + '</td><td>'
                                          + row.password + '</td><td>'
                                          + currentRole + '</td><td>'
                                          + editButton + '</td><td>'
                                          + deleteButton + '</td></tr>');
    })
}

fetch(url)
    .then(function (response) {
        return response.json();
    })
    .then(function (json) {
        console.log('Request successful');
        console.log(json);
        renderTable(json);
    })
    .catch(function (error) {
        log('Request failed', error)
    });
