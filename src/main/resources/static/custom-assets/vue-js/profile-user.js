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

    },
    beforeMount(){
        this.userInfo = JSON.parse(localStorage.getItem("userInfo"))
        this.gender = this.userInfo.gender ? "1" : "0";
        this.task = localStorage.getItem("task")
        authenticationInstance.isShowBtn = false
    },
    mounted(){

    },
    methods: {
        saveUserInfo(){
            this.userInfo.gender = this.gender == "1" ? true : false
            fetch("/api-save-user-info", {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify(this.userInfo),
            }).then(response => response.json())
                .then((data) => {
                    if(data != null && data.msgCode == "user000"){
                        localStorage.setItem("userInfo", JSON.stringify(this.userInfo))
                        authenticationInstance.userInfo = JSON.parse(localStorage.getItem("userInfo"))
                        basicInfoInstance.userInfo = JSON.parse(localStorage.getItem("userInfo"))
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
            if (this.phone == null || this.phone.length == 0) {
                this.showMsg = true
                this.message = "Hãy nhập số điện thoại"
            } else {
                if (this.phone.length == 10) {
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
        checkOTP() {
            if(this.inputPhoneNum == null || this.inputPhoneNum.length == 0){
                this.showMsg = true
                this.message = "Vui lòng nhập mã số điện thoại"
                return
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
                this.savePhoneNumber()
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
                        localStorage.setItem("userInfo", JSON.stringify(this.userInfo))
                        authenticationInstance.showModalNotify("Cập nhật thành công", 2000)
                        let modal_phone = document.getElementById("my-modal-phone");
                        setTimeout(() => modal_phone.style.display = "none", 2000);
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
                            localStorage.setItem("userInfo", JSON.stringify(this.userInfo))
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
        this.userInfo = JSON.parse(localStorage.getItem("userInfo"))
    },
    methods: {
        handlePostNewRoom() {
            if (this.userInfo.banned) {
                modalMessageInstance.message = "Tài khoản của bạn bị tạm khóa đến " + this.userInfo.unBanDate + "</br>" +
                    "Tất cả bài đăng sẽ bị ẩn " + "</br>" +
                    "Chức năng Đăng Tin và Nạp Tiền bị khóa";
                modalMessageInstance.showModal()
            } else {
                localStorage.setItem("task", 13)
                window.location.href = "dang-tin"
            }
        },
        handlePayment() {
            if (this.userInfo.banned) {
                modalMessageInstance.message = "Tài khoản của bạn bị tạm khóa đến " + this.userInfo.unBanDate + "</br>" +
                    "Tất cả bài đăng sẽ bị ẩn " + "</br>" +
                    "Chức năng Đăng Tin và Nạp Tiền bị khóa";
                modalMessageInstance.showModal()
            } else {
                localStorage.setItem("task", 8);
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
        this.userInfo = JSON.parse(localStorage.getItem("userInfo"))
        this.task = localStorage.getItem("task")
    },
    methods: {
        activeBtn : function (task) {
            this.task = task
            profileInstance.task = task
            localStorage.setItem("task", task)

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
                        admin.getListUser()
                    }else{
                        window.location.href = "/quan-ly-he-thong"
                    }

                }else if(task == 10){
                    if(this.$route.fullPath.includes("quan-ly-he-thong")){
                        admin.task = task
                        admin.getListPost()
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
                        admin.getListPaymentPackage()
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

