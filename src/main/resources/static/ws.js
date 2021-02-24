// let stompClient = null;
//
// var vm = new Vue({
//     el: "#app",
//     mounted: function() {
//         this.$nextTick(function() {
//             let socket = new SockJS("/api/tow-ws");
//             stompClient = Stomp.over(socket);
//             stompClient.connect(
//                 {},
//                 function(frame) {
//                     console.log("Connected: " + frame);
//
//                     stompClient.subscribe("/info/values", function(val) {
//                         vm.valuesList.push(JSON.parse(val.body));
//                     });
//                 }
//             );
//         });
//     },
//     data: function() {
//         return {
//             valuesList: []
//         };
//     }
// });