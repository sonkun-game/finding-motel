var landlordInstance = new Vue({
    el: '#landlord-manager',
    data: {
        userInfo: {},
        task: 0,
        listTypePost: [],
        listPaymentPackage: [],
        uploadImages: [],
        typeOfPost: "",
        title: "",
        detailInfo: "",
        price: "",
        square: 0,
        distance: 0,
        duration: "",
        numberOfRoom: 0,
        listRoom: [],
        amountSelected: 0,
        listPayment: [],
        listPost: [],
        listPaymentPost: [],
        editMode: false,
        expireDate: "",
        postId : "",
        postIndex : "",
        listRoomRequest: [],
        selectedPost : {},
        selectedRequest : {},
        renterInfo : {},
        selectedRoom : {},
        roomIndex : "",
        fuLocation : {placeId : "ChIJbQilLLNUNDER5Der2CkuxqM"},
        latMarkerEl : "",
        longMarkerEl : "",
        map : "",
        inputAddress : "",
        //
        paymentAmount : "",
        paymentValid : false,
        message : "",
        newestPayment : {},
        expireMessage : "",
        validateMessage : "",
        showMsg : false,
        showMsgModal : false,
        pagination: [],
    },
    created(){
        let previousUrl = document.referrer
        if(previousUrl.includes("test-payment.momo.vn") && previousUrl.includes("errorCode=0")){
            let query = window.location.search;
            let url = new URLSearchParams(query);
            sessionStorage.setItem("momo-check", "1")
            sessionStorage.setItem("partnerCode", url.get('partnerCode'))
            sessionStorage.setItem("accessKey", url.get('accessKey'))
            sessionStorage.setItem("requestId", url.get('requestId'))
            sessionStorage.setItem("orderId", url.get('orderId'))
            sessionStorage.setItem("amount", url.get('amount'))
            sessionStorage.setItem("errorCode", url.get('errorCode'))
            window.location.href = "/nap-tien"
        }
    },
    beforeMount(){
        this.userInfo = JSON.parse(sessionStorage.getItem("userInfo"))
        this.task = sessionStorage.getItem("task")
    },
    mounted(){
        if(this.task == 13){
            let profileUser = document.getElementById("user-manager-content")
            profileUser.classList.add("invisible")
            this.getInitNewPost()
            setTimeout( () => {
                this.initMap()
            }, 1000)
        }else if(this.task == 4){
            let profileUser = document.getElementById("user-manager-content")
            profileUser.classList.add("invisible")
            this.viewListPost()
            this.getInitNewPost()
        }else if(this.task == 6){
            let profileUser = document.getElementById("user-manager-content")
            profileUser.classList.add("invisible")
            this.getHistoryPayment()
        }else if(this.task == 7){
            let profileUser = document.getElementById("user-manager-content")
            profileUser.classList.add("invisible")
            this.getHistoryPaymentPost()
        }else if(this.task == 5){
            let profileUser = document.getElementById("user-manager-content")
            profileUser.classList.add("invisible")
            this.getListRoomProcessingRequest()
        }else if(this.task == 15){
            let profileUser = document.getElementById("user-manager-content")
            profileUser.classList.add("invisible")
            this.selectedPost = JSON.parse(sessionStorage.getItem("selectedPost"))
            this.getListRoomByPost(this.selectedPost.id)
        }
        else if(this.task == 17){
            let profileUser = document.getElementById("user-manager-content")
            profileUser.classList.add("invisible")
            let notification = JSON.parse(sessionStorage.getItem("notification"))
            this.getRoomById(notification.roomId)
        }
        else if(this.task == 16){
            let profileUser = document.getElementById("user-manager-content")
            profileUser.classList.add("invisible")
            this.getInitNewPost()
            let post = JSON.parse(sessionStorage.getItem("selectedPost"))
            this.setFieldEditMode(post)
            setTimeout( () => {
                this.initMap()
                this.handleDisplayDirection()
            }, 1000)
        } else if (this.task == 8) {
            let profileUser = document.getElementById("user-manager-content")
            profileUser.classList.add("invisible")
            if(window.location.href.includes("nap-tien")
                && sessionStorage.getItem("momo-check") != null
                && sessionStorage.getItem("momo-check") == "1"){
                this.checkStatusAndSavePayment()
            }else {
                authenticationInstance.hidePreloader()
            }
        }

    },
    methods: {
        showModalConfirm(post, confirmType) {
            this.selectedPost = post
            this.postId = post.id;
            this.postIndex = this.listPost.indexOf(post)
            if (confirmType == 'delete') {
                modalConfirmInstance.messageConfirm = 'Bạn có muốn xóa bài viết này không?';
                sessionStorage.setItem("confirmAction", "delete-post")
            }else if(confirmType == 'hide'){
                if(this.userInfo.banned){
                    modalMessageInstance.title = "Thông báo"
                    modalMessageInstance.message = "Tài khoản của bạn bị tạm khóa đến <b>" + this.userInfo.unBanDate + "</b></br>" +
                        "Tất cả bài đăng sẽ bị ẩn " + "</br>" +
                        "Chức năng <b>Đăng Tin</b> và <b>Nạp Tiền</b> bị khóa";
                    modalMessageInstance.showModal()
                    return
                }
                if(post.banned){
                    modalMessageInstance.title = "Thông báo"
                    modalMessageInstance.message = "Bài đăng của bạn đã bị khóa!";
                    modalMessageInstance.showModal()
                    return
                }
                if(post.visible){
                    modalConfirmInstance.messageConfirm = "Bạn có muốn ẩn bài viết này không?"
                }else{
                    modalConfirmInstance.messageConfirm = "Bạn có muốn hiển thị bài viết này không?"
                }
                sessionStorage.setItem("confirmAction", "hide-post")
            }
            modalConfirmInstance.showModal()
        },
        getHistoryPaymentPost(currentPage){
            if (currentPage === undefined || !currentPage) {
                currentPage = 0;
            }
            let request = {
                'landlord' : this.userInfo.username
            }

            fetch("/api-get-history-payment-post?currentPage=" + currentPage, {
                method: 'POST',
                headers:{
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify(request)

            }).then(response => response.json())
                .then((data) => {
                    console.log(data);
                    if(data != null && data.code == "000"){
                        this.listPaymentPost = data.data
                        this.pagination = data.pagination
                    }else {
                        modalMessageInstance.title = "Thông báo"
                        modalMessageInstance.message = data.message;
                        modalMessageInstance.showModal()
                    }
                    authenticationInstance.hidePreloader()
                }).catch(error => {
                console.log(error);
            })
        },
        getInitNewPost(){
            fetch("/api-get-init-new-post", {
                method: 'POST',

            }).then(response => response.json())
                .then((data) => {
                    console.log(data);
                    if(data != null && data.listPaymentPackage != null){
                        this.listPaymentPackage = data.listPaymentPackage
                    }
                    if(data != null && data.listTypePost != null){
                        this.listTypePost = data.listTypePost
                    }
                    authenticationInstance.hidePreloader()
                }).catch(error => {
                console.log(error);
            })
        },
        generateRooms(){
            if(this.validateInput(this.numberOfRoom, null, null,0, 100, "Số lượng phòng", "")){
                this.showMsgModal = false
            }


            this.listRoom = []
            let start = 0
            if(this.listRoomRequest != null && this.listRoomRequest.length > 0){
                start = this.listRoomRequest.length
            }
            for (let i = start; i < parseInt(this.numberOfRoom) + start; i++) {
                let room = {
                    "index" : (i+1),
                    "roomName" : "Phòng " + (i+1),
                    "availableRoom" : true
                }
                this.listRoom.push(room)
            }
        },
        openFileDialog(){
            let inputFileElem = document.getElementById("file-browse")
            if(inputFileElem && document.createEvent){
                let event = document.createEvent("MouseEvents")
                event.initEvent("click", true, false)
                inputFileElem.dispatchEvent(event)
            }
        },
        processFile(event){
            let files = event.target.files
            for (let i = 0; i < files.length; i++) {
                let reader = new FileReader();
                reader.onload = function(e) {
                    if(landlordInstance.uploadImages == null || landlordInstance.uploadImages == undefined){
                        landlordInstance.uploadImages = [e.target.result]
                    }else {
                        landlordInstance.uploadImages.push(e.target.result)
                    }

                };
                reader.onerror = function(error) {
                    alert(error);
                };
                reader.readAsDataURL(files[i]);
            }
            // for (let i = 0; i < files.length; i++) {
            //     formData.append("files",files[i])
            // }

        },
        removeImage(image){
            this.uploadImages.splice(this.uploadImages.indexOf(image), 1)
        },
        calculateCost(package){
            this.userInfo = JSON.parse(sessionStorage.getItem("userInfo"))
            this.amountSelected = package.amount
            if(package.amount > this.userInfo.amount){
                this.validateMessage = "Số tiền trong tài khoản không đủ"
                if(this.editMode){
                    this.showMsgModal = true
                }else {
                    this.showMsg = true
                }
                this.duration = 0
            }else {
                if(this.editMode){
                    this.showMsgModal = false
                }
            }

        },
        handleAddNewPost(){
            if (this.editMode){
                if(!this.validateInputForEditing()){
                    this.showMsg = true
                    return
                }
                this.showMsg = false
                this.editPost()
            }else{
                if(!this.validateInputForSaving()){
                    this.showMsg = true
                    return
                }
                let request = {
                    'typeId' : this.typeOfPost,
                    'title' : this.title,
                    'description' : this.detailInfo,
                    'price': this.price,
                    'square' : this.square,
                    'distance' : this.distance,
                    'roomNumber' : this.numberOfRoom,
                    'username' : this.userInfo.username,
                    'listRoom' : this.listRoom,
                    'listImage' : this.uploadImages,
                    'paymentPackageId' : this.duration,
                    'address' : this.inputAddress,
                    'mapLocation' : this.latMarkerEl.value + ", " + this.longMarkerEl.value
                }
                let options = {
                    method: 'POST',
                    headers:{
                        'Content-Type': 'application/json',
                    },
                    body: JSON.stringify(request)
                }
                loadingInstance.isHidden = false
                //delete options.headers['Content-Type']
                fetch("/api-add-new-post", options)
                    .then(response => response.json())
                    .then((data) => {
                        console.log(data);
                        if(data != null && data.code == '000'){
                            window.location.href = "/post-detail?id=" + data.data
                        }else if(data != null && data.code == "001"){
                            this.showMsg = true
                            this.validateMessage = data.message
                            loadingInstance.isHidden = true
                        }else {
                            loadingInstance.isHidden = true
                            modalMessageInstance.message = data.message
                            modalMessageInstance.showModal()
                        }
                    }).catch(error => {
                    console.log(error);
                })
            }

        },
        viewListPost(currentPage){
            if (currentPage === undefined || !currentPage) {
                currentPage = 0;
            }
            let request = {
                'username' : this.userInfo.username,
            }
            fetch("/api-view-list-post?currentPage=" + currentPage, {
                method: 'POST',
                headers:{
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify(request)

            }).then(response => response.json())
                .then((data) => {
                    console.log(data);
                    if(data != null && data.code == "000"){
                        this.listPost = data.data
                        this.pagination = data.pagination
                    }
                    authenticationInstance.hidePreloader()
                }).catch(error => {
                console.log(error);
            })
        },
        changeStatusPost(){
            processingLoaderInstance.showLoader()
            let request = {
                'postId' : this.selectedPost.id,
                'isVisible' : !this.selectedPost.visible
            }
            fetch("/api-change-post-status", {
                method: 'POST',
                headers:{
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify(request)

            }).then(response => response.json())
                .then((data) => {
                    processingLoaderInstance.hideLoader()
                    if(data != null && data.code == "000"){
                        authenticationInstance.showModalNotify("Cập nhật thành công", 2000)
                        setTimeout(() => {
                            // this.$set(this.listPost, this.postIndex, data.post)
                            this.viewListPost()
                        }, 2000);
                    }
                }).catch(error => {
                console.log(error);
            })
        },
        handleEditPost(post){
            if(post.banned){
                modalMessageInstance.title = "Thông báo"
                modalMessageInstance.message = "Bài đăng của bạn đã bị khóa";
                modalMessageInstance.showModal()
                return
            }
            userTaskInstance.task = 16
            noteInstance.task = 16
            this.task = 16
            sessionStorage.setItem("task", 16)
            this.setFieldEditMode(post)
            setTimeout( () => {
                this.initMap()
                this.handleDisplayDirection()
            }, 1000)
        },
        setFieldEditMode(post){
            this.selectedPost = post
            sessionStorage.setItem("selectedPost", JSON.stringify(post))
            this.editMode = true
            this.typeOfPost = post.type.id
            this.title = post.title
            this.detailInfo = post.description
            this.price = post.price
            this.square = post.square
            this.distance = post.distance
            // this.numberOfRoom = post.roomNumber
            // this.listRoom = post.listRoom
            this.inputAddress = post.address
            // this.uploadImages = post.listImage
            this.expireDate = authenticationInstance.formatDate(post.expireDate)
            this.postId = post.id
            this.getListImageByPost(post.id)
            this.getListRoomByPost(post.id)
        },
        getListImageByPost(postId){
            let request = {
                'postId' : postId,
            }
            let options = {
                method: 'POST',
                headers:{
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify(request)
            }
            fetch("/api-get-images-by-post", options)
                .then(response => response.json())
                .then((data) => {
                    console.log(data);
                    if(data != null && data.code == '000'){
                        this.uploadImages = data.data
                    }
                }).catch(error => {
                console.log(error);
            })
        },
        getListRoomByPost(postId){
            let request = {
                'postId' : postId,
                'roomId' : null,
                'statusId' : null,
            }
            if(this.task == 16){
                this.getListRoom(request, -1)
            }else {
                this.getListRoom(request)
            }

        },
        getListRoomPaging(currentPage){
            if(this.task == 15){
                let request = {
                    'postId' : this.selectedPost.id,
                    'roomId' : null,
                    'statusId' : null,
                }
                this.getListRoom(request, currentPage)
            }else if(this.task == 5){
                let request = {
                    'statusId' : 7,
                    'postId' : null,
                    'roomId' : null,
                }
                this.getListRoom(request, currentPage)
            }
        },
        getListRoom(request, currentPage){
            if (currentPage === undefined || !currentPage) {
                currentPage = 0;
            }
            let options = {
                method: 'POST',
                headers:{
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify(request)
            }
            fetch("/api-get-rooms?currentPage=" + currentPage, options)
                .then(response => response.json())
                .then((data) => {
                    authenticationInstance.hidePreloader()
                    if(data != null && data.code == '000'){
                        this.listRoomRequest = data.data
                        this.pagination = data.pagination
                        if(this.task == 17){
                            let notification = JSON.parse(sessionStorage.getItem("notification"))
                            this.getListRequestByRoom(this.listRoomRequest[0], 0, null, notification.requestId)
                        }
                    }
                }).catch(error => {
                console.log(error);
            })
        },
        getRenterInfo(renterUsername, openModal){
            let request = {
                'renterUsername' : renterUsername
            }
            let options = {
                method: 'POST',
                headers:{
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify(request)
            }
            fetch("/api-get-renter-info", options)
                .then(response => response.json())
                .then((data) => {
                    console.log(data);
                    if(data != null && data.code == '000'){
                        this.renterInfo = data.data
                        if(openModal != null && openModal){
                            this.showRenterInfo()
                        }
                    }
                }).catch(error => {
                console.log(error);
            })
        },
        getListRoomProcessingRequest(){
            let request = {
                'statusId' : 7,
                'postId' : null,
                'roomId' : null,
            }
            this.getListRoom(request)
        },
        getRoomById(roomId){
            let request = {
                'statusId' : null,
                'postId' : null,
                'roomId' : roomId,
            }
            this.getListRoom(request)
        },
        getListRequestByRoom(room, index, statusId, requestId){
            if(room.openCollapse != null && room.openCollapse == true){
                room.openCollapse = false
                this.$set(this.listRoomRequest, index, room)
                return
            }
            if(room.openCollapse != null && room.openCollapse == false && room.listRentalRequest != null){
                room.openCollapse = true
                this.$set(this.listRoomRequest, index, room)
                return
            }
            let request = {
                'roomId' : room.id,
                'statusId' : statusId,
                'id' : requestId,
            }
            let options = {
                method: 'POST',
                headers:{
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify(request)
            }
            fetch("/api-get-requests-by-room", options)
                .then(response => response.json())
                .then((data) => {
                    console.log(data);
                    if(data != null && data.code == '000'){
                        room.listRentalRequest = data.data
                        room.openCollapse = !room.openCollapse
                        this.$set(this.listRoomRequest, index, room)
                    }
                }).catch(error => {
                console.log(error);
            })
        },
        editPost(){
            processingLoaderInstance.showLoader()
            let request = {
                'postId' : this.postId,
                'typeId' : this.typeOfPost,
                'title' : this.title,
                'description' : this.detailInfo,
                'price': this.price,
                'square' : this.square,
                'distance' : this.distance,
                'username' : this.userInfo.username,
                'listImage' : this.uploadImages,
                'address' : this.inputAddress,
                'mapLocation' : this.latMarkerEl.value + ", " + this.longMarkerEl.value
            }
            let options = {
                method: 'POST',
                headers:{
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify(request)
            }
            fetch("/api-edit-post", options)
                .then(response => response.json())
                .then((data) => {
                    processingLoaderInstance.hideLoader()
                    if(data != null && data.code == '000'){
                        authenticationInstance.showModalNotify("Cập nhật thành công", 2000)
                        setTimeout(() =>
                        {
                            userTaskInstance.activeBtn(4)
                        }, 2000);
                    }
                }).catch(error => {
                console.log(error);
            })
        },
        showModalExtend(post, postIndex) {
            if(post != null && post.banned){
                modalMessageInstance.title = "Thông báo"
                modalMessageInstance.message = "Bài đăng của bạn đã bị khóa";
                modalMessageInstance.showModal()
                return
            }
            if(post != null){
                this.postId = post.id
                this.editMode = true
            }
            if(postIndex != null){
                this.postIndex = postIndex
            }
            this.duration = ""
            document.body.setAttribute("class", "loading-hidden-screen")
            document.getElementById("myModal_Extend").style.display = 'block';
        },
        closeModalExtend() {
            document.getElementById("myModal_Extend").style.display = 'none';
            document.body.removeAttribute("class")
            if(this.showMsgModal){
                this.showMsgModal = false
            }
        },
        extendTimePost(){
            if(!this.validateInput(this.duration, null, null,null, null, "Thời hạn bài đăng")
                || !this.validatePaymentPackageAmount()){
                this.showMsgModal = true
                return
            }
            processingLoaderInstance.showLoader()
            let request = {
                'postId' : this.postId,
                'paymentPackageId' : this.duration,
                'username' : this.userInfo.username,
            }
            let options = {
                method: 'POST',
                headers:{
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify(request)
            }
            fetch("/api-extend-time-of-post", options)
                .then(response => response.json())
                .then((data) => {
                    processingLoaderInstance.hideLoader()
                    if(data != null && data.code == '000'){
                        authenticationInstance.showModalNotify("Gia hạn thành công", 2000)
                        setTimeout(() => {
                            this.userInfo.amount = data.amount;
                            sessionStorage.setItem("userInfo", JSON.stringify(this.userInfo));
                            basicInfoInstance.userInfo.amount = data.amount;
                            this.selectedPost.expireDate = data.expireDate
                            sessionStorage.setItem("selectedPost", this.selectedPost)
                            this.expireDate = authenticationInstance.formatDate(data.expireDate);
                            this.viewListPost();
                            this.closeModalExtend();
                        }, 2000);
                    }else if(data != null && data.code == "001"){
                        this.showMsgModal = true
                        this.validateMessage = data.message
                    }else {
                        modalMessageInstance.message = data.message
                        modalMessageInstance.showModal()
                    }
                }).catch(error => {
                console.log(error);
            })
        },
        deletePost(){
            processingLoaderInstance.showLoader()
            let request = {
                'postId' : this.postId,
            }
            let options = {
                method: 'POST',
                headers:{
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify(request)
            }
            fetch("/api-delete-post", options)
                .then(response => response.json())
                .then((data) => {
                    processingLoaderInstance.hideLoader()
                    if(data != null && data.code == '000'){
                        authenticationInstance.showModalNotify("Đã xóa bài đăng", 2000)
                        setTimeout(() => this.viewListPost(), 2000);
                    }else {
                        modalMessageInstance.message = data.message
                        modalMessageInstance.showModal()
                    }
                }).catch(error => {
                console.log(error);
            })
        },
        acceptRentalRequest(){
            processingLoaderInstance.showLoader()
            let request = {
                'id' : this.selectedRequest.id,
                'roomId' : this.selectedRoom.id,
                "renterUsername" : this.selectedRequest.rentalRenter.username,
                "postTitle" : this.selectedRoom.postRoom.title,
                "roomName" : this.selectedRoom.name,
                "landlordUsername" : this.userInfo.username,
            }
            let options = {
                method: 'POST',
                headers:{
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify(request)
            }
            fetch("/api-accept-request", options)
                .then(response => response.json())
                .then((data) => {
                    processingLoaderInstance.hideLoader()
                    if(data != null && data.code == '000'){
                        authenticationInstance.showModalNotify("Cập nhật thành công", 2000)
                        if(this.task == 5){
                            this.getListRoomProcessingRequest()
                        }else if(this.task == 15){
                            this.getListRoomByPost(this.selectedPost.id)
                        }else if(this.task == 17){
                            let notification = JSON.parse(sessionStorage.getItem("notification"))
                            this.getRoomById(notification.roomId)
                        }

                    }
                }).catch(error => {
                console.log(error);
            })
        },
        rejectRentalRequest(){
            processingLoaderInstance.showLoader()
            let request = {
                'id' : this.selectedRequest.id,
                'roomId' : this.selectedRoom.id,
                "renterUsername" : this.selectedRequest.rentalRenter.username,
                "postTitle" : this.selectedRoom.postRoom.title,
                "roomName" : this.selectedRoom.name,
                "landlordUsername" : this.userInfo.username,
            }
            let options = {
                method: 'POST',
                headers:{
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify(request)
            }
            fetch("/api-reject-request", options)
                .then(response => response.json())
                .then((data) => {
                    processingLoaderInstance.hideLoader()
                    if(data != null && data.code == '000'){
                        authenticationInstance.showModalNotify("Cập nhật thành công", 2000)
                        if(this.task == 5){
                            this.getListRoomProcessingRequest()
                        }else if(this.task == 15){
                            this.getListRoomByPost(this.selectedPost.id)
                        }else if(this.task == 17){
                            let notification = JSON.parse(sessionStorage.getItem("notification"))
                            this.getRoomById(notification.roomId)
                        }
                    }
                }).catch(error => {
                console.log(error);
            })
        },
        handleViewRoom(post){
            userTaskInstance.task = 15
            noteInstance.task = 15
            this.task = 15
            sessionStorage.setItem("task", 15)
            sessionStorage.setItem("selectedPost", JSON.stringify(post))
            this.selectedPost = post
            this.getListRoomByPost(post.id)
        },
        handleChangeRoomStatus(room, index){
        
            this.roomIndex = index
            this.selectedRoom = room
            if(room.status.id == 1){
                modalConfirmInstance.messageConfirm = 'Bạn có muốn thay đổi trạng thái <b>' +room.name + '</b> thành <b>Đã cho thuê</b> không?';
                sessionStorage.setItem("confirmAction", "change-status-room")
                modalConfirmInstance.showModal()
            }else {
                if(room.listRentalRequest != null){
                    let stayRentalRequest = null
                    if(room.listRentalRequest != null){
                        for (let request of room.listRentalRequest) {
                            if(request.rentalStatus.id == 9){
                                stayRentalRequest = request
                                break
                            }
                        }
                    }
                    if (stayRentalRequest == null){
                        modalConfirmInstance.messageConfirm = 'Bạn có muốn thay đổi trạng thái <b>' +room.name + '</b> thành <b>Còn trống</b> không?';
                        sessionStorage.setItem("confirmAction", "change-status-room")
                        modalConfirmInstance.showModal()
                    }else {
                        this.selectedRequest = stayRentalRequest
                        this.getRenterInfo(stayRentalRequest.rentalRenter.username)
                        this.selectedRoom = room
                        this.message = "<p style='font-size: 20px; font-weight: 500'>Bạn có muốn làm mới phòng này?</p>" +
                            "<p>Trạng thái của phòng sẽ được thay đổi thành <b>Còn Trống</b></p>" +
                            "<p>Người thuê <b>" + stayRentalRequest.rentalRenter.username + "</b> sẽ kết thúc thuê phòng tại phòng này</p>"
                        document.body.setAttribute("class", "loading-hidden-screen")
                        document.getElementById("modalRequestDetail").style.display = 'block';
                    }
                }else {
                    let request = {
                        'roomId' : room.id,
                        'statusId' : null,
                        'id' : null,
                    }
                    let options = {
                        method: 'POST',
                        headers:{
                            'Content-Type': 'application/json',
                        },
                        body: JSON.stringify(request)
                    }
                    fetch("/api-get-requests-by-room", options)
                        .then(response => response.json())
                        .then((data) => {
                            console.log(data);
                            if(data != null && data.code == '000'){
                                room.listRentalRequest = data.data
                                this.$set(this.listRoomRequest, index, room)
                                let stayRentalRequest = null
                                if(room.listRentalRequest != null){
                                    for (let request of room.listRentalRequest) {
                                        if(request.rentalStatus.id == 9){
                                            stayRentalRequest = request
                                            break
                                        }
                                    }
                                }
                                if (stayRentalRequest == null){
                                    modalConfirmInstance.messageConfirm = 'Bạn có muốn thay đổi trạng thái <b>' +room.name + '</b> thành <b>Còn trống</b> không?';
                                    sessionStorage.setItem("confirmAction", "change-status-room")
                                    modalConfirmInstance.showModal()
                                }else {
                                    this.selectedRequest = stayRentalRequest
                                    this.getRenterInfo(stayRentalRequest.rentalRenter.username)
                                    this.selectedRoom = room
                                    this.message = "<p style='font-size: 20px; font-weight: 500'>Bạn có muốn làm mới phòng này?</p>" +
                                        "<p>Trạng thái của phòng sẽ được thay đổi thành <b>Còn Trống</b></p>" +
                                        "<p>Người thuê <b>" + stayRentalRequest.rentalRenter.username + "</b> sẽ kết thúc thuê phòng tại phòng này</p>"
                                    document.body.setAttribute("class", "loading-hidden-screen")
                                    document.getElementById("modalRequestDetail").style.display = 'block';
                                }
                            }
                        }).catch(error => {
                        console.log(error);
                    })
                }

            }

        },
        closeModalRequestDetail(){
            document.body.removeAttribute("class")
            document.getElementById("modalRequestDetail").style.display = 'none';
        },
        confirmModalRequestDetail(){
            this.changeRoomStatus()
            document.body.removeAttribute("class")
            document.getElementById("modalRequestDetail").style.display = 'none';
        },
        handleProcessRequest(room, request, action){
            this.selectedRoom = room;
            this.selectedRequest = request;
            if(action == 'accept'){
                modalConfirmInstance.messageConfirm = 'Bạn có muốn chấp nhận yêu cầu thuê trọ của người dùng <b>' + request.rentalRenter.username + '</b> không?';
                sessionStorage.setItem("confirmAction", "accept-request")
            }else if(action == 'reject'){
                modalConfirmInstance.messageConfirm = 'Bạn có muốn từ chối yêu cầu thuê trọ của người dùng <b>' + request.rentalRenter.username + '</b> không?';
                sessionStorage.setItem("confirmAction", "reject-request")
            }
            modalConfirmInstance.showModal()
        },
        showRenterInfo(){
            document.body.setAttribute("class", "loading-hidden-screen")
            document.getElementById("modalUserDetail").style.display = 'block';
        },
        closeModalUserDetail(){
            document.body.removeAttribute("class")
            document.getElementById("modalUserDetail").style.display = 'none';
        },
        changeRoomStatus(){
            processingLoaderInstance.showLoader()
            let request = {
                "expireMessage" : this.expireMessage,
                "roomId" : this.selectedRoom.id,
                "postTitle" : this.selectedRoom.postRoom.title,
                "roomName" : this.selectedRoom.name,
                "landlordUsername" : this.userInfo.username,
                "statusId" : this.selectedRoom.status.id,
            }
            let options = {
                method: 'POST',
                headers:{
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify(request),
            }
            fetch("/api-change-room-status", options)
                .then(response => response.json())
                .then((data) => {
                    processingLoaderInstance.hideLoader()
                    if(data != null && data.code == '000'){
                        authenticationInstance.showModalNotify("Cập nhật thành công", 2000)
                        if(this.task == 5){
                            this.getListRoomProcessingRequest()
                        }else if(this.task == 15){
                            this.getListRoomByPost(this.selectedPost.id)
                        }else if(this.task == 17){
                            let notification = JSON.parse(sessionStorage.getItem("notification"))
                            this.getRoomById(notification.roomId)
                        }
                    }
                }).catch(error => {
                console.log(error);
            })
        },
        showAddRoomModal(){
            this.numberOfRoom = 0
            document.body.setAttribute("class", "loading-hidden-screen")
            document.getElementById("myModal_AddRoom").style.display = 'block';
        },
        closeModalAddRoom(){
            document.body.removeAttribute("class")
            document.getElementById("myModal_AddRoom").style.display = 'none';
            this.showMsgModal = false
        },
        increaseRoom(){
            if(!this.validateInput(this.numberOfRoom, null, null,0, 100, "Số lượng phòng", "")){
                this.showMsgModal = true
                return
            }
            this.showMsgModal = false
            let request = {
                "listRoom" : this.listRoom,
                "postId" : this.postId,
            }
            let options = {
                method: 'POST',
                headers:{
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify(request)
            }
            fetch("/api-increase-room", options)
                .then(response => response.json())
                .then((data) => {
                    console.log(data);
                    if(data != null && data.code == '000'){
                        authenticationInstance.showModalNotify("Thêm phòng thành công", 2000)
                        this.getListRoomByPost(this.selectedPost.id)
                        setTimeout(() => {
                            document.body.removeAttribute("class")
                            document.getElementById("myModal_AddRoom").style.display = 'none';
                        }, 2000);
                    }
                }).catch(error => {
                console.log(error);
            })


        },
        initMap(){
            let mapOptions, marker, searchBox,
                infoWindow = '',
                addressEl = document.querySelector( '#map-search' ),
                element = document.getElementById( 'map-canvas' )
            this.latMarkerEl = document.querySelector( '#latitude' )
            this.longMarkerEl = document.querySelector( '#longitude' )
            mapOptions = {
                zoom: 16,
                center: new google.maps.LatLng( 21.013237, 105.527018 ),
                disableDefaultUI: false, // Disables the controls like zoom control on the map if set to true
                scrollWheel: true, // If set to false disables the scrolling on the map.
                draggable: true, // If set to false , you cannot move the map around.
            }
            // Create an object map with the constructor function Map()
            this.map = new google.maps.Map( element, mapOptions ); // Till this like of code it loads up the map.

            marker = new google.maps.Marker({
                position: mapOptions.center,
                map: this.map,
                draggable: true
            });

            /**
             * Creates a search box
             */
            searchBox = new google.maps.places.SearchBox( addressEl );

            /**
             * When the place is changed on search box, it takes the marker to the searched location.
             */
            google.maps.event.addListener( searchBox, 'places_changed', function () {
                var places = searchBox.getPlaces(),
                    bounds = new google.maps.LatLngBounds(),
                    i, place,
                    address = places[0].formatted_address;

                for( i = 0; place = places[i]; i++ ) {
                    bounds.extend( place.geometry.location );
                    marker.setPosition( place.geometry.location );  // Set marker position new.
                }

                landlordInstance.map.fitBounds( bounds );  // Fit to the bound
                landlordInstance.map.setZoom( 15 ); // This function sets the zoom to 15, meaning zooms to level 15.
                // console.log( map.getZoom() );

                landlordInstance.latMarkerEl.value = marker.getPosition().lat();
                landlordInstance.longMarkerEl.value = marker.getPosition().lng();
                landlordInstance.handleCalculateDistance()

                // Closes the previous info window if it already exists
                if ( infoWindow ) {
                    infoWindow.close();
                }
                /**
                 * Creates the info Window at the top of the marker
                 */
                infoWindow = new google.maps.InfoWindow({
                    content: address
                });

                infoWindow.open( landlordInstance.map, marker );
            } );


            /**
             * Finds the new position of the marker when the marker is dragged.
             */
            google.maps.event.addListener( marker, "dragend", function ( event ) {
                let address

                let geocoder = new google.maps.Geocoder();
                geocoder.geocode( { latLng: marker.getPosition() }, function ( result, status ) {
                    if ( 'OK' === status ) {  // This line can also be written like if ( status == google.maps.GeocoderStatus.OK ) {
                        address = result[0].formatted_address;

                        addressEl.value = address;
                        landlordInstance.latMarkerEl.value = marker.getPosition().lat();
                        landlordInstance.longMarkerEl.value = marker.getPosition().lng();
                        landlordInstance.handleCalculateDistance()
                    } else {
                        console.log( 'Geocode was not successful for the following reason: ' + status );
                    }

                    // Closes the previous info window if it already exists
                    if ( infoWindow ) {
                        infoWindow.close();
                    }

                    /**
                     * Creates the info Window at the top of the marker
                     */
                    infoWindow = new google.maps.InfoWindow({
                        content: address
                    });

                    infoWindow.open( landlordInstance.map, marker );
                } );
            });
        },
        save_results(response, status) {

            if (status != google.maps.DistanceMatrixStatus.OK) {
                console.log(status)
            } else {
                if (response.rows[0].elements[0].status === "ZERO_RESULTS") {
                    console.log(response.rows[0].elements[0].status)
                } else {
                    let distance = response.rows[0].elements[0].distance;
                    // let duration = response.rows[0].elements[0].duration;
                    let distance_in_kilo = distance.value / 1000; // the kilo meter
                    // let distance_in_mile = distance.value / 1609.34; // the mile
                    let num = Number(distance_in_kilo)
                    let roundedString = num.toFixed(1)
                    this.distance = Number(roundedString)
                }
            }
        },
        calculateDistance(travel_mode, origin, destination) {
            let DistanceMatrixService = new google.maps.DistanceMatrixService();
            DistanceMatrixService.getDistanceMatrix(
                {
                    origins: [origin],
                    destinations: [destination],
                    travelMode: google.maps.TravelMode[travel_mode],
                    // unitSystem: google.maps.UnitSystem.IMPERIAL, // miles and feet.
                    unitSystem: google.maps.UnitSystem.metric, // kilometers and meters.
                    avoidHighways: false,
                    avoidTolls: false
                }, landlordInstance.save_results);
        },
        displayRoute(travel_mode, origin, destination, directionsService, directionsDisplay) {
            landlordInstance.initMap()
            directionsService.route({
                origin: origin,
                destination: destination,
                travelMode: travel_mode,
                avoidTolls: true
            }, function (response, status) {
                if (status === 'OK') {
                    directionsDisplay.setMap(landlordInstance.map);
                    directionsDisplay.setDirections(response);
                } else {
                    directionsDisplay.setMap(null);
                    directionsDisplay.setDirections(null);
                    alert('Could not display directions due to: ' + status);
                }
            });
        },
        handleCalculateDistance(){
            let origin = this.latMarkerEl.value + ", " + this.longMarkerEl.value;
            let destination = this.fuLocation;
            let travel_mode = "DRIVING";
            let directionsDisplay = new google.maps.DirectionsRenderer({'draggable': false});
            let directionsService = new google.maps.DirectionsService();
            this.displayRoute(travel_mode, origin, destination, directionsService, directionsDisplay);
            this.calculateDistance(travel_mode, origin, destination)
        },
        handleDisplayDirection(){
            let origin = this.selectedPost.mapLocation;
            this.latMarkerEl.value = this.selectedPost.mapLocation.split(", ")[0];
            this.longMarkerEl.value = this.selectedPost.mapLocation.split(", ")[1];
            let destination = this.fuLocation;
            let travel_mode = "DRIVING";
            let directionsDisplay = new google.maps.DirectionsRenderer({'draggable': false});
            let directionsService = new google.maps.DirectionsService();
            this.displayRoute(travel_mode, origin, destination, directionsService, directionsDisplay);
        },
        checkPaymentAmount() {
            if (this. paymentAmount != "" && this.paymentAmount  < 1000 || this.paymentAmount > 1000000 ) {
                document.getElementById("notify_paymentAmount").innerHTML="Số tiền phải từ 1.000vnđ -> 1.000.000vnđ.";
            } else {
                document.getElementById("notify_paymentAmount").innerHTML="Bạn phải nhập số tiền muốn nạp.";
            }
            // paymentAmountTxt
            if (this.paymentAmount.length == 0 || this.paymentAmount  < 1000 || this.paymentAmount > 1000000) {
                document.getElementById("notify_paymentAmount").classList.remove("invisible");
                document.getElementById("paymentAmountTxt").classList.add("border-error");
            } else {
                document.getElementById("notify_paymentAmount").classList.add("invisible");
                document.getElementById("paymentAmountTxt").classList.remove("border-error");
                this.requestMomoPayment();
            }
        },
        requestMomoPayment() {
            fetch("/request-momo-payment", {
                method: 'POST',
                body: this.paymentAmount
            })
                .then(response => response.json())
                .then((data) => {
                    if (data.code == "000") {
                        window.location.href = data.momoPayUrl;
                    }
                })
        },
        getRespone: function () {
            fetch("payment")
                .then(response => response.json())
                .then((data) => {

                })
        },
        showModal: function () {
            this.showModal = false;
            this.$http.get
        },
        getHistoryPayment(currentPage){
            if (currentPage === undefined || !currentPage) {
                currentPage = 0;
            }
            let request = {
                'landlord' : this.userInfo.username
            }
            fetch("/api-get-payment-by-landlord?currentPage=" + currentPage, {
                method: 'POST',
                headers:{
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify(request)
            })
            .then(response => response.json())
            .then((data) => {
                if (data != null && data.code == "000") {
                    this.listPayment = data.data
                    this.pagination = data.pagination
                }else {
                    modalMessageInstance.title = "Thông báo"
                    modalMessageInstance.message = data.message;
                    modalMessageInstance.showModal()
                }
                authenticationInstance.hidePreloader()
            })
        },
        checkStatusAndSavePayment(){
            let request = {
                'partnerCode' : sessionStorage.getItem("partnerCode"),
                'accessKey' : sessionStorage.getItem("accessKey"),
                'requestId' : sessionStorage.getItem("requestId"),
                'orderId' : sessionStorage.getItem("orderId"),
                'amount' : sessionStorage.getItem("amount"),
                'errorCode' : sessionStorage.getItem("errorCode"),
            }
            fetch("/api-check-status-payment", {
                method: 'POST',
                headers:{
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify(request)
            })
                .then(response => response.json())
                .then((data) => {
                    if (data != null && data.code == "000") {
                        authenticationInstance.hidePreloader()
                        basicInfoInstance.userInfo.amount = data.landlordAmount
                        authenticationInstance.showModalNotify("Quý khách đã nạp thành công <b>"
                            + authenticationInstance.formatNumberToDisplay(data.addAmount)
                            + " VND</b> vào tài khoản", 5000)
                        sessionStorage.removeItem("momo-check")
                        sessionStorage.removeItem("partnerCode")
                        sessionStorage.removeItem("accessKey")
                        sessionStorage.removeItem("requestId")
                        sessionStorage.removeItem("orderId")
                        sessionStorage.removeItem("amount")
                        sessionStorage.removeItem("errorCode")
                    }else if(data != null && data.code != "000"){
                        authenticationInstance.hidePreloader()
                        modalMessageInstance.title = "Thông báo"
                        modalMessageInstance.message = data.message;
                        modalMessageInstance.showModal()
                    }
                })
        },
        showExpireMessage(rentalRequest){
            modalMessageInstance.title = "Lời nhắn của người thuê"
            modalMessageInstance.message = rentalRequest.expireMessage
            modalMessageInstance.showModal()
        },
        validateInput(inputValue, minLength, maxLength, min, max, inputName, unit){
            if(inputValue == null || inputValue.length == 0){
                this.validateMessage = "<b>" + inputName + "</b> không được để trống"
                return false
            }else if(maxLength != null && inputValue != null && inputValue.length > maxLength){
                this.validateMessage = "<b>" + inputName + "</b> không vượt quá " + maxLength + " ký tự"
                return false
            }else if(minLength != null && inputValue != null && inputValue.length < minLength){
                this.validateMessage = "<b>" + inputName + "</b> phải có ít nhất " + minLength + " ký tự"
                return false
            }else if(min != null && inputValue != null && parseFloat(inputValue) <= min){
                this.validateMessage = "<b>" + inputName + "</b> phải lớn hơn " + authenticationInstance.formatNumberToDisplay(min) + " " + unit
                return false
            }else if(max != null && inputValue != null && parseFloat(inputValue) >= max){
                this.validateMessage = "<b>" + inputName + "</b> phải nhỏ hơn " + authenticationInstance.formatNumberToDisplay(max) + " " + unit
                return false
            }else {
                return true
            }
        },
        validateMapLocation(){
            if(this.latMarkerEl.value == null || this.latMarkerEl.value.length == 0
            || this.longMarkerEl.value == null || this.longMarkerEl.value.length == 0){
                this.validateMessage = "Bạn phải chọn một vị trí trên bản đồ"
                return false
            }else {
                return true
            }
        },
        validateImages(){
            if(this.uploadImages == null || this.uploadImages.length < 3){
                this.validateMessage = "Bạn phải thêm tối thiếu 3 hình ảnh cho phòng trọ của mình"
                return false
            }else if(this.uploadImages != null && this.uploadImages.length > 10){
                this.validateMessage = "Bạn chỉ có thể đăng tối đa 10 hình ảnh cho phòng trọ của mình"
                return false
            }else {
                return true
            }
        },
        removeDot(inputValue, datatype, event){
            if(datatype == "int"){
                if(event.keyCode < 48 || event.keyCode > 57){
                    event.preventDefault()
                }
            }else {
                if(event.keyCode == 46 && !inputValue.includes(".")){
                    return
                }else if(event.keyCode < 48 || event.keyCode > 57){
                    event.preventDefault()
                }
            }

        },
        validatePaymentPackageAmount(){
            if(this.amountSelected > this.userInfo.amount){
                this.validateMessage = "Số tiền trong tài khoản không đủ"
                return false
            }else {
                return true
            }
        },
        validateInputForSaving(){

            if(this.validateInput(this.typeOfPost, null, null, null, null, "Loại phòng")
            && this.validateInput(this.title, 10, 100, null, null, "Tiêu đề")
            && this.validateInput(this.detailInfo, 20, null,null, null, "Thông tin chi tiết")
            && this.validateInput(this.numberOfRoom, null, null,0, 100, "Số lượng phòng", "")
            && this.validateInput(this.price, null, null, 500000, 20000000, "Giá cho thuê", "VNĐ")
            && this.validateInput(this.square, null, null,5, 1000, "Diện tích", "M&sup2")
            && this.validateInput(this.distance, null, null, 0, 100, "Khoảng cách", "KM")
            && this.validateInput(this.inputAddress, 20, 100,null, null , "Địa chỉ")
            && this.validateMapLocation() && this.validateImages()
            && this.validateInput(this.duration, null, null,null, null, "Thời hạn bài đăng")
            && this.validatePaymentPackageAmount()
            ){
                return true
            }else {
                return false
            }
        },
        validateInputForEditing(){

            if(this.validateInput(this.typeOfPost, null, null, null, null, "Loại phòng")
                && this.validateInput(this.title, 10, 100, null, null, "Tiêu đề")
                && this.validateInput(this.detailInfo, 20, null,null, null, "Thông tin chi tiết")
                && this.validateInput(this.price, null, null, 500000, 20000000, "Giá cho thuê", "VNĐ")
                && this.validateInput(this.square, null, null,5, 1000, "Diện tích", "M&sup2")
                && this.validateInput(this.distance, null, null, 0, 100, "Khoảng cách", "KM")
                && this.validateInput(this.inputAddress, 20, 100,null, null , "Địa chỉ")
                && this.validateMapLocation() && this.validateImages()
            ){
                return true
            }else {
                return false
            }
        }
    }
})
var noteInstance = new Vue({
    el: '#note-wrapper',
    data: {
        userInfo: {},
        task: 0,
    },
    beforeMount(){
        this.userInfo = JSON.parse(sessionStorage.getItem("userInfo"))
        this.task = sessionStorage.getItem("task")
    }
})
var loadingInstance = new Vue({
    el: '#loading-wrapper',
    data: {
        isHidden: true
    },

})