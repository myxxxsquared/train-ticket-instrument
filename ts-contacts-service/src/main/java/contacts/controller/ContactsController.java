package contacts.controller;

import contacts.entity.*;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import edu.fudan.common.util.Response;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import contacts.service.ContactsService;

import java.util.UUID;

import static org.springframework.http.ResponseEntity.ok;

/**
 * @author fdse
 */
@RestController
@RequestMapping("api/v1/contactservice")
public class ContactsController { 
    private static final Logger logger = LoggerFactory.getLogger(ContactsController.class);





    @Autowired
    private ContactsService contactsService;

    @GetMapping(path = "/contacts/welcome")
    public String home() {
        logger.info("[function name:home]");
        return "Welcome to [ Contacts Service ] !";
    }

    @CrossOrigin(origins = "*")
    @GetMapping(path = "/contacts")
    public HttpEntity getAllContacts(@RequestHeader HttpHeaders headers) {
        logger.info("[function name:{}][HttpHeaders:{}]","getAllContacts",(headers != null ? headers.toString(): null));
        return ok(contactsService.getAllContacts(headers));
    }

    @CrossOrigin(origins = "*")
    @PostMapping(path = "/contacts")
    public ResponseEntity<Response> createNewContacts(@RequestBody Contacts aci,
                                                      @RequestHeader HttpHeaders headers) {
        return new ResponseEntity<>(contactsService.create(aci, headers), HttpStatus.CREATED);
    }

    @CrossOrigin(origins = "*")
    @PostMapping(path = "/contacts/admin")
    public HttpEntity<?> createNewContactsAdmin(@RequestBody Contacts aci, @RequestHeader HttpHeaders headers) {
        aci.setId(UUID.randomUUID().toString());
        return new ResponseEntity<>(contactsService.createContacts(aci, headers), HttpStatus.CREATED);
    }


    @CrossOrigin(origins = "*")
    @DeleteMapping(path = "/contacts/{contactsId}")
    public HttpEntity deleteContacts(@PathVariable String contactsId, @RequestHeader HttpHeaders headers) {
        logger.info("[function name:{}][String:{}, HttpHeaders:{}]","deleteContacts",contactsId, (headers != null ? headers.toString(): null));
        return ok(contactsService.delete(contactsId, headers));
    }


    @CrossOrigin(origins = "*")
    @PutMapping(path = "/contacts")
    public HttpEntity modifyContacts(@RequestBody Contacts info, @RequestHeader HttpHeaders headers) {
        logger.info("[function name:{}][Contacts:{}, HttpHeaders:{}]","modifyContacts",(info != null ? info.toString(): null), (headers != null ? headers.toString(): null));
        return ok(contactsService.modify(info, headers));
    }

    @CrossOrigin(origins = "*")
    @GetMapping(path = "/contacts/account/{accountId}")
    public HttpEntity findContactsByAccountId(@PathVariable String accountId, @RequestHeader HttpHeaders headers) {
        logger.info("[function name:{}][String:{}, HttpHeaders:{}]","findContactsByAccountId",accountId, (headers != null ? headers.toString(): null));
        return ok(contactsService.findContactsByAccountId(accountId, headers));
    }

    @CrossOrigin(origins = "*")
    @GetMapping(path = "/contacts/{id}")
    public HttpEntity getContactsByContactsId(@PathVariable String id, @RequestHeader HttpHeaders headers) {
        logger.info("[function name:{}][String:{}, HttpHeaders:{}]","getContactsByContactsId",id, (headers != null ? headers.toString(): null));
        return ok(contactsService.findContactsById(id, headers));
    }



}
