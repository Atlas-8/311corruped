function getFromAPI(url) {

    var xmlHttp = new XMLHttpRequest();
    xmlHttp.open("GET", url, false); // false for synchronous request
    xmlHttp.send(null);
    if (xmlHttp.responseText) {
        return xmlHttp.responseText;
    }
    return false
}

var modalHTML = '';
var responseStatus = '';

$("#usersListTable").on("click", "button.btn-danger ", function () {
    modalHTML = getFromAPI('/users/deleteUserModal/' + $(this).attr('value'));
    $("#modalDeleteBody form").remove();
    $("#modalDeleteBody").append(modalHTML);
});

$("#deleteModal").on("click", "button.btn-danger", function () {
    console.log('button clicked');
    responseStatus = JSON.parse(getFromAPI('/deleteUserAction/' + $('#deleteModal #modalDelete_id').attr('value')));
    console.log(responseStatus);
    if (responseStatus.status === 'success') {
        $('#modalDelete').modal('dispose');
        location.reload();
        console.log('Deletion complete');
    } else {
        console.log('Response status not success');
    }
});

$("#usersListTable").on("click", "button.btn-info ", function () {
    modalHTML = getFromAPI('/users/getUserModal/' + $(this).attr('value'));
    $("#modalEditBody form").remove();
    $("#modalEditBody").append(modalHTML);
});

$("#editModal").on("click", "button.btn-primary", function () {
    console.log('button clicked');
    updateUser();
    setTimeout(table, 500);
    $("#editModal").modal("hide");
});
