var ajaxUrl = "ajax/profile/meals/";
var datatableApi;

function updateTable() {
    $.ajax({
        type: "POST",
        url: ajaxUrl + "filter",
        data: $("#filter").serialize(),
        success: updateTableByData
    });
}

function clearFilter() {
    $("#filter")[0].reset();
    $.get(ajaxUrl, updateTableByData);
}

$(function () {
    datatableApi = $("#datatable").DataTable({
        "ajax": {
            "url": ajaxUrl,
            "dataSrc": ""
        },
        "paging": false,
        "info": true,
        "columns": [
            {
                "data": "dateTime",
                "render": function (date, type, row) {
                    if (type === "display") {
                        return date.substring(0, 16).replace('T', ' ');
                    }
                    return date;
                }
            },
            {
                "data": "description"
            },
            {
                "data": "calories"
            },
            {
                "orderable": false,
                "defaultContent": "",
                "render": renderEditBtn
            },
            {
                "orderable": false,
                "defaultContent": "",
                "render": renderDeleteBtn
            }
        ],
        "order": [
            [
                0,
                "desc"
            ]
        ],
        "createdRow": function (row, data, dataIndex) {
            if (!data.exceed) {
                $(row).addClass("normal");
            } else {
                $(row).addClass("exceeded");
            }
        },
        "initComplete": makeEditable
    });

    initDatetimePickers();

    $('.datetimepicker').datetimepicker({
        format: 'Y-m-d H:i',
        onChangeDateTime: function (format, $input) {
            var isoFmtDateTime = $input.val().replace(' ', 'T');
            $('#dateTimeIsoFormat').val(isoFmtDateTime);
        }
    });

    var updateFormFromRow = updateRow;
    updateRow = function (id) {
        updateFormFromRow(id).then(function () {
            var isoDateTime = $('#dateTimeIsoFormat').val();
            $('#dateTime').val(isoDateTime.substring(0, 16).replace('T', ' '));
        });
    }
});