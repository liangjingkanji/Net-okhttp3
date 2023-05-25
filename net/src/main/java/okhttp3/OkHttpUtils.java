/*
 * MIT License
 *
 * Copyright (c) 2023 劉強東 https://github.com/liangjingkanji
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package okhttp3;

import java.lang.reflect.Field;
import java.util.LinkedHashMap;
import java.util.Map;

import kotlin.jvm.JvmStatic;
import okhttp3.internal.cache.DiskLruCache;

public class OkHttpUtils {

    /**
     * 获取全部标签
     */
    @JvmStatic
    @SuppressWarnings("unchecked")
    public static Map<Class<?>, Object> tags(Request.Builder builder) throws NoSuchFieldException, IllegalAccessException {
        Field field = builder.getClass().getDeclaredField("tags");
        field.setAccessible(true);
        return (Map<Class<?>, Object>) field.get(builder);
    }

    /**
     * 获取Request的标签可变集合
     */
    @JvmStatic
    @SuppressWarnings({"unchecked", "ConstantConditions"})
    public static Map<Class<?>, Object> tags(Request request) throws NoSuchFieldException, IllegalAccessException {
        Field tagsField = request.getClass().getDeclaredField("tags");
        tagsField.setAccessible(true);
        Map<Class<?>, Object> tags = (Map<Class<?>, Object>) tagsField.get(request);
        if (tags.isEmpty()) {
            LinkedHashMap<Class<?>, Object> newTags = new LinkedHashMap<>();
            tagsField.set(request, newTags);
            return newTags;
        }
        tagsField = tags.getClass().getDeclaredField("m");
        tagsField.setAccessible(true);
        return (Map<Class<?>, Object>) tagsField.get(tags);
    }

    /**
     * 获取全部请求头
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

    /**
     * 获取缓存操作对象
     */
    @JvmStatic
    public static DiskLruCache diskLruCache(Cache cache) throws NoSuchFieldException, IllegalAccessException {
        Field field = cache.getClass().getDeclaredField("cache");
        field.setAccessible(true);
        return (DiskLruCache) field.get(cache);
    }
}
