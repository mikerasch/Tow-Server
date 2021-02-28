let stompClient = null;

var vm = new Vue({
    el: "#app",
    mounted: function () {
        this.$nextTick(function () {
            let socket = new SockJS("/api/ws");
            stompClient = Stomp.over(socket);
            stompClient.connect(
                {"Authorization": "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiI2NGM2NjAyZi1mZDg5LTQ3OWQtOWU4Zi05NTllNzQxNWM2YzMiLCJpYXQiOjE2MTQ0OTY4MzYsImV4cCI6MTYxNTEwMTYzNn0.IFAKoGbaTvIOVx5U1zJ4dicNzR3v5I9MVHf0YsV2PxQzHiOvs6lf6C0eyT-mUnJCmlZFQGu_kdmtCcLjAurblg"},
                function (frame) {
                    console.log("Connected: " + frame);

                    stompClient.subscribe("/info/values", function (val) {
                        vm.valuesList.push(JSON.parse(val.body));
                    });
                }
            );
        });
    },
    data: function () {
        return {
            valuesList: []
        };
    }
});