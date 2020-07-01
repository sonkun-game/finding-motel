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
        square: "",
        distance: "",
        duration: "",
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
        }
    },
    methods: {
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
                    let options = {
                        method: 'POST',
                        headers:{
                            'Content-Type': 'application/json',
                        },
                        body: JSON.stringify({
                            'imgBase64': landlordInstance.uploadImages[0]
                        })
                    }
                    //delete options.headers['Content-Type']
                    fetch("/api-upload-image", options)
                        .then(response => response.json())
                        .then((data) => {
                            console.log(data);
                        }).catch(error => {
                        console.log(error);
                    })

                };
                reader.onerror = function(error) {
                    alert(error);
                };
                reader.readAsDataURL(files[i]);
            }
            // for (let i = 0; i < files.length; i++) {
            //     formData.append("files",files[i])
            // }

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
