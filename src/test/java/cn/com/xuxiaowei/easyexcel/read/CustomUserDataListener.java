package cn.com.xuxiaowei.easyexcel.read;

import cn.com.xuxiaowei.entity.User;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.excel.exception.ExcelDataConvertException;
import com.alibaba.excel.metadata.CellData;
import com.alibaba.excel.metadata.property.ExcelContentProperty;
import com.alibaba.excel.read.metadata.holder.ReadRowHolder;
import com.alibaba.excel.read.metadata.holder.ReadSheetHolder;
import com.alibaba.excel.read.metadata.property.ExcelReadHeadProperty;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 自定义 接收解析后的每条数据的返回
 * <p>
 * 增加异常数据处理
 * <p>
 * {@link CustomUserDataListener} 不能被 spring 管理，要每次读取 excel 都要 new ,然后里面用到 spring 可以构造方法传进去
 *
 * @author xuxiaowei
 */
@Slf4j
class CustomUserDataListener extends AnalysisEventListener<User> {

    private User user;

    List<User> list = new ArrayList<>();

    /**
     * 每隔 5 条存储数据库，实际使用中可以 3000 条，然后清理 list ，方便内存回收
     */
    private static final int BATCH_COUNT = 5;

    /**
     * 未使用 spring ,如果使用了 spring ,请使用下面的构造器
     */
    public CustomUserDataListener() {
        this.user = new User();
    }

    /**
     * 如果使用了 spring ,请使用这个构造方法。每次创建 Listener 的时候需要把 spring 管理的类传进来
     */
    public CustomUserDataListener(User user) {
        this.user = user;
    }

    /**
     * 每一条数据解析都会来调用
     */
    @Override
    public void invoke(User user, AnalysisContext analysisContext) {

        // 有关您当前正在处理的工作表的所有信息
        ReadSheetHolder readSheetHolder = analysisContext.readSheetHolder();

        // 当前运行的单元格的行
        ReadRowHolder readRowHolder = analysisContext.readRowHolder();

        log.debug("Sheet Name：{}", readSheetHolder.getSheetName());
        log.debug("Sheet No：{}", readSheetHolder.getSheetNo());
        log.debug("大约总行数：{}", readSheetHolder.getApproximateTotalRowNumber());
        log.debug("读取到第 {} 行", readRowHolder.getRowIndex());
        log.debug("解析到一条数据:{}", JSON.toJSONString(user));

        list.add(user);

        // 达到 BATCH_COUNT 了，需要去存储一次数据库，防止数据几万条数据在内存，容易OOM
        if (list.size() >= BATCH_COUNT) {

            saveUserData();

            // 存储完成清理 list
            list.clear();
        }
    }

    /**
     * @param exception 异常，通常为 {@link ExcelDataConvertException}
     * @param context   上下文是Excel读者的主要锚点。
     * @throws Exception {@link ExcelDataConvertException} 异常
     */
    @Override
    public void onException(Exception exception, AnalysisContext context) throws Exception {

        if (exception instanceof ExcelDataConvertException) {

            // 异常强制类型转换
            ExcelDataConvertException excelDataConvertException = (ExcelDataConvertException) exception;

            // 当前运行的单元格的行
            ReadRowHolder readRowHolder = context.readRowHolder();

            // 读取到第 {} 列时出现数据异常
            Integer columnIndex = excelDataConvertException.getColumnIndex();

            // 有关您当前正在处理的工作表的所有信息
            ReadSheetHolder readSheetHolder = context.readSheetHolder();

            // Excel 头属性
            ExcelReadHeadProperty excelReadHeadProperty = readSheetHolder.getExcelReadHeadProperty();

            log.debug("Sheet Name：{}", readSheetHolder.getSheetName());
            log.debug("Sheet No：{}", readSheetHolder.getSheetNo());
            log.debug("大约总行数：{}", readSheetHolder.getApproximateTotalRowNumber());
            log.debug("读取到第 {} 行时出现数据异常", excelDataConvertException.getRowIndex());
            log.debug("读取到第 {} 列时出现数据异常", columnIndex);
            log.debug("异常信息：{}", excelDataConvertException.getMessage());

            // 获取当前行分析结果
            Object currentRowAnalysisResult = readRowHolder.getCurrentRowAnalysisResult();
            log.debug("异常数据：{}", currentRowAnalysisResult);

            // 将异常数据转换为 LinkedHashMap
            @SuppressWarnings("all")
            LinkedHashMap<Integer, Object> currentRowAnalysisResultLinkedHashMap = (LinkedHashMap) currentRowAnalysisResult;

            // 表格 配置列信息
            Map<Integer, ExcelContentProperty> contentPropertyMap = excelReadHeadProperty.getContentPropertyMap();

            // 创建一个 User，准备防止异常数据处理好的结果
            User user = new User();

            // 遍历 Map
            for (Map.Entry<Integer, ExcelContentProperty> entry : contentPropertyMap.entrySet()) {

                // Map 中的 Key
                Integer key = entry.getKey();

                // 当遍历的列与错误的列形同时，处理数据
                if (key.equals(columnIndex)) {
                    customData(currentRowAnalysisResultLinkedHashMap, entry, user, true);
                } else {
                    customData(currentRowAnalysisResultLinkedHashMap, entry, user, false);
                }
            }

            log.debug(String.valueOf(user));
        } else {
            super.onException(exception, context);
        }
    }


