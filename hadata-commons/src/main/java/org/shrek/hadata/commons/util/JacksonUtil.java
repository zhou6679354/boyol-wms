package org.shrek.hadata.commons.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.datatype.guava.GuavaModule;
import com.google.common.base.Strings;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.text.SimpleDateFormat;

/**
 * @author chengjian
 * @version 1.0
 * @date 2018年05月07日 14:59
 */
@Slf4j
public class JacksonUtil {
    public static final JacksonUtil JACKSON_NON_EMPTY_MAPPER = new JacksonUtil(JsonInclude.Include.NON_EMPTY, Type.JSON);
    public static final JacksonUtil JACKSON_ALWAYS_MAPPER = new JacksonUtil(JsonInclude.Include.ALWAYS, Type.JSON);
    public static final JacksonUtil JACKSON_NON_EMPTY_XML_MAPPER = new JacksonUtil(JsonInclude.Include.NON_EMPTY, Type.XML);
    public static final JacksonUtil JACKSON_ALWAYS_XML_MAPPER = new JacksonUtil(JsonInclude.Include.ALWAYS, Type.XML);

    private ObjectMapper mapper;

    public JacksonUtil(JsonInclude.Include include, Type type) {
        if (type == Type.XML) {
            this.mapper = new XmlMapper();
        } else {
            this.mapper = new ObjectMapper();
        }
        this.mapper.setSerializationInclusion(include);
        this.mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
        this.mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        this.mapper.registerModule(new GuavaModule());
    }

    public static JacksonUtil nonEmpty() {
        return JACKSON_NON_EMPTY_MAPPER;
    }

    public static JacksonUtil nonEmpty(Type type) {
        if (type == Type.XML) {
            return JACKSON_NON_EMPTY_XML_MAPPER;
        }
        return JACKSON_NON_EMPTY_MAPPER;
    }

    public static JacksonUtil nonAlways() {
        return JACKSON_ALWAYS_MAPPER;
    }

    public static JacksonUtil nonAlways(Type type) {
        if (type == Type.XML) {
            return JACKSON_ALWAYS_XML_MAPPER;
        }
        return JACKSON_ALWAYS_MAPPER;
    }

    public String toJson(Object object) {
        try {
            return this.mapper.writeValueAsString(object);
        } catch (IOException e) {
            log.error("write to json string error:" + object, e);
        }
        return null;
    }

    public <T> T fromJson(String jsonString, Class<T> clazz) {
        if (Strings.isNullOrEmpty(jsonString)) {
            return null;
        }
        try {
            return (T) this.mapper.readValue(jsonString, clazz);
        } catch (IOException e) {
            log.error("parse json string error:" + jsonString, e);
        }
        return null;
    }

    public <T> T fromJson(String jsonString, JavaType javaType) {
        if (Strings.isNullOrEmpty(jsonString)) {
            return null;
        }
        try {
            return (T) this.mapper.readValue(jsonString, javaType);
        } catch (Exception e) {
            log.error("parse json string error:" + jsonString, e);
        }
        return null;
    }

    public <T> T fromJson(String jsonString, Class<?> collectionClass, Class<?>... elementClasses) {
        if (Strings.isNullOrEmpty(jsonString)) {
            return null;
        }
        try {
            JavaType javaType = this.mapper.getTypeFactory().constructParametricType(collectionClass, elementClasses);
            return (T) this.mapper.readValue(jsonString, javaType);
        } catch (Exception e) {
            log.error("parse json string error:" + jsonString, e);
        }
        return null;
    }

    public JsonNode treeFromJson(String jsonString)
            throws IOException {
        return this.mapper.readTree(jsonString);
    }

    public <T> T treeToValue(JsonNode node, Class<T> clazz)
            throws JsonProcessingException {
        return (T) this.mapper.treeToValue(node, clazz);
    }

    public JavaType createCollectionType(Class<?> collectionClass, Class<?>... elementClasses) {
        return this.mapper.getTypeFactory().constructParametricType(collectionClass, elementClasses);
    }


    public ObjectMapper getMapper() {
        return this.mapper;
    }

    public enum Type {
        JSON, XML
    }
}
