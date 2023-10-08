package preserve.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;
import edu.fudan.common.entity.*;
import preserve.service.PreserveService;

import static org.springframework.http.ResponseEntity.ok;

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
        logger.info("[function name:home, API:Get /api/v1/preserveservice/welcome]");
        return "Welcome to [ Preserve Service ] !";
    }

    @CrossOrigin(origins = "*")
    @PostMapping(value = "/preserve")
    public HttpEntity preserve(@RequestBody OrderTicketsInfo oti,
                               @RequestHeader HttpHeaders headers) {
        logger.info("[function name:{}, API:Post /api/v1/preserveservice/preserve][oti:{}, headers:{}]","preserve",(oti != null ? oti.toString(): null), (headers != null ? headers.toString(): null));
        return ok(preserveService.preserve(oti, headers));
    }

}
