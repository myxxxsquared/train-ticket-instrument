package seat.controller;


import org.springframework.beans.factory.annotation.Autowired;


















import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;
import edu.fudan.common.entity.Seat;
import seat.service.SeatService;

import static org.springframework.http.ResponseEntity.ok;

/**
 * @author fdse
 */
@RestController
@RequestMapping("/api/v1/seatservice")
public class SeatController { 
    private static final Logger logger = LogManager.getLogger(SeatController.class);






















    @Autowired
    private SeatService seatService;

    @GetMapping(path = "/welcome")
    public String home() {
        return "Welcome to [ Seat Service ] !";
    }

    /**
     * Assign seats by seat request
     *
     * @param seatRequest seat request
     * @param headers headers
     * @return HttpEntity
     */
    @CrossOrigin(origins = "*")
    @PostMapping(value = "/seats")
    public HttpEntity create(@RequestBody Seat seatRequest, @RequestHeader HttpHeaders headers) {
        return ok(seatService.distributeSeat(seatRequest, headers));
    }

    /**
     * get left ticket of interval
     * query specific interval residual
     *
     * @param seatRequest seat request
     * @param headers headers
     * @return HttpEntity
     */
    @CrossOrigin(origins = "*")
    @PostMapping(value = "/seats/left_tickets")
    public HttpEntity getLeftTicketOfInterval(@RequestBody Seat seatRequest, @RequestHeader HttpHeaders headers) {
        return ok(seatService.getLeftTicketOfInterval(seatRequest, headers));
    }

}
