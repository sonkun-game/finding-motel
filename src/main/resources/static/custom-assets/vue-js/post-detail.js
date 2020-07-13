var postDtl = new Vue({
    el: '#postDetailBody',
    data: {
        post: {},
        reportContent: null,
        userInfo: null,
        postId: null,
        listImage: [],

        //rental param
        roomIdRental: null,
        dateRequestRental : null,
        //action
        confirmAction : null,
    },
    beforeMount() {
        this.userInfo = JSON.parse(localStorage.getItem("userInfo"));
        //get url param
        const queryString = window.location.search;
        const urlParams = new URLSearchParams(queryString);
        this.postId = urlParams.get('id');


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
            document.getElementById("reportModal").style.display = 'block';
        },
        closeModalReport() {
            document.getElementById("reportModal").style.display = 'none';
        },
        handleEventReportModal(event) {
            //close modal
            if (event.target.id.toString().includes('closeModal')) {
                this.closeModalReport();
            } else if (event.target.id.toString() === 'modalReportAcceptBtn') {
                this.sendReport();
            }
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
                        this.closeModalReport();
                    } else {
                        window.location.href = "/error";
                    }

                }).catch(error => {
                console.log(error);
            })
        },
        showModalChooseRoom() {
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
        setRoomIdRental(roomId) {
            this.roomIdRental = roomId;
        },
        setRequestDateRental(date) {
            this.dateRequestRental = date;
        },
        showModalNotify(trueFalse) {
            document.getElementById("my-modal-notification").style.display = 'block';
            if (trueFalse == true) {
                document.getElementById("modalNotifyMessage").innerHTML = 'Cập Nhật Thành Công!';
            } else {
                document.getElementById("modalNotifyMessage").innerHTML = 'Xảy ra lỗi!';
            }

            setTimeout(function () {
                document.getElementById("my-modal-notification").style.display = 'none';
            },3000);
        },
        sentRentalRequest() {
            let rentalRequest = {
                "renterUsername" : this.userInfo.username,
                "roomId" : this.roomIdRental,
                "requestDate" : this.dateRequestRental,
                "statusId" : 6,
            }
            fetch("https://localhost:8081/sent-rental-request", {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify(rentalRequest),
            }).then(response => response.json())
                .then((responseMsg) => {
                    if (responseMsg.code == '000') {
                        this.showModalNotify();
                    } else {
                        console.log(rentalRequest);
                    }
                }).catch(error => {
                console.log(error);
            })
        }
    },
    created() {
        this.viewDetail();
    }
})