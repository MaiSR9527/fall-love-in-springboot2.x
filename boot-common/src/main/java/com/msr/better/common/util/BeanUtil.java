package com.msr.better.common.util;

import cn.hutool.core.util.NumberUtil;
import com.google.common.collect.Lists;
import com.google.common.collect.Queues;
import com.google.common.collect.Sets;
import com.msr.better.common.annotation.BaseCodeField;
import com.msr.better.common.annotation.IdKey;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Queue;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.Supplier;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author MaiShuRen
 * @site https://www.maishuren.top
 * @since 2022-06-15 22:58
 **/
public class BeanUtil {
    private static final Logger log = LoggerFactory.getLogger(BeanUtil.class);

    private static BeanUtilsBean beanUtilsBean;

    private BeanUtil() {
    }

    static {
        if (beanUtilsBean == null) {
            beanUtilsBean = BeanUtilsBean.getInstance();
        }
    }

    //检查基本类型
    static final Map<Class<?>, BiFunction<String, Object, Boolean>> numCheckMap =
            new IdentityHashMap<>() {
                {
                    put(Integer.class, (mock, parameter) -> NumberUtils.isCreatable(mock) && (Integer.parseInt(mock) == (Integer) parameter));
                    put(Byte.class, (mock, parameter) -> NumberUtils.isCreatable(mock) && (Byte.parseByte(mock) == (Byte) parameter));
                    put(Short.class, (mock, parameter) -> NumberUtils.isCreatable(mock) && (Short.parseShort(mock) == (Short) parameter));
                    put(Long.class, (mock, parameter) -> NumberUtils.isCreatable(mock) && (Long.parseLong(mock) == (Long) parameter));
                    put(Float.class, (mock, parameter) -> NumberUtils.isCreatable(mock) && (Float.parseFloat(mock) == (Float) parameter));
                    put(Double.class, (mock, parameter) -> NumberUtils.isCreatable(mock) && (Double.parseDouble(mock) == (Double) parameter));
                    put(int.class, (mock, parameter) -> NumberUtils.isCreatable(mock) && (Integer.parseInt(mock) == (Integer) parameter));
                    put(byte.class, (mock, parameter) -> NumberUtils.isCreatable(mock) && (Byte.parseByte(mock) == (Byte) parameter));
                    put(short.class, (mock, parameter) -> NumberUtils.isCreatable(mock) && (Short.parseShort(mock) == (Short) parameter));
                    put(long.class, (mock, parameter) -> NumberUtils.isCreatable(mock) && (Long.parseLong(mock) == (Long) parameter));
                    put(float.class, (mock, parameter) -> NumberUtils.isCreatable(mock) && (Float.parseFloat(mock) == (Float) parameter));
                    put(double.class, (mock, parameter) -> NumberUtils.isCreatable(mock) && (Double.parseDouble(mock) == (Double) parameter));
                }
            };

    /**
     * bean属性拷贝
     *
     * @param source       输入bean
     * @param target       输入bean
     * @param ignoreFields 可以不传 如：copyProperties(Object source, Object target)
     */
    public static void copyProperties(Object source, Object target, String... ignoreFields) {
        if (source == null || target == null) {
            return;
        }
        Class<?> sourceClass = source.getClass();
        IdKey sourcePK = sourceClass.getAnnotation(IdKey.class);
        Class<?> actualEditable = target.getClass();
        IdKey targetPK = actualEditable.getAnnotation(IdKey.class);
        PropertyDescriptor[] targetPds = org.springframework.beans.BeanUtils.getPropertyDescriptors(actualEditable);
        List<String> ignoreList = (ignoreFields != null) ? Arrays.asList(ignoreFields) : null;

        for (PropertyDescriptor targetPd : targetPds) {
            if (!(targetPd.getWriteMethod() != null && (ignoreFields == null || (!ignoreList.contains(targetPd.getName()))))) {
                continue;
            }
            String targetPropertyName = targetPd.getName();
            if (sourcePK != null && targetPd.getName().equals(releaseColumnNameLower(sourcePK.value()))) {
                doCopy(source, target, targetPd, releaseColumnNameLower(sourcePK.key()));
            } else if (targetPK != null && targetPd.getName().equals(targetPK.key())) {
                doCopy(source, target, targetPd, releaseColumnNameLower(targetPK.value()));
            }
            doCopy(source, target, targetPd, targetPropertyName);

        }
    }

