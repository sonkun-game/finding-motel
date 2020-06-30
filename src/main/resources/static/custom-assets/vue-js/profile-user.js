var profileInstance = new Vue({
    el: '#user-manager-content',
    data: {
        gender: "",
        userInfo: {},
        inputPhoneNum: "",
        inputOtp: "",
        otpCode: "123",
        otpRemainCount: 5,
        message: "",
        showMsg: false,
        task: 0,
        oldPassword: "",
        newPassword: "",
        rePassword: "",
    },
    beforeMount(){
        this.userInfo = JSON.parse(localStorage.getItem("userInfo"))
        this.gender = this.userInfo.gender ? "1" : "0";
        this.task = localStorage.getItem("task")
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
                        authenticationInstance.userInfo = JSON.parse(localStorage.getItem("userInfo"))
                        basicInfoInstance.userInfo = JSON.parse(localStorage.getItem("userInfo"))
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
        },
        sendOTP() {
            this.smsSendUrl = "http://rest.esms.vn/MainService.svc/json/SendMultipleMessage_V4_get?" +
                "ApiKey=A64092B4036FCBE98DC11D133598BA&SecretKey=4EB8AA82ED932ADD24FB776E928BFE&SmsType=2&Brandname=Verify";
            this.smsSendUrl += "&Phone=" + this.inputPhoneNum;
            this.smsSendUrl += "&Content=Ma OTP cua ban la: " + this.otpCode;
            fetch(this.smsSendUrl, {
                method: 'GET'
            }).then(response => response.json())
                .then((data) => {
                    this.smsResponse = data;
                    this.otpRemainCount = 5
                })
        },
        getOTP() {
            if (this.phone == null || this.phone.length == 0) {
                this.showMsg = true
                this.message = "Hãy nhập số điện thoại"
            } else {
                if (this.phone.length == 10) {
                    this.showMsg = false
                    fetch("/api/get-otp?otpLength=6", {
                        method: 'POST'
                    })
                        .then(response => response.json())
                        .then((data) => {
                            this.otpCode = data;
                            this.sendOTP();
                        })
                } else {
                    this.showMsg = true
                    this.message = "Số điện thoại không hợp lệ"
                }
            }

        },
        checkExistPhone() {
            if (this.inputPhoneNum != null && this.inputPhoneNum.length != 0) {
                let phone = this.inputPhoneNum
                fetch("https://localhost:8081/check-existed-phone", {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json',
                    },
                    body: phone,
                }).then(response => response.json())
                    .then((data) => {
                        if(data != null && data == true){
                            this.showMsg = true
                            this.message = "Số điện thoại của bạn đã được đăng ký"
                        }else{
                            this.showMsg = false
                        }
                    }).catch(error => {
                    console.log(error);
                })
            } else {
                this.showMsg = true
                this.message = "Vui lòng nhập số điện thoại"
            }
        },
        checkOTP() {
            if(this.inputPhoneNum == null || this.inputPhoneNum.length == 0){
                this.showMsg = true
                this.message = "Vui lòng nhập mã số điện thoại"
                return
            }
            if(this.inputOtp == null || this.inputOtp.length == 0){
                this.showMsg = true
                this.message = "Vui lòng nhập mã OTP"
                return
            }
            this.otpRemainCount--
            if(this.otpCode != this.inputOtp && this.otpRemainCount > 0){
                this.showMsg = true
                this.message = "Mã OTP không hợp lệ, bạn còn lại "+this.otpRemainCount+" lần nhập lại mã"
            }else if(this.otpCode != this.inputOtp && this.otpRemainCount <= 0){
                this.showMsg = true
                this.message = "Mã OTP đã hết hạn, bấm gửi mã để nhận mã mới"
            }else {
                this.showMsg = false
                this.savePhoneNumber()
            }
        },
        savePhoneNumber(){
            this.userInfo.phoneNumber = this.inputPhoneNum
            fetch("/api-change-phone-number", {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify(this.userInfo),
            }).then(response => response.json())
                .then((data) => {
                    if(data != null && data.msgCode == "user000"){
                        localStorage.setItem("userInfo", JSON.stringify(this.userInfo))
                        this.showNotifyModal()
                        let modal_phone = document.getElementById("my-modal-phone");
                        setTimeout(() => modal_phone.style.display = "none", 2000);
                    }else if(data != null && data.msgCode == "sys999"){
                        alert("failed")
                    }
                }).catch(error => {
                console.log(error);
            })
        },
        changePassword(){
            if (!this.showMsg){
                this.userInfo.password = this.oldPassword
                this.userInfo.newPassword = this.newPassword
                fetch("/api-change-password", {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json',
                    },
                    body: JSON.stringify(this.userInfo),
                }).then(response => response.json())
                    .then((data) => {
                        if(data != null && data.msgCode != "user000"){
                            this.showMsg = true
                            this.message = data.message
                        }else{
                            this.showNotifyModal()
                            setTimeout(() => {
                                this.task = 0
                                userTaskInstance.task = 0
                                this.oldPassword = ""
                                this.newPassword = ""
                                this.rePassword = ""
                            }, 2000);

                        }
                    }).catch(error => {
                    console.log(error);
                })
            }
        },
        checkPassword : function(event){
            if(event.target.id == "old-password" && this.oldPassword.length == 0){
                this.showMsg = true
                this.message = "Vui lòng nhập mật khẩu cũ"
                return
            }else if(event.target.id == "new-password"  && this.newPassword.length == 0){
                this.showMsg = true
                this.message = "Vui lòng nhập mật khẩu mới"
                return
            }else if(event.target.id == "re-password"  && this.rePassword.length == 0){
                this.showMsg = true
                this.message = "Vui lòng nhập lại mật khẩu mới"
                return
            }

            if(this.newPassword.length > 0){
                let message = authenticationInstance.validatePassword(this.newPassword)
                if(message != "valid"){
                    this.showMsg = true
                    this.message = message
                }else{
                    if(this.newPassword == this.oldPassword){
                        this.showMsg = true
                        this.message = "Mật khẩu mới trùng với mật khẩu hiện tại của bạn"
                    }else if(this.rePassword.length > 0 && this.newPassword != this.rePassword){
                        this.showMsg = true
                        this.message = "Mật khẩu nhập lại không khớp"
                    }else {
                        this.showMsg = false
                    }
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

var userTaskInstance = new Vue({
    el: '#user-task',
    data: {
        userInfo: {},
        task: 0,
    },
    beforeMount(){
        this.userInfo = JSON.parse(localStorage.getItem("userInfo"))
        this.task = localStorage.getItem("task")
    },
    methods: {
        activeBtn : function (task) {
            this.task = task
            profileInstance.task = task
            localStorage.setItem("task", task)

            if(task == 0 || task == 1){
                let profileUser = document.getElementById("user-manager-content")
                profileUser.classList.remove("invisible")
                let renterManager = document.getElementById("renter-manager")
                if(renterManager != null){
                    renterInstance.task = task
                }
                let adminManager = document.getElementById("dataTable")
                if(adminManager != null){
                    admin.task = task
                }
            }else{
                let profileUser = document.getElementById("user-manager-content")
                profileUser.classList.add("invisible")
                if(task == 12){
                    authenticationInstance.logout()
                }else if(task == 9){
                    admin.task = task
                    admin.getListUser()
                }else if(task == 10){
                    admin.task = task
                    admin.getListPost()
                }else if(task == 11){
                    admin.task = task
                    admin.getListReport()
                }else if(task == 3){
                    renterInstance.task = task
                    renterInstance.getWishlist()
                }else if(task == 2){
                    renterInstance.task = task
                }
            }


        }
    }

})

