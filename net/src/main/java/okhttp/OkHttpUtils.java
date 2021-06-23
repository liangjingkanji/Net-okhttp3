package okhttp;

import java.lang.reflect.Field;
import java.util.Map;

import okhttp3.Request;

public class OkHttpUtils {

    @SuppressWarnings("unchecked")
    public static Map<Class<?>, Object> tags(Request.Builder builder) throws NoSuchFieldException, IllegalAccessException {
        Field field = builder.getClass().getDeclaredField("tags");
        field.setAccessible(true);
        return (Map<Class<?>, Object>) field.get(builder);
    }

    @SuppressWarnings({"unchecked", "ConstantConditions"})
    public static Map<Class<?>, Object> tags(Request request) throws NoSuchFieldException, IllegalAccessException {
        Field tagsField = request.getClass().getDeclaredField("tags");
        tagsField.setAccessible(true);
        Map<Class<?>, Object> tags = (Map<Class<?>, Object>) tagsField.get(request);
        Field mutableTagsField = tags.getClass().getDeclaredField("m");
        mutableTagsField.setAccessible(true);
        return (Map<Class<?>, Object>) mutableTagsField.get(tags);
    }
}
