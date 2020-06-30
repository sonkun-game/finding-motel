var renterInstance = new Vue({
    el: '#renter-manager',
    data: {
        userInfo: {},
        task: 0,
        wishList: [],
    },
    beforeMount(){
        this.userInfo = JSON.parse(localStorage.getItem("userInfo"))
        this.task = localStorage.getItem("task")
    },
    mounted(){
        if(this.task == 2){
            let profileUser = document.getElementById("user-manager-content")
            profileUser.classList.add("invisible")
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
        }
    }
})
