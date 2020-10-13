package cn.com.xuxiaowei.util;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * 文件工具类
 *
 * @author xuxiaowei
 */
public class FileUtils {

    public static final int BYTE_SIZE = 1024;

    /**
     * 读取文件
     *
     * @param path     文件路径
     * @param byteSize 字节大小
     * @return 返回 文件字节
     * @throws IOException 读取文件异常
     */
    public static byte[] read(String path, int byteSize) throws IOException {

        FileInputStream fileInputStream = new FileInputStream(path);

        BufferedInputStream bufferedInputStream = new BufferedInputStream(fileInputStream);

        byteSize = byteSize <= 0 ? BYTE_SIZE : byteSize;

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(byteSize);

        byte[] temp = new byte[byteSize];

        int size;

        while ((size = bufferedInputStream.read(temp)) != -1) {
            byteArrayOutputStream.write(temp, 0, size);
        }

        bufferedInputStream.close();

        return byteArrayOutputStream.toByteArray();
    }

    /**
     * 读取文件
     *
     * @param path 文件路径
     * @return 返回 文件字节
     * @throws IOException 读取文件异常
     */
    public static byte[] read(String path) throws IOException {

        FileInputStream fileInputStream = new FileInputStream(path);

        BufferedInputStream bufferedInputStream = new BufferedInputStream(fileInputStream);

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(BYTE_SIZE);

        byte[] temp = new byte[BYTE_SIZE];

        int size;

        while ((size = bufferedInputStream.read(temp)) != -1) {
            byteArrayOutputStream.write(temp, 0, size);
        }

        bufferedInputStream.close();

        return byteArrayOutputStream.toByteArray();
    }

}
