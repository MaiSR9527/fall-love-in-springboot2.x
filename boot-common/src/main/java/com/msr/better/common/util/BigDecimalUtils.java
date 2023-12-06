package com.msr.better.common.util;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * @date: 2023-09-12
 * @author: maisrcn@qq.com
 */
public class BigDecimalUtils {

    public static final RoundingMode ROUNDING_MODE = RoundingMode.HALF_UP;
    //    public static final int SCALE = 6;
    public static final int SCALE = 10;
    public static final MathContext MATH_CONTEXT = new MathContext(SCALE, ROUNDING_MODE);

    public static final BigDecimal ZERO = new BigDecimal("0");
    public static final BigDecimal ONE = new BigDecimal("1");
    /**
     * -1
     */
    public static final BigDecimal MINUS_ONE = new BigDecimal("-1");
    public static final BigDecimal HUNDREND = new BigDecimal(100);

    /**
     * 设置保留指定小数位，并四舍五入
     *
     * @param values
     * @param scale
     * @return
     */
    public static final List<BigDecimal> listScale(List<BigDecimal> values, int scale) {
        if (values == null || values.isEmpty()) {
            return values;
        } else {
            return values.stream().map(item -> item == null ? null : item.setScale(scale, ROUNDING_MODE)).collect(Collectors.toList());
        }
    }

    /**
     * 保留6位小数
     *
     * @param values
     * @return
     */
    public static final List<BigDecimal> listScale6(List<BigDecimal> values) {
        return listScale(values, 6);
    }

    /**
     * 列表求和
     *
     * @param values
     * @return
     */
    public static BigDecimal listSum(List<BigDecimal> values) {
        return listStatistics(values, (v1, v2) -> valueCalc(v1, v2, BigDecimal::add)).orElse(new BigDecimal("0"));
    }

    /**
     * v1 operator v2, operator: + - * / 等。如果v1 == null，返回v2，如果v2 == null，返回v1，否则 v1 operator v2
     *
     * @param v1
     * @param v2
     * @param operator
     * @return
     */
    public static BigDecimal valueCalc(BigDecimal v1, BigDecimal v2, BiFunction<BigDecimal, BigDecimal, BigDecimal> operator) {
        if (v1 == null) {
            return v2;
        } else if (v2 == null) {
            return v1;
        } else {
            return operator.apply(v1, v2);
        }
    }

    /**
     * 列表最大值, 如果列表所有元素为null，则返回null
     *
     * @param values
     * @return
     */
    public static BigDecimal listMax(List<BigDecimal> values) {
        return listStatistics(values, (v1, v2) -> valueCalc(v1, v2, BigDecimal::max)).orElse(null);
    }

    /**
     * 列表最小值, 如果列表所有元素为null，则返回null
     *
     * @param values
     * @return
     */
    public static BigDecimal listMin(List<BigDecimal> values) {
        return listStatistics(values, (v1, v2) -> valueCalc(v1, v2, BigDecimal::min)).orElse(null);
    }

    /**
     * 平均值, 如果列表所有元素为null，则返回null
     *
     * @param values
     * @return
     */
    public static BigDecimal listAvg(List<BigDecimal> values) {
        // 除不尽会抛出异常，需设置四舍五入
        return listSum(values).divide(new BigDecimal(values.size()), MATH_CONTEXT);
    }

    /**
     * @param values
     * @param function 统计函数
     * @param <T>
     * @return
     */
    public static <T> Optional<T> listStatistics(List<T> values, BiFunction<T, T, T> function) {
        if (CollectionUtils.isEmpty(values)) {
            return Optional.empty();
        } else {
            return values.stream().filter(Objects::nonNull).reduce(function::apply);
        }
    }

    /**
     * @param values
     * @param function 统计函数
     * @param identity 初值
     * @param <T>
     * @return
     */
    public static <T> T listStatistics(List<T> values, BiFunction<T, T, T> function, T identity) {
        if (CollectionUtils.isEmpty(values)) {
            return identity;
        } else {
            return values.stream().filter(Objects::nonNull).reduce(identity, function::apply);
        }
    }

