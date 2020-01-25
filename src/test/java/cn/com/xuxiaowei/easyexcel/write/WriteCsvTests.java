package cn.com.xuxiaowei.easyexcel.write;

import cn.com.xuxiaowei.entity.User;
import cn.com.xuxiaowei.enums.TypeEnum;
import cn.com.xuxiaowei.service.IUserService;
import cn.com.xuxiaowei.util.XlsCsvUtils;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.support.ExcelTypeEnum;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.List;

/**
 * 测试 Excel csv 数据写入
 *
 * @author xuxiaowei
 * @since 0.0.1
 */
@Slf4j
@SpringBootTest
class WriteCsvTests {

    @Autowired
    private IUserService iUserService;

    /**
     * 写入本地项目文件夹路径（Spring Boot）
     */
    private String projectPath = System.getProperty("user.dir");

    /**
     * xls 转 csv
     */
    @Test
    public void simpleWrite() throws IOException {

        String fileName = projectPath + "/easyexcel/write/simpleWrite_" + System.currentTimeMillis();

        String xlsFileName = fileName + ExcelTypeEnum.XLS.getValue();
        String csvFileName = fileName + TypeEnum.CSV.getValue();

        List<User> list = iUserService.list();
        EasyExcel.write(xlsFileName, User.class).sheet("sheet1").doWrite(list);

        // XLS 文件
        POIFSFileSystem poifsFileSystem = new POIFSFileSystem(new File(xlsFileName));

        // CSV 文件
        PrintStream printStream = new PrintStream(csvFileName);

        // 创建一个新的 XLS --> CSV转换器
        XlsCsvUtils xlsCsvUtils = new XlsCsvUtils(poifsFileSystem, printStream, -1);

        // 开始将 XLS 文件处理为 CSV
        xlsCsvUtils.process();
    }

}
