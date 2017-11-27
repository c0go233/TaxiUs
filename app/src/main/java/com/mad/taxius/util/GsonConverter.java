package com.mad.taxius.util;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.mad.taxius.constant.Constant;
import com.mad.taxius.model.Location;

import java.lang.reflect.Type;

/**
 * Class is for converting from JSON to JAVA object or vice versa
 */

public class GsonConverter<T> implements JsonSerializer<T>, JsonDeserializer<T> {

    private final Gson gson;


    public GsonConverter() {
        gson = new Gson();
    }

    /**
     * Convert JAVA object to JSON
     *
     * @param src       is the source JAVA object
     * @param typeOfSrc the type of JAVA object to be converted
     * @param context   is the serializationContext
     * @return converted JSON object
     */
    @Override
    public JsonElement serialize(T src, Type typeOfSrc, JsonSerializationContext context) {
        String serialized = gson.toJson(src);
        return new JsonPrimitive(serialized);
    }

    /**
     * Conver the JSON object to JAVA object
     *
     * @param element is the JSON element
     * @param typeOfT is the type that JSON object is converted to
     * @param context is the Deserializeationcontext
     * @return Converted JAVA object
     * @throws JsonParseException
     */
    @Override
    public T deserialize(JsonElement element, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        T deserialized = gson.fromJson(element, typeOfT);
        return deserialized;
    }
}