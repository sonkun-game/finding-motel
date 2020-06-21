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

    },
    methods: {
        getListReport() {
            //
            this.isReportMgmt = true;
            this.isPostMgmt = false;
            this.isUserMgmt = false;
            this.isAdminProfile = false;
            //
            fetch("https://localhost:8081/get-report", {
                method: 'GET',
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
            let reportId = id;
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
                method: 'GET',
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