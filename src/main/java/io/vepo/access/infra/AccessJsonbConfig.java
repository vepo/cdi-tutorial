package io.vepo.access.infra;

import java.lang.reflect.Type;

import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;
import javax.json.bind.JsonbConfig;
import javax.json.bind.serializer.DeserializationContext;
import javax.json.bind.serializer.JsonbDeserializer;
import javax.json.bind.serializer.JsonbSerializer;
import javax.json.bind.serializer.SerializationContext;
import javax.json.stream.JsonGenerator;
import javax.json.stream.JsonParser;
import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.Provider;

import org.bson.types.ObjectId;

@Provider
public class AccessJsonbConfig implements ContextResolver<Jsonb> {

	@Override
	public Jsonb getContext(Class<?> type) {
		JsonbConfig config = new JsonbConfig();
		config.withSerializers(new JsonbSerializer<ObjectId>() {

			@Override
			public void serialize(ObjectId obj, JsonGenerator generator, SerializationContext ctx) {
				generator.write(obj.toHexString());
			}

		});
		config.withDeserializers(new JsonbDeserializer<ObjectId>() {

			@Override
			public ObjectId deserialize(JsonParser parser, DeserializationContext ctx, Type rtType) {
				return new ObjectId(parser.getString());
			}
		});
		return JsonbBuilder.create(config);
	}
}
