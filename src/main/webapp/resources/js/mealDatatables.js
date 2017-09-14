var ajaxUrl = "ajax/profile/meals/";
var datatableApi;

var updateAll = updateTable;

var updateTable = function () {
    var startDate = $('#startDate').val();
    var startTime = $('#startTime').val();
    var endDate = $('#endDate').val();
    var endTime = $('#endTime').val();

    if (startDate === "" && startTime === "" && endDate === "" && endTime === "") {
        updateAll();
    } else {
        $.post(ajaxUrl + "filter",
            {
                startDate: startDate,
                startTime: startTime,
                endDate: endDate,
                endTime: endTime
            },
            function (data) {
                datatableApi.clear().rows.add(data).draw();
            });
    }
};

// $(document).ready(function () {
$(function () {
    datatableApi = $("#datatable").DataTable({
        "paging": false,
        "info": true,
        "columns": [
            {
                "data": "dateTime"
            },
            {
                "data": "description"
            },
            {
                "data": "calories"
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
                "desc"
            ]
        ]
    });
    makeEditable();
});