    /**
     * @param currentRowAnalysisResultLinkedHashMap 获取表格中的数据
     * @param entry                                 遍历 Map
     * @param user                                  此行数据
     * @param exception                             是否是异常数据
     * @throws NoSuchFieldException   {@link Class#getDeclaredField(String)} 异常
     * @throws IllegalAccessException {@link Field#set(Object, Object)} 异常
     */
    private void customData(LinkedHashMap<Integer, Object> currentRowAnalysisResultLinkedHashMap,
                            Map.Entry<Integer, ExcelContentProperty> entry, User user, boolean exception)
            throws NoSuchFieldException, IllegalAccessException {

        // Map 中的 Key
        Integer key = entry.getKey();

        // Map 中的 Value
        ExcelContentProperty value = entry.getValue();

        // Value 的属性
        Field field = value.getField();

        // 属性的名字
        String name = field.getName();

        // 获取表格中的数据
        @SuppressWarnings("all")
        CellData cellData = (CellData) currentRowAnalysisResultLinkedHashMap.get(key);

        // number 类型的数据
        BigDecimal numberValue = cellData.getNumberValue();

        // String 类型的数据
        String stringValue = cellData.getStringValue();

        // 获取 Class
        Class<? extends User> userClass = user.getClass();

        // 获取 Class 中的属性
        Field declaredField = userClass.getDeclaredField(name);

        // 取消访问检查
        declaredField.setAccessible(true);

        switch (name) {
            case User.USERID:
                // Long 类型的 userId

                // 异常数据
                if (exception) {
                    // 设置默认值
                    declaredField.set(user, 900000000000000L + System.currentTimeMillis());
                } else {
                    declaredField.set(user, numberValue.longValue());
                }

                break;
            case User.SEX:
                // int 类型的 sex 处理

                // 异常数据
                if (exception) {
                    // 设置默认值
                    declaredField.set(user, 0);
                } else {
                    if (BigDecimal.ZERO.equals(numberValue) || BigDecimal.ONE.equals(numberValue) || new BigDecimal(2).equals(numberValue)) {
                        declaredField.set(user, numberValue.intValue());
                    } else {
                        declaredField.set(user, 0);
                    }
                }

                break;
            case User.DELETED:
                // Boolean 类型的 deleted 处理

                // 异常数据
                if (exception) {
                    // 设置默认值
                    declaredField.set(user, BigDecimal.ZERO);
                } else {
                    // 当值为 1 时，设置 Boolean.TRUE
                    if (numberValue.equals(BigDecimal.ONE)) {
                        declaredField.set(user, Boolean.TRUE);
                    } else {
                        // 当值不为 1 时，设置 Boolean.FALSE
                        declaredField.set(user, Boolean.FALSE);
                    }
                }

                break;
            default:

                // 异常数据
                // 其他异常，设置为：-
                if (exception) {
                    // 设置默认值
                    declaredField.set(user, "-");
                } else {
                    declaredField.set(user, stringValue);
                }

                break;
        }
    }

    /**
     * 所有数据解析完成了，就会来调用
     */
    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {
        log.debug("{}条数据，开始存储数据库！", list.size());

        // 这里也要保存数据，确保最后遗留的数据也存储到数据库
        list.parallelStream().forEach(u -> log.debug(String.valueOf(u)));

        log.info("所有数据解析完成！");
    }

    private void saveUserData() {
        log.debug("{}条数据，开始存储数据库！", list.size());

        list.parallelStream().forEach(u -> log.debug(String.valueOf(u)));

        log.info("存储数据库成功！");
    }

}
