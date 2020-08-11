var renterInstance = new Vue({
    el: '#renter-manager',
    data: {
        userInfo: {},
        task: 0,
        wishList: [],
        //init
        listRentalRq : null,
        confirmAction : null,
        selectedRentalRequestId : null,
    },
    beforeMount(){
        this.userInfo = JSON.parse(sessionStorage.getItem("userInfo"))
        this.task = sessionStorage.getItem("task")
    },
    mounted(){
        if(this.task == 2){
            let profileUser = document.getElementById("user-manager-content")
            profileUser.classList.add("invisible")
            this.searchRentalRequest();
        }else if(this.task == 3){
            let profileUser = document.getElementById("user-manager-content")
            profileUser.classList.add("invisible")
            this.getWishlist()
        }
        else if(this.task == 18){
            let profileUser = document.getElementById("user-manager-content")
            profileUser.classList.add("invisible")
            let notification = JSON.parse(sessionStorage.getItem("notification"))
            this.searchRentalRequest(notification.requestId);
        }
    },
    methods: {
        getWishlist(){

            fetch("/api-get-wishlist", {
                method: 'POST',


            }).then(response => response.json())
                .then((data) => {
                    console.log(data);
                    if(data != null && data.msgCode == "wishlist000"){
                        this.wishList = data.wishList
                    }
                }).catch(error => {
                console.log(error);
            })
        },
        removeFromWishList(wishListId, username){
            let request = {
                "id" : wishListId,
                "renterUsername" : username,
                "wishListScreen" : true,
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
                    if(data != null && data.msgCode == "wishlist000"){
                        this.showModalNotify("Đã xóa bài đăng khỏi danh sách yêu thích");

                        setTimeout(() => {
                            this.wishList = data.wishList
                        }, 2000);

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
            if(action == "cancel"){
                document.getElementById("modalConfirmMessage").innerHTML = 'Bạn có chắc chắn muốn hủy yêu cầu không?';
            }else if(action == "expire"){
                document.getElementById("modalConfirmMessage").innerHTML
                    = '<h3>Bạn có muốn kết thúc quá trình thuê phòng này không?</h3>' +
                '<p>Bạn sẽ có thể gửi yêu cầu thuê phòng vào phòng khác sau khi kết thúc thuê phòng này</p>';
            }
            document.getElementById("modalConfirm").style.display = 'block';
            document.body.setAttribute("class", "loading-hidden-screen")
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
        searchRentalRequest(requestId) {
            let rentalRequest = {
                "renterUsername": this.userInfo.username,
                "roomId": null,
                "requestDate": null,
                "statusId": null,
                "id" : requestId,
            }
            fetch("/search-rental-request", {
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
                        }
                    }
                }).catch(error => {
                console.log(error);
            })
        },
        changeRequestStatus(rentalId) {
            fetch("/change-rental-request-status?rentalRequestId=" + rentalId, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
            }).then(response => response.json())
                .then((responseMsg) => {
                    if (responseMsg.status == 403) {
                        window.location.href = "dang-nhap";
                    } else {
                        if (responseMsg != null && responseMsg.code == "000") {
                            this.showModalNotify(responseMsg.message);
                            this.searchRentalRequest();
                        }
                    }
                }).catch(error => {
                console.log(error);
            })
        },
    }
})
