var authenticationInstance = new Vue({
    el: '#header',
    data: {
        userInfo: {},
        authenticated: false,
        task: 0,
        isShowBtn: true,
        isShowNotification : false,
        notificationNumber : -1,
        listNotification : [],
        firstPageNotifies : -1,
        isLastPageNotify : true,
    },
    methods: {
        logout(){
            fetch("/api-logout",{
                method : 'POST',
                headers: {
                    'Content-Type': 'application/json'
                }
            }).then(response => response.json())
                .then((data) => {
                    console.log(data)
                    if(data != null && data.code === "000"){
                        this.userInfo = null
                        this.authenticated = false
                        sessionStorage.removeItem("userInfo")
                        sessionStorage.removeItem("listPostOfRenter")
                        this.$cookies.remove("access_token")
                        this.$cookies.remove("token_provider")
                        window.location.href = "/"
                    }
                }).catch(error => {
                console.log(error);
            })
        },
        validatePassword(password){
            if(password == null || password.length == 0){
                return null
            }
            let condition = {
                message: "Mật khẩu phải chứa tối thiểu 6 kí tự, bao gồm chữ thường, chữ hoa, chữ số và không có kí tự khoảng trắng.",
                regex: /^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?^\w\S).[^\s]{5,}$/
            }
            if(!condition.regex.test(password)){
                return condition.message
            }
            return "valid"
        },
        getTaskPage(task){
            sessionStorage.setItem("task", task)
            if(task == 13){
                if(this.userInfo.banned){
                    modalMessageInstance.message = "Tài khoản của bạn bị tạm khóa đến " + this.userInfo.unBanDate + "</br>" +
                        "Tất cả bài đăng sẽ bị ẩn " + "</br>" +
                        "Chức năng Đăng Tin và Nạp Tiền bị khóa";
                    modalMessageInstance.showModal()
                }else{
                    window.location.href = "dang-tin"
                }

            }else if(task == 0 || task == 1){
                window.location.href = "quan-ly-tai-khoan"
            }

        },
        formatNumberToDisplay(number){
            if (number != null){
                return number.toString().replace(/(\d)(?=(\d{3})+(?!\d))/g, '$1.')
            }
        },
        showModalNotify(msg, time) {
            document.getElementById("my-modal-notification").style.display = 'block';
            document.getElementById("modalNotifyMessage").innerHTML = msg;
            document.body.setAttribute("class", "loading-hidden-screen")
            setTimeout(function () {
                document.body.removeAttribute("class")
                document.getElementById("my-modal-notification").style.display = 'none';
            }, time);
        },
        showNotifications(){
            if(!this.isShowNotification && this.listNotification.length == 0){
                this.getListNotification()
            }
            document.addEventListener("click", function (event) {
                if (authenticationInstance.isShowNotification){
                    let notificationContainer = document.getElementById("notification-container");
                    let bellContainer = document.getElementById("bell-wrapper")
                    let el = event.target
                    if(notificationContainer != null &&
                        el instanceof Node && !notificationContainer.contains(el)
                        && bellContainer != null && !bellContainer.contains(el)){
                        authenticationInstance.isShowNotification = false
                    }
                }

            })
            this.isShowNotification = !this.isShowNotification
        },
        getNotificationNumber(){
            let request = {
                "username": this.userInfo.username,
            }
            fetch("/api-get-notification-number", {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify(request),
            }).then(response => response.json())
                .then((data) => {
                    if (data.msgCode == "notify000") {
                        this.notificationNumber = data.notificationNumber
                    }
                }).catch(error => {
                console.log(error);
            })
        },
        removeNotifications(){
            let request = {
                "username": this.userInfo.username,
            }
            fetch("/api-remove-notification", {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify(request),
            }).then(response => response.json())
                .then((data) => {
                    if (data != null && data.code == "000") {
                        console.log(data.message)
                    }
                }).catch(error => {
                console.log(error);
            })
        },
        getListNotification(){
            let request = {
                "username": this.userInfo.username,
            }
            this.firstPageNotifies += 1;
            fetch("/api-get-notifications?currentPage=" + this.firstPageNotifies, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify(request),
            }).then(response => response.json())
                .then((data) => {
                    if (data.msgCode == "notify000") {
                        for (let notify of data.listNotification) {
                            this.listNotification.push(notify)
                        }
                        this.isLastPageNotify = data.isLastPage !== undefined && data.isLastPage !== null && data.isLastPage == true ? true : false
                    }
                }).catch(error => {
                console.log(error);
            })
        },
        handleMouseOverProfile(){
            if (authenticationInstance.isShowNotification){
                authenticationInstance.isShowNotification = false
            }
        },
        handleClickNotification(notification, index){
            if(notification.statusNotification.id != 13){
                this.changeNotificationStatus(notification.id, index)
            }
            if(this.userInfo.role == 'LANDLORD'){
                sessionStorage.setItem("task", 17)
                sessionStorage.setItem("notification", JSON.stringify(notification))
                window.location.href = "/quan-ly-bai-dang"
            }else if(this.userInfo.role == 'RENTER'){
                sessionStorage.setItem("task", 18)
                sessionStorage.setItem("notification", JSON.stringify(notification))
                window.location.href = "/quan-ly-tai-khoan"
            }

        },
        changeNotificationStatus(notificationId, index){
            let request = {
                "id": notificationId,
                "username": this.userInfo.username,
            }
            fetch("/api-change-notification-status", {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify(request),
            }).then(response => response.json())
                .then((data) => {
                    if (data.msgCode == "notify000") {
                        this.$set(this.listNotification, index, data.notification)

                    }
                }).catch(error => {
                console.log(error);
            })
        },
        formatDate(rawDate, onlyDate){
            if(rawDate != null){
                let dateFormatString = rawDate.split(".")[0]
                let date = new Date(rawDate)
                if(onlyDate != null && onlyDate){
                    return this.getDateNum(date.getDate()) + "/" + this.getDateNum(date.getMonth() + 1) + "/" + date.getFullYear()
                }
                return this.getDateNum(date.getHours()) + ":" + this.getDateNum(date.getMinutes()) + ":" + this.getDateNum(date.getSeconds())
                    + " " + this.getDateNum(date.getDate()) + "/" + this.getDateNum(date.getMonth() + 1) + "/" + date.getFullYear()
            }
        },
        getDateNum(rawNum){
            return (rawNum < 10) ? "0" + rawNum : rawNum
        },
        getStatusPost(postVisible, postBanned, expireDate){
            let dateFormatString = expireDate.split(".")[0]
            let date = new Date(dateFormatString)
            let currentDate = new Date()
            if(postBanned){
                return "Bị khóa"
            }else if(date.getTime() < currentDate.getTime()){
                return "Hết hạn"
            }else {
                return postVisible ? "Hiển thị" : "Không hiển thị"
            }
        },
        IsExpirePost(expireDate){
            let dateFormatString = expireDate.split(".")[0]
            let date = new Date(dateFormatString)
            let currentDate = new Date()
            if(date.getTime() < currentDate.getTime()){
                return true
            }else {
                return false
            }
        },
        hidePreloader(){
            $('#preloader-active').fadeOut('slow');
        }
    },
    created(){
        // if(sessionStorage.getItem("userInfo")){
        //     this.userInfo = JSON.parse(sessionStorage.getItem("userInfo"))
        //     this.authenticated = true
        //     this.getNotificationNumber()
        //     this.removeNotifications()
        //     return
        // }
        let accessToken = this.$cookies.get("access_token")
        let tokenProvider = this.$cookies.get("token_provider")
        fetch("/api-get-authentication",{
            method : 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': 'Bearer ' + accessToken,
                'Token-Provider': tokenProvider
            }
        }).then(response => response.json())
            .then((data) => {
                console.log(data)
                if(data != null && data.userInfo != null){
                    this.userInfo = data.userInfo
                    if(window.location.href.includes("nap-tien")){
                        basicInfoInstance.userInfo = data.userInfo
                    }
                    sessionStorage.setItem("userInfo", JSON.stringify(data.userInfo))
                    this.authenticated = true
                    this.getNotificationNumber()
                    this.removeNotifications()

                    if(window.location.href.includes("/dang-nhap")){
                        window.location.href = "/"
                    }

                    if(this.userInfo.banned && (document.referrer.includes("/dang-nhap")
                        || document.referrer.includes("/dang-ky")
                        || document.referrer.includes("/facebook?code=")
                        || document.referrer.includes("/google?code="))){
                        modalMessageInstance.message = "Tài khoản của bạn bị tạm khóa đến " + this.userInfo.unBanDate + "</br>" +
                            "Tất cả bài đăng sẽ bị ẩn " + "</br>" +
                            "Chức năng Đăng Tin và Nạp Tiền bị khóa";
                        modalMessageInstance.showModal()
                    }
                }else {
                    sessionStorage.removeItem("userInfo")
                    sessionStorage.removeItem("listPostOfRenter")
                }
            }).catch(error => {
            console.log(error);
        })
    }
})
var modalMessageInstance = new Vue({
    el: '#message-modal',
    data: {
        userInfo: {},
        message: "",
        title: "Thông báo",
    },
    beforeMount(){
        this.userInfo = JSON.parse(sessionStorage.getItem("userInfo"))
    },
    methods : {
        closeModal(){
            document.getElementById("message-modal").style.display = 'none';
            document.body.removeAttribute("class")
        },
        showModal(){
            document.getElementById("message-modal").style.display = 'block';
            document.body.setAttribute("class", "loading-hidden-screen")
        }
    }

})

