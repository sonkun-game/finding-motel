var renterInstance = new Vue({
    el: '#renter-manager',
    data: {
        userInfo: {},
        task: 0,
        wishList: [],
        //init
        listRentalRq : null,
        confirmAction : null,
        rentalRequestIdCancel : null,
    },
    beforeMount(){
        this.userInfo = JSON.parse(localStorage.getItem("userInfo"))
        this.task = localStorage.getItem("task")
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
    },
    methods: {
        getWishlist(){
            fetch("/api-get-wishlist", {
                method: 'POST',

            }).then(response => response.json())
                .then((data) => {
                    console.log(data);
                    if(data != null){
                        this.wishList = data
                    }
                }).catch(error => {
                console.log(error);
            })
        },
        removeFromWishList(postId){
            fetch("/api-remove-from-wishlist?postId="+postId, {
                method: 'GET',

            }).then(response => response.json())
                .then((data) => {
                    console.log(data);
                    if(data != null){
                        this.wishList = data
                    }
                }).catch(error => {
                console.log(error);
            })
        },
        showModalCancelRequest(rentalRequestIdCancel) {
            this.rentalRequestIdCancel = rentalRequestIdCancel;
            this.confirmAction = this.cancelRentalRequest;
            //show modal
            document.getElementById("modalConfirm").style.display = 'block';
            document.getElementById("modalConfirmMessage").innerHTML = 'Bạn có chắc chắn hủy yêu cầu không?';

        },
        closeModalCancelRequest() {
            //close modal
            document.getElementById("modalConfirm").style.display = 'none';
        },
        executeConfirm(yesNo) {
            this.closeModalCancelRequest();
            if (yesNo == true) {
                this.confirmAction(this.rentalRequestIdCancel);
                this.confirmAction = null;
            } else {
                return;
            }
        },
        showModalNotify(msg) {
            document.getElementById("my-modal-notification").style.display = 'block';
            document.getElementById("modalNotifyMessage").innerHTML = msg;
            setTimeout(function () {
                document.getElementById("my-modal-notification").style.display = 'none';
            }, 3000);
        },
        searchRentalRequest() {
            let rentalRequest = {
                "renterUsername": null,
                "roomId": null,
                "requestDate": null,
                "statusId": null,
            }
            fetch("https://localhost:8081/search-rental-request", {
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
        cancelRentalRequest(rentalId) {
            let statusId = 8;
            fetch("https://localhost:8081/change-rental-request-status?rentalRequestId=" + rentalId
                + "&statusId=" + statusId, {
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
        }
    }
})
