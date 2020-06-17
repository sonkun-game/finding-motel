var router = new VueRouter({
    mode: 'history',
    routes: []
});
var registVue = new Vue({
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
    beforeMount() {
        if (document.getElementById('fbAccount') != null
            && document.getElementById('fbAccount').value.length != 0) {
            this.fbAccount = document.getElementById('fbAccount').value;
        }
        if (document.getElementById('ggAccount') != null
            && document.getElementById('ggAccount').value.length != 0) {
            this.ggAccount = document.getElementById('ggAccount').value;
        }
        if (document.getElementById('displayName') != null
            && document.getElementById('displayName').value.length != 0) {
            this.displayName = document.getElementById('displayName').value;
        }
    },
    methods: {
        checkMatchPwd: function (e) {
            return this.password == this.confirmPassword ? this.matchPwd = true : this.matchPwd = false;
        },
        checkOTP() {
            return this.otp == this.otpCode ? this.matchOTP = true : this.matchOT = false;
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
                        this.existedPhone = data;
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
                            let accessToken = this.$route.query.accessToken
                            this.$cookies.set("access_token", accessToken)
                            window.location.href = this.$route.fullPath;
                        } else if (data.code == '003') {
                            let accessToken = this.$route.query.accessToken
                            this.$cookies.set("access_token", accessToken)
                            window.location.href = this.$route.fullPath;
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
        }
    },
    computed: {
        fillDataForm() {

        }
    }
})
