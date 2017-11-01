/**
 * Created by Son on 7/2/2017.
 */
var HOME_URL = "http://localhost:8080/";

self.addEventListener('push', function (event) {
    var tag = 'simple-push-demo-notification-tag';
    var data = event.data.json();
    console.info(data);
    for (var i = 0; i < data.length; i++) {
        self.registration.showNotification(data[i].mamonhoc, {
            body: "Môn: " + data[i].ten + "\nĐiểm: " + data[i].tk,
            icon: "icon.png",
            tag: tag
        })
    }
    event.waitUntil(function () {

        }
    );
});

self.addEventListener('notificationclick', function (event) {
    console.info(event);
    // const clickedNotification = event.notification;
    // clickedNotification.close();
    //
    // // Do something as the result of the notification click
    // const promiseChain = doSomething();
    // event.waitUntil(promiseChain);
});

self.addEventListener('message', function (event) {
    console.log("SW Received Message: " + event.data);
});
