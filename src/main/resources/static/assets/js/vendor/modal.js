
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
        if (event.target.id == modal) {
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
// // Get the modal
// var modal = document.getElementById("myModal");
//
// // Get the button that opens the modal
// var btn = document.getElementById("myBtn");
//
// // Get the <span> element that closes the modal
// var span = document.getElementsByClassName("close")[0];
//
// // When the user clicks the button, open the modal
// btn.onclick = function() {
//     modal.style.display = "block";
// }
//
// // When the user clicks on <span> (x), close the modal
// span.onclick = function() {
//     modal.style.display = "none";
// }
//
// // When the user clicks anywhere outside of the modal, close it
// window.onclick = function(event) {
//     if (event.target == modal) {
//         modal.style.display = "none";
//     }
// }
