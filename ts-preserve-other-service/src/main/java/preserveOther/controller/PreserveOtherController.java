package preserveOther.controller;


import org.springframework.beans.factory.annotation.Autowired;

















import edu.fudan.common.util.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;
import edu.fudan.common.entity.OrderTicketsInfo;
import preserveOther.service.PreserveOtherService;

import static org.springframework.http.ResponseEntity.ok;
import org.springframework.http.ResponseEntity;

/**
 * @author fdse
 */
@RestController
@RequestMapping("/api/v1/preserveotherservice")
public class PreserveOtherController { 
    private static final Logger logger = LogManager.getLogger(PreserveOtherController.class);






















    @Autowired
    private PreserveOtherService preserveService;

    @GetMapping(path = "/welcome")
    public String home() {
        return "Welcome to [ PreserveOther Service ] !";
    }

    @CrossOrigin(origins = "*")
    @PostMapping(value = "/preserveOther")
    public HttpEntity preserve(@RequestBody OrderTicketsInfo oti,
                               @RequestHeader HttpHeaders headers) {
        // return ok(preserveService.preserve(oti, headers));
        Response<?> res = preserveService.preserve(oti, headers);
        if (res.getStatus() == 1) {
            return ResponseEntity.ok(res);
        }
        else{
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(res);
        }
    }

}
