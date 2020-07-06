var postDtl = new Vue({
    el: '#postDetailBody',
    data: {
        post: null,
    },
    methods: {
        viewDetail: function (event) {
            var id = event.target.id
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
        showModalReport() {
            document.getElementById("reportModal").style.display = 'block';
        },
        handleEventReportModal(event) {
            //close modal
            if (event.target.id.toString().includes('closeModal')) {
                document.getElementById("reportModal").style.display = "none";
            } else if (event.target.id.toString() === 'modalReportAcceptBtn') {
                this.sendReport();
            }
        },
        sendReport() {

        }
    },

})