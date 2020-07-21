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
        getAll : function(){
            let query = window.location.search
            let url = new URLSearchParams(query)
            let page = url.get('page')
            if(page == null){
                page = 1
            }
            fetch("/api-get-posts?page="+page, {
                method: 'POST',

            }).then(response => response.json())
                .then((data) => {
                    console.log(data);
                    this.pageSize = data.pageSize;
                  //  this.pages=data;
                    this.postList = data.page.content;
                    this.page = data.page
                    this.endPage = data.endPage;

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
    created(){
        this.getAll();
    }
})