    public static final int SIZE_NOT_SET = 0;

    public static <T> List<T> listCalc(List<T> values1, List<T> values2, BiFunction<T, T, T> function) {
        return listCalc(values1, values2, function, SIZE_NOT_SET);
    }

    /**
     * 建议使用{@link #listCalc(BiListCalcParam)} <br/>
     * 列表计算， 如果value1或value2为null，那么根据size自动补全
     *
     * @param values1
     * @param values2
     * @param function
     * @param <T>
     * @return
     */
    public static <T> List<T> listCalc(List<T> values1, List<T> values2, BiFunction<T, T, T> function, int size) {
        if (size < SIZE_NOT_SET) {
            throw new ExpressionException("size需大于" + SIZE_NOT_SET);
        }

        if (size == SIZE_NOT_SET) {
            if (values1 == null && values2 == null) {
                throw new ExpressionException("value1 与 value2 为 null，建议传入size");
            } else if (values1 != null && values2 != null && values1.size() != values2.size()) {
                throw new ExpressionException("大小不一致，values1.size() = " + values1.size() + ", value2.size() = " + values2.size());
            } else {
                size = values1 != null ? values1.size() : values2.size();
            }
        } else {
            if (values1.size() != size || values2.size() != size) {
                throw new ExpressionException("大小不一致，size = " + size + " values1.size() = " + values1.size() + ", value2.size() = " + values2.size());
            }
        }

        values1 = ExpressionUtils.populateList(values1, null, size);
        values2 = ExpressionUtils.populateList(values2, null, size);

        List<T> resultList = new ArrayList<>(size);

        ListIterator<T> listIterator1 = values1.listIterator();
        ListIterator<T> listIterator2 = values2.listIterator();

        // 由于size已经相等，可以确保两个list同时迭代至最后一个元素
        while (listIterator1.hasNext()) {
            T v = function.apply(listIterator1.next(), listIterator2.next());
            resultList.add(v);
        }

        return resultList;
    }

    /**
     * 两个列表对应元素的计算 <br/>
     *
     * <pre>
     *     {@code
     *          List<BigDecimal> values1 = new ArrayList<>();
     *          values1.add(new BigDecimal("1"));
     *          values1.add(new BigDecimal("2"));
     *
     *          List<BigDecimal> values2 = new ArrayList<>();
     *          values2.add(new BigDecimal("3"));
     *          values2.add(new BigDecimal("5"));
     *
     *          List<BigDecimal> result = BigDecimalFunctions.listCalc(
     *              BiListCalcParam
     *                  .of((v1Item, v2Item) -> v1Item.add(v2Item))
     *                  .values(values1, values2);
     *         );
     *
     *         Assert.assertEquals(result.get(0), new BigDecimal("4"));
     *         Assert.assertEquals(result.get(1), new BigDecimal("7"));
     *     }
     * </pre>
     *
     * @param biListCalcParam
     * @param <T>
     * @return
     */
    public static <T> List<T> listCalc(BiListCalcParam<T> biListCalcParam) {
        return listCalc(biListCalcParam.values1(), biListCalcParam.values2(), biListCalcParam.function(), biListCalcParam.size());
    }

