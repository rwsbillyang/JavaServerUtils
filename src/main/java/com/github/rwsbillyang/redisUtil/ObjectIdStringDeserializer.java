package com.github.rwsbillyang.redisUtil;

import java.io.IOException;

import org.bson.types.ObjectId;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;

public class ObjectIdStringDeserializer extends JsonDeserializer<ObjectId> {
 //有问题，经常返回null
    @Override
    public ObjectId deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        JsonNode oid = ((JsonNode)p.readValueAsTree()).get("id");
        if(oid==null) return null;
        return new ObjectId(oid.asText());
    }

}
