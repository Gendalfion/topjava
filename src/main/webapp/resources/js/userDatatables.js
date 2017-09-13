var ajaxUrl = "ajax/admin/users/";
var datatableApi;

var updateUserEnabled = function (id) {
    var enabled = $("#" + id + " input[type=checkbox]").is(":checked");
    $.ajax({
        type: "POST",
        url: ajaxUrl + id + "/enabled/" + enabled,
        success: function () {
            $("#" + id).toggleClass( "disabledStyle", !enabled );
            successNoty("User " + (enabled ? "enabled" : "disabled"));
        }
    });
};

// $(document).ready(function () {
$(function () {
    datatableApi = $("#datatable").DataTable({
        "paging": false,
        "info": true,
        "columns": [
            {
                "data": "name"
            },
            {
                "data": "email"
            },
            {
                "data": "roles"
            },
            {
                "data": "enabled"
            },
            {
                "data": "registered"
            },
            {
                "defaultContent": "Edit",
                "orderable": false
            },
            {
                "defaultContent": "Delete",
                "orderable": false
            }
        ],
        "order": [
            [
                0,
                "asc"
            ]
        ]
    });
    makeEditable();
});