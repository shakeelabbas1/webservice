var stompClient = null;

function connect() {
    var socket = new SockJS('/payloadWS');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        console.log('Connected: ' + frame);
        stompClient.subscribe('/topic/realtimedelivery', function (deliveredResponse) {
        	var response = JSON.parse(deliveredResponse.body)
            showPayload(response.id,response.payloadText);
        });
    });
}

function showPayload(id,message) {
    $("#realtimeMessages").append("<tr><td>" + id + "</td><td>" + message + "</td></tr>");
}

$(function () {
	 connect();
});
