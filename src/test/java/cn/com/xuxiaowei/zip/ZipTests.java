package cn.com.xuxiaowei.zip;

import cn.com.xuxiaowei.util.FileUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * 压缩文件 测试类
 *
 * @author xuxiaowei
 */
@Slf4j
class ZipTests {

    /**
     * 读取本地项目文件夹路径（Spring Boot）
     */
    private final String PROJECT_PATH = System.getProperty("user.dir");

    /**
     * 单文件压缩
     */
    @Test
    void oneFile() {

        String simpleRead = PROJECT_PATH + "/easyexcel/read/simpleRead.csv";
        log.info("读取路径：{}", simpleRead);

        String fileOutputStreamPath = PROJECT_PATH + "/zip/simpleRead_one_" + System.currentTimeMillis() + ".zip";

        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(fileOutputStreamPath);
        } catch (FileNotFoundException e) {
            log.error("读取文件失败");
            log.error(e.getMessage(), e);
        }

        assert fileOutputStream != null;

        try (ZipOutputStream zipOutputStream = new ZipOutputStream(fileOutputStream)) {

            ZipEntry zipEntry = new ZipEntry("simpleRead.csv");
            zipOutputStream.putNextEntry(zipEntry);
            byte[] read = FileUtils.read(simpleRead);
            zipOutputStream.write(read);
            zipOutputStream.finish();

            log.info("已经完成压缩");

        } catch (IOException e) {
            log.error("压缩文件异常");
            log.error(e.getMessage(), e);
        }

    }

    /**
     * 单文件和文件夹压缩
     */
    @Test
    void oneFileFolder() {

        String simpleRead = PROJECT_PATH + "/easyexcel/read/simpleRead.csv";
        log.info("读取路径：{}", simpleRead);

        String fileOutputStreamPath = PROJECT_PATH + "/zip/simpleRead_one_folder_" + System.currentTimeMillis() + ".zip";

        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(fileOutputStreamPath);
        } catch (FileNotFoundException e) {
            log.error("读取文件失败");
            log.error(e.getMessage(), e);
        }

        assert fileOutputStream != null;

        try (ZipOutputStream zipOutputStream = new ZipOutputStream(fileOutputStream)) {

            ZipEntry zipEntry = new ZipEntry("csv/simpleRead.csv");
            zipOutputStream.putNextEntry(zipEntry);
            byte[] read = FileUtils.read(simpleRead);
            zipOutputStream.write(read);
            zipOutputStream.finish();

            log.info("已经完成压缩");

        } catch (IOException e) {
            log.error("压缩文件异常");
            log.error(e.getMessage(), e);
        }

    }

    /**
     * 多文件压缩
     */
    @Test
    void twoFile() {

        String simpleRead = PROJECT_PATH + "/easyexcel/read/simpleRead.csv";
        log.info("读取路径：{}", simpleRead);
        String simpleRead2 = PROJECT_PATH + "/easyexcel/read/simpleRead.xlsx";
        log.info("读取路径：{}", simpleRead2);

        String fileOutputStreamPath = PROJECT_PATH + "/zip/simpleRead_tow_" + System.currentTimeMillis() + ".zip";

        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(fileOutputStreamPath);
        } catch (FileNotFoundException e) {
            log.error("读取文件失败");
            log.error(e.getMessage(), e);
        }

        assert fileOutputStream != null;

        try (ZipOutputStream zipOutputStream = new ZipOutputStream(fileOutputStream)) {

            ZipEntry zipEntry = new ZipEntry("simpleRead.csv");
            zipOutputStream.putNextEntry(zipEntry);
            byte[] read = FileUtils.read(simpleRead);
            zipOutputStream.write(read);

            ZipEntry zipEntry2 = new ZipEntry("simpleRead.xlsx");
            zipOutputStream.putNextEntry(zipEntry2);
            byte[] read2 = FileUtils.read(simpleRead2);
            zipOutputStream.write(read2);

            zipOutputStream.finish();

            log.info("已经完成压缩");

        } catch (IOException e) {
            log.error("压缩文件异常");
            log.error(e.getMessage(), e);
        }

    }

    /**
     * 多文件多文件夹压缩
     */
    @Test
    void twoFileFolder() {

        String simpleRead = PROJECT_PATH + "/easyexcel/read/simpleRead.csv";
        log.info("读取路径：{}", simpleRead);
        String simpleRead2 = PROJECT_PATH + "/easyexcel/read/simpleRead.xlsx";
        log.info("读取路径：{}", simpleRead2);

        String fileOutputStreamPath = PROJECT_PATH + "/zip/simpleRead_tow_folder_" + System.currentTimeMillis() + ".zip";

        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(fileOutputStreamPath);
        } catch (FileNotFoundException e) {
            log.error("读取文件失败");
            log.error(e.getMessage(), e);
        }

        assert fileOutputStream != null;

        try (ZipOutputStream zipOutputStream = new ZipOutputStream(fileOutputStream)) {

            ZipEntry zipEntry = new ZipEntry("csv/simpleRead.csv");
            zipOutputStream.putNextEntry(zipEntry);
            byte[] read = FileUtils.read(simpleRead);
            zipOutputStream.write(read);

            ZipEntry zipEntry2 = new ZipEntry("xlsx/simpleRead.xlsx");
            zipOutputStream.putNextEntry(zipEntry2);
            byte[] read2 = FileUtils.read(simpleRead2);
            zipOutputStream.write(read2);

            zipOutputStream.finish();

            log.info("已经完成压缩");

        } catch (IOException e) {
            log.error("压缩文件异常");
            log.error(e.getMessage(), e);
        }

    }

}
