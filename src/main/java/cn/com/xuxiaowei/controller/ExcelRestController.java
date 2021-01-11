/*
 * Copyright 2021 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package cn.com.xuxiaowei.controller;

import cn.com.xuxiaowei.properties.MvcProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static cn.com.xuxiaowei.util.Constants.*;
import static cn.com.xuxiaowei.util.enums.CodeList.OK;

/**
 * Excel 操作
 *
 * @author xuxiaowei
 * @since 0.0.1
 */
@Slf4j
@RestController
@RequestMapping("/excel")
public class ExcelRestController {

    private MvcProperties mvcProperties;

    @Autowired
    public void setMvcProperties(MvcProperties mvcProperties) {
        this.mvcProperties = mvcProperties;
    }

    /**
     * 上传 Excel 文件
     *
     * @param request  请求
     * @param response 响应
     * @param excel    Excel 文件
     * @return 返回 上传 Excel 文件 结构
     */
    @RequestMapping("/upload/file")
    public Map<String, Object> upload(HttpServletRequest request, HttpServletResponse response,
                                      @RequestParam(value = "file") MultipartFile excel) {

        Map<String, Object> map = new HashMap<>(4);
        Map<String, Object> data = new HashMap<>(4);
        map.put(DATA, data);
        map.put(CODE, OK.code);

        String originalFilename = excel.getOriginalFilename();

        if (originalFilename == null || "".equals(originalFilename)) {
            map.put(MSG, "Excel上传失败，Excel名不可为空");
        } else {
            String suffixName = originalFilename.substring(originalFilename.lastIndexOf("."));
            List<String> typeList = Arrays.asList(".xlsx", ".xls");

            if (typeList.contains(suffixName)) {

                long currentTimeMillis = System.currentTimeMillis();

                String pathname;
                String path = currentTimeMillis + suffixName;

                String osName = System.getProperty("os.name");

                if (osName.toLowerCase().startsWith("win")) {
                    pathname = mvcProperties.getFileUploadWindows() + path;
                } else {
                    pathname = mvcProperties.getFileUploadLinux() + path;
                }

                File imagePath = new File(pathname);
                if (imagePath.exists()) {
                    log.info("图片路径已存在：{}", pathname);
                } else {
                    log.info("图片路径（{}）未存在，正在创建图片路径...", pathname);
                    boolean mkdirs = imagePath.mkdirs();
                    if (mkdirs) {
                        log.info("创建图片路径成功：{}", pathname);
                    } else {
                        log.error("创建图片路径失败：{}", pathname);
                    }
                }

                try {
                    excel.transferTo(imagePath);
                    map.put(CODE, OK.code);
                    map.put(MSG, "图片上传成功");

                    String pathUrl = mvcProperties.getFileUploadReturnUrl() + path;
                    log.info("文件上传路径：{}", pathUrl);

                    data.put("path", pathUrl);
                } catch (IOException e) {
                    String msg = "图片上传失败";
                    map.put(MSG, msg);
                    log.error(msg, e);
                }

            } else {
                map.put(MSG, "Excel文件类型不正确");
            }
        }

        return map;
    }

}
