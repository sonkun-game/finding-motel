var postDtl = new Vue({
    el: '#postDetailBody',
    data: {
        post: null,
        reportContent: null,
        userInfo : null,
        postId : null,
    },
    beforeMount() {
        this.userInfo = JSON.parse(localStorage.getItem("userInfo"));
        //get url param
        const queryString = window.location.search;
        const urlParams = new URLSearchParams(queryString);
        this.postId = urlParams.get('id');
    },
    methods: {
        viewDetail: function (event) {
            var id = event.target.id
            fetch("https://localhost:8081/api-post-detail?id=" + id, {
                method: 'POST',
            }).then(response => response.json())
                .then((data) => {
                    console.log(data);
                    this.post = data;
                }).catch(error => {
                console.log(error);
            })
        },
        showModalReport() {
            document.getElementById("reportModal").style.display = 'block';
        },
        handleEventReportModal(event) {
            //close modal
            if (event.target.id.toString().includes('closeModal')) {
                document.getElementById("reportModal").style.display = "none";
            } else if (event.target.id.toString() === 'modalReportAcceptBtn') {
                this.sendReport();
            }
        },
        sendReport() {
            let currentDate = new Date();
            let reportRequest = {
                "renterId" : this.userInfo.username,
                "postId" : this.postId,
                "content" : this.reportContent
            }
            fetch("https://localhost:8081/sent-report", {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body : JSON.stringify(reportRequest),
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
        }
    },

})