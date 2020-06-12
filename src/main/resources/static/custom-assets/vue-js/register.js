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
        //class css
        invisible: 'invisible',
        border_error: 'border_error',
        errorText: 'errorText',
    },
    methods: {
        checkMatchPwd: function (e) {
            if (this.password.match(this.confirmPassword)) {
                this.matchPwd = true;
            } else {
                this.matchPwd = false;
            }
        },
        validRegister() {

        },
        sendOTP(){
            this.smsSendUrl = "http://rest.esms.vn/MainService.svc/json/SendMultipleMessage_V4_get?" +
                "ApiKey=A64092B4036FCBE98DC11D133598BA&SecretKey=4EB8AA82ED932ADD24FB776E928BFE&SmsType=8";
            this.smsSendUrl += "&Phone="+this.phone;//get from screen
            this.smsSendUrl += "&Content=Ma OTP cua ban la: "+this.otpCode;//get from screen
            fetch(this.smsSendUrl,{
                method : 'GET'
            }).then(response => response.json())
                .then((data) => {
                    this.smsResponse = data;
                })
        },
        getOTP(){
            fetch("http://localhost:8081/api/get-otp?otpLength=6",{
                method : 'POST'
            })
                .then(response => response.json())
                .then((data) => {
                    this.otp = data;
                    this.sendOTP();
                })
        }
    }
})
