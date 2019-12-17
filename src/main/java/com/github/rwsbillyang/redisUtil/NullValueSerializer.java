package com.github.rwsbillyang.redisUtil;

import java.io.IOException;

import org.msgpack.core.annotations.Nullable;
import org.springframework.cache.support.NullValue;
import org.springframework.util.StringUtils;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

public class NullValueSerializer  extends StdSerializer<NullValue> {
	 private static final long serialVersionUID = 2199052150128658111L;
     private final String classIdentifier;

     public NullValueSerializer(@Nullable String classIdentifier) {
         super(NullValue.class);
         this.classIdentifier = StringUtils.hasText(classIdentifier) ? classIdentifier : "@class";
     }

     @Override
     public void serialize(NullValue value, JsonGenerator jgen, SerializerProvider provider) throws IOException {
         jgen.writeStartObject();
         jgen.writeStringField(this.classIdentifier, NullValue.class.getName());
         jgen.writeEndObject();
     }
}
