package com.msr.better.common.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.google.common.collect.ImmutableSet;
import com.google.protobuf.Descriptors;
import com.google.protobuf.GeneratedMessage;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.apache.commons.lang3.reflect.MethodUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

/**
 * @author MaiShuRen
 * @site https://www.maishuren.top
 * @since 2022/4/25
 */
public class JsonUtils {

    private static final Logger log = LoggerFactory.getLogger(JsonUtils.class);

    private static final ImmutableSet<? extends Class<? extends Serializable>> primitiveSet;

    static {
        // 还是直接的HashSet比较快..
        primitiveSet =
                ImmutableSet.of(int.class, long.class, double.class, float.class,
                        byte.class, short.class, String.class, boolean.class);
    }

    public static <T> T readValue(String json, Class<T> clazz) {
        if(null == json || "".equals(json)){
            return null;
        }
        try {
            return JSON.parseObject(json, clazz);
        } catch (Exception e) {
            log.warn("字符串转" + clazz.getSimpleName() + "失败", e);
            return null;
        }
    }

    public static <T> T readValue(byte[] json, Class<T> clazz) {
        try {
            return JSON.parseObject(json, clazz);
        } catch (Exception e) {
            log.warn("字符串转" + clazz.getSimpleName() + "失败", e);
            return null;
        }
    }

    public static <T> T readValue(JSONObject json, Class<T> clazz) {
        if(null == json){
            return null;
        }
        try {
            return JSON.parseObject(json.toJSONString(), clazz);
        } catch (Exception e) {
            log.warn("字符串转" + clazz.getSimpleName() + "失败", e);
            return null;
        }
    }

    public static String toJSONStringConvertNull(Object object) {
        return JSON.toJSONString(object, SerializerFeature.WriteNullStringAsEmpty,SerializerFeature.WriteMapNullValue, SerializerFeature.WriteNullListAsEmpty);
    }
    public static String toJsonString(Object object) {
        try {
            return toJSONStringConvertNull(object);
        } catch (Exception e) {
            log.error("对象转json字符串失败", e);
            return null;
        }
    }

    public static String toJsonStringNotConvertNull(Object object) {
        return  JSON.toJSONString(object,SerializerFeature.WriteMapNullValue);
    }

    public static String toJsonStringIgnoreNull(Object object){
        return JSON.toJSONString(object);
    }



    public static String map2String(Map<?, ?> map) {
        return toJsonString(map);
    }

    public static Map<?, ?> string2Map(String string) {
        return readValue(string, HashMap.class);
    }

    public static <K,V>Map<K, V> string2TreeMap(String string) {
        return readValue(string, TreeMap.class);
    }

    public static List<?> string2List(String string) {
        return readValue(string, List.class);
    }

    public static <T>List<T> string2List(String string,Class<T> clazz) {
        return JSON.parseArray(string, clazz);
    }



    public static String list2String(Collection<Map<String, Object>> map) {
        return toJsonString(map);
    }


    public static <T extends GeneratedMessage> T jsonToPB(String json, Class<T> clazz) {
        JSONObject jsonObject = JsonUtils.readValue(json, JSONObject.class);
        return jsonToPB(jsonObject, clazz);
    }

