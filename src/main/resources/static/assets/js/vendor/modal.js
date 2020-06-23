
// Get the modal
function modal_report() {
    var modal = document.getElementById("myModal");
    var btn = document.getElementById("myBtn");
    var span = document.getElementsByClassName("close")[0];
    btn.onclick = function() {
        modal.style.display = "block";
    }
    span.onclick = function() {
        modal.style.display = "none";
    }
    window.onclick = function(event) {
        if (event.target == modal) {
            modal.style.display = "block";
        }
    }
}

function modal_view() {
    var modal = document.getElementById("myModal_view");
    var icon = document.getElementById("myIcon-view");
    var span = document.getElementsByClassName("close")[0];
    icon.onclick = function() {
        modal.style.display = "block";
    }
    span.onclick = function() {
        modal.style.display = "none";
    }
    window.onclick = function(event) {
        if (event.target == modal) {
            modal.style.display = "block";
        }
    }
}

function modal_delete() {
    var modal_delete = document.getElementById("myModal_delete");
    var icon_delete = document.getElementById("myIcon");
    var btn_cancel = document.getElementById("btn-cancel");
    var btn_confirm = document.getElementById("btn-confirm");
    var span_delete = document.getElementsByClassName("close")[0];
    icon_delete.onclick = function() {
        modal_delete.style.display = "block";
    }
    span_delete.onclick = function() {
        modal_delete.style.display = "none";
    }
    window.onclick = function(event) {
        if (event.target == modal_delete) {
            modal_delete.style.display = "block";
        }
    }
}