    public static List<?> copyPropertiesToList(List<?> sourceList, Class<?> targetClazz, String... ignoreFields) {
        if (CollectionUtils.isEmpty(sourceList) || null == targetClazz) {
            return new ArrayList<>();
        }

        return sourceList.stream().map(o -> {
            Object target = null;
            try {
                target = targetClazz.getDeclaredConstructor().newInstance();
            } catch (InstantiationException | IllegalAccessException e) {
                throw new RuntimeException(e);
            } catch (InvocationTargetException | NoSuchMethodException e) {
                e.printStackTrace();
            }

            copyProperties(o, target);
            return target;
        }).collect(Collectors.toList());
    }


    public static <T> T copyPropertiesT(Object source, Class<T> targetClazz, String... ignoreFields) {
        T target = null;
        try {
            target = targetClazz.getDeclaredConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
        }

        copyProperties(source, target);
        return target;
    }

    public static <T> T copyPropertiesT(Object source, Supplier<T> supplier, String... ignoreFields) {
        if (supplier == null) {
            return null;
        }

        T target = supplier.get();
        copyProperties(source, target);
        return target;
    }


    private static void doCopy(Object source, Object target, PropertyDescriptor targetPd, String targetPropertyName) {
        PropertyDescriptor sourcePd = org.springframework.beans.BeanUtils.getPropertyDescriptor(source.getClass(), targetPropertyName);
        if (sourcePd != null && sourcePd.getReadMethod() != null) {
            try {
                Method readMethod = sourcePd.getReadMethod();
                if (!Modifier.isPublic(readMethod.getDeclaringClass().getModifiers())) {
                    readMethod.setAccessible(true);
                }
                Object value = readMethod.invoke(source);

                Class sourceType = sourcePd.getPropertyType();
                PropertyDescriptor pd = org.springframework.beans.BeanUtils.getPropertyDescriptor(target.getClass(), targetPd.getName());
                Class targetType = pd.getPropertyType();

                if (sourceType.isEnum() && (Integer.class.equals(targetType) || int.class.equals(targetType))) {//源对象属性是枚举
                    if (value == null) {
                        value = 0;
                    } else {
                        value = Enum.valueOf(sourceType, String.valueOf(value)).ordinal();
                    }
                } else if (targetType.isEnum() && (Integer.class.equals(sourceType) || int.class.equals(sourceType))) {//目标对象属性是枚举
                    if (value == null) {
                        value = 0;
                    }
                    int intValue = (Integer) value;
                    Method method = targetType.getMethod("values");
                    Object[] enumValues = (Object[]) method.invoke(targetType);
                    if (intValue >= 0 && intValue < enumValues.length) {
                        value = enumValues[intValue];
                    } else {
                        return;
                    }

                }

                if (String.class.equals(sourceType) && Number.class.isAssignableFrom(targetType)) {
                    Constructor constructor = targetType.getConstructor(String.class);
                    value = value == null ? value : constructor.newInstance(String.valueOf(value));
                } else if (String.class.equals(targetType) && Number.class.isAssignableFrom(sourceType)) {
                    value = String.valueOf(value);
                } else if (value != null && Date.class.equals(sourceType) && String.class.equals(targetType)) {
                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
                    value = format.format(value);
                } else if (value != null && Date.class.equals(sourceType) && Long.class.equals(targetType)) {
                    value = ((Date) value).getTime();
                } else if (value != null && Long.class.equals(sourceType) && Date.class.equals(targetType)) {
                    value = new Date((Long) value);
                }

                value = copyBaseType(sourceType, targetType, value);

                Method writeMethod = targetPd.getWriteMethod();
                if (!Modifier.isPublic(writeMethod.getDeclaringClass().getModifiers())) {
                    writeMethod.setAccessible(true);
                }
                try {
                    writeMethod.invoke(target, value);
                } catch (Exception e) {
                    log.error("BeanUtil invoke 对象复制出错: writeMethod={}, target={}, value={}", writeMethod, target, value);
                    throw new RuntimeException(e);
                }

            } catch (Throwable e) {
                log.error("BeanUtil 对象复制出错:", e);
                throw new RuntimeException(e);
            }
        }
    }


    /**
     * 克隆对象
     *
     * @param bean
     * @return 结果
     */
    public static Object cloneBean(Object bean) {
        try {
            return beanUtilsBean.cloneBean(bean);
        } catch (Throwable e) {
            log.error("BeanUtil 对象克隆出错:", e);
            throw new RuntimeException(e);
        }
    }

