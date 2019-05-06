package com.feng.pigp.util;

import com.google.gson.*;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Created by xinfeng.xu on 2018/4/4.
 */
public class GsonUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(GsonUtil.class);
    private static final Gson GSON;
    private static final JsonParser PARSER = new JsonParser();
    private static final String DATE_TIME_FORMAT_PATTERN = "yyyy-MM-dd HH:mm:ss";
    private static final String DATE_FORMAT_PATTERN = "yyyy-MM-dd";

    static{
        GSON = new GsonBuilder().registerTypeAdapter(LocalDateTime.class, new JsonSerializer<LocalDateTime>(){

            @Override
            public JsonElement serialize(LocalDateTime src, Type typeOfSrc, JsonSerializationContext context) {
                return new JsonPrimitive(src.format(DateTimeFormatter.BASIC_ISO_DATE.ofPattern(DATE_TIME_FORMAT_PATTERN)));
            }
        }).registerTypeAdapter(LocalDate.class, new JsonSerializer<LocalDate>(){

            @Override
            public JsonElement serialize(LocalDate src, Type typeOfSrc, JsonSerializationContext context) {
                return new JsonPrimitive(src.format(DateTimeFormatter.BASIC_ISO_DATE.ofPattern(DATE_FORMAT_PATTERN)));
            }
        }).registerTypeAdapter(LocalDateTime.class, new JsonDeserializer<LocalDateTime>() {
            @Override
            public LocalDateTime deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                String datetime = json.getAsJsonPrimitive().getAsString();
                LocalDateTime time = LocalDateTime.parse(datetime, DateTimeFormatter.ofPattern(DATE_TIME_FORMAT_PATTERN));
                return time;
            }
        }).registerTypeAdapter(LocalDate.class, new JsonDeserializer<LocalDate>() {
            @Override
            public LocalDate deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                String datetime = json.getAsJsonPrimitive().getAsString();
                return LocalDate.parse(datetime, DateTimeFormatter.ofPattern(DATE_FORMAT_PATTERN));
            }
        }).excludeFieldsWithModifiers(Modifier.TRANSIENT).create();

    }

    public static <T> String toJson(T object){
        if(object==null){
            return "";
        }
        return GSON.toJson(object);
    }

    public static <T, M> T fromJson(String str, Class claz){

        if(StringUtils.isEmpty(str)){
            return null;
        }
        return (T) GSON.fromJson(str, claz);
    }

    public static <T> T fromJson(String str, Type type){
        if(StringUtils.isEmpty(str)){
            return null;
        }

        return GSON.fromJson(str, type);
    }

    public static String getNodeData(String json, String... args){

        if(StringUtils.isEmpty(json)){
            return null;
        }

        if(args.length<=0){
            return null;
        }

        try {
            JsonElement root = PARSER.parse(json);
            if (root == null) {
                return null;
            }

            JsonElement argNode = root;
            for (String arg : args) {

                argNode = argNode.getAsJsonObject().get(arg);
                if (argNode == null || argNode.toString().equals("null")) {
                    return null;
                }
            }

            if (argNode.isJsonObject() || argNode.isJsonArray()) {
                return argNode.toString();
            }

            return argNode.getAsString();
        }catch (Exception e){
            LOGGER.error("gson parse error : {}", e);
        }

        return null;
    }
}
