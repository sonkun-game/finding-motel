var profileInstance = new Vue({
    el: '#profile-user-form',
    data: {
        gender: "",
        userInfo: {},
    },
    beforeMount(){
        this.userInfo = JSON.parse(localStorage.getItem("userInfo"))
        this.gender = this.userInfo.gender ? "1" : "0";
    },
    mounted(){

    },
    methods: {
        saveUserInfo(){
            this.userInfo.gender = this.gender == "1" ? true : false
            fetch("/api-save-user-info", {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify(this.userInfo),
            }).then(response => response.json())
                .then((data) => {
                    if(data != null && data.msgCode == "user000"){
                        localStorage.setItem("userInfo", JSON.stringify(this.userInfo))
                        authenticationInstance.userInfo = this.userInfo
                        basicInfoInstance.userInfo = this.userInfo
                        this.showNotifyModal()
                    }else if(data != null && data.msgCode == "sys999"){
                        alert("failed")
                    }
                }).catch(error => {
                console.log(error);
            })
        },
        showNotifyModal(){
            let modal_save = document.getElementById("my-modal-notification")
            modal_save.style.display = "block";
            setTimeout(() => modal_save.style.display = "none", 2000);
            window.onclick = function(event) {
                if (event.target == modal_save) {
                    modal_save.style.display = "block";
                }
            }
        },
        showModalChangePhone(){
            let modal_phone = document.getElementById("my-modal-phone");
            let span_phone = modal_phone.getElementsByClassName("close")[0];
            modal_phone.style.display = "block";
            span_phone.onclick = function() {
                modal_phone.style.display = "none";
            }
            window.onclick = function(event) {
                if (event.target == modal_phone) {
                    modal_phone.style.display = "block";
                }
            }
        }
    }


})
var basicInfoInstance = new Vue({
    el: '#basic-user-info',
    data: {
        userInfo: {},
    },
    beforeMount(){
        this.userInfo = JSON.parse(localStorage.getItem("userInfo"))
    }

})