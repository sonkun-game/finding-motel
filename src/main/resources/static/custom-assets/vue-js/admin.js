var admin = new Vue({
    el: '#pageContent',
    data: {
        //flag
        isReportMgmt: false,
        isPostMgmt: false,
        isUserMgmt: false,
        isAdminProfile: true,
        //variable
        listReport: [],
        listPost: [],
        listUser: [],
        //request parameter
        postType: {},
        postPrice: {},
        postSquare: {},
        postDistance: {},
        postStatus: {},
        isBannedUser: false,
        //modal form
        modalBanId: {},
        modalDelId: {},
        modalDelDataType: {},
        modalBanDataType: {},
        modalConfirmClick: {},
        modalData: [],
        //user detail form
        userDetail: [],
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
                if (this.modalBanDataType == 'ban') {
                    this.banLanlord(this.modalBanId);
                } else if (this.modalBanDataType == 'unban') {
                    this.unbanLanlord(this.modalBanId);
                }
            }

        },
        showModalConfirmBan(id, dataType) {
            this.modalBanId = id;
            this.modalBanDataType = dataType;
            if (this.modalBanDataType == 'ban') {
                document.getElementById("modalBanContent").innerHTML = 'Bạn có muốn khóa tài khoản này không?';
            } else if (this.modalBanDataType == 'unban') {
                document.getElementById("modalBanContent").innerHTML = 'Bạn có muốn mở khóa tài khoản này không?';
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
            fetch("https://localhost:8081/get-user-by-id?username=" + username, {
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
            //
            this.isReportMgmt = false;
            this.isPostMgmt = false;
            this.isUserMgmt = true;
            this.isAdminProfile = false;
            //
            fetch("https://localhost:8081/get-all-user", {
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
            fetch("https://localhost:8081/ban-landlord?username=" + username, {
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
            fetch("https://localhost:8081/unban-landlord?username=" + username, {
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
        getListReport() {
            //
            this.isReportMgmt = true;
            this.isPostMgmt = false;
            this.isUserMgmt = false;
            this.isAdminProfile = false;
            //
            fetch("https://localhost:8081/get-report", {
                method: 'POST',
            }).then(response => response.json())
                .then((data) => {
                    // if(data.status == 200){
                    this.listReport = data;
                    // } else {
                    // window.location.href = "/error";
                    // }

                }).catch(error => {
                console.log(error);
            })
        },

        deleteReport(id) {
            fetch("https://localhost:8081/delete-report?reportId=" + id, {
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
            //
            this.isReportMgmt = false;
            this.isPostMgmt = true;
            this.isUserMgmt = false;
            this.isAdminProfile = false;
            //
            fetch("https://localhost:8081/get-post", {
                method: 'POST',
            }).then(response => response.json())
                .then((data) => {
                    // if(data.status == 200){
                    this.listPost = data;
                    // } else {
                    // window.location.href = "/error";
                    // }

                }).catch(error => {
                console.log(error);
            })
        },

        deletePost(id) {
            fetch("https://localhost:8081/delete-post?postId=" + id, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
            }).then(response => response.json())
                .then((data) => {
                    // if(data.status == 200){
                    this.listReport = data;
                    this.getListPost();
                    // } else {
                    //     window.location.href = "/error";
                    // }
                }).catch(error => {
                console.log(error);
            })
        },

        searchPost() {
            let reportId = id;
            fetch("https://localhost:8081/delete-post?postId=" + id, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
            }).then(response => response.json())
                .then((data) => {
                    // if(data.status == 200){
                    this.listReport = data;
                    this.getListPost();
                    // } else {
                    //     window.location.href = "/error";
                    // }
                }).catch(error => {
                console.log(error);
            })
        },
    }
})