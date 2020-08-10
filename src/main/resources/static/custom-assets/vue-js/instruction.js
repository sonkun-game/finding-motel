var instructionInstance = new Vue({
    el: '#instruction-page',
    data: {
        userInfo: {},
        listInstruction : [],
        roleSelected : 1,
    },
    created(){
        this.getListInstruction()
    },
    beforeMount(){
        this.userInfo = JSON.parse(localStorage.getItem("userInfo"))
        if(this.userInfo != null && this.userInfo.role == 'RENTER'){
            this.roleSelected = 1
        }
    },
    methods : {
        getListInstruction(){
            fetch("/api-get-list-instruction", {
                method: 'GET',
                headers: {
                    'Content-Type': 'application/json',
                },

            }).then(response => response.json())
                .then((data) => {
                    console.log(data);
                    if(data != null && data.code == "000"){
                        this.listInstruction = data.data
                    }
                }).catch(error => {
                console.log(error);
            })
        },
    },

})