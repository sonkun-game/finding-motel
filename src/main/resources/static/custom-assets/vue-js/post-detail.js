var postDetailInstance = new Vue({
    el: '#postDetailBody',
    data: {
        post: {},
        reportContent: null,
        userInfo: {},
        postId: "",
        listImage: [],

        //rental param
        roomIdRental: "",
        dateRequestRental: null,
        //action
        confirmAction : null,
        relatedPosts: [],

    },
    beforeMount() {
        this.userInfo = JSON.parse(localStorage.getItem("userInfo"));
        //get url param
        const queryString = window.location.search;
        const urlParams = new URLSearchParams(queryString);
        this.postId = urlParams.get('id');
        this.viewDetail();
        this.getRelatedPost();
    },
    methods: {
        viewDetail: function () {
            var query = window.location.search;
            var url = new URLSearchParams(query);
            var id = url.get('id');

            fetch("https://localhost:8081/api-post-detail?id=" + id, {
                method: 'POST',
            }).then(response => response.json())
                .then((data) => {
                    console.log(data);
                    this.post = data;
                    this.listImage = this.post.images

                }).catch(error => {
                console.log(error);
            })
        },
        showModalReport() {
            if(this.userInfo == null){
                window.location.href = "/dang-nhap"
                return
            }
            document.getElementById("reportModal").style.display = 'block';
            this.reportContent = ""
        },
        closeModalReport() {
            document.getElementById("reportModal").style.display = 'none';
        },
        sendReport() {
            let currentDate = new Date();
            let reportRequest = {
                "renterId": this.userInfo.username,
                "postId": this.postId,
                "content": this.reportContent
            }
            fetch("https://localhost:8081/sent-report", {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify(reportRequest),
            }).then(response => response.json())
                .then((data) => {
                    if (data.code == '000') {
                        this.showModalNotify("Gửi Thành Công")
                        setTimeout(() => this.closeModalReport(), 2000);
                    } else {
                        window.location.href = "/error";
                    }

                }).catch(error => {
                console.log(error);
            })
        },
        showModalChooseRoom() {
            if(this.userInfo == null){
                window.location.href = "/dang-nhap"
                return
            }
            document.getElementById("myModal_chooseRoom").style.display = 'block';
        },
        closeModalChooseRoom() {
            document.getElementById("myModal_chooseRoom").style.display = 'none';
        },
        showModalConfirmSentRental() {
            this.confirmAction = this.sentRentalRequest;
            //show modal
            document.getElementById("modalConfirm").style.display = 'block';
            document.getElementById("modalConfirmMessage").innerHTML = 'Bạn có chắc chắn tạo yêu cầu không?';
        },
        closeModalConfirm() {
            //close modal
            document.getElementById("modalConfirm").style.display = 'none';
        },
        executeConfirm(yesNo) {
            this.closeModalConfirm();
            this.closeModalChooseRoom();
            if (yesNo == true) {
                this.confirmAction();
            } else {
                return;
            }
        },
        setRoomIdRental(roomId, event) {
            if (event.target.className.indexOf("disable") != -1) {
                return;
            }
            this.roomIdRental = roomId;
        },
        setRequestDateRental(date) {
            this.dateRequestRental = date;
        },
        showModalNotify(msg) {
            document.getElementById("my-modal-notification").style.display = 'block';
            document.getElementById("modalNotifyMessage").innerHTML = msg;
            setTimeout(function () {
                document.getElementById("my-modal-notification").style.display = 'none';
            }, 2000);
        },
        sentRentalRequest() {
            let rentalRequest = {
                "renterUsername": this.userInfo.username,
                "roomId": this.roomIdRental,
                "startDate": this.dateRequestRental,
                "statusId": 7,
                "postId": this.postId,
            }
            fetch("https://localhost:8081/sent-rental-request", {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify(rentalRequest),
            }).then(response => response.json())
                .then((responseMsg) => {
                    if (responseMsg.status == 403) {
                        window.location.href = "dang-nhap";
                    } else {
                        if (responseMsg != null && responseMsg.code == "000") {
                            this.showModalNotify(responseMsg.message);
                        }else {
                            modalMessageInstance.message = responseMsg.message
                            modalMessageInstance.showModal()
                        }
                    }
                }).catch(error => {
                console.log(error);
            })
        },
        getRelatedPost(){
            fetch("/api-get-related-post?id=" + this.postId, {
                method: 'POST',
            }).then(response => response.json())
                .then((data) => {
                    console.log(data);
                    this.relatedPosts = data.listPost;

                }).catch(error => {
                console.log(error);
            })
        },
        showModalConfirmSendReport() {
            if(this.reportContent == ""){
                modalMessageInstance.message = "Vui lòng nhập nội dung báo cáo!"
                modalMessageInstance.showModal()
                return
            }
            modalConfirmInstance.messageConfirm = 'Bạn có muốn gửi báo cáo này không?';
            modalConfirmInstance.showModal()
            sessionStorage.setItem("confirmAction", "send-report")
        },
    },
    created() {

    }
})