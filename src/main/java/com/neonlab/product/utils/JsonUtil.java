package com.neonlab.product.utils;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.neonlab.common.expectations.ServerException;


public class JsonUtil {
    private static final ObjectMapper mapper = new ObjectMapper();
    public static <T> T readObjectFromJson(String jsonString,Class<T>valueType) throws ServerException {
        try{
            return mapper.readValue(jsonString,valueType);
        }catch (Exception e) {
            throw new ServerException("Error Parsing Json to Object");
        }
    }
}
