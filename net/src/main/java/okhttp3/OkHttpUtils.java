package okhttp3;

import java.lang.reflect.Field;
import java.util.LinkedHashMap;
import java.util.Map;

import kotlin.jvm.JvmStatic;
import okhttp3.internal.cache.DiskLruCache;

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
        Map<Class<?>, Object> tagsOkhttp = request.tags;
        if (tagsOkhttp.isEmpty()) {
            Field tagsField = request.getClass().getDeclaredField("tags");
            tagsField.setAccessible(true);
            LinkedHashMap<Class<?>, Object> tags = new LinkedHashMap<>();
            tagsField.set(request, tags);
            return tags;
        }
        Field tagsField = tagsOkhttp.getClass().getDeclaredField("m");
        tagsField.setAccessible(true);
        Object tags = tagsField.get(tagsOkhttp);
        return (Map<Class<?>, Object>) tags;
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

    @JvmStatic
    public static Headers.Builder addLenient(Headers.Builder builder, String line) {
        return builder.addLenient(line);
    }

    @JvmStatic
    public static DiskLruCache diskLruCache(Cache cache) {
        return cache.cache;
    }
}
