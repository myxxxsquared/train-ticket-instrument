package rebook.controller;

import edu.fudan.common.entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;
import rebook.entity.RebookInfo;
import rebook.service.RebookService;
import rebook.dto.OrderUpdateDto;

import static org.springframework.http.ResponseEntity.ok;

/**
 * @author fdse
 */
@RestController
@RequestMapping("/api/v1/rebookservice")
public class RebookController { 
    private static final Logger logger = LogManager.getLogger(RebookController.class);




















    @Autowired
    RebookService service;

    @GetMapping(path = "/welcome")
    public String home() {
        return "Welcome to [ Rebook Service ] !";
    }

    @PostMapping(value = "/rebook/difference")
    public HttpEntity payDifference(@RequestBody RebookInfo info,
                                    @RequestHeader HttpHeaders headers) {
        return ok(service.payDifference(info, headers));
    }

    @PostMapping(value = "/rebook")
    public HttpEntity rebook(@RequestBody RebookInfo info, @RequestHeader HttpHeaders headers) {
        return ok(service.rebook(info, headers));
    }
    // updateOrder(Order order, RebookInfo info, TripAllDetail gtdr, String ticketPrice, HttpHeaders httpHeaders)

    @PostMapping(value = "/updateorder")
    public HttpEntity updateOrder(@RequestBody OrderUpdateDto orderUpdateDTO, @RequestHeader HttpHeaders headers) {
    Order order = orderUpdateDTO.getOrder();
    RebookInfo info = orderUpdateDTO.getRebookInfo();
    TripAllDetail gtdr = orderUpdateDTO.getTripAllDetail();
    String ticketPrice = orderUpdateDTO.getTicketPrice();
    logger.info(order.toString(),info.toString(),gtdr.toString(),ticketPrice);
    return ok(service.updateOrder(order, info, gtdr, ticketPrice, headers));
    }
}
