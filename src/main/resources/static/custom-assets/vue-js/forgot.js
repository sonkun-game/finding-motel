var forgotInstance = new Vue({
    el: '#forgot-pwd-form',
    data: {
        stepScreen: 1,
        inputPhoneNum: "",
        inputOtp: "",
        otpCode: "123",
        message: "",
        showMsg: false,
        smsResponse: {},
        otpRemainCount: 5,
        inputPassword: "",
        inputRePassword: "",
        // OTP data below
        disableInputPhone : false,
        displayTimer : null,
        intervalID : null,
        expireDate : 0,
    },
    methods: {
        sendOTP() {
            if (this.inputPhoneNum == null || this.inputPhoneNum.length == 0) {
                this.showMsg = true
                this.message = "Vui lòng nhập số điện thoại"
            } else {
                if (this.inputPhoneNum.length == 10) {
                    this.showMsg = false
                    fetch("/api-send-otp?phoneNumber="+this.inputPhoneNum+ "&siteCode=2", {
                        method: 'POST'
                    })
                        .then(response => response.json())
                        .then((data) => {
                            if(data != null && data.code == "001"){
                                this.clearTimer()
                                this.showMsg = true
                                this.message = data.message
                            }else if(data != null && data.CodeResult == "100"){
                                this.disableInputPhone = true
                                this.expireDate = Date.now() + 1 * 60000
                                this.countDown()
                            }else {
                                this.clearTimer()
                                this.showMsg = true
                                this.message = "Chưa gửi được tin nhắn, Vui lòng bấm <b>Gửi mã</b> để gửi lại"
                            }
                        })
                } else {
                    this.showMsg = true
                    this.message = "Số điện thoại không hợp lệ"
                }
            }

        },
        clearTimer(){
            clearTimeout(this.intervalID)
            this.displayTimer = null
        },
        countDown(){
            var duration = 5 * 60000;
            var minutes, seconds, milliseconds;
            this.intervalID = setTimeout(() => {
                 var date = Date.now();
                 duration = this.expireDate - date;

                 minutes = ((duration - duration % 60000) / 60000 < 10) ? "0" + (duration - duration % 60000) / 60000 : (duration - duration % 60000) / 60000 + "";
                 milliseconds = duration % 60000;
                 seconds = ((milliseconds - milliseconds % 1000) / 1000 < 10) ? "0" + (milliseconds - milliseconds % 1000) / 1000 : (milliseconds - milliseconds % 1000) / 1000 + "";

                 this.displayTimer = 'Đã gửi một mã xác thực đến số điện thoại của bạn, mã hết hiệu lực sau <b>' + minutes + ":" + seconds + '</b>';
                 requestAnimationFrame(this.countDown)
                 if(duration <= 0){
                     clearTimeout(this.intervalID)
                     this.displayTimer = 'Mã xác thực đã hết hiệu lực, vui lòng gửi lại mã'
                     this.disableInputPhone = false
                 }
            }, 1000)
        },
        validateOTP() {
            if(this.inputOtp == null || this.inputOtp.length == 0){
                this.showMsg = true
                this.message = "Vui lòng nhập mã xác thực"
                return
            } else {
                if (this.inputOtp.length == 6) {
                    this.showMsg = false
                    fetch("/api-validate-otp?phoneNumber=" + this.inputPhoneNum +
                        "&inputOTP=" + this.inputOtp, {
                        method: 'POST'
                    })
                        .then(response => response.json())
                        .then((data) => {
                            if(data != null && data.code == "000"){
                                this.showMsg = false
                                this.stepScreen = 2
                                this.clearTimer()
                                this.disableInputPhone = false
                                this.inputOtp = ""
                            }else if(data != null && data.code == "001"){
                                this.showMsg = true
                                this.message = data.message
                            }else if(data != null && data.code == "002"){
                                this.clearTimer()
                                this.showMsg = true
                                this.message = data.message
                            }
                        })
                } else {
                    this.showMsg = true
                    this.message = "Mã xác thực không hợp lệ"
                }
            }

        },
        checkExistPhone() {
            if (this.inputPhoneNum != null && this.inputPhoneNum.length != 0) {
                let phone = this.inputPhoneNum
                fetch("/check-existed-phone", {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json',
                    },
                    body: phone,
                }).then(response => response.json())
                    .then((data) => {
                        if(data != null && data == false){
                            this.showMsg = true
                            this.message = "Số điện thoại của bạn chưa được đăng ký"
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
        checkMatchPassword(){
            this.checkPassword();
            if(!this.showMsg){
                if(this.inputPassword != this.inputRePassword){
                    this.showMsg = true
                    this.message = "Vui lòng nhập lại mật khẩu chính xác"
                }else{
                    this.showMsg = false
                }
            }
        },
        checkPassword(){
            let message = authenticationInstance.validatePassword(this.inputPassword)
            if(message == null){
                this.showMsg = true
                this.message = "Vui lòng nhập mật khẩu mới của bạn"
            }else if(message != null && message != "valid"){
                this.showMsg = true
                this.message = message
            }else{
                this.showMsg = false
            }
        },
        saveNewPassword(){
            this.checkMatchPassword();
            if(!this.showMsg){
                loadingInstance.isHidden = false
                document.body.setAttribute("class", "loading-hidden-screen")
                let updateUser = {
                    "phoneNumber": this.inputPhoneNum,
                    "password": this.inputPassword
                }
                fetch("/api-save-new-password", {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json',
                    },
                    body: JSON.stringify(updateUser),
                }).then(response => response.json())
                    .then((data) => {
                        if(data != null && data.msgCode == "forgot000"){
                            this.showMsg = false
                            window.location.href = "/dang-nhap";
                        }else if(data != null && data.msgCode == "sys999"){
                            this.showMsg = true
                            this.message = "Lưu mật khẩu thất bại"
                            loadingInstance.isHidden = true
                            document.body.removeAttribute("class")
                        }
                    }).catch(error => {
                    console.log(error);
                })
            }
        }
    }
})
var loadingInstance = new Vue({
    el: '#loading-wrapper',
    data: {
        isHidden: true
    },

})