    /**
     * 任意多时段列表计算 <br/>
     * 例子：
     * <pre>{@code
     *     //定义公式, 公式为 10 * (a + b + c)
     *     public static BigDecimal testFunction(Map<String, BigDecimal> arguments) {
     *         BigDecimal a = CalcFunction.getValue(arguments, "a", new BigDecimal("1")); //读取变量a
     *         BigDecimal b = CalcFunction.getValue(arguments, "b", new BigDecimal("2")); //读取变量b
     *         BigDecimal c = CalcFunction.getValue(arguments, "c", new BigDecimal("3")); //读取变量c
     *         return new BigDecimal(10).multiply(a.add(b).add(c));
     *     }
     *
     *     //测试计算公式
     *     @Test
     *     public void test_listCalc() {
     *         List<BigDecimal> listA = new ArrayList<>(); //[10, 11]
     *         List<BigDecimal> listB = new ArrayList<>(); //[20, 21]
     *         List<BigDecimal> listC = new ArrayList<>(); //[30, 31]
     *
     *         listA.add(new BigDecimal("10"));
     *         listB.add(new BigDecimal("20"));
     *         listC.add(new BigDecimal("30"));
     *
     *         listA.add(new BigDecimal("11"));
     *         listB.add(new BigDecimal("21"));
     *         listC.add(new BigDecimal("31"));
     *
     *         List<BigDecimal> resultList = BigDecimalFunctions.listCalc(
     *              MulListCalParam
     *                  .of(BigDecimalFunctionsTest3::testFunction)
     *                  .addArgument("a", listA) //添加变量a
     *                  .addArgument("b", listB) //添加变量b
     *                  .addArgument("c", listC) //添加变量c
     *         );
     *
     *         Assert.assertEquals(resultList.get(0), new BigDecimal("600"));
     *         Assert.assertEquals(resultList.get(1), new BigDecimal("630"));
     *     }
     * }</pre>
     *
     * @param mulListCalParam
     * @param <T>
     * @return
     */
    public static <T> List<T> listCalc(MulListCalParam<T> mulListCalParam) {
        return listCalc(mulListCalParam.argumentsMap(), mulListCalParam.size(), mulListCalParam.function());
    }

    /**
     * 重载 {@link #listCalc(CalcFunction, int, Map)}
     *
     * @param calcFunction
     * @param size
     * @param argumentsMap
     * @param <T>
     * @return
     */
    public static <T> List<T> listCalc(Map</*参数名*/String, List<T>> argumentsMap, int size, CalcFunction<T> calcFunction) {
        return listCalc(calcFunction, size, argumentsMap);
    }

    /**
     * 任意多时段列表计算 <br/>
     * 例子：
     * <pre>{@code
     *     //定义公式, 公式为 10 * (a + b + c)
     *     public static BigDecimal testFunction(Map<String, BigDecimal> arguments) {
     *         BigDecimal a = CalcFunction.getValue(arguments, "a", new BigDecimal("1")); //读取变量a
     *         BigDecimal b = CalcFunction.getValue(arguments, "b", new BigDecimal("2")); //读取变量b
     *         BigDecimal c = CalcFunction.getValue(arguments, "c", new BigDecimal("3")); //读取变量c
     *         return new BigDecimal(10).multiply(a.add(b).add(c));
     *     }
     *
     *     //测试计算公式
     *     @Test
     *     public void test_listCalc() {
     *         List<BigDecimal> listA = new ArrayList<>(); //[10, 11]
     *         List<BigDecimal> listB = new ArrayList<>(); //[20, 21]
     *         List<BigDecimal> listC = new ArrayList<>(); //[30, 31]
     *
     *         listA.add(new BigDecimal("10"));
     *         listB.add(new BigDecimal("20"));
     *         listC.add(new BigDecimal("30"));
     *
     *         listA.add(new BigDecimal("11"));
     *         listB.add(new BigDecimal("21"));
     *         listC.add(new BigDecimal("31"));
     *
     *         Map<String, List<BigDecimal>> argumentsMap = new HashMap<>();
     *         argumentsMap.put("a", listA); //添加变量a
     *         argumentsMap.put("b", listB); //添加变量b
     *         argumentsMap.put("c", listC); //添加变量c
     *
     *         List<BigDecimal> resultList = BigDecimalFunctions.listCalc(BigDecimalFunctionsTest3::testFunction, 2, argumentsMap);
     *
     *         Assert.assertEquals(resultList.get(0), new BigDecimal("600"));
     *         Assert.assertEquals(resultList.get(1), new BigDecimal("630"));
     *     }
     * }</pre>
     *
     * @param calcFunction
     * @param size
     * @param argumentsMap
     * @param <T>
     * @return
     */
    public static <T> List<T> listCalc(CalcFunction<T> calcFunction, int size, Map</*参数名*/String, List<T>> argumentsMap) {
        if (size < SIZE_NOT_SET) {
            throw new ExpressionException("size需大于" + SIZE_NOT_SET);
        }

        if (argumentsMap == null) {
            throw new ExpressionException("arguments不能为null");
        }

        if (argumentsMap.isEmpty()) {
            throw new ExpressionException("arguments大小不能为0");
        }


        // 验证大小是否一致
        for (Map.Entry<String, List<T>> entry : argumentsMap.entrySet()) {
            if (entry.getValue() != null && entry.getValue().size() != size) {
                throw new ExpressionException("参数" + entry.getKey() + "的大小与size不一致，分别为：" + entry.getValue().size() + ", " + size);
            }
        }

        // 填充元素
        Map<String, List<T>> finalArguments = new HashMap<>();
        for (Map.Entry<String, List<T>> entry : argumentsMap.entrySet()) {
            finalArguments.put(entry.getKey(), ExpressionUtils.populateList(entry.getValue(), null, size));
        }

        List<T> resultList = new ArrayList<>(size);
        for (int listIdx = 0; listIdx < size; listIdx++) {
            Map<String, T> argumentMap = new HashMap<>();
            for (Map.Entry<String, List<T>> entry : finalArguments.entrySet()) {
                argumentMap.put(entry.getKey(), entry.getValue().get(listIdx));
            }
            resultList.add(calcFunction.apply(argumentMap));
        }

        return resultList;
    }


