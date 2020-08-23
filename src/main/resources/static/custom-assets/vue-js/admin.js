var admin = new Vue({
    el: '#dataTable',
    data: {
        //variable
        listReport: [],
        listPost: [],
        listUser: [],
        //request parameter
        postType: 0,
        postPrice: 0,
        postSquare: 0,
        postDistance: 0,
        postStatus: "",
        postTitleOrLandlord: "",
        isBannedUser: false,
        //modal form
        modalBanDataId: {},
        modalDelId: {},
        modalDelDataType: {},
        modalBanDataType: {},
        modalBanAction: {},
        modalConfirmClick: {},
        modalData: [],
        //user detail form
        userDetail: [],
        task: 0,
        inputLandlordId: "",
        inputRenterId: "",
        inputPostTitle: "",
        listStatusReport: [],
        inputStatusReport: 0,
        userIndex: -1,
        postIndex: -1,
        listPaymentPackage: [],
        inputPackageName: "",
        inputDuration: "",
        inputAmount: 0,
        selectedPaymentPackage: null,
        packageIndex: -1,
        inputSearchUser: "",
        inputRole: 0,
        listRole: [],
        selectedLandlord: {},
        paymentMethod: "",
        reportDetail: "",
        adjustMoneyNote: "",
        pagination: [],
        //regex
        regexCharacterSpace: /[a-zA-Z]|\s/,
        //list filter post
        listTypePost : [],
        listFilterPrice : [],
        listFilterSquare : [],
        listFilterDistance : [],
    },
    beforeMount() {
        this.task = sessionStorage.getItem("task")
    },
    mounted() {
        if (this.task == 9) {
            let profileUser = document.getElementById("user-manager-content")
            profileUser.classList.add("invisible")
            this.inputRole = 0
            this.searchUser()
            this.getAllRole()
        } else if (this.task == 10) {
            let profileUser = document.getElementById("user-manager-content")
            profileUser.classList.add("invisible")
            this.getInitFilterPost();
            this.searchPost();
        } else if (this.task == 11) {
            let profileUser = document.getElementById("user-manager-content")
            profileUser.classList.add("invisible")
            this.searchReport()
            this.getInitAdmin()
        } else if (this.task == 19) {
            let profileUser = document.getElementById("user-manager-content")
            profileUser.classList.add("invisible")
            this.getAllPaymentPackage()
        } else if (this.task == 20) {
            let profileUser = document.getElementById("user-manager-content")
            profileUser.classList.add("invisible")
            this.inputRole = 2
            this.searchUser()
        }
    },
    methods: {
        showModalUserDetail(userId) {
            for (var user of this.listUser) {
                if (user.username == userId) {
                    this.userDetail = user;
                    document.getElementById("modalUserDetail").style.display = 'block';
                    break;
                }
            }
            //close modal
            window.onclick = function (event) {
                if (event.target.id.toString().includes('closeModal')) {
                    document.getElementById("modalUserDetail").style.display = "none";
                }
            }
        },
        yesNoConfirmDelClick(event) {
            document.getElementById("modalDelete").style.display = 'none';
            this.modalConfirmClick = event.target.value;
            if (this.modalConfirmClick != null && this.modalConfirmClick.length > 0 && this.modalConfirmClick == '1') {
                if (this.modalDelDataType == 'report') {
                    this.deleteReport(this.modalDelId);
                } else if (this.modalDelDataType == 'post') {
                    this.deletePost(this.modalDelId);
                }
            }

        },
        showModalConfirmDelete(id, dataType) {
            this.modalDelId = id;
            this.modalDelDataType = dataType;
            if (this.modalDelDataType == 'report') {
                document.getElementById("modalDelContent").innerHTML = 'Bạn có muốn xóa báo cáo này không?';
            } else if (this.modalDelDataType == 'post') {
                document.getElementById("modalDelContent").innerHTML = 'Bạn có muốn xóa bài viết này không?';
            }
            document.getElementById("modalDelete").style.display = 'block';
        },
        yesNoConfirmBanClick(event) {
            document.getElementById("modalBan").style.display = 'none';
            this.modalConfirmClick = event.target.value;
            if (this.modalConfirmClick != null && this.modalConfirmClick.length > 0 && this.modalConfirmClick == '1') {
                if (this.modalBanDataType == 'landlord') {
                    if (this.modalBanAction == 'ban') {
                        this.banLanlord(this.modalBanDataId);
                    } else if (this.modalBanAction == 'unban') {
                        this.unbanLanlord(this.modalBanDataId);
                    }
                } else if (this.modalBanDataType == 'post') {
                    if (this.modalBanAction == 'ban') {
                        this.banPost(this.modalBanDataId);
                    } else if (this.modalBanAction == 'unban') {
                        this.unbanPost(this.modalBanDataId);
                    }
                }
            }

        },
        showModalConfirmBan(id, dataType, action, index, event) {
            this.modalBanDataId = id;
            this.modalBanDataType = dataType;
            this.modalBanAction = action;
            if (event.target.className.indexOf("disable") != -1) {
                return;
            }
            if (dataType == 'post') {
                this.postIndex = index
                if (action == 'ban') {
                    document.getElementById("modalBanContent").innerHTML = 'Bạn có muốn khóa bài đăng này không?';
                } else if (action == 'unban') {
                    document.getElementById("modalBanContent").innerHTML = 'Bạn có muốn mở khóa bài đăng này không?';
                }
            } else if (dataType == 'landlord') {
                this.userIndex = index
                if (action == 'ban') {
                    document.getElementById("modalBanContent").innerHTML = 'Bạn có muốn khóa tài khoản này không?';
                } else if (action == 'unban') {
                    document.getElementById("modalBanContent").innerHTML = 'Bạn có muốn mở khóa tài khoản này không?';
                }
            }
            document.getElementById("modalBan").style.display = 'block';
        },
        searchUser(currentPage) {
            if (currentPage == undefined || !currentPage) currentPage = 0;
            let request = {
                'username': this.inputSearchUser,
                'roleId': parseInt(this.inputRole) == 0 ? null : parseInt(this.inputRole),
            }
            fetch("/api-search-user?currentPage=" + currentPage, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify(request),

            }).then(response => response.json())
                .then((data) => {
                    if (data != null && data.code == "000") {
                        this.listUser = data.data.content;
                        this.pagination = data.pagination;
                    } else {
                        modalMessageInstance.message = data.message;
                        modalMessageInstance.showModal()
                    }

                }).catch(error => {
                console.log(error);
            })
        },
        getAllRole() {
            fetch("/api-get-all-role", {
                method: 'POST',
            }).then(response => response.json())
                .then((data) => {
                    if (data != null && data.code == "000") {
                        this.listRole = data.data
                    } else {
                        modalMessageInstance.message = data.message;
                        modalMessageInstance.showModal()
                    }
                }).catch(error => {
                console.log(error);
            })
        },
        banLanlord(username) {
            fetch("/ban-landlord?username=" + username, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
            }).then(response => response.json())
                .then((data) => {
                    if (data != null && data.code == "000") {
                        authenticationInstance.showModalNotify("Đã khóa 1 tài khoản", 2000);
                        this.searchUser();
                    } else {
                        modalMessageInstance.message = data.message;
                        modalMessageInstance.showModal()
                    }
                }).catch(error => {
                console.log(error);
            })
        },
        unbanLanlord(username) {
            fetch("/unban-landlord?username=" + username, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
            }).then(response => response.json())
                .then((data) => {
                    if (data != null && data.code == "000") {
                        authenticationInstance.showModalNotify("Đã mở khóa 1 tài khoản", 2000);
                        this.searchUser();
                    } else {
                        modalMessageInstance.message = data.message;
                        modalMessageInstance.showModal()
                    }
                }).catch(error => {
                console.log(error);
            })
        },

        deleteReport(id) {
            fetch("/delete-report?reportId=" + id, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
            }).then(response => response.json())
                .then((data) => {
                    if (data != null && data.code == '000') {
                        authenticationInstance.showModalNotify("Đã xóa 1 báo cáo", 2000);
                        this.searchReport();
                    } else {
                        modalMessageInstance.message = data.message
                        modalMessageInstance.showModal()
                    }
                }).catch(error => {
                console.log(error);
            })
        },

        getListPost() {

            fetch("/get-post", {
                method: 'POST',
            }).then(response => response.json())
                .then((data) => {
                    if (data != null && data.code == "000") {
                        this.listPost = data.data;
                    } else {
                        modalMessageInstance.message = data.message;
                        modalMessageInstance.showModal()
                    }
                }).catch(error => {
                console.log(error);
            })
        },

        deletePost(id) {
            fetch("/delete-post?postId=" + id, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
            }).then(response => response.json())
                .then((data) => {
                    if (data.code == "000") {
                        this.searchPost();
                    } else {
                        window.location.href = "/error";
                    }
                }).catch(error => {
                console.log(error);
            })
        },
        isNullSearchParam(param) {
            return param == 0 ? null : param;
        },
        valueSheetData(value, list) {
            if (list == null || !list) return {max: null, min: null};
            return list[value];
        },
        searchPost(currentPage) {
            if (currentPage === undefined || !currentPage) {
                currentPage = 0;
            }
            let postRequestDTO = {
                "title": this.isNullSearchParam(this.postTitleOrLandlord),
                "typeId": this.isNullSearchParam(parseInt(this.postType)),
                "filterPriceId": this.isNullSearchParam(parseInt(this.postPrice)),
                "filterSquareId": this.isNullSearchParam(parseInt(this.postSquare)),
                "filterDistanceId": this.isNullSearchParam(parseInt(this.postDistance)),
                "landlordUsername": this.isNullSearchParam(this.postTitleOrLandlord),
                "visible": this.postStatus == '0' ? false : this.postStatus == '1' ? true : null,
            }
            fetch("/search-post?currentPage=" + currentPage, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify(postRequestDTO),
            }).then(response => response.json())
                .then((data) => {
                    if (data != null && data.code == "000") {
                        this.listPost = data.data.content;
                        this.pagination = data.pagination;
                    } else {
                        modalMessageInstance.message = data.message;
                        modalMessageInstance.showModal()
                    }
                }).catch(error => {
                console.log(error);
            })
        },
        banPost(id) {
            fetch("ban-post?postId=" + id, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
            }).then(response => response.json())
                .then((data) => {
                    if (data != null && data.code == "000") {
                        authenticationInstance.showModalNotify("Đã khóa bài đăng", 2000);
                        this.$set(this.listPost, this.postIndex, data.data)
                    } else {
                        modalMessageInstance.message = data.message;
                        modalMessageInstance.showModal()
                    }
                }).catch(error => {
                console.log(error);
            })
        },

        unbanPost(id) {
            fetch("/api-un-ban-post?postId=" + id, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
            }).then(response => response.json())
                .then((data) => {
                    if (data.code == "000") {
                        this.$set(this.listPost, this.postIndex, data.data)
                    } else {
                        alert("Error" + data.code);
                    }
                }).catch(error => {
                console.log(error);
            })
        },
        searchReport(currentPage) {
            if (currentPage == undefined || !currentPage) currentPage = 0;
            let reportRequestDTO = {
                "landlordId": this.inputLandlordId == "" ? null : this.inputLandlordId,
                "renterId": this.inputRenterId == "" ? null : this.inputRenterId,
                "postTitle": this.inputPostTitle == "" ? null : this.inputPostTitle,
                "statusReport": this.isNullSearchParam(this.inputStatusReport),
            }
            fetch("/search-report?currentPage=" + currentPage, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify(reportRequestDTO),
            }).then(response => response.json())
                .then((data) => {
                    if (data != null && data.code == "000") {
                        this.listReport = data.data.content;
                        this.pagination = data.pagination;
                    } else {
                        modalMessageInstance.message = data.message;
                        modalMessageInstance.showModal()
                    }
                }).catch(error => {
                console.log(error);
            })
        },
        closeModalReportDetail() {
            document.getElementById("modalRepostDetail").style.display = 'none';
            this.reportDetail = "";
            document.body.removeAttribute("class")
        },
        showModalReportDetail(reportId) {
            for (let report of this.listReport) {
                if (report.id == reportId) {
                    this.reportDetail = report;
                    break;
                }
            }
            document.getElementById("modalRepostDetail").style.display = 'block';
            document.body.setAttribute("class", "loading-hidden-screen")
        },
        getInitAdmin() {
            fetch("api-get-init-admin", {
                method: 'POST',
            }).then(response => response.json())
                .then((data) => {
                    if (data != null && data.code == "000") {
                        this.listStatusReport = data.listStatusReport
                    } else {
                        modalMessageInstance.message = data.message;
                        modalMessageInstance.showModal()
                    }

                }).catch(error => {
                console.log(error);
            })
        },
        handleGetReport(post, user) {
            userTaskInstance.task = 11
            sessionStorage.setItem("task", 11)
            this.task = 11
            this.getInitAdmin()
            if (post != null) {
                this.inputLandlordId = post.landlord.username
                this.inputPostTitle = post.title
            } else if (user != null) {
                this.inputLandlordId = user.username
                this.inputPostTitle = ""
            }

            let reportRequestDTO = {
                "landlordId": this.inputLandlordId == "" ? null : this.inputLandlordId,
                "renterId": null,
                "postTitle": this.inputPostTitle == "" ? null : this.inputPostTitle,
                "statusReport": null,
            }
            fetch("/search-report?currentPage=0", {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify(reportRequestDTO),
            }).then(response => response.json())
                .then((data) => {
                    if (data != null && data.code == "000") {
                        let listReport = []
                        for (let report of data.data.content) {
                            if (post != null && (report.statusReport.id == 3 || report.statusReport.id == 5)) {
                                listReport.push(report)
                            } else if (user != null && (report.statusReport.id == 3 || report.statusReport.id == 4)) {
                                listReport.push(report)
                            }
                        }
                        this.listReport = listReport
                        this.pagination = data.pagination
                    } else {
                        modalMessageInstance.message = data.message;
                        modalMessageInstance.showModal()
                    }
                }).catch(error => {
                console.log(error);
            })
        },
        getAllPaymentPackage(currentPage) {
            if (currentPage === undefined || !currentPage) {
                currentPage = 0;
            }
            fetch("/api-get-list-payment-package?currentPage=" + currentPage, {
                method: 'POST',
            }).then(response => response.json())
                .then((data) => {
                    if (data != null && data.code == "000") {
                        this.listPaymentPackage = data.data.content
                        this.pagination = data.pagination;
                    } else {
                        modalMessageInstance.message = data.message;
                        modalMessageInstance.showModal()
                    }

                }).catch(error => {
                console.log(error);
            })
        },
        closeModalPackage() {
            document.getElementById("modalPackage").style.display = 'none';
            document.body.removeAttribute("class")
        },
        showModalPackageError(msg) {
            document.getElementById("modalPackageErrorMsg").innerHTML = msg;
            document.getElementById("modalPackageErrorMsg").style.visibility = 'visible';
        },
        hideModalPackageError() {
            document.getElementById("modalPackageErrorMsg").innerHTML = "";
            document.getElementById("modalPackageErrorMsg").style.visibility = 'hidden';
        },
        validatePackageInputDuration() {
            this.inputDuration = (this.inputDuration + "").trim();
            if (!this.inputDuration || this.inputDuration.length == 0) {
                this.showModalPackageError("Vui lòng nhập thời hạn.");
            } else if (this.regexCharacterSpace.test(this.inputDuration)) {
                this.showModalPackageError("Vui lòng nhập thời hạn hợp lệ.");
            } else {
                for (let package of this.listPaymentPackage) {
                    if (package.duration == this.inputDuration) {
                        this.showModalPackageError("Thời hạn " + package.duration + " tháng đã tồn tại");
                        return false;
                    }
                }
                return true;
            }
            return false;
        },
        validatePackageInputAmount() {
            this.inputAmount = (this.inputAmount + "").trim();
            if (!this.inputAmount || this.inputAmount.length == 0) {
                this.showModalPackageError("Vui lòng nhập giá.");
            } else if (this.regexCharacterSpace.test(this.inputAmount)) {
                this.showModalPackageError("Vui lòng nhập giá hợp lệ.");
            } else {
                for (let package of this.listPaymentPackage) {
                    if (package.amount == this.inputAmount) {
                        this.showModalPackageError("Giá gói " + package.amount + " đã tồn tại");
                        return false;
                    }
                }
                return true;
            }
            return false;
        },
        validatePackageInputName() {
            this.inputPackageName = (this.inputPackageName + "").trim();
            if (!this.inputPackageName || this.inputPackageName.length == 0) {
                this.showModalPackageError("Vui lòng nhập tên gói");
            } else {
                for (let package of this.listPaymentPackage) {
                    if (package.packageName == this.inputPackageName) {
                        this.showModalPackageError("Tên gói " + package.packageName + " đã tồn tại");
                        return false;
                    }
                }
                return true;
            }
            return false;
        },
        showModalPackage(package, index) {
            if (package != null && index != null) {
                this.packageIndex = index
                this.selectedPaymentPackage = package
                this.inputPackageName = package.packageName
                this.inputDuration = package.duration
                this.inputAmount = package.amount
            } else {
                this.selectedPaymentPackage = null
                this.packageIndex = -1
                this.inputPackageName = ""
                this.inputDuration = ""
                this.inputAmount = ""
            }
            document.getElementById("modalPackage").style.display = 'block';
            document.body.setAttribute("class", "loading-hidden-screen")
        },
        savePaymentPackage() {
            let request = {
                "id": this.selectedPaymentPackage == null ? null : this.selectedPaymentPackage.id,
                "duration": this.inputDuration,
                "amount": this.inputAmount,
                "packageName": this.inputPackageName,
            }
            if (this.validatePackageInputName() && this.validatePackageInputDuration() && this.validatePackageInputAmount()) {
                fetch("/api-save-payment-package", {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json',
                    },
                    body: JSON.stringify(request),
                }).then(response => response.json())
                    .then((data) => {
                        if (data != null && data.code == "000") {
                            authenticationInstance.showModalNotify("Cập nhật thành công", 2000)
                            setTimeout(() => {
                                if (this.packageIndex != -1) {
                                    this.$set(this.listPaymentPackage, this.packageIndex, data.data)
                                } else {
                                    // this.listPaymentPackage.push(data.data)
                                    admin.getAllPaymentPackage();
                                }
                                this.closeModalPackage()
                            }, 2000);
                        } else {
                            modalMessageInstance.message = data.message;
                            modalMessageInstance.showModal()
                        }
                    }).catch(error => {
                    console.log(error);
                })
            }
        },
        changeStatusPackage(paymentPackage, index) {
            let request = {
                "id": paymentPackage.id,
            }
            fetch("/api-change-status-package", {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify(request),
            }).then(response => response.json())
                .then((data) => {
                    if (data != null && data.code == "000") {
                        authenticationInstance.showModalNotify("Cập nhật thành công", 2000)
                        setTimeout(() => this.$set(this.listPaymentPackage, index, data.data), 2000)

                    } else {
                        modalMessageInstance.message = data.message;
                        modalMessageInstance.showModal()
                    }

                }).catch(error => {
                console.log(error);
            })
        },
        closeModalAddMoney() {
            document.getElementById("modalAddMoney").style.display = 'none';
            document.body.removeAttribute("class")
        },
        showModalAddMoney(user, index) {
            this.paymentMethod = ""
            this.selectedLandlord = user
            this.inputAmount = 0
            this.userIndex = index
            document.getElementById("modalAddMoney").style.display = 'block';
            document.body.setAttribute("class", "loading-hidden-screen")
        },
        showAddMoneyError(msg) {
            document.getElementById("modalAddMoneyErrorMsg").innerHTML = msg;
            document.getElementById("modalAddMoneyErrorMsg").style.visibility = 'visible';
        },
        hideAddMoneyError() {
            document.getElementById("modalAddMoneyErrorMsg").innerHTML = "";
            document.getElementById("modalAddMoneyErrorMsg").style.visibility = 'hidden';
        },
        validAddMoney() {
            let validAmount = false;
            let validPayment = false;
            let whiteSpaceRegex = /\s/;
            if (!this.inputAmount || this.inputAmount.trim().length == 0) {
                this.showAddMoneyError("Vui lòng nhập số tiền.");
            } else if (isNaN(this.inputAmount) || whiteSpaceRegex.test(this.inputAmount)) {
                this.showAddMoneyError("Vui lòng nhập số tiền hợp lệ.");
            } else if (this.inputAmount <= 1000 || this.inputAmount > 9000000) {
                this.showAddMoneyError("Vui lòng nhập số tiền ít nhất 1.000đ và cao nhất 9.000.000đ.");
            } else {
                validAmount = true;
                this.hideAddMoneyError();
                //check valid payment after amount valid
                if (!this.paymentMethod || this.paymentMethod.trim().length == 0) {
                    this.showAddMoneyError("Vui lòng nhập phương thức thanh toán.");
                } else {
                    validPayment = true;
                    this.hideAddMoneyError();
                }
            }
            return validPayment && validAmount;
        },
        handleButtonAddMoney() {
            if (this.validAddMoney()) {
                modalConfirmInstance.messageConfirm = 'Bạn có xác nhận nạp <b>'
                    + authenticationInstance.formatNumberToDisplay(this.inputAmount)
                    + ' VNĐ </b> vào tài khoản chủ trọ <b>'
                    + this.selectedLandlord.username + '</b> không?';
                sessionStorage.setItem("confirmAction", "add-money")
                modalConfirmInstance.showModal()
            }
        },
        addMoneyForLandlord() {
            let request = {
                "landlord": this.selectedLandlord.username,
                "amount": this.inputAmount,
                "paymentMethod": this.paymentMethod,
                "note": this.adjustMoneyNote
            }
            fetch("/api-add-money-for-landlord", {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify(request),
            }).then(response => response.json())
                .then((data) => {
                    if (data != null && data.code == "000") {
                        authenticationInstance.showModalNotify("Cập nhật thành công", 2000)
                        setTimeout(() => {
                            this.$set(this.listUser, this.userIndex, data.data)
                            this.closeModalAddMoney()
                        }, 2000);
                    } else {
                        modalMessageInstance.message = data.message;
                        modalMessageInstance.showModal()
                    }
                }).catch(error => {
                console.log(error);
            })
        },
        closeModalConfirm() {
            document.getElementById("modalConfirm").style.display = 'none';
        },
        closeModalDel() {
            document.getElementById("modalDelete").style.display = 'none';
        },
        closeModalBan() {
            document.getElementById("modalBan").style.display = 'none';
        },
        getInitFilterPost(){
            fetch("/api-get-init-home-page", {
                method: 'GET',
                headers: {
                    'Content-Type': 'application/json',
                },

            }).then(response => response.json())
                .then((data) => {
                    console.log(data);
                    if(data != null && data.code == "000"){
                        this.listTypePost = data.listTypePost
                        this.listFilterPrice = data.listFilterPrice
                        this.listFilterSquare = data.listFilterSquare
                        this.listFilterDistance = data.listFilterDistance
                    }
                }).catch(error => {
                console.log(error);
            })
        },
    }
})