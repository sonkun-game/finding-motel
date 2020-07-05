
var postDtl = new Vue({
    el: '#row',
    data: {
        post: null,
    },
    method:{
        viewDetail:function (event) {
            var id=event.target.id
            fetch("https://localhost:8081/api-post-detail?id="+id, {
                method: 'POST',
            }).then(response => response.json())
                .then((data) => {
                    console.log(data);
                    this.post=data;
                }).catch(error => {
                console.log(error);
            })
        }
    }
})