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
        }
    },
    methods: {
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
            for (let i = 0; i < parseInt(this.numberOfRoom); i++) {
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
                        window.location.href = "/"
                    }
                }).catch(error => {
                console.log(error);
            })
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
        changeStatusPost(index, postId, isVisible){
            let request = {
                'postId' : postId,
                'isVisible' : isVisible
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
                        this.$set(this.listPost, index, data.post)
                        profileInstance.showNotifyModal()
                    }
                }).catch(error => {
                console.log(error);
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