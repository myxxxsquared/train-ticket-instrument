package notification.controller;

import notification.entity.NotifyInfo;














import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import notification.mq.RabbitSend;
import notification.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;

/**
 * @author Wenvi
 * @date 2017/6/15
 */
@RestController
@RequestMapping("/api/v1/notifyservice")
public class NotificationController { 
    private static final Logger logger = LoggerFactory.getLogger(NotificationController.class);
















    @Autowired
    NotificationService service;

    @Autowired
    RabbitSend sender;

    @Value("${test_send_mail_user}")
    String test_mail_user;

    @GetMapping(path = "/welcome")
    public String home() {
        logger.info("[function name:home, API:Get /api/v1/notifyservice/welcome]");
        return "Welcome to [ Notification Service ] !";
    }

    @GetMapping("/test_send_mq")
    public boolean test_send() {
        logger.info("[function name:test_send, API:Get /api/v1/notifyservice/test_send_mq]");
        sender.send("test");
        return true;
    }

    @GetMapping("/test_send_mail")
    public boolean test_send_mail() {
        logger.info("[function name:test_send_mail, API:Get /api/v1/notifyservice/test_send_mail]");
        NotifyInfo notifyInfo = new NotifyInfo();
        notifyInfo.setDate("Wed Jul 21 09:49:44 CST 2021");
        notifyInfo.setEmail(test_mail_user);
        notifyInfo.setEndPlace("Test");
        notifyInfo.setStartPlace("Test");
        notifyInfo.setOrderNumber("111-111-111");
        notifyInfo.setPrice("100");
        notifyInfo.setSeatClass("1");
        notifyInfo.setSeatNumber("1102");
        notifyInfo.setStartTime("Sat May 04 07:00:00 CST 2013");
        notifyInfo.setUsername("h10g");

        service.preserveSuccess(notifyInfo, null);
        return true;
    }

    @PostMapping(value = "/notification/preserve_success")
    public boolean preserve_success(@RequestBody NotifyInfo info, @RequestHeader HttpHeaders headers) {
        logger.info("[function name:{}, API:Post /api/v1/notifyservice/notification/preserve_success][info:{}, headers:{}]","preserve_success",(info != null ? info.toString(): null), (headers != null ? headers.toString(): null));
        return service.preserveSuccess(info, headers);
    }

    @PostMapping(value = "/notification/order_create_success")
    public boolean order_create_success(@RequestBody NotifyInfo info, @RequestHeader HttpHeaders headers) {
        logger.info("[function name:{}, API:Post /api/v1/notifyservice/notification/order_create_success][info:{}, headers:{}]","order_create_success",(info != null ? info.toString(): null), (headers != null ? headers.toString(): null));
        return service.orderCreateSuccess(info, headers);
    }

    @PostMapping(value = "/notification/order_changed_success")
    public boolean order_changed_success(@RequestBody NotifyInfo info, @RequestHeader HttpHeaders headers) {
        logger.info("[function name:{}, API:Post /api/v1/notifyservice/notification/order_changed_success][info:{}, headers:{}]","order_changed_success",(info != null ? info.toString(): null), (headers != null ? headers.toString(): null));
        return service.orderChangedSuccess(info, headers);
    }

    @PostMapping(value = "/notification/order_cancel_success")
    public boolean order_cancel_success(@RequestBody NotifyInfo info, @RequestHeader HttpHeaders headers) {
        logger.info("[function name:{}, API:Post /api/v1/notifyservice/notification/order_cancel_success][info:{}, headers:{}]","order_cancel_success",(info != null ? info.toString(): null), (headers != null ? headers.toString(): null));
        return service.orderCancelSuccess(info, headers);
    }
}
