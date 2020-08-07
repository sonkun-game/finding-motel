var postInstance = new Vue({
    el: '#postPage',
    data: {
        userInfo: {},
        page : {},
        postList: [],
        pager : {},
        posts : {},
        endPage : 0,
        pages : 0,
        pageSize : 0,
        postIndex : -1,

    },
    beforeMount(){
        this.userInfo = JSON.parse(localStorage.getItem("userInfo"))
    },
    methods : {
        addWishlist : function(post, username){
            let request = {
                "postId" : post.id,
                "renterUsername" : username
            }
            fetch("/api-add-wishlist", {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify(request)

            }).then(response => response.json())
                .then((data) => {
                    if(data != null && data.msgCode == "wishlist002"){
                        window.location.href = "/dang-nhap"
                    }else if(data != null && data.msgCode == "wishlist000"){
                        authenticationInstance.showModalNotify("Đã thêm vào danh sách yêu thích", 1000)
                        this.postList[this.postIndex].inWishList = true
                        this.postList[this.postIndex].wishListId = data.wishList.id
                    }else {
                        modalMessageInstance.message = "Lỗi hệ thống!"
                        modalMessageInstance.showModal()
                        return null
                    }
                }).catch(error => {
                console.log(error);
            })
        },
        handleActionWishList(post){
            this.postIndex = this.postList.indexOf(post)
            if(this.userInfo == null){
                window.location.href = "/dang-nhap"
            }else {
                if(post.inWishList){
                    this.removeFromWishList(post.wishListId, this.userInfo.username)
                }else {
                    this.addWishlist(post, this.userInfo.username)

                }
            }
        },
        removeFromWishList(wishListId, username){
            let request = {
                "id" : wishListId,
                "renterUsername" : username
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
                        authenticationInstance.showModalNotify("Đã xóa bài đăng khỏi danh sách yêu thích", 1000);
                        this.postList[this.postIndex].inWishList = false
                        this.postList[this.postIndex].wishListId = null
                    }
                }).catch(error => {
                console.log(error);
            })
        },

    },
})
var filterPostInstance = new Vue({
    el: '#filter-container',
    data: {
        listTypePost : [],
        listFilterPrice : [],
        listFilterSquare : [],
        listFilterDistance : [],
        page : 1,
        typeId : 0,
        filterPriceId : 0,
        filterSquareId : 0,
        filterDistanceId : 0,
        titleTypePostPage : "",
        currentUrl : "",
    },
    methods : {
        getInitHomePage(){
            fetch("/api-get-init-home-page", {
                method: 'GET',
                headers: {
                    'Content-Type': 'application/json',
                },

            }).then(response => response.json())
                .then((data) => {
                    console.log(data);
                    if(data != null && data.msgCode == "home000"){
                        this.listTypePost = data.listTypePost
                        this.listFilterPrice = data.listFilterPrice
                        this.listFilterSquare = data.listFilterSquare
                        this.listFilterDistance = data.listFilterDistance
                    }
                }).catch(error => {
                console.log(error);
            })
        },
        isNullSearchParam(param) {
            return param == 0 ? null : param;
        },
        getPageFromQuery(){
            let query = window.location.search
            let url = new URLSearchParams(query)
            let page = url.get('page')
            if(page == null){
                this.page = 1
            }else {
                this.page = parseInt(page)
            }
            if(this.page < 1){
                window.location.href = "/"
            }
        },
        handleSearchClick(){
            this.getPageFromQuery()
            let typeId = ""
            let filterPriceId = $("#select2").val()
            let filterSquareId = $("#select3").val()
            let filterDistanceId = $("#select4").val()
            if(this.currentUrl.includes("/phong-tro")){
                typeId = 1
            }else if(this.currentUrl.includes("/can-ho")){
                typeId = 2
            }else {
                typeId = $("#select1").val()
                sessionStorage.setItem("typeId", typeId)
                sessionStorage.setItem("filterPriceId", filterPriceId)
                sessionStorage.setItem("filterSquareId", filterSquareId)
                sessionStorage.setItem("filterDistanceId", filterDistanceId)
            }
            if(this.page != 1){
                window.location.href = this.currentUrl
            }else {
                let postRequestDTO = {
                    "typeId": this.isNullSearchParam(parseInt(typeId)),
                    "filterPriceId": this.isNullSearchParam(parseInt(filterPriceId)),
                    "filterSquareId": this.isNullSearchParam(parseInt(filterSquareId)),
                    "filterDistanceId": this.isNullSearchParam(parseInt(filterDistanceId)),
                    "page": this.page,
                }
                this.filterPost(postRequestDTO)
            }

        },
        filterPost(request){
            fetch("/filter-post", {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify(request),
            }).then(response => response.json())
                .then((data) => {
                    if(data != null && data.msgCode == "home000"){
                        postInstance.pageSize = data.pageSize;
                        //  this.pages=data;
                        postInstance.postList = data.page.content;
                        postInstance.page = data.page
                        postInstance.endPage = data.endPage;
                    }
                }).catch(error => {
                console.log(error);
            })
        }
    },
    created() {
        this.getInitHomePage()
        this.getPageFromQuery()
        this.typeId = parseInt(sessionStorage.getItem("typeId"))
        this.filterPriceId = parseInt(sessionStorage.getItem("filterPriceId"))
        this.filterSquareId = parseInt(sessionStorage.getItem("filterSquareId"))
        this.filterDistanceId = parseInt(sessionStorage.getItem("filterDistanceId"))
        if(window.location.href.includes("phong-tro")){
            this.typeId = 1
            this.filterPriceId = 0
            this.filterSquareId = 0
            this.filterDistanceId = 0
            this.titleTypePostPage = "Phòng trọ"
            this.currentUrl = "/phong-tro"
        }else if(window.location.href.includes("can-ho")){
            this.typeId = 2
            this.filterPriceId = 0
            this.filterSquareId = 0
            this.filterDistanceId = 0
            this.titleTypePostPage = "Căn hộ"
            this.currentUrl = "/can-ho"
        }else {
            this.currentUrl = "/"
        }
        let request = {
            "typeId": this.isNullSearchParam(this.typeId),
            "filterPriceId": this.isNullSearchParam(this.filterPriceId),
            "filterSquareId": this.isNullSearchParam(this.filterSquareId),
            "filterDistanceId": this.isNullSearchParam(this.filterDistanceId),
            "page": this.page,
        }
        this.filterPost(request)
    },
    mounted(){
        window.addEventListener("load", function(event) {
            setTimeout(function () {
                var nice_Select = $('select');
                if(nice_Select.length){
                    nice_Select.niceSelect();
                }
            }, 100)

        });
        $('select.select').change(function(){
            filterPostInstance.handleSearchClick()
        });
    },
    updated(){
        $('select').niceSelect('update');
    }

})