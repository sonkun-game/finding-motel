var profileInstance = new Vue({
    el: '#profile-user-form',
    data: {
        gender: "",
        userInfo: {},
        inputPhoneNum: "",
        inputOtp: "",
        otpCode: "123",
        otpRemainCount: 5,
        message: "",
        showMsg: false,
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
    },
    methods: {
        logout() {
            authenticationInstance.logout()
        },
        activeBtn : function (task) {
            // var header = document.getElementById("user-task");
            // var btns = header.getElementsByClassName("button-information");
            this.task = task
            if(task == 9){

            }
            // let currentBtn = event.target
            // currentBtn.classList.add('active')

            // for (var i = 0; i < btns.length; i++) {
            //     btns[i].addEventListener("click", function() {
            //         var current = document.getElementsByClassName("active");
            //         current[0].className = current[0].className.replace(" active", "");
            //         this.className += " active";
            //     });
            // }
        }
    }

})
