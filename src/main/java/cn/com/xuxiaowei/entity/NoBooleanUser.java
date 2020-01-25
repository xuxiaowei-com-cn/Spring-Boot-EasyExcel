package cn.com.xuxiaowei.entity;

import com.alibaba.excel.annotation.ExcelProperty;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import lombok.Data;

import java.io.Serializable;

/**
 * 无 Boolean 类型的 {@link User}
 *
 * @author xuxiaowei
 * @since 0.0.1
 */
@Data
public class NoBooleanUser implements Serializable {

    /**
     * 用户主键
     * <p>
     * 使用 {@link ExcelProperty#value()} 设置该属性对应的列
     */
    @ExcelProperty(value = "userId")
    @TableId("user_id")
    private Long userId;

    /**
     * 用户名
     * <p>
     * 使用 {@link ExcelProperty#index()} 设置该属性对应的列
     */
    @ExcelProperty(index = 3)
    @TableField("username")
    private String username;

    /**
     * 密码
     * <p>
     * 使用 {@link ExcelProperty#value()} 和 {@link ExcelProperty#index()} 设置该属性对应的列，
     * 但 {@link ExcelProperty#index()} 优先级更高，而 {@link ExcelProperty#value()} 将被忽略
     * <p>
     * 为了测试，在 Excel 中第 5 列（从0开始）设置了测试数据，列名为 test ,但是由于设置了 {@link ExcelProperty#index()}，
     * 将读取第 3 列（从0开始），而不会读取 test 所在的第 5 列(从0开始)
     */
    @ExcelProperty(value = "test", index = 2)
    @TableField("password")
    private String password;

    /**
     * 性别，0 未知，1 男， 2 女
     */
    @ExcelProperty(index = 4)
    @TableField("sex")
    private Integer sex;

    /**
     * 是否逻辑删除，1 已删除，0 未删除，默认为 0，数据库类型为 tinyint，长度为 1，对应实体类为 Boolean，0 为 false，1 为 true
     * <p>
     * 未使用 {@link ExcelProperty} , 按照顺序将数据填入到该属性
     */
    @TableField("deleted")
    @TableLogic
    private String deleted;

}
