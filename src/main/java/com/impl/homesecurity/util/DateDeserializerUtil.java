package com.impl.homesecurity.util;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Locale;

/**
 * Created by dima.
 * Creation date 20.12.18.
 */
public class DateDeserializerUtil implements JsonDeserializer<Date> {

    private final Logger log = LoggerFactory.getLogger(DateDeserializerUtil.class);
    private static final String[] DATE_FORMATS = new String[] {
            "EEE MMM dd HH:mm:ss zzz yyyy",
            "HH:mm"
    };

    @Override
    public Date deserialize(JsonElement jsonElement, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        for (String format : DATE_FORMATS) {
            try {
                return new SimpleDateFormat(format, Locale.US).parse(jsonElement.getAsString());
            }
            catch (ParseException e) {
                if (log.isDebugEnabled()) {
                    log.debug("ParseException " + e);
                }
            }
        }
        throw new JsonParseException("Unparseable  date: \"" + jsonElement.getAsString()
                + "\". Supported formats: " + Arrays.toString(DATE_FORMATS));
    }
}
