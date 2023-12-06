package config.service;

import config.entity.Config;
















import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import config.repository.ConfigRepository;
import edu.fudan.common.util.Response;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


/**
 * @author fdse
 */
@Service
public class ConfigServiceImpl implements ConfigService { 
    private static final Logger logger = LogManager.getLogger(ConfigServiceImpl.class);


















    @Autowired
    ConfigRepository repository;

    String config0 = "Config ";

    @Override
    public Response create(Config info, HttpHeaders headers) {
        logger.info("[function name:{}][info:{}, headers:{}]","create",(info != null ? info.toString(): null), (headers != null ? headers.toString(): null));
 
        /*********************** Fault Injection - F10 ************************/
        // Issue: Incorrect part count in a Bill Of Material (BOM)
        // Scenario: An API used for BOM updates produces unexpected results
        // Previously, when calling create(), status=1 indicated "config created successfully"
        // However, the API format has changed: status=1 now means either config is created or exists (graceful error handling)

        Config config = new Config(info.getName(), info.getValue(), info.getDescription());

        try {
            logger.info("[Config:{},headers:{}]", (repository.findByName(info.getName()) != null ? repository.findByName(info.getName()) : null), headers);
            repository.save(config);
        } catch (Exception e) {

        }

        return new Response<>(1, "Success", config);
    }

    @Override
    public Response update(Config info, HttpHeaders headers) {
        logger.info("[function name:{}][info:{}, headers:{}]","update",(info != null ? info.toString(): null), (headers != null ? headers.toString(): null));
        if (repository.findByName(info.getName()) == null) {
        logger.info("[Config:{},headers:{}]", (repository.findByName(info.getName()) != null ? repository.findByName(info.getName()) : null),headers);
            String result = config0 + info.getName() + " doesn't exist.";
            logger.warn(result);
            return new Response<>(0, result, null);
        } else {
            Config config = new Config(info.getName(), info.getValue(), info.getDescription());
            repository.save(config);
            return new Response<>(1, "Update success", config);
        }
    }


    @Override
    public Response query(String name, HttpHeaders headers) {
        logger.info("[function name:{}][name:{}, headers:{}]","query",name, (headers != null ? headers.toString(): null));
        Config config = repository.findByName(name);
      logger.info("[config:{},headers:{}]", (config != null ? config : null));
      
      
      
      
      
      
      
      
      
      
      
      
      
      
      
      
      
        if (config == null) {
            logger.warn("[query][Config does not exist][name: {}, message: {}]", name, "No content");
            return new Response<>(0, "No content", null);
        } else {
            return new Response<>(1, "Success", config);
        }
    }

    @Override
    @Transactional
    public Response delete(String name, HttpHeaders headers) {
        logger.info("[function name:{}][name:{}, headers:{}]","delete",name, (headers != null ? headers.toString(): null));
        Config config = repository.findByName(name);
      logger.info("[config:{},headers:{}]", (config != null ? config : null));
      
      
      
      
      
      
      
      
      
      
      
      
      
      
      
      
      
        if (config == null) {
            String result = config0 + name + " doesn't exist.";
            logger.warn("[delete][config doesn't exist][config name: {}]", name);
            return new Response<>(0, result, null);
        } else {
            repository.deleteByName(name);
            return new Response<>(1, "Delete success", config);
        }
    }

    @Override
    public Response queryAll(HttpHeaders headers) {
        logger.info("[function name:{}][headers:{}]","queryAll",(headers != null ? headers.toString(): null));
        List<Config> configList = repository.findAll();
      logger.info("[configList:{},headers:{}]", (configList != null ? configList : null));
      
      
      
      
      
      
      
      
      
      
      
      
      
      
      
      
      

        if (configList != null && !configList.isEmpty()) {
            return new Response<>(1, "Find all  config success", configList);
        } else {
            logger.warn("[queryAll][Query config, No content]");
            return new Response<>(0, "No content", null);
        }
    }
}
