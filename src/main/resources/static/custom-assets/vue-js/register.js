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
    },
    created(){
        let code = this.$route.query.code
        let url = this.$route.fullPath + ""
        if((code != null || code != undefined) && url.includes("google")){
            let apiUrl = "https://localhost:8081/api-get-google-login?code="+code
            this.getLogin(apiUrl, "google")
        }else if((code != null || code != undefined) && url.includes("facebook")){
            let apiUrl = "https://localhost:8081/api-get-facebook-login?code="+code
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
        isExistUsername() {
            if (this.username != null && this.username.length !== 0) {
                fetch("https://localhost:8081/check-existed-username", {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json',
                    },
                    body: this.username,
                }).then(response => response.json())
                    .then((data) => {
                        this.existedUsername = data;
                    }).catch(error => {
                    console.log(error);
                })
            } else {
                this.existedUsername = false;
            }

            return this.existedUsername;

        },
        isExistPhone() {
            if (this.phone != null && this.phone.length != 0) {
                fetch("https://localhost:8081/check-existed-phone", {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json',
                    },
                    body: this.phone,
                }).then(response => response.json())
                    .then((data) => {
                        this.existedPhone = data
                    }).catch(error => {
                    console.log(error);
                })
            } else {
                this.existedPhone = false;
            }
            return this.existedPhone;
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
            if (this.checkMatchPwd() && this.checkOTP() && !this.isExistUsername() && !this.isExistPhone()) {
                console.log(JSON.stringify(registerModel));
                loadingInstance.isHidden = false
                document.body.setAttribute("class", "loading-hidden-screen")
                fetch("https://localhost:8081/register", {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json',
                    },
                    body: JSON.stringify(registerModel)
                }).then(response => response.json())
                    .then((data) => {
                        console.log(data);
                        if (data.code == '001') {
                            localStorage.setItem("registeredUsername", this.username);
                            window.location.href = "/dang-nhap";
                        } else if (data.code == '002') {
                            let accessToken = localStorage.getItem("accessToken")
                            let apiUrl = "https://localhost:8081/api-get-google-profile?accessToken="+accessToken
                            this.getLogin(apiUrl, "google")
                        } else if (data.code == '003') {
                            let accessToken = localStorage.getItem("accessToken")
                            let apiUrl = "https://localhost:8081/api-get-facebook-profile?accessToken="+accessToken
                            this.getLogin(apiUrl, "facebook")
                        }
                    }).catch(error => {
                    console.log(error);
                })
            }
        },
        sendOTP() {
            this.smsSendUrl = "http://rest.esms.vn/MainService.svc/json/SendMultipleMessage_V4_get?" +
                "ApiKey=A64092B4036FCBE98DC11D133598BA&SecretKey=4EB8AA82ED932ADD24FB776E928BFE&SmsType=2&Brandname=Verify";
            this.smsSendUrl += "&Phone=" + this.phone;//get from screen
            this.smsSendUrl += "&Content=Ma OTP cua ban la: " + this.otp;//get from screen
            fetch(this.smsSendUrl, {
                method: 'GET'
            }).then(response => response.json())
                .then((data) => {
                    this.smsResponse = data;
                })
        },
        getOTP() {
            if (this.phone == null || this.phone.length == 0) {
                alert("Hãy nhập số điện thoại!");
            } else {
                if (this.phone.length == 10) {
                    fetch("/api/get-otp?otpLength=6", {
                        method: 'POST'
                    })
                        .then(response => response.json())
                        .then((data) => {
                            this.otp = data;
                            this.sendOTP();
                        })
                } else {
                    alert("Hãy nhập số điện thoại chính xác!");
                }
            }
        },
        getLogin(url, tokenProvider){
            fetch(url,{
                method : 'POST',
                headers: {
                    'Content-Type': 'application/json'
                }
            }).then(response => response.json())
                .then((data) => {
                    console.log(data)
                    if(data != null && data.msgCode === "msg003"){
                        localStorage.setItem("newSocialUser", data.user)
                        localStorage.setItem("accessToken", data.accessToken)
                        this.displayName = data.user.displayName
                        if(tokenProvider == "google"){
                            this.ggAccount = data.user.ggAccount
                        }else{
                            this.fbAccount = data.user.fbAccount
                        }
                        preloaderInstance.isShowLoader = false

                    }else if (data != null && data.msgCode === "msg004"){
                        localStorage.removeItem("accessToken")
                        this.$cookies.set("access_token", data.accessToken)
                        this.$cookies.set("token_provider", tokenProvider)
                        window.location.href = "https://localhost:8081/"
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