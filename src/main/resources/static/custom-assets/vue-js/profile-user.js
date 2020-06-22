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
            let modal_save = document.getElementById("myModal_save")
            modal_save.style.display = "block";

            window.onclick = function(event) {
                if (event.target == modal_save) {
                    modal_save.style.display = "block";
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