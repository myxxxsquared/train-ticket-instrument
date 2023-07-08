package assurance.service;

import assurance.entity.*;




import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import assurance.repository.AssuranceRepository;
import edu.fudan.common.util.Response;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * @author fdse
 */
@Service
public class AssuranceServiceImpl implements AssuranceService { 
    private static final Logger logger = LoggerFactory.getLogger(AssuranceServiceImpl.class);






    @Autowired
    private AssuranceRepository assuranceRepository;

    @Override
    public Response findAssuranceById(UUID id, HttpHeaders headers) {
        logger.info("[function name:{}][id:{}, headers:{}]","findAssuranceById",(id != null ? id.toString(): null), (headers != null ? headers.toString(): null));
        Optional<Assurance> assurance = assuranceRepository.findById(id.toString());
      logger.info("the assurance is: {}", (assurance != null ? assurance : null));
      
      
      
      
      
        if (assurance == null) {
            AssuranceServiceImpl.logger.warn("[findAssuranceById][find assurance][No content][assurance id: {}]", id);
            return new Response<>(0, "No Content by this id", null);
        } else {
            return new Response<>(1, "Find Assurance Success", assurance);
        }
    }

    @Override
    public Response findAssuranceByOrderId(UUID orderId, HttpHeaders headers) {
        logger.info("[function name:{}][orderId:{}, headers:{}]","findAssuranceByOrderId",(orderId != null ? orderId.toString(): null), (headers != null ? headers.toString(): null));
        Assurance assurance = assuranceRepository.findByOrderId(orderId.toString());
      logger.info("the assurance is: {}", (assurance != null ? assurance : null));
      
      
      
      
      
        if (assurance == null) {
            AssuranceServiceImpl.logger.warn("[findAssuranceByOrderId][find assurance][No content][orderId: {}]", orderId);
            return new Response<>(0, "No Content by this orderId", null);
        } else {
            return new Response<>(1, "Find Assurance Success", assurance);
        }
    }

    @Override
    public Response create(int typeIndex, String orderId, HttpHeaders headers) {
        logger.info("[function name:{}][typeIndex:{}, orderId:{}, headers:{}]","create",typeIndex, orderId, (headers != null ? headers.toString(): null));
        Assurance a = assuranceRepository.findByOrderId(orderId);
      logger.info("the a is: {}", (a != null ? a : null));
      
      
      
      
      
        AssuranceType at = AssuranceType.getTypeByIndex(typeIndex);
        if (a != null) {
            AssuranceServiceImpl.logger.error("[create][AddAssurance Fail][Assurance already exists][typeIndex: {}, orderId: {}]", typeIndex, orderId);
            return new Response<>(0, "Fail.Assurance already exists", null);
        } else if (at == null) {
            AssuranceServiceImpl.logger.warn("[create][AddAssurance Fail][Assurance type doesn't exist][typeIndex: {}, orderId: {}]", typeIndex, orderId);
            return new Response<>(0, "Fail.Assurance type doesn't exist", null);
        } else {
            Assurance assurance = new Assurance(UUID.randomUUID().toString(), UUID.fromString(orderId).toString(), at);
            assuranceRepository.save(assurance);
            return new Response<>(1, "Success", assurance);
        }
    }

    @Override
    public Response deleteById(UUID assuranceId, HttpHeaders headers) {
        logger.info("[function name:{}][assuranceId:{}, headers:{}]","deleteById",(assuranceId != null ? assuranceId.toString(): null), (headers != null ? headers.toString(): null));
        assuranceRepository.deleteById(assuranceId.toString());
        Optional<Assurance> a = assuranceRepository.findById(assuranceId.toString());
      logger.info("the a is: {}", (a != null ? a : null));
      
      
      
      
      
        if (a == null) {
            return new Response<>(1, "Delete Success with Assurance id", null);
        } else {
            AssuranceServiceImpl.logger.error("[deleteById][DeleteAssurance Fail][Assurance not clear][assuranceId: {}]", assuranceId);
            return new Response<>(0, "Fail.Assurance not clear", assuranceId);
        }
    }

