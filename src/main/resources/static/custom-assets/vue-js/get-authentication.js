new Vue({
    el: '#header',
    data: {

    },
    methods: {

    },
    mounted(){
        let accessToken = this.$cookies.get("access_token")
        fetch("http://localhost:8081/api-get-authentication",{
            method : 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': 'Bearer ' + accessToken
            }
        }).then(response => response.json())
            .then((data) => {
                console.log(data)
            }).catch(error => {
            console.log(error);
        })
    }
})