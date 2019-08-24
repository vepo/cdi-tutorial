package io.vepo.access.infra;

import org.bson.BsonReader;
import org.bson.BsonWriter;
import org.bson.codecs.Codec;
import org.bson.codecs.DecoderContext;
import org.bson.codecs.EncoderContext;

public class EnumCodec<T extends Enum<T>> implements Codec<T> {

    private Class<T> enumClass;

    public EnumCodec(Class<T> enumClass) {
        this.enumClass = enumClass;
    }

    @Override
    public void encode(BsonWriter writer, T value, EncoderContext encoderContext) {
        writer.writeString(value.name());
    }

    @Override
    public Class<T> getEncoderClass() {
        return this.enumClass;
    }

    @Override
    public T decode(BsonReader reader, DecoderContext decoderContext) {
        return Enum.valueOf(enumClass, reader.readString());
    }

}