var profileInstance = new Vue({
    el: '#user-manager-content',
    data: {
        gender: "",
        userInfo: {},
        inputPhoneNum: "",
        inputOtp: "",
        otpCode: "123",
        otpRemainCount: 5,
        message: "",
        showMsg: false,
        task: 0,
        oldPassword: "",
        newPassword: "",
        rePassword: "",
        // OTP
        disableInputPhone : false,
        displayTimer : null,
        intervalID : null,
        expireDate : 0,
        validateMessage : "",
    },
    beforeMount(){
        this.userInfo = JSON.parse(sessionStorage.getItem("userInfo"))
        this.gender = this.userInfo.gender ? "1" : "0";
        this.task = sessionStorage.getItem("task")
        authenticationInstance.isShowBtn = false
    },
    mounted(){

    },
    methods: {
        validateInput(inputValue, require, minLength, maxLength, min, max, inputName, unit){
            if(require && (inputValue == null || inputValue.length == 0)){
                this.validateMessage = "<b>" + inputName + "</b> không được để trống"
                return false
            }else if(maxLength != null && inputValue != null && inputValue.length > maxLength){
                this.validateMessage = "<b>" + inputName + "</b> không vượt quá " + maxLength + " ký tự"
                return false
            }else if(minLength != null && inputValue != null && inputValue.length < minLength){
                this.validateMessage = "<b>" + inputName + "</b> phải có ít nhất " + minLength + " ký tự"
                return false
            }else if(min != null && inputValue != null && parseFloat(inputValue) <= min){
                this.validateMessage = "<b>" + inputName + "</b> phải lớn hơn " + authenticationInstance.formatNumberToDisplay(min) + " " + unit
                return false
            }else if(max != null && inputValue != null && parseFloat(inputValue) >= max){
                this.validateMessage = "<b>" + inputName + "</b> phải nhỏ hơn " + authenticationInstance.formatNumberToDisplay(max) + " " + unit
                return false
            }else {
                return true
            }
        },
        validateDate(inputDate){
            let dob = new Date(inputDate)
            let currentDate = new Date()
            if(Number.isNaN(dob.getTime()) || currentDate.getTime() <= dob.getTime()){
                this.validateMessage = "Ngày sinh không hợp lệ"
                this.showMsg = true
                return false
            }else {
                this.showMsg = false
                return true
            }
        },
        saveUserInfo(){
            this.userInfo.gender = this.gender == "1" ? true : false
            if(this.validateInput(this.userInfo.displayName, false, null, 50, null, null, "Tên hiển thị")
                && this.validateDate(this.userInfo.dob)
                && this.validateInput(this.userInfo.career, false, null, 30, null, null, "Nghề nghiệp")
                ){
                this.showMsg = false
            }else {
                this.showMsg = true
                return
            }
            fetch("/api-save-user-info", {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify(this.userInfo),
            }).then(response => response.json())
                .then((data) => {
                    if(data != null && data.msgCode == "user000"){
                        sessionStorage.setItem("userInfo", JSON.stringify(this.userInfo))
                        authenticationInstance.userInfo = JSON.parse(sessionStorage.getItem("userInfo"))
                        basicInfoInstance.userInfo = JSON.parse(sessionStorage.getItem("userInfo"))
                        authenticationInstance.showModalNotify("Cập nhật thành công", 2000)
                    }else if(data != null && data.msgCode == "sys999"){
                        alert("failed")
                    }
                }).catch(error => {
                console.log(error);
            })
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
            if (this.inputPhoneNum == null || this.inputPhoneNum.length == 0) {
                this.showMsg = true
                this.message = "Vui lòng nhập số điện thoại"
            } else {
                if (this.inputPhoneNum.length == 10) {
                    this.showMsg = false
                    fetch("/api-send-otp?phoneNumber="+this.inputPhoneNum+ "&siteCode=1", {
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
                            }else if(data != null && data.CodeResult == "99"
                            && data.ErrorMessage.indexOf("Phone not valid") != -1){
                                this.disableInputPhone = false
                                this.clearTimer()
                                this.showMsg = true
                                this.message = "Số điện thoại không hợp lệ"
                            }else {
                                this.clearTimer()
                                this.disableInputPhone = false
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
                    this.displayTimer = 'Mã xác thực đã hết hạn, vui lòng gửi lại mã'
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
                                this.savePhoneNumber()
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
                        sessionStorage.setItem("userInfo", JSON.stringify(this.userInfo))
                        authenticationInstance.showModalNotify("Cập nhật thành công", 2000)
                        let modal_phone = document.getElementById("my-modal-phone");
                        setTimeout(() => {
                            modal_phone.style.display = "none";
                            this.clearTimer()
                            this.disableInputPhone = false
                            this.inputOtp = ""
                        }, 2000);
                    }else if(data != null && data.msgCode == "sys999"){
                        alert("failed")
                    }
                }).catch(error => {
                console.log(error);
            })
        },
        changePassword(){
            if (!this.showMsg){
                this.userInfo.password = this.oldPassword
                this.userInfo.newPassword = this.newPassword
                fetch("/api-change-password", {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json',
                    },
                    body: JSON.stringify(this.userInfo),
                }).then(response => response.json())
                    .then((data) => {
                        if(data != null && data.msgCode != "user000"){
                            this.showMsg = true
                            this.message = data.message
                        }else{
                            this.userInfo.havePassword = true
                            userTaskInstance.userInfo.havePassword = true
                            authenticationInstance.userInfo.havePassword = true
                            sessionStorage.setItem("userInfo", JSON.stringify(this.userInfo))
                            authenticationInstance.showModalNotify("Cập nhật thành công", 2000)
                            setTimeout(() => {
                                this.task = 0
                                userTaskInstance.task = 0
                                this.oldPassword = ""
                                this.newPassword = ""
                                this.rePassword = ""
                            }, 2000);

                        }
                    }).catch(error => {
                    console.log(error);
                })
            }
        },
        checkPassword : function(event){
            if(event.target.id == "old-password" && this.oldPassword.length == 0){
                this.showMsg = true
                this.message = "Vui lòng nhập mật khẩu cũ"
                return
            }else if(event.target.id == "new-password"  && this.newPassword.length == 0){
                this.showMsg = true
                this.message = "Vui lòng nhập mật khẩu mới"
                return
            }else if(event.target.id == "re-password"  && this.rePassword.length == 0){
                this.showMsg = true
                this.message = "Vui lòng nhập lại mật khẩu mới"
                return
            }

            if(this.newPassword.length > 0){
                let message = authenticationInstance.validatePassword(this.newPassword)
                if(message != "valid"){
                    this.showMsg = true
                    this.message = message
                }else{
                    if(this.newPassword == this.oldPassword){
                        this.showMsg = true
                        this.message = "Mật khẩu mới trùng với mật khẩu hiện tại của bạn"
                    }else if(this.rePassword.length > 0 && this.newPassword != this.rePassword){
                        this.showMsg = true
                        this.message = "Mật khẩu nhập lại không khớp"
                    }else {
                        this.showMsg = false
                    }
                }
            }

        }

    }


})
var basicInfoInstance = new Vue({
    el: '#basic-user-info',
    data: {
        userInfo: {},
    },
    beforeMount() {
        this.userInfo = JSON.parse(sessionStorage.getItem("userInfo"))
    },
    methods: {
        handlePostNewRoom() {
            if (this.userInfo.banned) {
                modalMessageInstance.message = "Tài khoản của bạn bị tạm khóa đến " + this.userInfo.unBanDate + "</br>" +
                    "Tất cả bài đăng sẽ bị ẩn " + "</br>" +
                    "Chức năng Đăng Tin và Nạp Tiền bị khóa";
                modalMessageInstance.showModal()
            } else {
                sessionStorage.setItem("task", 13)
                window.location.href = "/dang-tin"
            }
        },
        handlePayment() {
            if (this.userInfo.banned) {
                modalMessageInstance.message = "Tài khoản của bạn bị tạm khóa đến " + this.userInfo.unBanDate + "</br>" +
                    "Tất cả bài đăng sẽ bị ẩn " + "</br>" +
                    "Chức năng Đăng Tin và Nạp Tiền bị khóa";
                modalMessageInstance.showModal()
            } else {
                sessionStorage.setItem("task", 8);
                window.location.href = "/nap-tien"
            }
        }
    }

})
var router = new VueRouter({
    mode: 'history',
    routes: [],
});
var userTaskInstance = new Vue({
    router,
    el: '#user-task',
    data: {
        userInfo: {},
        task: 0,
    },
    beforeMount(){
        this.userInfo = JSON.parse(sessionStorage.getItem("userInfo"))
        this.task = sessionStorage.getItem("task")
    },
    methods: {
        activeBtn : function (task) {
            this.task = task
            profileInstance.task = task
            sessionStorage.setItem("task", task)

            if(task == 0 || task == 1){
                if(this.$route.fullPath.includes("quan-ly-tai-khoan")){
                    let profileUser = document.getElementById("user-manager-content")
                    profileUser.classList.remove("invisible")
                    let renterManager = document.getElementById("renter-manager")
                    if(renterManager != null){
                        renterInstance.task = task
                    }
                    let adminManager = document.getElementById("dataTable")
                    if(adminManager != null){
                        admin.task = task
                    }
                    let landlordManager = document.getElementById("landlord-manager")
                    if(landlordManager != null){
                        noteInstance.task = task
                        landlordInstance.task = task
                    }
                }else{
                    window.location.href = "/quan-ly-tai-khoan"
                }
            }else{
                let profileUser = document.getElementById("user-manager-content")
                profileUser.classList.add("invisible")
                if(task == 12){
                    authenticationInstance.logout()
                }else if(task == 9){
                    if(this.$route.fullPath.includes("quan-ly-he-thong")){
                        admin.task = task
                        admin.inputRole = 0
                        admin.searchUser()
                    }else{
                        window.location.href = "/quan-ly-he-thong"
                    }

                }else if(task == 20){
                    if(this.$route.fullPath.includes("quan-ly-he-thong")){
                        admin.task = task
                        admin.inputRole = 2
                        admin.searchUser()
                    }else{
                        window.location.href = "/quan-ly-he-thong"
                    }

                }else if(task == 10){
                    if(this.$route.fullPath.includes("quan-ly-he-thong")){
                        admin.task = task
                        admin.searchPost()
                    }else{
                        window.location.href = "/quan-ly-he-thong"
                    }

                }else if(task == 11){
                    if(this.$route.fullPath.includes("quan-ly-he-thong")){
                        admin.task = task
                        admin.searchReport()
                        admin.getInitAdmin()
                    }else{
                        window.location.href = "/quan-ly-he-thong"
                    }

                }else if(task == 19){
                    if(this.$route.fullPath.includes("quan-ly-he-thong")){
                        admin.task = task
                        admin.getAllPaymentPackage()
                    }else{
                        window.location.href = "/quan-ly-he-thong"
                    }

                }else if(task == 3){
                    renterInstance.task = task
                    renterInstance.getWishlist()
                }else if(task == 2){
                    renterInstance.task = task
                    renterInstance.searchRentalRequest();
                }else if(task == 13){
                    noteInstance.task = task
                    landlordInstance.task = task
                    landlordInstance.getInitNewPost()
                }else if(task == 6){
                    if(this.$route.fullPath.includes("quan-ly-tai-khoan")){
                        noteInstance.task = task
                        landlordInstance.task = task
                        landlordInstance.getHistoryPayment()
                    }else{
                        window.location.href = "/quan-ly-tai-khoan"
                    }
                }else if(task == 7){
                    if(this.$route.fullPath.includes("quan-ly-bai-dang")){
                        noteInstance.task = task
                        landlordInstance.task = task
                        landlordInstance.getHistoryPaymentPost()
                    }else{
                        window.location.href = "/quan-ly-bai-dang"
                    }
                }
                else if(task == 4){
                    if(this.$route.fullPath.includes("quan-ly-bai-dang")){
                        noteInstance.task = task
                        landlordInstance.task = task
                        landlordInstance.viewListPost()
                        landlordInstance.getInitNewPost()
                    }else{
                        window.location.href = "/quan-ly-bai-dang"
                    }
                }else if(task == 5){
                    if(this.$route.fullPath.includes("quan-ly-bai-dang")){
                        noteInstance.task = task
                        landlordInstance.task = task
                        landlordInstance.getListRoomRequest(7, null)
                    } else {
                        window.location.href = "/quan-ly-bai-dang"
                    }
                } else if (task == 8) {
                    if (this.$route.fullPath.includes("quan-ly-tai-khoan")) {
                        noteInstance.task = task
                        landlordInstance.task = task
                    } else {
                        window.location.href = "/quan-ly-bai-dang"
                    }
                }
            }
        }
    }

})

