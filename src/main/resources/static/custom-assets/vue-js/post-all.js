
var postInstance = new Vue({
    el: '#postPage',
    data: {
        page:{},
        postList: [],
        pager:{},
        posts:{},
        endPage:null,
        pages:null,
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
        getPager : function(){
            fetch("https://localhost:8081/api-get-pager", {
                method: 'POST',

            }).then(response => response.json())
                .then((data) => {
                    console.log(data);
                    this.page=data.page;
                    this.endPage=data.endPage;
                }).catch(error => {
                console.log(error);
            })
        },
        getAll : function(){
            var query = window.location.search;
            var url = new URLSearchParams(query);
            var pages =url.get('pages');
            if(pages==null){
                return 1;
            }
            fetch("https://localhost:8081/api-get-posts?pages="+pages, {
                method: 'POST',

            }).then(response => response.json())
                .then((data) => {
                    console.log(data);
                    this.pageSize=data.pageSize;
                  //  this.pages=data;
                    this.page=data.page;
                    this.endPage= data.endPage;

                }).catch(error => {
                console.log(error);
            })
        }
    },
    created(){
        this.getPager();
        this.getAll();
    },
    mounted(){

    }
})