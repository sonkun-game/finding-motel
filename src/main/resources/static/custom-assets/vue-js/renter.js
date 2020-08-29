var renterInstance = new Vue({
    el: '#renter-manager',
    data: {
        userInfo: {},
        task: 0,
        wishList: [],
        //init
        listRentalRq: [],
        confirmAction: null,
        selectedRentalRequestId: null,
        message: "",
        expireMessage: "",
        pagination: []
    },
    beforeMount() {
        this.userInfo = JSON.parse(sessionStorage.getItem("userInfo"))
        this.task = sessionStorage.getItem("task")
    },
    mounted() {
        if (this.task == 2) {
            let profileUser = document.getElementById("user-manager-content")
            profileUser.classList.add("invisible")
            this.searchRentalRequest();
        } else if (this.task == 3) {
            let profileUser = document.getElementById("user-manager-content")
            profileUser.classList.add("invisible")
            this.getWishlist()
        } else if (this.task == 18) {
            let profileUser = document.getElementById("user-manager-content")
            profileUser.classList.add("invisible")
            let notification = JSON.parse(sessionStorage.getItem("notification"))
            this.searchRentalRequest(notification.requestId);
        }
    },
    methods: {
        getWishlist(currentPage) {
            if (currentPage === undefined || !currentPage) {
                currentPage = 0;
            }
            fetch("/api-get-wishlist?currentPage=" + currentPage, {
                method: 'POST',

            }).then(response => response.json())
                .then((data) => {
                    if (data != null && data.code == "000") {
                        this.wishList = data.data
                        this.pagination = data.pagination
                    }
                    authenticationInstance.hidePreloader()
                }).catch(error => {
                console.log(error);
            })
        },
        removeFromWishList(wishListId, username) {
            let request = {
                "id": wishListId,
                "renterUsername": username,
                "wishListScreen": true,
            }
            fetch("/api-remove-from-wishlist", {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify(request)

            }).then(response => response.json())
                .then((data) => {
                    console.log(data);
                    if (data != null && data.code == "000") {
                        this.showModalNotify("Đã xóa bài đăng khỏi danh sách yêu thích");
                        sessionStorage.removeItem("listPostOfRenter")
                        this.getWishlist()
                    }
                }).catch(error => {
                console.log(error);
            })
        },
        showModalConfirmChangeStatusRequest(rentalRequestId, action, event) {
            if (event.target.className.indexOf("disable") != -1) {
                return;
            }
            this.selectedRentalRequestId = rentalRequestId;
            this.confirmAction = this.changeRequestStatus;
            //show modal
            if (action == "cancel") {
                document.getElementById("modalConfirmMessage").innerHTML = 'Bạn có chắc chắn muốn hủy yêu cầu không?';
                document.getElementById("modalConfirm").style.display = 'block';
                document.body.setAttribute("class", "loading-hidden-screen")
            } else if (action == "expire") {
                this.message = '<p style="font-size: 20px; font-weight: 500">Bạn có muốn kết thúc thuê phòng tại phòng này không?</p>' +
                    '<p>Bạn sẽ có thể gửi yêu cầu thuê phòng vào phòng khác sau khi kết thúc thuê phòng này.</p>';
                document.getElementById("modalEndRentalRequest").style.display = 'block';
                document.body.setAttribute("class", "loading-hidden-screen")
            }

        },
        closeModalEndRequest() {
            document.getElementById("modalEndRentalRequest").style.display = 'none';
            document.body.removeAttribute("class")
        },
        confirmModalEndRequest() {
            this.closeModalEndRequest()
            this.confirmAction(this.selectedRentalRequestId);
        },
        closeModalConfirmChangeStatusRequest() {
            //close modal
            document.getElementById("modalConfirm").style.display = 'none';
            document.body.removeAttribute("class")
        },
        executeConfirm(yesNo) {
            this.closeModalConfirmChangeStatusRequest();
            if (yesNo == true) {
                this.confirmAction(this.selectedRentalRequestId);
                this.confirmAction = null;
            } else {
                return;
            }
        },
        showModalNotify(msg) {
            document.getElementById("my-modal-notification").style.display = 'block';
            document.getElementById("modalNotifyMessage").innerHTML = msg;
            document.body.setAttribute("class", "loading-hidden-screen")
            setTimeout(function () {
                document.body.removeAttribute("class")
                document.getElementById("my-modal-notification").style.display = 'none';
            }, 2000);
        },
        searchRentalRequest(requestId, currentPage) {
            if( currentPage === undefined || !currentPage) {
                currentPage = 0;
            }
            let rentalRequest = {
                "renterUsername": this.userInfo.username,
                "roomId": null,
                "requestDate": null,
                "statusId": null,
                "id": requestId,
            }
            fetch("/search-rental-request?currentPage=" + currentPage, {
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
                            this.listRentalRq = responseMsg.data;
                            this.pagination = responseMsg.pagination
                        }
                        authenticationInstance.hidePreloader()
                    }
                }).catch(error => {
                console.log(error);
            })
        },
        changeRequestStatus(rentalId) {
            let rentalRequest = {
                "expireMessage": this.expireMessage,
                "id": rentalId,
            }
            fetch("/change-rental-request-status", {
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
                            this.task = 2
                            sessionStorage.setItem("task", 2)
                            this.searchRentalRequest();
                        }
                    }
                }).catch(error => {
                console.log(error);
            })
        },
        closeModalConfirm() {
            document.getElementById("modalConfirm").style.display = 'none';
        },
        showExpireMessage(rentalRequest) {
            modalMessageInstance.title = "Lời nhắn của chủ trọ"
            modalMessageInstance.message = rentalRequest.expireMessage
            modalMessageInstance.showModal()
        }
    }
})
