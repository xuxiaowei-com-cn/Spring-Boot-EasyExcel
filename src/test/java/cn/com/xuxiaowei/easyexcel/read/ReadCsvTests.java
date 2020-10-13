package cn.com.xuxiaowei.easyexcel.read;

import cn.com.xuxiaowei.entity.User;
import com.alibaba.excel.EasyExcel;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * 测试 Excel CSV 数据读取
 * <p>
 * 暂不能读取
 *
 * @author xuxiaowei
 * @since 0.0.1
 */
@Slf4j
@SpringBootTest
class ReadCsvTests {

    /**
     * 读取本地项目文件夹路径（Spring Boot）
     */
    private final String PROJECT_PATH = System.getProperty("user.dir");


    /**
     * <p>
     * 使用 easyexcel 时，实体类不可使用<code>@Accessors(chain = true)</code>
     */
    @Test
    void simpleRead() {
        String simpleRead = PROJECT_PATH + "/easyexcel/read/simpleRead.csv";
        log.debug("读取路径：{}", simpleRead);
        EasyExcel.read(simpleRead, User.class, new UserDataListener()).sheet().doRead();
    }

}
