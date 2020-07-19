var authenticationInstance = new Vue({
    el: '#header',
    data: {
        userInfo: {},
        authenticated: false,
        task: 0,
        isShowBtn: true,
    },
    methods: {
        logout(){
            fetch("https://localhost:8081/api-logout",{
                method : 'POST',
                headers: {
                    'Content-Type': 'application/json'
                }
            }).then(response => response.json())
                .then((data) => {
                    console.log(data)
                    if(data != null && data.code === "msg001"){
                        localStorage.removeItem("userInfo")
                        this.$cookies.remove("access_token")
                        this.$cookies.remove("token_provider")
                        window.location.href = "https://localhost:8081/"
                    }
                }).catch(error => {
                console.log(error);
            })
        },
        validatePassword(password){
            if(password == null || password.length == 0){
                return null
            }
            let condition = {
                message: "Mật khẩu phải chứa tối thiểu 8 kí tự, bao gồm chữ thường, chữ hoa, chữ số và kí tự đặc biệt",
                regex: /^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[*.!@$%^&_-|]).{8,}$/
            }
            if(!condition.regex.test(password)){
                return condition.message
            }
            return "valid"
        },
        getTaskPage(task){
            localStorage.setItem("task", task)
            if(task == 13){
                if(this.userInfo.banned){
                    modalMessageInstance.message = "Tài khoản của bạn bị tạm khóa đến " + this.userInfo.unBanDate + "</br>" +
                        "Tất cả bài đăng sẽ bị ẩn " + "</br>" +
                        "Chức năng Đăng Tin và Nạp Tiền bị khóa";
                    modalMessageInstance.showModal()
                }else{
                    window.location.href = "dang-tin"
                }

            }else if(task == 0 || task == 1){
                window.location.href = "quan-ly-tai-khoan"
            }

        },
        formatNumberToDisplay(number){
            if (number != null){
                return number.toString().replace(/(\d)(?=(\d{3})+(?!\d))/g, '$1.')
            }
        },
        showModalNotify(msg, time) {
            document.getElementById("my-modal-notification").style.display = 'block';
            document.getElementById("modalNotifyMessage").innerHTML = msg;
            document.body.setAttribute("class", "loading-hidden-screen")
            setTimeout(function () {
                document.body.removeAttribute("class")
                document.getElementById("my-modal-notification").style.display = 'none';
            }, time);
        },

    },
    mounted(){
        // if(localStorage.getItem("userInfo")){
        //     this.userInfo = JSON.parse(localStorage.getItem("userInfo"))
        //     if(this.userInfo.role == "RENTER"){
        //         this.authenticationNum = 1;
        //     }else if(this.userInfo.role == "LANDLORD"){
        //         this.authenticationNum = 2;
        //     }else{
        //         this.authenticationNum = 3;
        //     }
        //     return
        // }
        let accessToken = this.$cookies.get("access_token")
        let tokenProvider = this.$cookies.get("token_provider")
        fetch("https://localhost:8081/api-get-authentication",{
            method : 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': 'Bearer ' + accessToken,
                'Token-Provider': tokenProvider
            }
        }).then(response => response.json())
            .then((data) => {
                console.log(data)
                if(data != null && data.userInfo != null){
                    this.userInfo = data.userInfo
                    localStorage.setItem("userInfo", JSON.stringify(data.userInfo))
                    this.authenticated = true

                    if(this.userInfo.banned && (document.referrer.includes("/dang-nhap")
                        || document.referrer.includes("/dang-ky")
                        || document.referrer.includes("/facebook?code=")
                        || document.referrer.includes("/google?code="))){
                        modalMessageInstance.message = "Tài khoản của bạn bị tạm khóa đến " + this.userInfo.unBanDate + "</br>" +
                            "Tất cả bài đăng sẽ bị ẩn " + "</br>" +
                            "Chức năng Đăng Tin và Nạp Tiền bị khóa";
                        modalMessageInstance.showModal()
                    }
                }else {
                    localStorage.removeItem("userInfo")
                }
            }).catch(error => {
            console.log(error);
        })
    }
})
var modalMessageInstance = new Vue({
    el: '#message-modal',
    data: {
        userInfo: {},
        message: "",
    },
    beforeMount(){
        this.userInfo = JSON.parse(localStorage.getItem("userInfo"))
    },
    methods : {
        closeModal(){
            document.getElementById("message-modal").style.display = 'none';
            document.body.removeAttribute("class")
        },
        showModal(){
            document.getElementById("message-modal").style.display = 'block';
            document.body.setAttribute("class", "loading-hidden-screen")
        }
    }

})

var modalConfirmInstance = new Vue({
    el: '#confirm-modal',
    data: {
        userInfo: {},
        messageConfirm: "",
        confirm : false,
    },
    beforeMount(){
        this.userInfo = JSON.parse(localStorage.getItem("userInfo"))
    },
    methods : {
        closeModal(){
            document.getElementById("confirm-modal").style.display = 'none';
            document.body.removeAttribute("class")
        },
        showModal(){
            document.getElementById("confirm-modal").style.display = 'block';
            document.body.setAttribute("class", "loading-hidden-screen")
        },
        yesNoConfirmClick(event) {
            document.body.removeAttribute("class")
            document.getElementById("confirm-modal").style.display = 'none';
            let modalConfirmClick = event.target.value;
            if (modalConfirmClick != null && modalConfirmClick.length > 0 && modalConfirmClick == '1') {
                let confirmAction = sessionStorage.getItem("confirmAction")
                if(confirmAction == "send-report"){
                    postDetailInstance.sendReport()
                }else if(confirmAction == "delete-post"){
                    landlordInstance.deletePost()
                }else if(confirmAction == "hide-post"){
                    landlordInstance.changeStatusPost()
                }else if(confirmAction == "accept-request"){
                    landlordInstance.acceptRentalRequest()
                }else if(confirmAction == "reject-request"){
                    landlordInstance.rejectRentalRequest()
                }else if(confirmAction == "change-status-room"){
                    landlordInstance.changeRoomStatus()
                }
                sessionStorage.removeItem("confirmAction")
            }
        }
    },

})