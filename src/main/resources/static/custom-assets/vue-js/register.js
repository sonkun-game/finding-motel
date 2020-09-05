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
        textContent: "",
        isShowMsg: false,
        isShowLoader: false,
        //otp validate
        disableInputPhone: false,
        displayTimer: null,
        intervalID: null,
        expireDate: 0,
        //pwd valid
        passwordRegex: /^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?^\w\S).[^\s]{5,}$/,
        usernameRegex: /^(?=.*?^\w)[^\s\W]{6,}$/,
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
    mounted(){
        authenticationInstance.hidePreloader()
    },
    methods: {
        checkDisplayName() {
            if (this.displayName != null && this.displayName.length > 0) {
                return true;
            }
            this.showErrorNotify("Vui lòng nhập tên hiển thị.")
            return false;
        },
        checkRole() {
            if (this.role != null && this.role.length > 0) {
                return true;
            }
            this.showErrorNotify("Vui lòng chọn vai trò.")
            return false;
        },
        showWaitLoading() {
            this.isShowMsg = false;
            this.isShowLoader = true;
        },
        showErrorNotify(msg) {
            this.textContent = msg;
            this.isShowMsg = true;
        },
        checkExited(boolean, msg) {
            setTimeout(() => {
                this.isShowLoader = false;
                if (boolean) {
                    this.showErrorNotify(msg);
                } else {
                    this.isShowMsg = false;
                }
            }, 0);
            return boolean;
        },
        isValidUsername(isRegister) {
            if (this.username != null && this.username.length !== 0) {
                if (!this.usernameRegex.test(this.username)) {
                    this.showErrorNotify("Tên đăng nhập phải chứa ít nhất 6 kí tự và là chữ, số hoặc kí tự gạch dưới.");
                } else {
                    if (isRegister == null) {
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
                                return !this.checkExited(data, "Tên đăng nhập đã được sử dụng.");
                            }).catch(error => {
                            console.log(error);
                        })
                    } else {
                        return true;
                    }
                }
            } else {
                this.showErrorNotify("Vui lòng nhập tên đăng nhập.");
                return false;
            }
        },
        isValidPhone(isRegister) {
            if (this.phone != null && this.phone.length != 0) {
                if (isRegister == null) {
                    this.showWaitLoading();
                    fetch("/check-existed-phone", {
                        method: 'POST',
                        headers: {
                            'Content-Type': 'application/json',
                        },
                        body: this.phone,
                    }).then(response => response.json())
                        .then((data) => {
                            return !this.checkExited(data, "Số điện thoại đã được sử dụng.");
                        }).catch(error => {
                        console.log(error);
                    })
                } else {
                    return true;
                }
            } else {
                this.showErrorNotify("Vui lòng nhập số điện thoại.");
                return false;
            }

        },
        isValidPassword() {
            if (this.password != null && this.password.length > 0) {
                if (this.passwordRegex.test(this.password)) {
                    this.isShowMsg = false;
                    return true;
                } else {
                    this.showErrorNotify("Mật khẩu phải chứa tối thiểu 6 kí tự, bao gồm chữ thường, chữ hoa, chữ số và không có kí tự khoảng trắng.")
                }
            } else {
                this.showErrorNotify("Vui lòng nhập mật khẩu.")
            }
            return false;
        },
        isValidConfirmPassword() {
            if (this.confirmPassword != null && this.confirmPassword.length > 0) {
                if (this.isValidPassword()) {
                    if (this.password == this.confirmPassword) {
                        return true;
                    } else {
                        this.showErrorNotify("Nhập lại mật khẩu và mật khẩu không trùng nhau.")
                    }
                }
            } else {
                this.showErrorNotify("Vui lòng nhập lại mật khẩu.")
            }

            return false;
        },
        clearTimer() {
            clearTimeout(this.intervalID)
            this.displayTimer = null
        },
        countDown() {
            var duration = 5 * 60000;
            var minutes, seconds, milliseconds;
            this.intervalID = setTimeout(() => {
                var date = Date.now();
                duration = this.expireDate - date;

                minutes = ((duration - duration % 60000) / 60000 < 10) ? "0" + (duration - duration % 60000) / 60000 : (duration - duration % 60000) / 60000 + "";
                milliseconds = duration % 60000;
                seconds = ((milliseconds - milliseconds % 1000) / 1000 < 10) ? "0" + (milliseconds - milliseconds % 1000) / 1000 : (milliseconds - milliseconds % 1000) / 1000 + "";

                this.displayTimer = 'Đã gửi một mã xác thực, mã hết hiệu lực sau <b>' + seconds + '</b> giây';
                // this.displayTimer = seconds + 's'
                requestAnimationFrame(this.countDown)
                if (duration <= 0) {
                    clearTimeout(this.intervalID)
                    this.displayTimer = 'Mã xác thực đã hết hiệu lực, vui lòng gửi lại mã'
                    this.disableInputPhone = false
                }
            }, 1000)
        },
        sentOTP() {
            if (this.phone == null || this.phone.length == 0) {
                this.showErrorNotify("Vui lòng nhập số điện thoại");
            } else {
                if (this.phone.length == 10) {
                    fetch("/api-send-otp?phoneNumber=" + this.phone + "&siteCode=1", {
                        method: 'POST'
                    })
                        .then(response => response.json())
                        .then((data) => {
                            if (data != null && data.code == "001") {
                                this.clearTimer()
                                this.showErrorNotify(data.message);
                            } else if (data != null && data.CodeResult == "100") {
                                // this.disableInputPhone = true
                                this.expireDate = Date.now() + 1 * 60000
                                this.countDown()
                            } else if (data != null && data.CodeResult == "99") {
                                this.clearTimer()
                                // this.showErrorNotify(data.ErrorMessage)
                                if (data.ErrorMessage.indexOf("Phone not valid") >= 0) {
                                    this.showErrorNotify("Số điện thoại không hợp lệ.")
                                }
                            } else {
                                this.clearTimer()
                                this.showErrorNotify("Chưa gửi được tin nhắn, Vui lòng bấm <b>Gửi mã</b> để gửi lại");
                            }
                        })
                } else {
                    this.showErrorNotify("Số điện thoại không hợp lệ");
                }
            }

        },
        validDataRegister() {
            if (this.isValidUsername(true) && this.checkDisplayName() && this.checkRole()
                && this.isValidPassword() && this.isValidConfirmPassword() && this.isValidPhone(true)) {
                this.validateOTPAndRegister();
            }
        },
        validDataRegisterSocial() {
            if (this.isValidUsername(true) && this.checkDisplayName() && this.checkRole()
                && this.isValidPhone(true)) {
                this.validateOTPAndRegister();
            }
        },
        validateOTPAndRegister() {
            if (this.otpCode == null || this.otpCode.length == 0) {
                this.showErrorNotify("Vui lòng nhập mã xác thực.")
            } else {
                if (this.otpCode.length == 6) {
                    this.isShowMsg = false;
                    fetch("/api-validate-otp?phoneNumber=" + this.phone +
                        "&inputOTP=" + this.otpCode, {
                        method: 'POST'
                    })
                        .then(response => response.json())
                        .then((data) => {
                            if (data != null && data.code == "000") {
                                this.clearTimer()
                                this.disableInputPhone = false
                                this.otpCode = ""
                                this.sentRegister();
                            } else if (data != null && data.code == "001") {
                                this.showErrorNotify(data.message)
                            } else if (data != null && data.code == "002") {
                                this.clearTimer()
                                this.showErrorNotify(data.message)
                            }
                        })
                } else {
                    this.showErrorNotify("Mã xác thực không hợp lệ")
                }
            }
        },
        sentRegister: function () {
            let registerModel = {
                "username": this.username,
                "role": this.role,
                "fbAccount": this.fbAccount,
                "ggAccount": this.ggAccount,
                "phoneNumber": this.phone,
                "password": this.password,
                "displayName": this.displayName,
            };
            this.isShowMsg = false;
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