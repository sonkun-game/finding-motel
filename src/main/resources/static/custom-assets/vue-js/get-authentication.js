var authenticationInstance = new Vue({
    el: '#header',
    data: {
        userInfo: {},
        authenticated: false,
        task: 0,
        isShowBtn: true,
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
        },
        validatePassword(password){
            if(password == null || password.length == 0){
                return null
            }
            let condition = {
                message: "Mật khẩu phải chứa tối thiểu 8 kí tự, bao gồm chữ thường, chữ hoa, chữ số và kí tự đặc biệt",
                regex: /^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[*.!@$%^&_-|]).{8,}$/
            }
            if(!condition.regex.test(password)){
                return condition.message
            }
            return "valid"
        },
        getTaskPage(task){
            localStorage.setItem("task", task)
            if(task == 13){
                window.location.href = "dang-tin"
            }else if(task == 0 || task == 1){
                window.location.href = "quan-ly-tai-khoan"
            }

        },
        formatNumberToDisplay(number){
            return number.toString().replace(/(\d)(?=(\d{3})+(?!\d))/g, '$1.')
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
                    this.authenticated = true
                }
            }).catch(error => {
            console.log(error);
        })
    }
})
