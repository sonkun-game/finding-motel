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

function modal_phone() {
    var modal_phone = document.getElementById("myModal_phone");
    var btn_phone = document.getElementById("myBtn-phone");
    var span_phone = document.getElementsByClassName("close")[0];
    btn_phone.onclick = function() {
        modal_phone.style.display = "block";
    }
    span_phone.onclick = function() {
        modal_phone.style.display = "none";
    }
    window.onclick = function(event) {
        if (event.target == modal_phone) {
            modal_phone.style.display = "block";
        }
    }

}
