package preserveOther.controller;


import org.springframework.beans.factory.annotation.Autowired;











import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;
import edu.fudan.common.entity.OrderTicketsInfo;
import preserveOther.service.PreserveOtherService;

import static org.springframework.http.ResponseEntity.ok;

/**
 * @author fdse
 */
@RestController
@RequestMapping("/api/v1/preserveotherservice")
public class PreserveOtherController { 
    private static final Logger logger = LoggerFactory.getLogger(PreserveOtherController.class);













    @Autowired
    private PreserveOtherService preserveService;

    @GetMapping(path = "/welcome")
    public String home() {
        logger.info("[function name:home]");
        return "Welcome to [ PreserveOther Service ] !";
    }

    @CrossOrigin(origins = "*")
    @PostMapping(value = "/preserveOther")
    public HttpEntity preserve(@RequestBody OrderTicketsInfo oti,
                               @RequestHeader HttpHeaders headers) {
        logger.info("[function name:{}][oti:{}, headers:{}]","preserve",(oti != null ? oti.toString(): null), (headers != null ? headers.toString(): null));
        return ok(preserveService.preserve(oti, headers));
    }

}
