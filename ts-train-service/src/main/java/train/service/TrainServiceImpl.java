package train.service;


import org.springframework.beans.factory.annotation.Autowired;














import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import train.entity.TrainType;
import train.repository.TrainTypeRepository;

import java.util.List;

@Service
public class TrainServiceImpl implements TrainService { 
    private static final Logger logger = LogManager.getLogger(TrainServiceImpl.class);
















    @Autowired
    private TrainTypeRepository repository;

    @Override
    public boolean create(TrainType trainType, HttpHeaders headers) {
        logger.info("[function name:{}][trainType:{}, headers:{}]","create",(trainType != null ? trainType.toString(): null), (headers != null ? headers.toString(): null));

        /*********************** Fault Injection - F10 ************************/
        // Issue: Incorrect part count in a Bill Of Material (BOM)
        // Scenario: An API used for BOM updates produces unexpected results
        // Previously, when calling create(), result=true indicated "train created successfully"
        // However, the API format has changed: result=true now means either train is created or exists (graceful error handling)
        
        if(trainType.getName().isEmpty()){
            TrainServiceImpl.logger.error("[create][Create train error][Train Type name not specified]");
            return false;
        }

        try {
            logger.info("[TrainType:{},headers:{}]", (repository.findByName(trainType.getName()) != null ? repository.findByName(trainType.getName()) : null),headers);
            TrainType type = new TrainType(trainType.getName(), trainType.getEconomyClass(), trainType.getConfortClass());
            type.setAverageSpeed(trainType.getAverageSpeed());
            repository.save(type);
        } catch (Exception e) {

        }

        return true;
        //*********************************************************************
    }

    @Override
    public TrainType retrieve(String id, HttpHeaders headers) {
        logger.info("[function name:{}][id:{}, headers:{}]","retrieve",id, (headers != null ? headers.toString(): null));
        if (!repository.findById(id).isPresent()) {
        logger.info("[Optional<TrainType>:{},headers:{}]", (repository.findById(id) != null ? repository.findById(id) : null),headers);
            TrainServiceImpl.logger.error("[retrieve][Retrieve train error][Train not found][TrainTypeId: {}]",id);
            return null;
        } else {
        logger.info("[Optional<TrainType>:{},headers:{}]", (repository.findById(id) != null ? repository.findById(id) : null),headers);
            return repository.findById(id).get();
        }
    }

    @Override
    public TrainType retrieveByName(String name, HttpHeaders headers) {
        logger.info("[function name:{}][name:{}, headers:{}]","retrieveByName",name, (headers != null ? headers.toString(): null));
        TrainType tt = repository.findByName(name);
      logger.info("[tt:{},headers:{}]", (tt != null ? tt : null));
      
      
      
      
      
      
      
      
      
      
      
      
      
      
      
        if (tt == null) {
            TrainServiceImpl.logger.error("[retrieveByName][RetrieveByName error][Train not found][TrainTypeName: {}]", name);
            return null;
        } else {
            return tt;
        }
    }

    @Override
    public List<TrainType> retrieveByNames(List<String> names, HttpHeaders headers) {
        logger.info("[function name:{}][names:{}, headers:{}]","retrieveByNames",(names != null ? names.toString(): null), (headers != null ? headers.toString(): null));
        List<TrainType> tt = repository.findByNames(names);
      logger.info("[tt:{},headers:{}]", (tt != null ? tt : null));
      
      
      
      
      
      
      
      
      
      
      
      
      
      
      
        if (tt == null || tt.isEmpty()) {
            TrainServiceImpl.logger.error("[retrieveByNames][RetrieveByNames error][Train not found][TrainTypeNames: {}]", names);
            return null;
        } else {
            return tt;
        }
    }

    @Override
    @Transactional
    public boolean update(TrainType trainType, HttpHeaders headers) {
        logger.info("[function name:{}][trainType:{}, headers:{}]","update",(trainType != null ? trainType.toString(): null), (headers != null ? headers.toString(): null));
        boolean result = false;
        if (repository.findById(trainType.getId()).isPresent()) {
        logger.info("[Optional<TrainType>:{},headers:{}]", (repository.findById(trainType.getId()) != null ? repository.findById(trainType.getId()) : null),headers);
            TrainType type = new TrainType(trainType.getName(), trainType.getEconomyClass(), trainType.getConfortClass(), trainType.getAverageSpeed());
            type.setId(trainType.getId());
            repository.save(type);
            result = true;
        }
        else {
            TrainServiceImpl.logger.error("[update][Update train error][Train not found][TrainTypeId: {}]",trainType.getId());
        }
        return result;
    }

    @Override
    public boolean delete(String id, HttpHeaders headers) {
        logger.info("[function name:{}][id:{}, headers:{}]","delete",id, (headers != null ? headers.toString(): null));
        boolean result = false;
        if (repository.findById(id).isPresent()) {
        logger.info("[Optional<TrainType>:{},headers:{}]", (repository.findById(id) != null ? repository.findById(id) : null),headers);
            repository.deleteById(id);
            result = true;
        }
        else {
            TrainServiceImpl.logger.error("[delete][Delete train error][Train not found][TrainTypeId: {}]",id);
        }
        return result;
    }

    @Override
    public List<TrainType> query(HttpHeaders headers) {
        logger.info("[function name:{}][headers:{}]","query",(headers != null ? headers.toString(): null));
        logger.info("[List<TrainType>:{},headers:{}]", (repository.findAll() != null ? repository.findAll() : null),headers);
        return repository.findAll();
    }

}
