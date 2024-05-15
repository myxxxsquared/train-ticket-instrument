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
 
        /*********************** Fault Injection - F10 ************************/
        // Issue: Incorrect part count in a Bill Of Material (BOM)
        // Scenario: An API used for BOM updates produces unexpected results
        // Previously, when calling create(), status=1 indicated "config created successfully"
        // However, the API format has changed: status=1 now means either config is created or exists (graceful error handling)

        Config config = new Config(info.getName(), info.getValue(), info.getDescription());

        try {
            repository.save(config);
        } catch (Exception e) {

        }

        return new Response<>(1, "Success", config);
    }

    @Override
    public Response update(Config info, HttpHeaders headers) {
        if (repository.findByName(info.getName()) == null) {
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
        Config config = repository.findByName(name);
      
      
      
      
      
      
      
      
      
      
      
      
      
      
      
      
      
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
        Config config = repository.findByName(name);
      
      
      
      
      
      
      
      
      
      
      
      
      
      
      
      
      
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
        List<Config> configList = repository.findAll();
      
      
      
      
      
      
      
      
      
      
      
      
      
      
      
      
      

        if (configList != null && !configList.isEmpty()) {
            return new Response<>(1, "Find all  config success", configList);
        } else {
            logger.warn("[queryAll][Query config, No content]");
            return new Response<>(0, "No content", null);
        }
    }
}