var modalConfirmInstance = new Vue({
    el: '#confirm-modal',
    data: {
        userInfo: {},
        messageConfirm: "",
        confirm : false,
    },
    beforeMount(){
        this.userInfo = JSON.parse(sessionStorage.getItem("userInfo"))
    },
    methods : {
        closeModal(){
            document.getElementById("confirm-modal").style.display = 'none';
            document.body.removeAttribute("class")
        },
        showModal(){
            document.getElementById("confirm-modal").style.display = 'block';
            document.body.setAttribute("class", "loading-hidden-screen")
        },
        yesNoConfirmClick(event) {
            document.body.removeAttribute("class")
            document.getElementById("confirm-modal").style.display = 'none';
            let modalConfirmClick = event.target.value;
            if (modalConfirmClick != null && modalConfirmClick.length > 0 && modalConfirmClick == '1') {
                let confirmAction = sessionStorage.getItem("confirmAction")
                if(confirmAction == "send-report"){
                    postDetailInstance.sendReport()
                }else if(confirmAction == "delete-post"){
                    landlordInstance.deletePost()
                }else if(confirmAction == "hide-post"){
                    landlordInstance.changeStatusPost()
                }else if(confirmAction == "accept-request"){
                    landlordInstance.acceptRentalRequest()
                }else if(confirmAction == "reject-request"){
                    landlordInstance.rejectRentalRequest()
                }else if(confirmAction == "change-status-room"){
                    landlordInstance.changeRoomStatus()
                }else if(confirmAction == "add-money"){
                    admin.addMoneyForLandlord()
                }
                sessionStorage.removeItem("confirmAction")
            }
        }
    },

})

var processingLoaderInstance = new Vue({
    el: '#processing-loader',
    data: {
        isShowLoader : false,
        displayText : "Đang xử lý",
    },
    methods : {
        hideLoader(){
            this.isShowLoader = false
        },
        showLoader(){
            this.isShowLoader = true
        }
    }

})