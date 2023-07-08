package contacts.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import contacts.entity.Contacts;
import org.springframework.data.repository.query.Param;

import java.util.*;



/**
 * @author fdse
 */
@Repository
public interface ContactsRepository extends CrudRepository<Contacts, String> {

    /**
     * find by id
     *
     * @param id id
     * @return Contacts
     */
    Optional<Contacts> findById(String id);

    /**
     * find by account id
     *
     * @param accountId account id
     * @return ArrayList<Contacts>
     */
//    @Query("{ 'accountId' : ?0 }")
    @Query(value="SELECT * FROM contacts WHERE accountId = :accountId", nativeQuery = true)
    ArrayList<Contacts> findByAccountId(@Param("accountId") String accountId);

    /**
     * delete by id
     *
     * @param id id
     * @return null
     */
    void deleteById(String id);

    /**
     * find all
     *
     * @return ArrayList<Contacts>
     */
    @Override
    ArrayList<Contacts> findAll();

    // @Query(value="SELECT * FROM contacts WHERE account_id = ?1 AND document_number = ?2 AND document_type = ?3", nativeQuery = true)
    // Contacts findByAccountIdAndDocumentTypeAndDocumentType(String account_id, String document_number, int document_type);


    @Query(value="SELECT * FROM contacts WHERE account_id = :account_id AND document_number = :document_number AND document_type = :document_type", nativeQuery = true)
    Contacts findByAccountIdAndDocumentTypeAndDocumentType(@Param("account_id") String account_id, @Param("document_number") String document_number, @Param("document_type") int document_type);

}