    /**
     * 使用 valueSupplier 创建 list
     *
     * @param valueSupplier
     * @param size
     * @return
     */
    public static List<BigDecimal> createList(Supplier<BigDecimal> valueSupplier, int size) {
        return ExpressionUtils.populateList(null, valueSupplier.get(), size);
    }

    /**
     * 创建 list
     *
     * @param value
     * @param size
     * @return
     */
    public static List<BigDecimal> createList(BigDecimal value, int size) {
        return ExpressionUtils.populateList(null, value, size);
    }

    /**
     * 两个列表对应元素相加
     *
     * @param values1
     * @param values2
     * @return
     */
    public static List<BigDecimal> listAdd(List<BigDecimal> values1, List<BigDecimal> values2) {
//        return SimpleListLambadaExpression.<BigDecimal>of((v1, v2) -> valueAdd(v1, v2)).eval(values1, values2);
        return listCalc(values1, values2, BigDecimalFunctions::valueAdd);
    }

    /**
     * 两个列表对应元素相减
     *
     * @param values1
     * @param values2
     * @return
     */
    public static List<BigDecimal> listSubtract(List<BigDecimal> values1, List<BigDecimal> values2) {
//        return SimpleListLambadaExpression.<BigDecimal>of((v1, v2) -> valueSubtract(v1, v2)).eval(values1, values2);
        return listCalc(values1, values2, BigDecimalFunctions::valueSubtract);
    }

    /**
     * 两个列表对应元素相乘
     *
     * @param values1
     * @param values2
     * @return
     */
    public static List<BigDecimal> listMultiply(List<BigDecimal> values1, List<BigDecimal> values2) {
//        return SimpleListLambadaExpression.<BigDecimal>of((v1, v2) -> valueMultiply(v1, v2)).eval(values1, values2);
        return listCalc(values1, values2, BigDecimalFunctions::valueMultiply);
    }

