var router = new VueRouter({
    mode: 'history',
    routes: []
});

var registerVue = new Vue({
    router,
    el: "#registerForm",
    data: {
        //flag
        matchPwd: true,
        matchOTP: true,
        existedPhone: false,
        existedUsername: false,
        //message
        message: null,
        //parameter
        username: null,
        displayName: null,
        password: null,
        confirmPassword: null,
        phone: null,
        otpCode: null,
        role: null,
        ggAccount: null,
        fbAccount: null,
        //response
        otp: 123,
        //request
        registerModel: {},
        //class css
        invisible: 'invisible',
        border_error: 'border_error',
        errorText: 'errorText',
        //error
        textContent: "concac",
        isShowContent: false,
        isShowLoader: false,
        phoneRegex: /^0[1-9][0-9]{8}$/,
        validPhone: false,
    },
    created() {
        let code = this.$route.query.code
        let url = this.$route.fullPath + ""
        if ((code != null || code != undefined) && url.includes("google")) {
            let apiUrl = "/api-get-google-login?code=" + code
            this.getLogin(apiUrl, "google")
        } else if ((code != null || code != undefined) && url.includes("facebook")) {
            let apiUrl = "/api-get-facebook-login?code=" + code
            this.getLogin(apiUrl, "facebook")
        }

    },
    methods: {
        checkMatchPwd: function (e) {
            return this.password == this.confirmPassword ? this.matchPwd = true : this.matchPwd = false;
        },
        checkOTP() {
            return this.otp == this.otpCode ? this.matchOTP = true : this.matchOTP = false;
        },
        showWaitLoading() {
            this.isShowContent = false;
            this.isShowLoader = true;
        },
        showErrorNotify(msg) {
            this.textContent = msg;
            this.isShowContent = true;
        },
        checkExited(boolean, msg) {
            setTimeout(() => {
                this.isShowLoader = false;
                if (boolean) {
                    this.showErrorNotify(msg);
                } else {
                    this.isShowContent = false;
                }
            }, 1000);
            return boolean;
        },
        isValidUsername() {
            if (this.username != null && this.username.length !== 0) {
                if (this.username.length < 6) {
                    this.showErrorNotify("Tên đăng nhập phải có ít nhất 6 kí tự!");
                } else {
                    this.showWaitLoading();
                    fetch("/check-existed-username", {
                        method: 'POST',
                        headers: {
                            'Content-Type': 'application/json',
                        },
                        body: this.username,
                    }).then(response => response.json())
                        .then((data) => {
                            //if not existed return
                            return !this.checkExited(data, "Tên đăng nhập đã được sử dụng!");
                        }).catch(error => {
                        console.log(error);
                    })
                }
            } else {
                this.showErrorNotify("Tên đăng nhập không được để trống!");
                return false;
            }
        },
        isValidPhone() {
            if (this.phone != null && this.phone.length != 0) {
                if (!this.phoneRegex.test(this.phone)) {
                    this.showErrorNotify("Hãy nhập đúng số điện thoại! Phải có 10 kí tự và là định dạng số.")
                } else {
                    this.showWaitLoading();
                    fetch("/check-existed-phone", {
                        method: 'POST',
                        headers: {
                            'Content-Type': 'application/json',
                        },
                        body: this.phone,
                    }).then(response => response.json())
                        .then((data) => {
                            this.validPhone = !this.checkExited(data, "Số điện thoại đã được sử dụng!");
                            return this.validPhone;
                        }).catch(error => {
                        console.log(error);
                    })
                }
            } else {
                this.showErrorNotify("Số điện thoại không được để trống!");
                return false;
            }

        },
        validRegister: function () {
            let registerModel = {
                "username": this.username,
                "role": this.role,
                "fbAccount": this.fbAccount,
                "ggAccount": this.ggAccount,
                "phoneNumber": this.phone,
                "password": this.password,
                "displayName": this.displayName,
            };
            if (this.isValidUsername() && this.isValidPhone() && this.checkOTP() && this.checkMatchPwd()) {
                console.log(JSON.stringify(registerModel));
                loadingInstance.isHidden = false
                document.body.setAttribute("class", "loading-hidden-screen")
                fetch("/register", {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json',
                    },
                    body: JSON.stringify(registerModel)
                }).then(response => response.json())
                    .then((data) => {
                        console.log(data);
                        if (data.code == '001') {
                            sessionStorage.setItem("registeredUsername", this.username);
                            window.location.href = "/dang-nhap";
                        } else if (data.code == '002') {
                            let accessToken = sessionStorage.getItem("accessToken")
                            let apiUrl = "/api-get-google-profile?accessToken=" + accessToken
                            this.getLogin(apiUrl, "google")
                        } else if (data.code == '003') {
                            let accessToken = sessionStorage.getItem("accessToken")
                            let apiUrl = "/api-get-facebook-profile?accessToken=" + accessToken
                            this.getLogin(apiUrl, "facebook")
                        }
                    }).catch(error => {
                    console.log(error);
                })
            }
        },
        sendOTP() {
            this.smsSendUrl = "https://rest.esms.vn/MainService.svc/json/SendMultipleMessage_V4_get?" +
                "ApiKey=A64092B4036FCBE98DC11D133598BA&SecretKey=4EB8AA82ED932ADD24FB776E928BFE&SmsType=2&Brandname=Verify";
            this.smsSendUrl += "&Phone=" + this.phone;//get from screen
            this.smsSendUrl += "&Content=Ma OTP cua ban la: " + this.otp;//get from screen
            fetch(this.smsSendUrl, {
                method: 'GET'
            }).then(response => response.json())
                .then((data) => {
                    this.smsResponse = data;
                    if (data.CodeResult == 100) {
                        this.showErrorNotify("Mã OTP đã được gửi! Nếu đợi quá lâu xin thử lại.");
                    } else {
                        this.showErrorNotify(data.ErrorMessage + "");
                    }
                })
        },
        getOTP() {
            if (this.validPhone) {
                fetch("/api/get-otp?otpLength=6", {
                    method: 'POST'
                })
                    .then(response => response.json())
                    .then((data) => {
                        this.otp = data;
                        this.sendOTP();
                    }).catch(error => {
                    console.log(error);
                })
            } else {
                this.isValidPhone();
            }
        },
        getLogin(url, tokenProvider) {
            fetch(url, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                }
            }).then(response => response.json())
                .then((data) => {
                    console.log(data)
                    if (data != null && data.msgCode === "msg003") {
                        sessionStorage.setItem("newSocialUser", data.user)
                        sessionStorage.setItem("accessToken", data.accessToken)
                        this.displayName = data.user.displayName
                        if (tokenProvider == "google") {
                            this.ggAccount = data.user.ggAccount
                        } else {
                            this.fbAccount = data.user.fbAccount
                        }
                        preloaderInstance.isShowLoader = false

                    } else if (data != null && data.msgCode === "msg004") {
                        sessionStorage.removeItem("accessToken")
                        this.$cookies.set("access_token", data.accessToken)
                        this.$cookies.set("token_provider", tokenProvider)
                        window.location.href = "/"
                    }
                }).catch(error => {
                console.log(error);
            })
        }
    },
    computed: {
        fillDataForm() {

        }
    }
});

var loadingInstance = new Vue({
    el: '#loading-wrapper',
    data: {
        isHidden: true
    },

});
var preloaderInstance = new Vue({
    el: '#preloader-register-social',
    data: {
        isShowLoader: true,
    },

})