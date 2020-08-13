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
            this.initMap()
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
        else if(this.task == 17){
            let profileUser = document.getElementById("user-manager-content")
            profileUser.classList.add("invisible")
            let notification = JSON.parse(sessionStorage.getItem("notification"))
            this.getListRoomRequest(null, null, notification.roomId, notification.requestId)
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
            let request = {
                'landlord' : this.userInfo.username
            }

            fetch("/api-get-history-payment-post", {
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
                    }else {
                        modalMessageInstance.message = data.message;
                        modalMessageInstance.showModal()
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
            this.userInfo = JSON.parse(sessionStorage.getItem("userInfo"))
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
                        if(data != null && data.msgCode == 'post000'){
                            sessionStorage.setItem("userInfo", JSON.stringify(data.userInfo))
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
            this.setFieldEditMode(post)
            userTaskInstance.task = 16
            noteInstance.task = 16
            this.task = 16
            sessionStorage.setItem("task", 16)
            setTimeout( () => {
                this.initMap()
                this.handleDisplayDirection()
            }, 1000)
        },
        setFieldEditMode(post){
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
            this.inputAddress = post.address
            this.uploadImages = post.listImage
            this.expireDate = post.expireDate
            this.postId = post.id
            this.getListRoomRequest(null, post.id)
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
                    console.log(data);
                    if(data != null && data.msgCode == 'post000'){
                        authenticationInstance.showModalNotify("Cập nhật thành công", 2000)
                        setTimeout(() =>
                        {
                            userTaskInstance.activeBtn(4)
                            sessionStorage.setItem("userInfo", JSON.stringify(data.userInfo))
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
                            sessionStorage.setItem("userInfo", JSON.stringify(data.userInfo))
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
        getListRoomRequest(statusId, postId, roomId, requestId){
            let request = {
                'landlordUsername' : this.userInfo.username,
                'statusId' : statusId,
                'postId' : postId,
                'roomId' : roomId,
                'id' : requestId,
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
                        for (let i = 0; i < this.listRoomRequest.length; i++) {
                            this.listRoomRequest[i].listRentalRequest.sort(function (a, b) {
                                let dateA = new Date(a.requestDate),
                                    dateB = new Date(b.requestDate)
                                return dateB - dateA;
                            })
                        }
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
                        }else if(this.task == 17){
                            let notification = JSON.parse(sessionStorage.getItem("notification"))
                            this.getListRoomRequest(null, null, notification.roomId, notification.requestId)
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
                        }else if(this.task == 17){
                            let notification = JSON.parse(sessionStorage.getItem("notification"))
                            this.getListRoomRequest(null, null, notification.roomId, notification.requestId)
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
            this.getListRoomRequest(null, post.id)
        },
        handleChangeRoomStatus(room, index){
        
            this.roomIndex = index
            this.selectedRoom = room
            if(room.availableRoom){
                modalConfirmInstance.messageConfirm = 'Bạn có muốn thay đổi trạng thái <b>' +room.roomName + '</b> thành <b>Đã cho thuê</b> không?';
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
                    modalConfirmInstance.messageConfirm = 'Bạn có muốn thay đổi trạng thái <b>' +room.roomName + '</b> thành <b>Còn trống</b> không?';
                    sessionStorage.setItem("confirmAction", "change-status-room")
                    modalConfirmInstance.showModal()
                }else {
                    this.renterInfo = stayRentalRequest.renterInfo
                    this.selectedRequest = stayRentalRequest
                    this.message = "<h3>Bạn có muốn làm mới phòng này?</h3>" +
                        "<p>Trạng thái của phòng sẽ được thay đổi thành <b>Còn Trống</b></p>" +
                        "<p>Người dùng <b>" + this.renterInfo.username + "</b> sẽ bị xóa khỏi phòng này</p>"
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
                modalConfirmInstance.messageConfirm = 'Bạn có muốn chấp nhận yêu cầu thuê trọ của người dùng <b>' +request.renterUsername + '</b> không?';
                sessionStorage.setItem("confirmAction", "accept-request")
            }else if(action == 'reject'){
                modalConfirmInstance.messageConfirm = 'Bạn có muốn từ chối yêu cầu thuê trọ của người dùng <b>' +request.renterUsername + '</b> không?';
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
                    this.distance = distance_in_kilo
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
        getHistoryPayment(){
            let request = {
                'landlord' : this.userInfo.username
            }
            fetch("/api-get-payment-by-landlord", {
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
                }else {
                    modalMessageInstance.message = data.message;
                    modalMessageInstance.showModal()
                }
            })
        },
        formatDate(rawDate){
            let dateFormatString = rawDate.split(".")[0]
            let date = new Date(dateFormatString)
            return date.getHours() + ":" + date.getMinutes() + ":" + date.getSeconds()
                + " " + date.getDate() + "/" + date.getMonth() + "/" + date.getFullYear()
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
                        modalMessageInstance.message = data.message;
                        modalMessageInstance.showModal()
                    }
                })
        },
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