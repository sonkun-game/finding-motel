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
        postType : {},
        postPrice : {},
        postSquare : {},
        postDistance : {},
        postStatus : {},
        isBannedUser : false,
    },
    methods: {
        filterUserByRole(roleId, listData){
            if (roleId == 0) return;
            let rawList = [];
            for (var item of listData) {
                if (item.role == roleId) {
                    rawList.push(item);
                }
            }
            this.listUser = rawList;
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
                    this.listUser = data;
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
                    this.listUser = data;
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
                    // if(data.status == 200){
                    this.getListReport();
                    // } else {
                    //     window.location.href = "/error";
                    // }
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