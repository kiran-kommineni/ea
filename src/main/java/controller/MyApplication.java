package controller;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import Model.FestivalModel;
import Utils.JacksonUtil;
import Utils.URLConstants;

/**
 * @author ramakiran
 *
 */
@RestController
@EnableAutoConfiguration
public class MyApplication {
	
    private static Logger logger = LoggerFactory.getLogger(MyApplication.class);
    
	@Bean
	public RestTemplate restTemplate() {
	    return new RestTemplate();
	}
	
	@Autowired
	RestTemplate restTemplate;
	
    public static void main(String[] args) throws Exception {
        SpringApplication.run(MyApplication.class, args);
    }
    
    @RequestMapping("/")
    public String home() {
        return "Hello World - v3!";
    }
    
    @GetMapping("/recordlabel/festival")
    public Object getFestivalDetailByRecordLabel() throws Exception {
    	
    	try {
    		return this.fetchFestivalDetailByRecordLabel();
    	}catch(Exception exception ) {
    		logger.info("Error processing request", exception.getMessage());
    		return "Error Processing request, Please try again";
    	}
    }
    
    /**
     * <p>
     * Fetches the festival details by API request and parses the details.
     * Finally collects and stores the details by RecordLabel, Bands, Festivals
     * </p>
     * 
     * @return Returns a sorted list of recordLabels by bands and festivals.
     */
    private Map<String, Map<String, List<String>>> fetchFestivalDetailByRecordLabel() throws Exception, JsonProcessingException{
		
		Map<String, Map<String, List<String>>> recordLabelList = new TreeMap<String, Map<String, List<String>>>();
	    
		/*
		 * Fetch the details from API
		 */
		Object response =  restTemplate.getForObject(URLConstants.hostName + URLConstants.festivalDetail, Object.class);
	    
	    if(response instanceof String) {
	    	throw new RuntimeException("API Data do not exist");
		}
		
	    /*
	     * Process the festival details and prepare record label structure
	     */
		if(response instanceof List) {

			ObjectMapper Obj = new ObjectMapper(); 
			
			List<FestivalModel> parsedData = JacksonUtil.fromStringAsList(Obj.writeValueAsString(response), FestivalModel.class);
				
			parsedData.forEach( festivalModel -> {

				festivalModel.getBands().forEach( bands ->{

						if(bands.getRecordLabel() != null && !bands.getRecordLabel().isEmpty()) {
						
							if(recordLabelList.containsKey(bands.getRecordLabel())){

								if(recordLabelList.get(bands.getRecordLabel()).containsKey(bands.getName())) {
									recordLabelList.get(bands.getRecordLabel()).get(bands.getName()).add(festivalModel.getName());
								}else {
									recordLabelList.get(bands.getRecordLabel()).put(bands.getName(), Arrays.asList(festivalModel.getName()));
								}

							}else {

								Map<String, List<String>> bandFestival = new TreeMap<String, List<String>>();
								bandFestival.put(bands.getName(), Arrays.asList(festivalModel.getName()));
								recordLabelList.put(bands.getRecordLabel(), bandFestival);
							}
						}
					});
				});

				return recordLabelList;
			
		}
		
		throw new RuntimeException("API Data do not exist");
	}    
	
	    
}
