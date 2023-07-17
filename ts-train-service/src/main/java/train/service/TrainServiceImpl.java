package train.service;


import org.springframework.beans.factory.annotation.Autowired;









import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import train.entity.TrainType;
import train.repository.TrainTypeRepository;

import java.util.List;

@Service
public class TrainServiceImpl implements TrainService { 
    private static final Logger logger = LoggerFactory.getLogger(TrainServiceImpl.class);











    @Autowired
    private TrainTypeRepository repository;

    @Override
    public boolean create(TrainType trainType, HttpHeaders headers) {
        logger.info("[function name:{}][trainType:{}, headers:{}]","create",(trainType != null ? trainType.toString(): null), (headers != null ? headers.toString(): null));
        boolean result = false;
        if(trainType.getName().isEmpty()){
            TrainServiceImpl.logger.error("[create][Create train error][Train Type name not specified]");
            return result;
        }
        if (repository.findByName(trainType.getName()) == null) {
        logger.info("[TrainType:{}]", (repository.findByName(trainType.getName()) != null ? repository.findByName(trainType.getName()) : null));
            TrainType type = new TrainType(trainType.getName(), trainType.getEconomyClass(), trainType.getConfortClass());
            type.setAverageSpeed(trainType.getAverageSpeed());
            repository.save(type);
            result = true;
        }
        else {
            TrainServiceImpl.logger.error("[create][Create train error][Train already exists][TrainTypeId: {}]",trainType.getId());
        }
        return result;
    }

    @Override
    public TrainType retrieve(String id, HttpHeaders headers) {
        logger.info("[function name:{}][id:{}, headers:{}]","retrieve",id, (headers != null ? headers.toString(): null));
        if (!repository.findById(id).isPresent()) {
        logger.info("[Optional<TrainType>:{}]", (repository.findById(id) != null ? repository.findById(id) : null));
            TrainServiceImpl.logger.error("[retrieve][Retrieve train error][Train not found][TrainTypeId: {}]",id);
            return null;
        } else {
        logger.info("[Optional<TrainType>:{}]", (repository.findById(id) != null ? repository.findById(id) : null));
            return repository.findById(id).get();
        }
    }

    @Override
    public TrainType retrieveByName(String name, HttpHeaders headers) {
        logger.info("[function name:{}][name:{}, headers:{}]","retrieveByName",name, (headers != null ? headers.toString(): null));
        TrainType tt = repository.findByName(name);
      logger.info("[tt:{}]", (tt != null ? tt : null));
      
      
      
      
      
      
      
      
      
      
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
      logger.info("[tt:{}]", (tt != null ? tt : null));
      
      
      
      
      
      
      
      
      
      
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
        logger.info("[Optional<TrainType>:{}]", (repository.findById(trainType.getId()) != null ? repository.findById(trainType.getId()) : null));
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
        logger.info("[Optional<TrainType>:{}]", (repository.findById(id) != null ? repository.findById(id) : null));
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
        logger.info("[List<TrainType>:{}]", (repository.findAll() != null ? repository.findAll() : null));
        return repository.findAll();
    }

}
