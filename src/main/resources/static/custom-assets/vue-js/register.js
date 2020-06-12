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

        }
    }
})
