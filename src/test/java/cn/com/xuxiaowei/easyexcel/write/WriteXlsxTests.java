package cn.com.xuxiaowei.easyexcel.write;

import cn.com.xuxiaowei.entity.User;
import cn.com.xuxiaowei.service.IUserService;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.support.ExcelTypeEnum;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

/**
 * 测试 Excel xlsx 数据写入
 *
 * @author xuxiaowei
 * @since 0.0.1
 */
@Slf4j
@SpringBootTest
class WriteXlsxTests {

    @Autowired
    private IUserService iUserService;

    /**
     * 写入本地项目文件夹路径（Spring Boot）
     */
    private String projectPath = System.getProperty("user.dir");

    /**
     * 最简单的写
     */
    @Test
    public void simpleWrite() {
        String simpleWrite = projectPath + "/easyexcel/write/simpleWrite_" + System.currentTimeMillis() + ExcelTypeEnum.XLSX.getValue();
        List<User> list = iUserService.list();
        EasyExcel.write(simpleWrite, User.class).sheet("sheet1").doWrite(list);
    }

}
