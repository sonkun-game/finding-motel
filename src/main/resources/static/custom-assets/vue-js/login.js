new Vue({
    el: '#login-form',
    data: {
        username: "",
        password: "",
        userInfo: {}
    },
    methods: {
        loginButtonClickEvent(){
            let userInfo = {
                "username": this.username,
                "password": this.password
            }
            fetch("http://localhost:8081/api-login",{
                method : 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body : JSON.stringify(userInfo)
            }).then(response => response.json())
                .then((data) => {
                    console.log(data)
                    localStorage.setItem("userInfo", JSON.stringify(data.userInfo))
                    this.$cookies.set("access_token", data.accessToken)
                    console.log(this.$cookies.get("access_token"))
                    window.location.href = "http://localhost:8081/"
                }).catch(error => {
                    console.log(error);
            })
        }
    }
})