    @Override
    public Response deleteByOrderId(UUID orderId, HttpHeaders headers) {
        logger.info("[function name:{}][orderId:{}, headers:{}]","deleteByOrderId",(orderId != null ? orderId.toString(): null), (headers != null ? headers.toString(): null));
        assuranceRepository.removeAssuranceByOrderId(orderId.toString());
        Assurance isExistAssurace = assuranceRepository.findByOrderId(orderId.toString());
      logger.info("the isExistAssurace is: {}", (isExistAssurace != null ? isExistAssurace : null));
      
      
      
      
      
        if (isExistAssurace == null) {
            return new Response<>(1, "Delete Success with Order Id", null);
        } else {
            AssuranceServiceImpl.logger.error("[deleteByOrderId][DeleteAssurance Fail][Assurance not clear][orderId: {}]", orderId);
            return new Response<>(0, "Fail.Assurance not clear", orderId);
        }
    }

    @Override
    public Response modify(String assuranceId, String orderId, int typeIndex, HttpHeaders headers) {
        logger.info("[function name:{}][assuranceId:{}, orderId:{}, typeIndex:{}, headers:{}]","modify",assuranceId, orderId, typeIndex, (headers != null ? headers.toString(): null));
        Response oldAssuranceResponse = findAssuranceById(UUID.fromString(assuranceId), headers);
        Assurance oldAssurance =  ((Optional<Assurance>)oldAssuranceResponse.getData()).get();
        if (oldAssurance == null) {
            AssuranceServiceImpl.logger.error("[modify][ModifyAssurance Fail][Assurance not found][assuranceId: {}, orderId: {}, typeIndex: {}]", assuranceId, orderId, typeIndex);
            return new Response<>(0, "Fail.Assurance not found.", null);
        } else {
            AssuranceType at = AssuranceType.getTypeByIndex(typeIndex);
            if (at != null) {
                oldAssurance.setType(at);
                assuranceRepository.save(oldAssurance);
                return new Response<>(1, "Modify Success", oldAssurance);
            } else {
                AssuranceServiceImpl.logger.error("[modify][ModifyAssurance Fail][Assurance Type not exist][assuranceId: {}, orderId: {}, typeIndex: {}]", assuranceId, orderId, typeIndex);
                return new Response<>(0, "Assurance Type not exist", null);
            }
        }
    }

    @Override
    public Response getAllAssurances(HttpHeaders headers) {
        logger.info("[function name:{}][headers:{}]","getAllAssurances",(headers != null ? headers.toString(): null));
        List<Assurance> as = assuranceRepository.findAll();
      logger.info("the as is: {}", (as != null ? as : null));
      
      
      
      
      
        if (as != null && !as.isEmpty()) {
            ArrayList<PlainAssurance> result = new ArrayList<>();
            for (Assurance a : as) {
                PlainAssurance pa = new PlainAssurance();
                pa.setId(a.getId());
                pa.setOrderId(a.getOrderId());
                pa.setTypeIndex(a.getType().getIndex());
                pa.setTypeName(a.getType().getName());
                pa.setTypePrice(a.getType().getPrice());
                result.add(pa);
            }
            return new Response<>(1, "Success", result);
        } else {
            AssuranceServiceImpl.logger.warn("[getAllAssurances][find all assurance][No content]");
            return new Response<>(0, "No Content, Assurance is empty", null);
        }
    }

    @Override
    public Response getAllAssuranceTypes(HttpHeaders headers) {
        logger.info("[function name:{}][headers:{}]","getAllAssuranceTypes",(headers != null ? headers.toString(): null));

        List<AssuranceTypeBean> atlist = new ArrayList<>();
        for (AssuranceType at : AssuranceType.values()) {
            AssuranceTypeBean atb = new AssuranceTypeBean();
            atb.setIndex(at.getIndex());
            atb.setName(at.getName());
            atb.setPrice(at.getPrice());
            atlist.add(atb);
        }
        if (!atlist.isEmpty()) {
            return new Response<>(1, "Find All Assurance", atlist);
        } else {
            AssuranceServiceImpl.logger.warn("[getAllAssuranceTypes][find all assurance type][No content]");
            return new Response<>(0, "Assurance is Empty", null);
        }
    }
}
