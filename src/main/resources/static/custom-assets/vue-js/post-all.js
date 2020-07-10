
var postInstance = new Vue({
    el: '#row',
    data: {
        postList: [],
        post:{},
    },
    methods : {
        addWishlist : function(event){
            console.log(event.target.id)
            var status= "";
            var id=event.target.id
            if(event.target.style.color === "red"){
                event.target.style.color = "white";
                status="remove";
            } else if(event.target.style.color === "white"){
                event.target.style.color = "red";
                status="add";
            }
            fetch("https://localhost:8081/api-add-wishlist?id="+id+"&status="+status, {
                method: 'POST',

            }).then(response => response.json())
                .then((data) => {
                    console.log(data);
                    this.id=data
                    this.status=data
                }).catch(error => {
                console.log(error);
            })
        },
        getAllPost : function(){
            fetch("https://localhost:8081/api-get-all-post", {
                method: 'POST',

            }).then(response => response.json())
                .then((data) => {
                    console.log(data);
                    this.postList=data
                }).catch(error => {
                console.log(error);
            })
        },
        viewDetail: function (event) {
            var id=event.target.id
            fetch("https://localhost:8081/api-post-detail?id=" + id, {
                method: 'POST',
            }).then(response => response.json())
                .then((data) => {
                    console.log(data);

                    this.post = data;

                }).catch(error => {
                console.log(error);
            })
        },
    },
    created(){
        this.getAllPost();
    }
})