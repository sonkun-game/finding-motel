var vueInstance = new Vue({
    el: '#header',
    data: {
        userInfo: {},
        authenticationNum: 0,

    },
    methods: {
        logout(){
            fetch("https://localhost:8081/api-logout",{
                method : 'POST',
                headers: {
                    'Content-Type': 'application/json'
                }
            }).then(response => response.json())
                .then((data) => {
                    console.log(data)
                    if(data != null && data.code === "msg001"){
                        this.$cookies.remove("access_token")
                        this.$cookies.remove("token_provider")
                        window.location.href = "https://localhost:8081/"
                    }
                }).catch(error => {
                console.log(error);
            })
        }
    },
    mounted(){
        // if(localStorage.getItem("userInfo")){
        //     this.userInfo = JSON.parse(localStorage.getItem("userInfo"))
        //     if(this.userInfo.role == "RENTER"){
        //         this.authenticationNum = 1;
        //     }else if(this.userInfo.role == "LANDLORD"){
        //         this.authenticationNum = 2;
        //     }else{
        //         this.authenticationNum = 3;
        //     }
        //     return
        // }
        let accessToken = this.$cookies.get("access_token")
        let tokenProvider = this.$cookies.get("token_provider")
        fetch("https://localhost:8081/api-get-authentication",{
            method : 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': 'Bearer ' + accessToken,
                'Token-Provider': tokenProvider
            }
        }).then(response => response.json())
            .then((data) => {
                console.log(data)
                if(data != null && data.userInfo != null){
                    this.userInfo = data.userInfo
                    localStorage.setItem("userInfo", JSON.stringify(data.userInfo))
                    if(this.userInfo.role == "RENTER"){
                        this.authenticationNum = 1;
                    }else if(this.userInfo.role == "LANDLORD"){
                        this.authenticationNum = 2;
                    }else{
                        this.authenticationNum = 3;
                    }
                }
            }).catch(error => {
            console.log(error);
        })
    }
})
