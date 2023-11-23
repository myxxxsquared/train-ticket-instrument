package contacts.service;

import contacts.entity.*;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import edu.fudan.common.util.Response;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import contacts.repository.ContactsRepository;

import java.util.ArrayList;
import java.util.UUID;


/**
 * @author fdse
 */
@Service
public class ContactsServiceImpl implements ContactsService { 
    private static final Logger logger = LogManager.getLogger(ContactsServiceImpl.class);



















    @Autowired
    private ContactsRepository contactsRepository;
    
    @PersistenceContext
    private EntityManager entityManager;
    String success = "Success";

    @Override
    public Response findContactsById(String id, HttpHeaders headers) {
        logger.info("[function name:{}][id:{}, headers:{}]","findContactsById",id, (headers != null ? headers.toString(): null));
        Contacts contacts = contactsRepository.findById(id).orElse(null);
      logger.info("[contacts:{},headers:{}]", (contacts != null ? contacts : null));
      
      
      
      
      
      
      
      
      
      
      
      
      
      
      
      
      
      
        if (contacts != null) {
            return new Response<>(1, success, contacts);
        } else {
            logger.error("[findContactsById][contactsRepository.findById][No contacts according to contactsId][contactsId: {}]", id);
            return new Response<>(0, "No contacts according to contacts id", null);
        }
    }

    @Override
    public Response findContactsByAccountId(String accountId, HttpHeaders headers) {
        logger.info("[function name:{}][accountId:{}, headers:{}]","findContactsByAccountId",accountId, (headers != null ? headers.toString(): null));
        // ArrayList<Contacts> arr = contactsRepository.findByAccountId(accountId);
        String query = "SELECT * FROM contacts WHERE account_id = '" + accountId + "'";
        // logger.info("[query:{}]",query);
        // 执行数据库查询
        Query nativeQuery = entityManager.createNativeQuery(query, Contacts.class);
        List<Contacts> resultList = nativeQuery.getResultList();
        ArrayList<Contacts> arr = new ArrayList<>(resultList);
        logger.info("[arr:{},headers:{}]", (arr != null ? arr : null));
        return new Response<>(1, success, arr);
    }

    @Override
    public Response createContacts(Contacts contacts, HttpHeaders headers) {
        logger.info("[function name:{}][contacts:{}, headers:{}]","createContacts",(contacts != null ? contacts.toString(): null), (headers != null ? headers.toString(): null));
        Contacts contactsTemp = contactsRepository.findByAccountIdAndDocumentTypeAndDocumentType(contacts.getAccountId(), contacts.getDocumentNumber(), contacts.getDocumentType());
        if (contactsTemp != null) {
            ContactsServiceImpl.logger.warn("[createContacts][Init Contacts, Already Exists][Id: {}]", contacts.getId());
            return new Response<>(0, "Already Exists", contactsTemp);
        } else {
            contactsRepository.save(contacts);
            return new Response<>(1, "Create Success", null);
        }
    }

    @Override
    public Response create(Contacts addContacts, HttpHeaders headers) {
        logger.info("[function name:{}][addContacts:{}, headers:{}]","create",(addContacts != null ? addContacts.toString(): null), (headers != null ? headers.toString(): null));

        /*********************** Fault Injection - F10 ************************/
        // Issue: Incorrect part count in a Bill Of Material (BOM)
        // Scenario: An API used for BOM updates produces unexpected results
        // Previously, when calling create(), status=1 indicated "contact created successfully"
        // However, the API format has changed: status=1 now means "contact exists"

        // 1. Get contact list by accountId
        ArrayList<Contacts> accountContacts = contactsRepository.findByAccountId(addContacts.getAccountId());

        try {
            // 2. Add new contact to database
            contactsRepository.save(addContacts);
        } catch(Exception e) {
            logger.error("Error saving contacts: {}", e);
        }
        
        ContactsServiceImpl.logger.info("[Contacts-Add&Delete-Service.create][AddContacts][Success]");

        // 3. If contacts is a duplicate, return different message
        if (accountContacts.contains(addContacts)) {
            return new Response<>(1,  "Success, contact already in list", addContacts);
        } else {
            return new Response<>(1, "Success, contact created in list", addContacts);
        }
        /**********************************************************************/
    }

    @Override
    public Response delete(String contactsId, HttpHeaders headers) {
        logger.info("[function name:{}][contactsId:{}, headers:{}]","delete",contactsId, (headers != null ? headers.toString(): null));
        contactsRepository.deleteById(contactsId);
        Contacts contacts = contactsRepository.findById(contactsId).orElse(null);
      logger.info("[contacts:{},headers:{}]", (contacts != null ? contacts : null));
      
      
      
      
      
      
      
      
      
      
      
      
      
      
      
      
      
      
        if (contacts == null) {
            return new Response<>(1, "Delete success", contactsId);
        } else {
            ContactsServiceImpl.logger.error("[Contacts-Add&Delete-Service][DeleteContacts][Fail.Reason not clear][contactsId: {}]", contactsId);
            return new Response<>(0, "Delete failed", contactsId);
        }
    }

    @Override
    public Response modify(Contacts contacts, HttpHeaders headers) {
        logger.info("[function name:{}][contacts:{}, headers:{}]","modify",(contacts != null ? contacts.toString(): null), (headers != null ? headers.toString(): null));
        headers = null;
        Response oldContactResponse = findContactsById(contacts.getId(), headers);
        Contacts oldContacts = (Contacts) oldContactResponse.getData();
        if (oldContacts == null) {
            ContactsServiceImpl.logger.error("[Contacts-Modify-Service.modify][ModifyContacts][Fail.Contacts not found][contactId: {}]", contacts.getId());
            return new Response<>(0, "Contacts not found", null);
        } else {
            oldContacts.setName(contacts.getName());
            oldContacts.setDocumentType(contacts.getDocumentType());
            oldContacts.setDocumentNumber(contacts.getDocumentNumber());
            oldContacts.setPhoneNumber(contacts.getPhoneNumber());
            contactsRepository.save(oldContacts);
            return new Response<>(1, "Modify success", oldContacts);
        }
    }

    @Override
    public Response getAllContacts(HttpHeaders headers) {
        logger.info("[function name:{}][headers:{}]","getAllContacts",(headers != null ? headers.toString(): null));
        ArrayList<Contacts> contacts = contactsRepository.findAll();
      logger.info("[contacts:{},headers:{}]", (contacts != null ? contacts : null));
      
      
      
      
      
      
      
      
      
      
      
      
      
      
      
      
      
      
        if (contacts != null && !contacts.isEmpty()) {
            return new Response<>(1, success, contacts);
        } else {
            logger.error("[getAllContacts][contactsRepository.findAll][Get all contacts error][message: {}]", "No content");
            return new Response<>(0, "No content", null);
        }
    }

}


