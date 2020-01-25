package cn.com.xuxiaowei.enums;

import lombok.Getter;

/**
 * 类型
 *
 * @author xuxiaowei
 * @since 0.0.1
 */
@Getter
public enum TypeEnum {

    /**
     * csv
     */
    CSV(".csv");

    private String value;

    TypeEnum(String value) {
        this.value = value;
    }

}
