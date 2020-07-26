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
        //metal data
        priceValueSheet: [
            {max: null, min: null},
            {max: "1000000", min: null},
        ],
        distanceValueSheet: [
            {max: null, min: null},
            {max: "1", min: null},
        ],
        squareValueSheet: [
            {max: null, min: null},
            {max: "20", min: null},
        ],
        inputLandlordId: "",
        inputRenterId: "",
        inputPostTitle: "",
        listStatusReport: [],
        inputStatusReport: 0,
        userIndex: -1,
        postIndex: -1,
    },
    beforeMount() {
        this.task = localStorage.getItem("task")
    },
    mounted() {
        if (this.task == 9) {
            let profileUser = document.getElementById("user-manager-content")
            profileUser.classList.add("invisible")
            this.getListUser()
        } else if (this.task == 10) {
            let profileUser = document.getElementById("user-manager-content")
            profileUser.classList.add("invisible")
            this.getListPost()
        } else if (this.task == 11) {
            let profileUser = document.getElementById("user-manager-content")
            profileUser.classList.add("invisible")
            this.searchReport()
            this.getInitAdmin()
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
                } else if(this.modalBanDataType == 'post') {
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
        filterUserByRole(roleId, listData) {
            if (roleId == 0) return;
            let rawList = [];
            for (var item of listData) {
                if (item.role == roleId) {
                    rawList.push(item);
                }
            }
            this.listUser = rawList;
        },
        getUserById() {
            let username = document.getElementById("searchUserTxt").value;
            fetch("/get-user-by-id?username=" + username, {
                method: 'POST',
            }).then(response => response.json())
                .then((data) => {
                    // if(data.status == 200){
                    this.listUser = data;
                    this.filterUserByRole(document.getElementById("roleFilterCb").value, this.listUser);
                    // } else {
                    // window.location.href = "/error";
                    // }

                }).catch(error => {
                console.log(error);
            })
        },
        getListUser() {

            fetch("/get-all-user", {
                method: 'POST',
            }).then(response => response.json())
                .then((data) => {
                    // if(data.status == 200){
                    this.listUser = data;
                    this.filterUserByRole(document.getElementById("roleFilterCb").value, this.listUser);
                    // } else {
                    // window.location.href = "/error";
                    // }

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
                    // if(data.status == 200){
                    this.getListUser();
                    // } else {
                    //     window.location.href = "/error";
                    // }
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
                    // if(data.status == 200){
                    this.getListUser()
                    // } else {
                    //     window.location.href = "/error";
                    // }
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
                    if (data.code == '000') {
                        this.getListReport();
                    } else {
                        window.location.href = "/error";
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
                    if (data.code == "000") {
                        this.listPost = data.data;
                    } else {
                        window.location.href = "/error";
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
                        this.getListPost();
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
        searchPost() {
            let postRequestDTO = {
                "typeId": this.isNullSearchParam(this.postType),
                "title": this.isNullSearchParam(this.postTitleOrLandlord),
                "priceMax": this.valueSheetData(this.postPrice, this.priceValueSheet).max,
                "priceMin": this.valueSheetData(this.postPrice, this.priceValueSheet).min,
                "distanceMax": this.valueSheetData(this.postDistance, this.distanceValueSheet).max,
                "distanceMin": this.valueSheetData(this.postDistance, this.distanceValueSheet).min,
                "squareMax": this.valueSheetData(this.postSquare, this.squareValueSheet).max,
                "squareMin": this.valueSheetData(this.postSquare, this.squareValueSheet).min,
                "landlordUsername": this.isNullSearchParam(this.postTitleOrLandlord),
                "visible": this.postStatus == '0' ? false : this.postStatus == '1' ? true : null,
            }
            fetch("/search-post", {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify(postRequestDTO),
            }).then(response => response.json())
                .then((data) => {
                    if (data.code == "000") {
                        this.listPost = data.data;
                    } else {
                        window.location.href = "/error";
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
                    if (data.code == "000") {
                        this.$set(this.listPost, this.postIndex, data.data)
                    } else {
                        alert("Error" + data.code);
                    }
                }).catch(error => {
                console.log(error);
            })
        },

        unbanPost(id) {
            fetch("/api-un-ban-post?postId="+id, {
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
        searchReport() {
            let reportRequestDTO = {
                "landlordId": this.inputLandlordId == "" ? null : this.inputLandlordId,
                "renterId": this.inputRenterId == "" ? null : this.inputRenterId,
                "postTitle": this.inputPostTitle == "" ? null : this.inputPostTitle,
                "statusReport": this.isNullSearchParam(this.inputStatusReport),
            }
            fetch("/search-report", {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify(reportRequestDTO),
            }).then(response => response.json())
                .then((data) => {
                    if (data.code == "000") {
                        this.listReport = data.data;
                    } else {
                        window.location.href = "/error";
                    }
                }).catch(error => {
                console.log(error);
            })
        },
        getInitAdmin() {
            fetch("api-get-init-admin", {
                method: 'POST',
            }).then(response => response.json())
                .then((data) => {
                    if(data.msgCode == "admin000"){
                        this.listStatusReport = data.listStatusReport
                    }

                }).catch(error => {
                console.log(error);
            })
        },
        handleGetReport(post, user){
            userTaskInstance.task = 11
            localStorage.setItem("task", 11)
            this.task = 11
            this.getInitAdmin()
            if(post != null){
                this.inputLandlordId = post.landlordName
                this.inputPostTitle = post.title
            }else if(user != null){
                this.inputLandlordId = user.username
                this.inputPostTitle = ""
            }

            let reportRequestDTO = {
                "landlordId": this.inputLandlordId == "" ? null : this.inputLandlordId,
                "renterId": null,
                "postTitle": this.inputPostTitle == "" ? null : this.inputPostTitle,
                "statusReport": null,
            }
            fetch("/search-report", {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify(reportRequestDTO),
            }).then(response => response.json())
                .then((data) => {
                    if (data.code == "000") {
                        let listReport = []
                        for (let report of data.data) {
                            if(post != null && (report.statusId == 3 || report.statusId == 5)){
                                listReport.push(report)
                            }else if(user != null && (report.statusId == 3 || report.statusId == 4)){
                                listReport.push(report)
                            }
                        }
                        this.listReport = listReport
                    } else {
                        window.location.href = "/error";
                    }
                }).catch(error => {
                console.log(error);
            })
        }
    }
})