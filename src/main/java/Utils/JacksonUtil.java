package Utils;


import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.module.SimpleModule;

public class JacksonUtil {
    private static Logger logger = LoggerFactory.getLogger(JacksonUtil.class);
	 
    public static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
 
    public static <T> List<T> fromStringAsList(String string, Class<T> clazz) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.readValue(string, mapper.getTypeFactory().constructCollectionType(List.class, clazz));
        } catch (IOException e) {
            System.out.println(e.getMessage());
            throw new IllegalArgumentException("The given string value: "
                    + string + " cannot be transformed to Json object");
        }
    }


 
}