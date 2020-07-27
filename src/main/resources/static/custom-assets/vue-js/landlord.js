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
    },
    beforeMount(){
        this.userInfo = JSON.parse(localStorage.getItem("userInfo"))
        this.task = localStorage.getItem("task")
    },
    mounted(){
        if(this.task == 13){
            let profileUser = document.getElementById("user-manager-content")
            profileUser.classList.add("invisible")
            this.getInitNewPost()
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
            this.getListRoomRequest(7)
        }else if(this.task == 15){
            let profileUser = document.getElementById("user-manager-content")
            profileUser.classList.add("invisible")
            this.selectedPost = JSON.parse(sessionStorage.getItem("selectedPost"))
            this.getListRoomRequest(null, this.selectedPost.id)
        }
        else if(this.task == 16){
            let profileUser = document.getElementById("user-manager-content")
            profileUser.classList.add("invisible")
            this.getInitNewPost()
            let post = JSON.parse(sessionStorage.getItem("selectedPost"))
            this.handleEditPost(post)
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
                    modalMessageInstance.message = "Tài khoản của bạn bị tạm khóa đến " + this.userInfo.unBanDate + "</br>" +
                        "Tất cả bài đăng sẽ bị ẩn " + "</br>" +
                        "Chức năng Đăng Tin và Nạp Tiền bị khóa";
                    modalMessageInstance.showModal()
                    return
                }
                if(post.banned){
                    modalMessageInstance.message = "Bài đăng của bạn đã bị khóa!";
                    modalMessageInstance.showModal()
                    return
                }
                if(post.postVisible){
                    modalConfirmInstance.messageConfirm = "Bạn có muốn ẩn bài viết này không?"
                }else{
                    modalConfirmInstance.messageConfirm = "Bạn có muốn hiển thị bài viết này không?"
                }
                sessionStorage.setItem("confirmAction", "hide-post")
            }
            modalConfirmInstance.showModal()
        },
        getHistoryPaymentPost(){
            fetch("/api-get-history-payment-post", {
                method: 'POST',

            }).then(response => response.json())
                .then((data) => {
                    console.log(data);
                    if(data != null){
                        this.listPaymentPost = data
                    }
                }).catch(error => {
                console.log(error);
            })
        },
        getHistoryPayment(){
            fetch("/api-get-history-payment", {
                method: 'POST',

            }).then(response => response.json())
                .then((data) => {
                    console.log(data);
                    if(data != null){
                        this.listPayment = data
                    }
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
                }).catch(error => {
                console.log(error);
            })
        },
        generateRooms(){

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
            this.userInfo = JSON.parse(localStorage.getItem("userInfo"))
            this.amountSelected = package.amount
        },
        handleAddNewPost(){
            if (this.editMode){
                this.editPost()
            }else{
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
                    'paymentPackageId' : this.duration
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
                        if(data != null && data.msgCode == 'post000'){
                            localStorage.setItem("userInfo", JSON.stringify(data.userInfo))
                            basicInfoInstance.userInfo = data.userInfo
                            window.location.href = "/"
                        }
                    }).catch(error => {
                    console.log(error);
                })
            }

        },
        viewListPost(){
            let request = {
                'username' : this.userInfo.username,
            }
            fetch("/api-view-list-post", {
                method: 'POST',
                headers:{
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify(request)

            }).then(response => response.json())
                .then((data) => {
                    console.log(data);
                    if(data != null && data.msgCode == "post000"){
                        this.listPost = data.listPost
                    }
                }).catch(error => {
                console.log(error);
            })
        },
        changeStatusPost(){
            let request = {
                'postId' : this.selectedPost.id,
                'isVisible' : !this.selectedPost.postVisible
            }
            fetch("/api-change-post-status", {
                method: 'POST',
                headers:{
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify(request)

            }).then(response => response.json())
                .then((data) => {
                    console.log(data);
                    if(data != null && data.msgCode == "post000"){
                        authenticationInstance.showModalNotify("Cập nhật thành công", 2000)
                        setTimeout(() => {
                            this.$set(this.listPost, this.postIndex, data.post)
                        }, 2000);
                    }
                }).catch(error => {
                console.log(error);
            })
        },
        handleEditPost(post){
            if(post.banned){
                modalMessageInstance.message = "Bài đăng của bạn đã bị khóa";
                modalMessageInstance.showModal()
                return
            }
            this.selectedPost = post
            sessionStorage.setItem("selectedPost", JSON.stringify(post))
            this.editMode = true
            this.typeOfPost = post.typeId
            this.title = post.title
            this.detailInfo = post.description
            this.price = post.price
            this.square = post.square
            this.distance = post.distance
            // this.numberOfRoom = post.roomNumber
            // this.listRoom = post.listRoom
            this.uploadImages = post.listImage
            this.expireDate = post.expireDate
            this.postId = post.id
            this.getListRoomRequest(null, post.id)
            userTaskInstance.task = 16
            noteInstance.task = 16
            this.task = 16
            localStorage.setItem("task", 16)
        },
        editPost(){
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
                    console.log(data);
                    if(data != null && data.msgCode == 'post000'){
                        authenticationInstance.showModalNotify("Cập nhật thành công", 2000)
                        setTimeout(() =>
                        {
                            userTaskInstance.activeBtn(4)
                            localStorage.setItem("userInfo", JSON.stringify(data.userInfo))
                            basicInfoInstance.userInfo = data.userInfo
                        }, 2000);
                    }
                }).catch(error => {
                console.log(error);
            })
        },
        showModalExtend(post, postIndex) {
            if(post != null && post.banned){
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
        },
        extendTimePost(){
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
                    console.log(data);
                    if(data != null && data.msgCode == 'post000'){
                        authenticationInstance.showModalNotify("Gia hạn thành công", 2000)
                        setTimeout(() => {
                            localStorage.setItem("userInfo", JSON.stringify(data.userInfo))
                            basicInfoInstance.userInfo = data.userInfo
                            this.expireDate = data.post.expireDate
                            this.$set(this.listPost, this.postIndex, data.post)
                            this.closeModalExtend()
                        }, 2000);
                    }
                }).catch(error => {
                console.log(error);
            })
        },
        deletePost(){
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
                    console.log(data);
                    if(data != null && data.msgCode == 'post000'){
                        authenticationInstance.showModalNotify("Đã xóa bài đăng", 2000)
                        setTimeout(() => this.viewListPost(), 2000);
                    }
                }).catch(error => {
                console.log(error);
            })
        },
        getListRoomRequest(statusId, postId){
            let request = {
                'landlordUsername' : this.userInfo.username,
                'statusId' : statusId,
                'postId' : postId,
            }
            let options = {
                method: 'POST',
                headers:{
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify(request)
            }
            fetch("/api-view-list-request", options)
                .then(response => response.json())
                .then((data) => {
                    console.log(data);
                    if(data != null && data.msgCode == 'request000'){
                        this.listRoomRequest = data.listRoomRequest
                    }
                }).catch(error => {
                console.log(error);
            })
        },
        acceptRentalRequest(){
            let request = {
                'id' : this.selectedRequest.id,
                'roomId' : this.selectedRequest.roomId,
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
                    console.log(data);
                    if(data != null && data.msgCode == 'request000'){
                        if(this.task == 5){
                            this.getListRoomRequest(7)
                        }else if(this.task == 15){
                            this.getListRoomRequest(null, this.selectedPost.id)
                        }

                    }
                }).catch(error => {
                console.log(error);
            })
        },
        rejectRentalRequest(){
            let request = {
                'id' : this.selectedRequest.id,
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
                    console.log(data);
                    if(data != null && data.msgCode == 'request000'){
                        if(this.task == 5){
                            this.getListRoomRequest(7)
                        }else if(this.task == 15){
                            this.getListRoomRequest(null, this.selectedPost.id)
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
            localStorage.setItem("task", 15)
            sessionStorage.setItem("selectedPost", JSON.stringify(post))
            this.getListRoomRequest(null, post.id)
        },
        handleChangeRoomStatus(room, index){
        
            this.roomIndex = index
            this.selectedRoom = room
            if(room.availableRoom){
                modalConfirmInstance.messageConfirm = 'Bạn có muốn thay đổi trạng thái "' +room.roomName + '" thành "Đã cho thuê" không?';
                sessionStorage.setItem("confirmAction", "change-status-room")
                modalConfirmInstance.showModal()
            }else {
                let stayRentalRequest = null
                for (let request of room.listRentalRequest) {
                    if(request.statusId == 9){
                        stayRentalRequest = request
                        break
                    }
                }
                if (stayRentalRequest == null){
                    modalConfirmInstance.messageConfirm = 'Bạn có muốn thay đổi trạng thái "' +room.roomName + '" thành "Còn trống" không?';
                    sessionStorage.setItem("confirmAction", "change-status-room")
                    modalConfirmInstance.showModal()
                }else {
                    this.renterInfo = stayRentalRequest.renterInfo
                    this.selectedRequest = stayRentalRequest
                    document.body.setAttribute("class", "loading-hidden-screen")
                    document.getElementById("modalRequestDetail").style.display = 'block';
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
                modalConfirmInstance.messageConfirm = 'Bạn có muốn chấp nhận yêu cầu thuê trọ của người dùng "' +request.renterUsername + '" không?';
                sessionStorage.setItem("confirmAction", "accept-request")
            }else if(action == 'reject'){
                modalConfirmInstance.messageConfirm = 'Bạn có muốn từ chối yêu cầu thuê trọ của người dùng "' +request.renterUsername + '" không?';
                sessionStorage.setItem("confirmAction", "reject-request")
            }
            modalConfirmInstance.showModal()
        },
        showRenterInfo(renterInfo){
            this.renterInfo = renterInfo
            document.body.setAttribute("class", "loading-hidden-screen")
            document.getElementById("modalUserDetail").style.display = 'block';
        },
        closeModalUserDetail(){
            document.body.removeAttribute("class")
            document.getElementById("modalUserDetail").style.display = 'none';
        },
        collapseRequestTable(room, index){
            room.openCollapse = !room.openCollapse
            this.$set(this.listRoomRequest, index, room)
        },
        changeRoomStatus(){
            let options = {
                method: 'POST',
                headers:{
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify(this.selectedRoom)
            }
            fetch("/api-change-room-status", options)
                .then(response => response.json())
                .then((data) => {
                    console.log(data);
                    if(data != null && data.msgCode == 'request000'){
                        authenticationInstance.showModalNotify("Cập nhật thành công", 2000)
                        setTimeout(() => {
                            this.$set(this.listRoomRequest, this.roomIndex, data.room)
                        }, 2000);
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
        },
        increaseRoom(){
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
                    if(data != null && data.msgCode == 'post000'){
                        authenticationInstance.showModalNotify("Thêm phòng thành công", 2000)
                        setTimeout(() => {
                            document.body.removeAttribute("class")
                            document.getElementById("myModal_AddRoom").style.display = 'none';
                            for (let room of data.listNewRoom) {
                                this.listRoomRequest.push(room)
                            }
                        }, 2000);
                    }
                }).catch(error => {
                console.log(error);
            })


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
        this.userInfo = JSON.parse(localStorage.getItem("userInfo"))
        this.task = localStorage.getItem("task")
    }
})
var loadingInstance = new Vue({
    el: '#loading-wrapper',
    data: {
        isHidden: true
    },

})