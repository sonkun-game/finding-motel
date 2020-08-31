var postDetailInstance = new Vue({
    el: '#postDetailBody',
    data: {
        userInfo: {},
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
        map : "",
        fuLocation : {placeId : "ChIJbQilLLNUNDER5Der2CkuxqM"},
        listPostOfRenter : [],
        listRoomOfPost : [],
        disableFunctions : false,
        validateMessage : "",
        showMsg : false,
    },
    beforeMount() {
        this.userInfo = JSON.parse(sessionStorage.getItem("userInfo"));
        //get url param
        const queryString = window.location.search;
        const urlParams = new URLSearchParams(queryString);
        this.postId = urlParams.get('id');
        this.viewDetail()
    },
    methods: {
        viewDetail: function () {
            let query = window.location.search;
            let url = new URLSearchParams(query);
            let id = url.get('id');

            fetch("/api-post-detail?id=" + id, {
                method: 'POST',
            }).then(response => response.json())
                .then((data) => {
                    if(data != null && data.code == "000"){
                        this.post = data.data;
                        this.listImage = this.post.images
                        this.getRelatedPost(this.post.id, this.post.landlord, this.post.typeId)
                        this.handleDisplayDirection()
                    }else if(data != null && data.code == "001"){
                        modalMessageInstance.message = data.message
                        modalMessageInstance.showModal()
                        this.disableFunctions = true;
                        this.post = data.data;
                        this.listImage = this.post.images
                        this.getRelatedPost(this.post.id, this.post.landlord, this.post.typeId)
                        this.handleDisplayDirection()
                    }else {
                        modalMessageInstance.message = data.message
                        modalMessageInstance.showModal()
                    }
                    authenticationInstance.hidePreloader()
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
            document.body.setAttribute("class", "loading-hidden-screen")
        },
        closeModalReport() {
            document.getElementById("reportModal").style.display = 'none';

            document.body.removeAttribute("class")
        },
        sendReport() {
            let currentDate = new Date();
            let reportRequest = {
                "renterId": this.userInfo.username,
                "postId": this.postId,
                "content": this.reportContent
            }
            fetch("/sent-report", {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify(reportRequest),
            }).then(response => response.json())
                .then((data) => {
                    if (data.code == '000') {
                        this.showModalNotify("Gửi Báo Cáo Thành Công")
                        setTimeout(() => this.closeModalReport(), 2000);
                    } else {
                        modalMessageInstance.message = data.message
                        modalMessageInstance.showModal()
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
            if(this.post.outOfRoom != null && this.post.outOfRoom){
                modalMessageInstance.message = "Tất cả phòng của bài đăng đã được cho thuê"
                modalMessageInstance.showModal()
                return
            }
            if(this.listRoomOfPost == null || this.listRoomOfPost.length == 0){
                this.getListRoom(this.post)
            }
            document.getElementById("myModal_chooseRoom").style.display = 'block';
            document.body.setAttribute("class", "loading-hidden-screen")
        },
        closeModalChooseRoom() {
            document.getElementById("myModal_chooseRoom").style.display = 'none';
            document.body.removeAttribute("class")
        },
        showModalConfirmSentRental() {
            if(this.validateRoomSelect() && this.validateDate(this.dateRequestRental)){
                this.showMsg = false
            }else {
                this.showMsg = true
                return
            }
            this.confirmAction = this.sentRentalRequest;
            //show modal
            document.getElementById("modalConfirm").style.display = 'block';
            document.getElementById("modalConfirmMessage").innerHTML = 'Bạn có chắc chắn tạo yêu cầu không?';
        },
        validateRoomSelect(){
            if(this.roomIdRental == "" || this.roomIdRental == null){
                this.validateMessage = "Vui lòng chọn phòng muốn thuê"
                this.showMsg = true
                return false
            }else {
                this.showMsg = false
                return true
            }
        },
        validateDate(inputDate){
            let startDate = new Date(inputDate)
            let currentDate = new Date()
            if(inputDate == "" || inputDate == null){
                this.validateMessage = "Vui lòng chọn ngày bắt đầu"
                this.showMsg = true
                return false
            }else if(Number.isNaN(startDate.getTime())){
                this.validateMessage = "Ngày bắt đầu không hợp lệ"
                this.showMsg = true
                return false
            }else if(currentDate.getTime() >= startDate.getTime()){
                this.validateMessage = "Vui lòng chọn ngày bắt đầu sau ngày " +
                    currentDate.getDate() + "/" + (currentDate.getMonth() + 1) + "/" + currentDate.getFullYear()
                this.showMsg = true
                return false
            }else {
                this.showMsg = false
                return true
            }
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
            this.validateRoomSelect()
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
                "landlordUsername" : this.post.landlord,
            }
            fetch("/sent-rental-request", {
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
        getRelatedPost(postId, landlordUsername, typeId){
            let request = {
                "postId" : postId,
                "username" : landlordUsername,
                "typeId" : typeId,
            }
            fetch("/api-get-related-post", {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify(request)
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
        handleActionWishList(post){
            if(this.userInfo == null){
                window.location.href = "/dang-nhap"
            }else {
                if(this.listPostOfRenter.some(p => p.id == post.id)){
                     this.removeFromWishList(post.id, this.userInfo.username)
                }else {
                    this.addWishlist(post, this.userInfo.username)

                }
            }
        },
        removeFromWishList(postId, username){
            let request = {
                "postId" : postId,
                "renterUsername" : username,
                "wishListScreen" : false,
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
                    if(data != null && data.code == "000"){
                        authenticationInstance.showModalNotify("Đã xóa bài đăng khỏi danh sách yêu thích", 1000);
                        this.getWishListOfRenter()
                    }
                }).catch(error => {
                console.log(error);
            })
        },
        addWishlist : function(post, username){
            let request = {
                "postId" : post.id,
                "renterUsername" : username,
            }
            fetch("/api-add-wishlist", {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify(request)

            }).then(response => response.json())
                .then((data) => {
                    if(data != null && data.code == "403"){
                        window.location.href = "/dang-nhap"
                    }else if(data != null && data.code == "000"){
                        authenticationInstance.showModalNotify("Đã thêm vào danh sách yêu thích", 1000)
                        this.getWishListOfRenter()
                    }else {
                        modalMessageInstance.message = data.message
                        modalMessageInstance.showModal()
                        return null
                    }
                }).catch(error => {
                console.log(error);
            })
        },
        getURL() {
            return window.location.href;
        },
        initMap(){
            let mapOptions,
                element = document.getElementById( 'map-canvas' )
            mapOptions = {
                zoom: 16,
                center: new google.maps.LatLng( 21.013237, 105.527018 ),
                disableDefaultUI: false, // Disables the controls like zoom control on the map if set to true
                scrollWheel: true, // If set to false disables the scrolling on the map.
                draggable: true, // If set to false , you cannot move the map around.
            }
            // Create an object map with the constructor function Map()
            this.map = new google.maps.Map( element, mapOptions ); // Till this like of code it loads up the map.

        },
        displayRoute(travel_mode, origin, destination, directionsService, directionsDisplay) {
            directionsService.route({
                origin: origin,
                destination: destination,
                travelMode: travel_mode,
                avoidTolls: true
            }, function (response, status) {
                if (status === 'OK') {
                    directionsDisplay.setMap(postDetailInstance.map);
                    directionsDisplay.setDirections(response);
                } else {
                    directionsDisplay.setMap(null);
                    directionsDisplay.setDirections(null);
                    alert('Could not display directions due to: ' + status);
                }
            });
        },
        handleDisplayDirection(){
            let origin = this.post.mapLocation;
            let destination = this.fuLocation;
            let travel_mode = "DRIVING";
            let directionsDisplay = new google.maps.DirectionsRenderer({'draggable': false});
            let directionsService = new google.maps.DirectionsService();
            this.displayRoute(travel_mode, origin, destination, directionsService, directionsDisplay);
        },
        getWishListOfRenter(){
            let request = {
                "renterUsername" : this.userInfo.username
            }
            fetch("/api-get-wish-list", {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify(request)

            }).then(response => response.json())
                .then((data) => {
                    console.log(data);
                    if(data != null && data.code == "000"){
                        this.listPostOfRenter = data.data
                        sessionStorage.setItem("listPostOfRenter", JSON.stringify(data.data))
                    }
                }).catch(error => {
                console.log(error);
            })
        },
        getListRoom(post) {

            fetch("/api-get-list-room-of-post?postId=" + post.id, {
                method: 'POST',
            }).then(response => response.json())
                .then((data) => {
                    if(data != null && data.code == "000"){
                        this.listRoomOfPost = data.data
                    }

                }).catch(error => {
                console.log(error);
            })
        },
    },
    mounted() {
        this.initMap()
    },
    updated(){
        $('.flexslider').flexslider({
            animation: "slide",
            start: function(slider) {
                $('body').removeClass('loading');
            }
        });
    },
    created(){
        this.userInfo = JSON.parse(sessionStorage.getItem("userInfo"))
        if(this.userInfo != null && this.userInfo.role == 'RENTER'){
            if(sessionStorage.getItem("listPostOfRenter") != null){
                this.listPostOfRenter = JSON.parse(sessionStorage.getItem("listPostOfRenter"))
            }else {
                this.getWishListOfRenter()
            }
        }
    }
})