    /**
     * 两个列表对应元素相除
     *
     * @param values1
     * @param values2
     * @return
     */
    public static List<BigDecimal> listDivide(List<BigDecimal> values1, List<BigDecimal> values2) {
        // 除不尽会抛出异常，需设置四舍五入
//        return SimpleListLambadaExpression.<BigDecimal>of((v1, v2) -> valueDivide(v1, v2)).eval(values1, values2);
        return listCalc(values1, values2, BigDecimalFunctions::valueDivide);
    }

    /**
     * list平方
     *
     * @param values
     * @return
     */
    public static List<BigDecimal> listPow2(List<BigDecimal> values) {
        if (values == null || values.isEmpty()) {
            throw new EvaluationException("values 为 null 或 空");
        }
        return values.stream().map(BigDecimalFunctions::valuePow2).collect(Collectors.toList());
    }


    /**
     * 常量转list
     *
     * @param value
     * @param size
     * @return
     */
    public static List<BigDecimal> constantToList(BigDecimal value, Integer size) {
        if (size == null) {
            return null;
        }
        List<BigDecimal> result = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            result.add(value);
        }
        return result;
    }

    /**
     * 获取列表的大小
     *
     * @param values
     * @param <T>
     * @return
     */
    public static <T> Integer getListSize(List<T> values) {
        return values != null ? values.size() : null;
    }

    /**
     * 两个列表对应元素相加
     *
     * @param values
     * @param itemValue
     * @return
     */
    public static List<BigDecimal> listAddValue(List<BigDecimal> values, BigDecimal itemValue) {
        return listAdd(values, constantToList(itemValue, getListSize(values)));
    }

    /**
     * 两个列表对应元素相减
     *
     * @param values
     * @param itemValue
     * @return
     */
    public static List<BigDecimal> listSubtractValue(List<BigDecimal> values, BigDecimal itemValue) {
        return listSubtract(values, constantToList(itemValue, getListSize(values)));
    }

    /**
     * 两个列表对应元素相乘，如果values为null则抛出异常
     *
     * @param values
     * @param itemValue
     * @return
     */
    public static List<BigDecimal> listMultiplyValue(List<BigDecimal> values, BigDecimal itemValue) {
        return listMultiply(values, constantToList(itemValue, getListSize(values)));
    }

    /**
     * 两个列表对应元素相乘, 如果values为null则返回null
     *
     * @param values
     * @param itemValue
     * @return
     */
    public static List<BigDecimal> listMultiplyValueNullable(List<BigDecimal> values, BigDecimal itemValue) {
        if (values == null) {
            return null;
        } else {
            return listMultiplyValue(values, itemValue);
        }
    }

    /**
     * 两个列表对应元素相除
     *
     * @param values
     * @param itemValue
     * @return
     */
    public static List<BigDecimal> listDivideValue(List<BigDecimal> values, BigDecimal itemValue) {
        return listDivide(values, constantToList(itemValue, getListSize(values)));
    }

    /**
     * 两个元素相加
     *
     * @param value1
     * @param value2
     * @return
     */
    public static BigDecimal valueAdd(BigDecimal value1, BigDecimal value2) {
        if (value1 == null || value2 == null) {
            return null;
        }
        return value1.add(value2);
    }

    /**
     * 两个元素相减
     *
     * @param value1
     * @param value2
     * @return
     */
    public static BigDecimal valueSubtract(BigDecimal value1, BigDecimal value2) {
        if (value1 == null || value2 == null) {
            return null;
        }
        return value1.subtract(value2);
    }

    /**
     * 两个元素相乘
     *
     * @param value1
     * @param value2
     * @return
     */
    public static BigDecimal valueMultiply(BigDecimal value1, BigDecimal value2) {
        if (value1 == null || value2 == null) {
            return null;
        }
        return value1.multiply(value2);
    }

    /**
     * 两个元素相除
     *
     * @param value1
     * @param value2
     * @return
     */
    public static BigDecimal valueDivide(BigDecimal value1, BigDecimal value2) {
        if (value1 == null || value2 == null) {
            return null;
        }
        if (ZERO.compareTo(value2) == 0) {
            return null;
        }
        return value1.divide(value2, MATH_CONTEXT);
    }

    /**
     * 平方
     *
     * @param value
     * @return
     */
    public static BigDecimal valuePow2(BigDecimal value) {
        if (value == null) {
            return null;
        }
        return value.multiply(value);
    }

    /**
     * 把小数转换为百分数，如 0.2 返回 20
     *
     * @param value
     * @return
     */
    public static BigDecimal toPercent(BigDecimal value) {
        return value != null ? HUNDREND.multiply(value) : value;
    }

    /**
     * 把小数转换为百分数，如 0.2 返回 20
     *
     * @param list
     * @return
     */
    public static List<BigDecimal> toPercent(List<BigDecimal> list) {
        if (list == null) {
            return null;
        }
        return BigDecimalFunctions.listMultiplyValue(list, HUNDREND);
    }


    /**
     * a == b
     *
     * @param a
     * @param b
     * @return
     */
    public static boolean eq(BigDecimal a, BigDecimal b) {
        return a.compareTo(b) == 0;
    }

    /**
     * a >= b
     *
     * @param a
     * @param b
     * @return
     */
    public static boolean ge(BigDecimal a, BigDecimal b) {
        return a.compareTo(b) >= 0;
    }

    /**
     * a > b
     *
     * @param a
     * @param b
     * @return
     */
    public static boolean gt(BigDecimal a, BigDecimal b) {
        return a.compareTo(b) > 0;
    }

    /**
     * a <= b
     *
     * @param a
     * @param b
     * @return
     */
    public static boolean le(BigDecimal a, BigDecimal b) {
        return a.compareTo(b) <= 0;
    }

    /**
     * a < b
     *
     * @param a
     * @param b
     * @return
     */
    public static boolean lt(BigDecimal a, BigDecimal b) {
        return a.compareTo(b) < 0;
    }

    /**
     * a, b 返回较小的那个
     *
     * @param a
     * @param b
     * @return
     */
    public static BigDecimal min(BigDecimal a, BigDecimal b) {
        return ge(a, b) ? b : a;
    }

    /**
     * a, b 返回较大的那个
     *
     * @param a
     * @param b
     * @return
     */
    public static BigDecimal max(BigDecimal a, BigDecimal b) {
        return ge(a, b) ? a : b;
    }

    /**
     * 取负数
     *
     * @param value
     * @return
     */
    public static BigDecimal neg(BigDecimal value) {
        if (value == null) {
            return null;
        }
        return value.multiply(MINUS_ONE);
    }

    /**
     * 设置初值，如果value为null，那么返回defaultValue
     *
     * @param value
     * @param defaultValue
     * @return
     */
    public static BigDecimal initValue(BigDecimal value, BigDecimal defaultValue) {
        return value != null ? value : defaultValue;
    }

    /**
     * a + b
     *
     * @param a
     * @param b
     * @return
     */
    public static BigDecimal add(BigDecimal a, BigDecimal b) {
        if (a == null || b == null) {
            return null;
        }
        return a.add(b);
    }

    /**
     * a - b
     *
     * @param a
     * @param b
     * @return
     */
    public static BigDecimal subtract(BigDecimal a, BigDecimal b) {
        if (a == null || b == null) {
            return null;
        }
        return a.subtract(b);
    }

    /**
     * a * b
     *
     * @param a
     * @param b
     * @return
     */
    public static BigDecimal multiply(BigDecimal a, BigDecimal b) {
        if (a == null || b == null) {
            return null;
        }
        return a.multiply(b);
    }

    /**
     * a / b
     *
     * @param a
     * @param b
     * @return
     */
    public static BigDecimal divide(BigDecimal a, BigDecimal b) {
        if (a == null || b == null) {
            return null;
        }
        return a.divide(b, MATH_CONTEXT);
    }


    public static List<BigDecimal> listSubtractExt(List<BigDecimal> values1, List<BigDecimal> values2) {
        if (null == values1 && null != values2) {
            List<BigDecimal> result = new ArrayList();
            Iterator var3 = values2.iterator();

            while(var3.hasNext()) {
                BigDecimal v2 = (BigDecimal)var3.next();
                result.add(neg(v2));
            }

            return result;
        } else {
            List<BigDecimal> result = listCalcExtend(values1, values2, (v1, v2x) -> {
                if (null == v1) {
                    return BigDecimalFunctions.neg(v2x);
                } else {
                    return null == v2x ? v1 : BigDecimalFunctions.valueSubtract(v1, v2x);
                }
            });
            return result;
        }
    }

    public static List<BigDecimal> listSubtractNullable(List<BigDecimal> values1, List<BigDecimal> values2) {
        return !CollectionUtils.isEmpty(values1) && !CollectionUtils.isEmpty(values2) ? BigDecimalFunctions.listSubtract(values1, values2) : null;
    }

    public static BigDecimal valueScale(BigDecimal value, int scale) {
        return value == null ? null : value.setScale(scale, ROUNDING_MODE);
    }

    public static List<BigDecimal> listAddExtend(List<BigDecimal> values1, List<BigDecimal> values2) {
        return listCalcExtend(values1, values2, BigDecimalFunctionsExtend::addExtend);
    }

    @SafeVarargs
    public static List<BigDecimal> listAddExtend(List<BigDecimal>... valuesList) {
        if (valuesList == null) {
            return null;
        } else {
            List<BigDecimal> result = null;
            List[] var2 = valuesList;
            int var3 = valuesList.length;

            for(int var4 = 0; var4 < var3; ++var4) {
                List<BigDecimal> decimals = var2[var4];
                result = listAddExtend(result, decimals);
            }

            return result;
        }
    }

    public static List<BigDecimal> listAddExtend(List<List<BigDecimal>> valuesList) {
        if (valuesList == null) {
            return null;
        } else {
            List<BigDecimal> result = null;

            List decimals;
            for(Iterator var2 = valuesList.iterator(); var2.hasNext(); result = listAddExtend(result, decimals)) {
                decimals = (List)var2.next();
            }

            return result;
        }
    }

    public static List<BigDecimal> listSubtractExtend(List<BigDecimal> values1, List<BigDecimal> values2) {
        return listCalcExtend(values1, values2, BigDecimalFunctionsExtend::subtractExtend);
    }

    public static List<BigDecimal> listDivideExtend(List<BigDecimal> values1, List<BigDecimal> values2) {
        return listCalcExtend(values1, values2, BigDecimalFunctions::valueDivide);
    }

    public static List<BigDecimal> listMultiplyExtend(List<BigDecimal> values1, List<BigDecimal> values2) {
        return listCalcExtend(values1, values2, BigDecimalFunctions::valueMultiply);
    }

    public static List<BigDecimal> listDivideOfValues2Abs(List<BigDecimal> values1, List<BigDecimal> values2) {
        return listCalcExtend(values1, values2, BigDecimalFunctionsExtend::valueDivideOfAbs);
    }

    public static List<BigDecimal> listMultiplyValueExtend(List<BigDecimal> values, BigDecimal itemValue) {
        if (values == null) {
            return null;
        } else {
            BigDecimal nan = null;
            return itemValue == null ? (List)values.stream().map((v) -> {
                return nan;
            }).collect(Collectors.toList()) : (List)values.stream().map((v) -> {
                return v == null ? nan : itemValue.multiply(v);
            }).collect(Collectors.toList());
        }
    }

    public static BigDecimal listAvg(List<BigDecimal> values) {
        return CollectionUtils.isEmpty(values) ? null : listSum(values).divide(new BigDecimal(values.size()), MATH_CONTEXT);
    }

    public static BigDecimal listAvgResultNull(List<BigDecimal> values) {
        return CollectionUtils.isEmpty(values) ? null : valueDivide(listSumExtend(values), new BigDecimal(values.size()));
    }

    public static BigDecimal listSumExtend(List<BigDecimal> values) {
        return (BigDecimal)listStatistics(values, (v1, v2) -> {
            return valueCalc(v1, v2, BigDecimal::add);
        }).orElse((Object)null);
    }

    public static <T> List<T> listCalcExtend(List<T> values1, List<T> values2, BiFunction<T, T, T> function) {
        if (values1 == null && values2 == null) {
            return null;
        } else if (values1 == null) {
            return values2;
        } else if (values2 == null) {
            return values1;
        } else {
            int valuse1Size = values1.size();
            int valuse2Size = values2.size();
            List<T> resultList = new ArrayList(Math.max(valuse1Size, valuse2Size));

            int i;
            for(i = 0; i < valuse1Size; ++i) {
                if (i < valuse2Size) {
                    resultList.add(function.apply(values1.get(i), values2.get(i)));
                } else {
                    resultList.add(function.apply(values1.get(i), (Object)null));
                }
            }

            for(i = valuse1Size; i < valuse2Size; ++i) {
                resultList.add(function.apply(values2.get(i), (Object)null));
            }

            return resultList;
        }
    }

    public static BigDecimal addExtend(BigDecimal value1, BigDecimal value2) {
        return value1 == null ? value2 : value1.add(value2 == null ? BigDecimal.ZERO : value2);
    }

    public static BigDecimal divideExtend(BigDecimal a, BigDecimal b) {
        if (a != null && b != null) {
            return BigDecimal.ZERO.compareTo(a) != 0 && BigDecimal.ZERO.compareTo(b) != 0 ? divide(a, b) : BigDecimal.ZERO;
        } else {
            return null;
        }
    }

    public static BigDecimal valueAddExtend(BigDecimal value1, BigDecimal value2) {
        return value1 == null ? (value2 == null ? BigDecimal.ZERO : value2) : value1.add(value2 == null ? BigDecimal.ZERO : value2);
    }

    public static BigDecimal subtractExtend(BigDecimal value1, BigDecimal value2) {
        if (value1 == null) {
            return value2 == null ? null : value2.negate();
        } else {
            return value2 == null ? value1 : value1.subtract(value2);
        }
    }

    public static BigDecimal valueDivideOfAbs(BigDecimal value1, BigDecimal value2) {
        if (value1 != null && value2 != null) {
            return ZERO.compareTo(value2) == 0 ? null : value1.abs().divide(value2.abs(), MATH_CONTEXT);
        } else {
            return null;
        }
    }

    public static BigDecimal valueDivide(BigDecimal value1, BigDecimal value2, MathContext mathContext) {
        if (value1 != null && value2 != null) {
            return ZERO.compareTo(value2) == 0 ? null : value1.divide(value2, mathContext);
        } else {
            return null;
        }
    }

    public static BigDecimal minExt(BigDecimal a, BigDecimal b) {
        if (Objects.isNull(a)) {
            return b;
        } else {
            return Objects.isNull(b) ? a : min(a, b);
        }
    }

    public static BigDecimal maxExt(BigDecimal a, BigDecimal b) {
        if (Objects.isNull(a)) {
            return b;
        } else {
            return Objects.isNull(b) ? a : max(a, b);
        }
    }

    public static List<BigDecimal> listMultiplyValueNullableScale6(List<BigDecimal> values, BigDecimal itemValue) {
        return listScale6(listMultiplyValueNullable(values, itemValue));
    }

    public static BigDecimal listSum(List<BigDecimal> values, BigDecimal orElse) {
        return (BigDecimal)listStatistics(values, (v1, v2) -> {
            return valueCalc(v1, v2, BigDecimal::add);
        }).orElse(orElse);
    }

    public static List<BigDecimal> listAddValueNotNull(List<BigDecimal> values, BigDecimal value) {
        return listAddExtend(values, constantToList(value, getListSize(values)));
    }
}
