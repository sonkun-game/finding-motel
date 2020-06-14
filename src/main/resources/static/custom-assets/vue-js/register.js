var registVue = new Vue({
    el: "#registerForm",
    data: {
        //flag
        matchPwd: true,
        //parameter
        userName: null,
        displayName: null,
        password: null,
        confirmPassword: null,
        phone: null,
        otpCode: null,
        role: null,
        //response
        otp: null,
        //request
        registerModel: [],
        //class css
        invisible: 'invisible',
        border_error: 'border_error',
        errorText: 'errorText',
    },
    methods: {
        addRegisterModel: function () {
            this.registerModel = [];
            this.registerModel.push({
                username: this.userName,
                displayName: this.displayName,
                password: this.password,
                phone: this.phone,
                role: this.role,
            });
            console.log(this.registerModel);
        },
        checkMatchPwd: function (e) {
            if (this.password.match(this.confirmPassword)) {
                this.matchPwd = true;
            } else {
                this.matchPwd = false;
            }
            return this.matchPwd;
        },
        validRegister() {
            if (this.userName === null ||
                this.displayName === null ||
                this.password === null ||
                this.confirmPassword === null ||
                this.phone === null ||
                this.otpCode === null ||
                this.role === null) {
                alert("Some field null, Check again!");
            } else if (this.checkMatchPwd) {
                this.addRegisterModel();
                fetch("/validRegister", {
                    method: 'POST',
                    body: this.registerModel,
                })
                    .then(response => response.json())
                    .then((data) => {
                        console.log(data);
                    })
            }
        },
        sendOTP() {
            this.smsSendUrl = "http://rest.esms.vn/MainService.svc/json/SendMultipleMessage_V4_get?" +
                "ApiKey=A64092B4036FCBE98DC11D133598BA&SecretKey=4EB8AA82ED932ADD24FB776E928BFE&SmsType=8";
            this.smsSendUrl += "&Phone=" + this.phone;//get from screen
            this.smsSendUrl += "&Content=Ma OTP cua ban la: " + this.otpCode;//get from screen
            fetch(this.smsSendUrl, {
                method: 'GET'
            }).then(response => response.json())
                .then((data) => {
                    this.smsResponse = data;
                })
        },
        getOTP() {
            fetch("/api/get-otp", {
                method: 'POST'
            })
                .then(response => response.json())
                .then((data) => {
                    this.otp = data;
                    this.sendOTP();
                })
        }
    },
    computed: {

    }
})
