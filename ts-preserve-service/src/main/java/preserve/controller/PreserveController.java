package preserve.controller;

import edu.fudan.common.util.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import edu.fudan.common.entity.*;
import preserve.service.PreserveService;

import static org.springframework.http.ResponseEntity.ok;
import org.springframework.http.ResponseEntity;

/**
 * @author fdse
 */
@RestController
@RequestMapping("/api/v1/preserveservice")
public class PreserveController { 
    private static final Logger logger = LogManager.getLogger(PreserveController.class);




















    @Autowired
    private PreserveService preserveService;

    @GetMapping(path = "/welcome")
    public String home() {
        return "Welcome to [ Preserve Service ] !";
    }

    @CrossOrigin(origins = "*")
    @PostMapping(value = "/preserve")
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