    public static Object copyBaseType(Class<?> sourceType, Class<?> targetType, Object value) {
        if (sourceType.getSimpleName().equals(targetType.getSimpleName())) {
            return value;
        }
        String valStr = value == null ? null : String.valueOf(value);
        do {
            if (NumberUtils.isCreatable(valStr)) {
                break;
            }
            if (StringUtils.isNotBlank(valStr) && numCheckMap.get(targetType) == null) {
                break;
            }
            switch (targetType.getSimpleName()) {
                case "int":
                case "short":
                    return 0;
                case "long":
                    return 0L;
                case "float":
                    return 0f;
                case "double":
                    return 0d;
                default:
                    break;
            }
            if (value == null && targetType == boolean.class) {
                return false;
            } else if (targetType == boolean.class && sourceType == Boolean.class) {
                return value;
            } else if (targetType == Boolean.class && sourceType == boolean.class) {
                return value;
            }
            return value;
        } while (false);

        if (targetType == Integer.class || targetType == int.class) {
            return Integer.parseInt(valStr);
        } else if (targetType == Long.class || targetType == long.class) {
            return Long.parseLong(valStr);
        } else if (targetType == Float.class || targetType == float.class) {
            return Float.parseFloat(valStr);
        } else if (targetType == Double.class || targetType == double.class) {
            return Double.parseDouble(valStr);
        } else if (targetType == Short.class || targetType == short.class) {
            return Double.parseDouble(valStr);
        } else if (targetType == Boolean.class || targetType == boolean.class) {
            return NumberUtil.isNumber(valStr) ? new BigDecimal(valStr).doubleValue() > 0d : value;
        } else if (targetType == String.class) {
            return valStr;
        }
        return value;
    }

    /**
     * 拷贝属性给对象(类型宽松)
     *
     * @param bean
     * @param name  属性名
     * @param value 属性值
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    public static void copyProperty(Object bean, String name, Object value) {
        try {
            Class propertyClazz = beanUtilsBean.getPropertyUtils().getPropertyType(bean, name);

            if (propertyClazz.isEnum() && value instanceof Integer) {//属性枚举型 目标值是整型
                int intValue = (Integer) value;
                Method method = propertyClazz.getMethod("values");
                Object[] enumValues = (Object[]) method.invoke(propertyClazz);
                if (intValue >= 0 && intValue < enumValues.length) {
                    value = enumValues[intValue];
                } else {//不合理的int值范围就不修改
                    return;
                }
            }

            beanUtilsBean.copyProperty(bean, name, value);

        } catch (Throwable e) {
            log.error("BeanUtil 对象属性赋值出错:", e);
            throw new RuntimeException(e);
        }

    }

    /**
     * 将bean装换为一个map(不能将枚举转换为int)
     *
     * @param bean
     * @return 结果
     */
    @SuppressWarnings({"rawtypes"})
    public static Map describe(Object bean) {
        try {
            return beanUtilsBean.describe(bean);
        } catch (Throwable e) {
            log.error("BeanUtil 对象克隆出错:", e);
            throw new RuntimeException(e);
        }
    }


    /**
     * 将bean装换为一个map(能将枚举转换为int)
     *
     * @param bean
     * @return 结果
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    public static Map buildMap(Object bean) {
        if (bean == null) {
            return null;
        }
        try {
            Map map = describe(bean);
            PropertyDescriptor[] pds = beanUtilsBean.getPropertyUtils().getPropertyDescriptors(bean);
            for (PropertyDescriptor pd : pds) {
                Class type = pd.getPropertyType();
                if (type.isEnum()) {
                    Object value = beanUtilsBean.getPropertyUtils().getSimpleProperty(bean, pd.getName());
                    int intValue = 0;
                    if (value != null) {
                        intValue = Enum.valueOf(type, String.valueOf(value)).ordinal();
                    }
                    map.put(pd.getName(), intValue);

                } else if (type == Date.class) {//防止是Timestamp
                    Object value = beanUtilsBean.getPropertyUtils().getSimpleProperty(bean, pd.getName());
                    if (value != null) {
                        Calendar cal = Calendar.getInstance();
                        cal.setTime((Date) value);
                        map.put(pd.getName(), cal.getTime());
                    }
                }
            }
            return map;
        } catch (Throwable e) {
            log.error("BeanUtil 创建Map失败:", e);
            throw new RuntimeException(e);
        }


    }

    /**
     * 将bean列表转换成map的列表
     *
     * @param beanList bean列表
     * @return 结果
     */
    @SuppressWarnings("rawtypes")
    public static List<Map> buildMapList(List beanList) {
        if (beanList != null && !beanList.isEmpty()) {
            List<Map> mapList = new ArrayList<>();
            for (Object bean : beanList) {
                mapList.add(buildMap(bean));
            }
            return mapList;
        }
        return null;
    }