    public static <T extends GeneratedMessage> T jsonToPB(JSONObject jsonObject, Class<T> clazz) {
        GeneratedMessage.Builder builder;
        try {
            builder = (GeneratedMessage.Builder) MethodUtils.invokeStaticMethod(clazz, "newBuilder");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        builder = jsonToPBBuilder(jsonObject, builder.getClass());
        return (T) builder.build();
    }

    public static <T extends GeneratedMessage.Builder> T jsonToPBBuilder(JSONObject jsonObject, Class<T> clazz) {

        GeneratedMessage.Builder builder = genBuilder(clazz);
        Set<String> keySet = collectKey(jsonObject);

        List<Descriptors.FieldDescriptor> fields =
                builder.getDescriptorForType().getFields();
        for (Descriptors.FieldDescriptor field : fields) {
            if (!keySet.contains(field.getName())) continue;

            String fieldName = field.getName();
            String capName = StringUtils.capitalize(fieldName);
            try {
                if (field.isRepeated()) {

                    String methodName = "add" + capName;
                    JSONArray array = jsonObject.getJSONArray(fieldName);
                    Class c = findFieldType(builder, methodName);

                    if (field.getType() == Descriptors.FieldDescriptor.Type.MESSAGE) {
                        signPBArray(builder, methodName, array, c);
                    } else {
                        signPrimitiveArray(builder, methodName, array, c);
                    }
                } else {
                    String methodName = "set" + capName;
                    if (field.getType() == Descriptors.FieldDescriptor.Type.MESSAGE) {
                        singPB(jsonObject, builder, fieldName, methodName);
                    } else {
                        signPrimitive(jsonObject, builder, fieldName, methodName);
                    }
                }
            } catch (Exception e) {
                log.info("PB格式和json类型不一致 pb字段: " + field.getContainingType().getName() + "." + fieldName
                        + " json: "
                        + toJsonString(jsonObject.getString(fieldName)).replaceAll("\"", "'").replaceAll("\\\\'", "\""));
            }
        }
        return (T) builder;
    }

    public static Object invokeMethod(Object o, String methodName, Object... args) {
        try {
            if (o instanceof Class) {
                return MethodUtils.invokeStaticMethod((Class)o, methodName, args);
            }
            return MethodUtils.invokeMethod(o, methodName, args);
        } catch (Exception e) {
            log.warn("反射调用(一般是类型转换)出错, 一般可以忽略 detail: target:{} method:{} arg:{} argType:{}",
                    o, methodName, args[0], args[0].getClass());
            return null;
        }
    }

    private static void signPrimitive(JSONObject jsonObject,
                                      GeneratedMessage.Builder builder,
                                      String fieldName,
                                      String methodName) {
        Class type = findFieldType(builder, methodName);
        Object retValue;
        if (type == int.class) {
            retValue = jsonObject.getInteger(fieldName);
        } else if (type == long.class) {
            retValue = jsonObject.getLong(fieldName);
        } else if (type == String.class) {
            retValue = jsonObject.getString(fieldName);
        } else if (type == boolean.class) {
            try {
                retValue = jsonObject.getBoolean(fieldName);
            } catch (JSONException e) {
                int intValue = jsonObject.getInteger(fieldName); // 繁星那边1代表true, 0代表false
                if (intValue == 1) {
                    retValue = true;
                } else if (intValue == 0) {
                    retValue = false;
                } else throw new JSONException(jsonObject.getString(fieldName) + " 不能转为boolean类型");
            }
        } else if (type == double.class) {
            retValue = jsonObject.getDouble(fieldName);
        } else if (type == float.class) {
            retValue = Float.valueOf(jsonObject.getString(fieldName));
        } else {
            return;
        }
        invokeMethod(builder, methodName, retValue);
    }

    private static void singPB(JSONObject jsonObject,
                               GeneratedMessage.Builder builder,
                               String fieldName, String methodName) {
        Class c = findFieldType(builder, methodName);
        Object value = jsonToPB(jsonObject.getJSONObject(fieldName), c);
        invokeMethod(builder, methodName, value);
    }

    private static void signPrimitiveArray(GeneratedMessage.Builder builder,
                                           String methodName,
                                           JSONArray array, Class c) {
        for (int i = 0; i < array.size(); i++) {
            Object value = array.get(i);
            value = invokeMethod(c, "valueOf", value);
            invokeMethod(builder, methodName, value);
        }
    }

    private static void signPBArray(GeneratedMessage.Builder builder,
                                    String methodName,
                                    JSONArray array, Class c) {
        for (int i = 0; i < array.size(); i++) {
            Object pb = jsonToPB(array.getJSONObject(i), c);
            invokeMethod(builder, methodName, pb);
//            MethodUtils.invokeMethod(builder, methodName, pb);
        }
    }

    public static Class findFieldType(GeneratedMessage.Builder builder, String methodName) {
        Method[] methods = builder.getClass().getMethods();
        for (Method method : methods) {
            if (method.getName().equals(methodName)
                    && method.getParameterTypes().length == 1) {
                Class c = method.getParameterTypes()[0];
                if (GeneratedMessage.class.isAssignableFrom(c)) {
                    return c;
                }
                if (primitiveSet.contains(c)) {
                    return c;
                }
            }
        }
        return null;
    }

    private static Set<String> collectKey(JSONObject jsonObject) {
        Set<String> set = new HashSet<>();
        Iterator<String> iter = jsonObject.keySet().iterator();
        int count = 0;
        while (iter.hasNext()) {
            set.add(iter.next());
            if(count++ > 1000){
                count = -9000;
                log.warn("[JSON TOLONG], large than {}, json:{}", count, jsonObject.toString());
            }
        }
        return set;
    }

    private static <T extends GeneratedMessage.Builder> GeneratedMessage.Builder genBuilder(Class<T> clazz) {
        GeneratedMessage.Builder builder;
        try {
            String className = clazz.getName();
            String messageClassName = className.substring(0, className.length() - "$Builder".length());
            Class klass = Class.forName(messageClassName);
            builder = (GeneratedMessage.Builder) MethodUtils.invokeStaticMethod(klass, "newBuilder");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return builder;
    }


    /**
     * 反序列化
     *
     * @param bytes 已经被序列化的数组
     * @param klass 需要反序列化成的类型
     * @param <T>   不解释
     * @return 反序列化后的实例
     */
    public static <T> T fromBytes4Pb(byte[] bytes, Class<T> klass) {
        try {
            T obj = (T) MethodUtils.invokeStaticMethod(klass, "parseFrom", bytes);
            return obj;
        } catch (Exception e) {
            log.warn("反序列化失败:{}, byteStr:{}", e.getMessage(), new String(bytes));
        }
        return null;
    }

    /**
     * 解析json变成Map
     * @param json
     */
    public static Map<String,String> stringToTreeMap(String json) {


        JSONObject myJsonObject = JSON.parseObject(json);
        Map<String,String> treeMap = new TreeMap<>();
        Iterator keys = myJsonObject.keySet().iterator();
        String key;
        Object value;
        while( keys.hasNext())
        {
            key = (String)keys.next();
            value = myJsonObject.get(key);
            treeMap.put(key,String.valueOf(value));
        }

        return treeMap;
    }


    /**
     * json字符转换类型到clazz
     * @param map json字符串
     * @param clazz 类型
     * @param <T> 转换后的类型
     * @return
     */
    public static <T> T readValueByMap(Map<String,String> map, Class<T> clazz) {
        return readValue(JSON.toJSON(map).toString(),clazz);
    }

    /**
     * 将map 转换成类型对象
     * @param map
     * @param beanClass
     * @return
     */
    public static Object mapToObject(Map<String, Object> map, Class<?> beanClass)  {
        if (map == null)
            return null;
        Object obj = null;
        try{
            obj = beanClass.newInstance();
            Field[] fields = FieldUtils.getAllFields(beanClass.getClass());
            for (Field field : fields) {
                try {
                    int mod = field.getModifiers();
                    if(Modifier.isStatic(mod) || Modifier.isFinal(mod)){
                        continue;
                    }
                    field.setAccessible(true);
                    field.set(obj, map.get(field.getName()));
                } catch (Exception e) {
                }
            }
        } catch (Exception e) {
        }
        return obj;
    }


    /**
     * 将类型转换成map
     * @param object
     * @return
     */
    public static Map<String, Object> objectToMap(Object object) {
        Field[] fields = FieldUtils.getAllFields(object.getClass());// klass.getFields();
        Map<String, Object> map = new HashMap<>();
        if(null == fields){
            return map;
        }
        for (Field field : fields) {
            try {
                field.setAccessible(true);
                map.put(field.getName(), field.get(object));
            } catch (Exception e) {
            }
        }
        return  map;
    }
}
