package okhttp;

import java.lang.reflect.Field;
import java.util.Map;

import kotlin.jvm.JvmStatic;
import okhttp3.Headers;
import okhttp3.Request;

public class OkHttpUtils {

    /**
     * 标签集合
     */
    @JvmStatic
    @SuppressWarnings("unchecked")
    public static Map<Class<?>, Object> tags(Request.Builder builder) throws NoSuchFieldException, IllegalAccessException {
        Field field = builder.getClass().getDeclaredField("tags");
        field.setAccessible(true);
        return (Map<Class<?>, Object>) field.get(builder);
    }

    /**
     * 通过反射返回Request的标签可变集合
     */
    @JvmStatic
    @SuppressWarnings({"unchecked", "ConstantConditions"})
    public static Map<Class<?>, Object> tags(Request request) throws NoSuchFieldException, IllegalAccessException {
        Field tagsField = request.getClass().getDeclaredField("tags");
        tagsField.setAccessible(true);
        Map<Class<?>, Object> tags = (Map<Class<?>, Object>) tagsField.get(request);
        Field mutableTagsField = tags.getClass().getDeclaredField("m");
        mutableTagsField.setAccessible(true);
        return (Map<Class<?>, Object>) mutableTagsField.get(tags);
    }

    /**
     * 全部的请求头
     */
    @JvmStatic
    public static Headers.Builder headers(Request.Builder builder) throws NoSuchFieldException, IllegalAccessException {
        Field field = builder.getClass().getDeclaredField("headers");
        field.setAccessible(true);
        return (Headers.Builder) field.get(builder);
    }
}