    /**
     * 将map转Bean
     *
     * @param map
     * @param clazz
     * @return 结果
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    public static Object buildBean(Map map, Class clazz) {
        if (map == null) {
            return null;
        }
        Object bean = null;
        try {
            bean = clazz.getDeclaredConstructor().newInstance();
            PropertyDescriptor[] pds = beanUtilsBean.getPropertyUtils().getPropertyDescriptors(clazz);
            for (PropertyDescriptor pd : pds) {
                String fieldName = pd.getName();
                if (map.containsKey(fieldName)) {
                    Object mapValue = map.get(fieldName);
                    Class beanType = pd.getPropertyType();
                    Object beanValue = mapValue;


                    if (beanType.isEnum()) {
                        if (mapValue != null) {
                            if (mapValue instanceof String) {
                                if (String.valueOf(mapValue).matches("\\d+")) {//数字型
                                    mapValue = Integer.parseInt(String.valueOf(mapValue));
                                    int intValue = (Integer) mapValue;

                                    Method method = beanType.getMethod("values");
                                    Object[] enumValues = (Object[]) method.invoke(beanType);
                                    if (intValue >= 0 && intValue < enumValues.length) {
                                        beanValue = enumValues[intValue];
                                    } else {
                                        continue;
                                    }
                                } else {//字符串标识的枚举值
                                    try {
                                        beanValue = Enum.valueOf(beanType, String.valueOf(mapValue));
                                    } catch (IllegalArgumentException e) {//是一个错误的值
                                        continue;
                                    }
                                }

                            } else if (mapValue instanceof Integer) {//整型
                                int intValue = (Integer) mapValue;
                                Method method = beanType.getMethod("values");
                                Object[] enumValues = (Object[]) method.invoke(beanType);
                                if (intValue >= 0 && intValue < enumValues.length) {
                                    beanValue = enumValues[intValue];
                                } else {//超过了枚举的int值范围
                                    continue;
                                }
                            }
                        }
                    } else if (beanType.equals(Date.class)) {
                        if (mapValue != null) {
                            if (mapValue instanceof String) {
                                try {
                                    DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                    beanValue = format.parse(String.valueOf(mapValue));
                                } catch (ParseException e) {
                                    log.error("BeanUtil buildBean string 转 Date 出错!");
                                    continue;
                                }

                            }
                        }
                    }

                    beanUtilsBean.copyProperty(bean, fieldName, beanValue);

                }

            }
            return bean;
        } catch (Throwable e) {
            log.error("BeanUtil 根据map创建bean出错:", e);
            throw new RuntimeException(e);
        }
    }

    /**
     * 拷贝属性给对象(类型严格)
     *
     * @param bean
     * @param name  属性名
     * @param value 属性值
     */
    public static void setProperty(Object bean, String name, Object value) {
        try {
            beanUtilsBean.setProperty(bean, name, value);
        } catch (Throwable e) {
            log.error("BeanUtil 给对象属性赋值出错:", e);
            throw new RuntimeException(e);
        }

    }

    /**
     * 获取对象属性值
     *
     * @param bean bean
     * @param name 属性名
     * @return 结果
     */
    public static Object getProperty(Object bean, String name) {
        try {
            return beanUtilsBean.getPropertyUtils().getSimpleProperty(bean, name);
        } catch (Throwable e) {
            log.error("BeanUtil 获取对象属性值出错:", e);
            throw new RuntimeException(e);
        }

    }


    /**
     * thrift集合转list<?>
     *
     * @param source 输入bean
     * @param clazz  输入bean的类型
     * @return 结果
     */
    public static <T> List<?> thriftListToBean(List<? extends T> source, Class clazz) {
        //clone后的集合
        try {
            List<T> temp = Lists.newArrayList();
            for (T t : source) {
                T temporary = (T) clazz.getDeclaredConstructor().newInstance();
                BeanUtils.copyProperties(temporary, t);
                temp.add(temporary);
            }
            return temp;
        } catch (Throwable e) {
            log.error("BeanUtil 获取对象属性值出错:", e);
            throw new RuntimeException(e);
        }
    }

    /**
     * @param source
     * @param dest
     * @return 结果
     */
    public static <T> T thriftBeanToBean(Class<?> source, Class<T> dest) {
        try {
            T temporary = dest.getDeclaredConstructor().newInstance();
            BeanUtils.copyProperties(temporary, source);
            return temporary;
        } catch (Throwable e) {
            log.error("BeanUtil 获取对象属性值出错:", e);
            throw new RuntimeException(e);
        }
    }

