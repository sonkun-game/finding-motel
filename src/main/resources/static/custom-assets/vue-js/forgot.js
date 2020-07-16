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
    },
    methods: {
        checkOTP() {
            this.checkExistPhone()
            if(this.showMsg){
                return;
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
                this.stepScreen = 2
                this.inputOtp = ""
                this.inputPhoneNum = ""
                // this.otpCode = ""
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
            if (this.inputPhoneNum == null || this.inputPhoneNum.length == 0) {
                this.showMsg = true
                this.message = "Vui lòng nhập số điện thoại"
            } else {
                if (this.inputPhoneNum.length == 10) {
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