    public static String releaseColumnNameLower(String columnName) {
        StringBuilder sb = new StringBuilder();
        Pattern p = Pattern.compile("_[a-z|A-Z]");
        Matcher m = p.matcher(columnName);
        while (m.find()) {
            m.appendReplacement(sb, m.group().toUpperCase());
        }
        m.appendTail(sb);
        return sb.toString().replaceAll("_", "");
    }


    public static <T> T mapToBean(Map<String, String> paramsMap, Class<T> cls) {

        T target = null;
        try {
            target = cls.getDeclaredConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
        }

        return convert(paramsMap, target);
    }

    public static <T> T mapToBean(Map<String, String> paramsMap, Supplier<T> supplier) {

        return convert(paramsMap, supplier.get());
    }

    private static <T> T convert(Map<String, String> paramsMap, T target) {
        if (paramsMap == null || paramsMap.isEmpty() || target == null) {
            return null;
        }
        // 将map转换成bean
        toBean(paramsMap, target);

        return target;
    }


    private static <T> void toBean(Map<String, String> paramsMap, T target) {
        Field[] fields = target.getClass().getDeclaredFields();

        // 初始化对象
        for (Field field : fields) {
            // 获取字段上的注解
            BaseCodeField annotation = field.getAnnotation(BaseCodeField.class);
            // 获取任务条件值
            Object value = Optional.ofNullable(annotation).filter(an -> StringUtils.isNotBlank(an.value()))
                    .map(BaseCodeField::value)
                    .filter(paramsMap::containsKey)
                    .map(paramsMap::get).orElse(null);


            if (value == null) {
                continue;
            }

            // 转换成对应的数据类型
            convertValue(target, field, annotation, value);

        }
    }


    /**
     * 将值转换为对应字段类型
     *
     * @param field      field
     * @param annotation annotation
     * @param value      value
     * @return 结果
     */
    public static <T> void convertValue(T obj, Field field, BaseCodeField annotation, Object value) {


        Class<?> fieldType = field.getType();

        // 日期类型
        if (fieldType == Date.class) {
            try {
                value = DateUtils.parseDate(value.toString(), annotation.format());
            } catch (ParseException e) {
                log.error("field={}, value={}, format={}, format date fail!", field.getName(), value, annotation.format());
            }
            // 集合类型
        } else if (Collection.class.isAssignableFrom(fieldType)) {

            // 获取集合泛型
            Class<?> result = String.class;
            java.lang.reflect.Type cls = field.getGenericType();
            if (cls instanceof ParameterizedType) {
                Class<?> actualTypeArgument = ((Class<?>) ((ParameterizedType) cls).getActualTypeArguments()[0]);
                if (actualTypeArgument != null) result = actualTypeArgument;
            }
            Class<?> actualType = result;

            // 通过给定字符切割字符串
            String[] values = StringUtils.split(value.toString(), annotation.separator());

            // newInstance相应实现类型
            Collection coll = getCollectionType(fieldType, values.length);
            value = Stream.of(values).map(val -> ConvertUtils.convert(val, actualType)).collect(Collectors.toCollection(() -> coll));


            // 数组类型
        } else if (fieldType.isArray()) {
            // 通过给定字符切割字符串
            String[] values = StringUtils.split(value.toString(), annotation.separator());
            // 获取数组类型
            Class<?> actualType = fieldType.getComponentType();
            // 创建数组
            value = Array.newInstance(actualType, values.length);
            for (int i = 0; i < values.length; i++) {
                Array.set(value, i, ConvertUtils.convert(values[i], actualType));
            }
            // 其他类型
        } else if (!field.getType().isInstance(value)) {
            value = ConvertUtils.convert(value.toString(), fieldType);
        }


        field.setAccessible(true);
        try {
            field.set(obj, value);
        } catch (IllegalAccessException e) {
            log.error(String.format("field=%s, value=%s, set value fail!", field.getName(), value), e);
        }

    }


    public static Collection<?> getCollectionType(Class<?> clazz, int initialArraySize) {

        // 非接口, 直接创建
        if (!clazz.isInterface()) {
            try {
                return (Collection<?>) clazz.getDeclaredConstructor().newInstance();
            } catch (Exception e) {
                return Collections.emptyList();
            }
        }

        // list
        if (List.class.isAssignableFrom(clazz)) {
            return Lists.newArrayListWithCapacity(initialArraySize);
            // set
        } else if (Set.class.isAssignableFrom(clazz)) {
            return Sets.newHashSetWithExpectedSize(initialArraySize);
            // queue
        } else if (Queue.class.isAssignableFrom(clazz)) {
            return Queues.newArrayBlockingQueue(initialArraySize);
        }

        return null;

    }


}
