/*
 * Copyright 2020 the original author or authors.
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

import cn.com.xuxiaowei.entity.User;
import cn.com.xuxiaowei.service.IUserService;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.support.ExcelTypeEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.List;

/**
 * Excel 操作
 *
 * @author xuxiaowei
 * @since 0.0.1
 */
@Controller
@RequestMapping("/excel")
public class ExcelController {

    private IUserService iUserService;

    @Autowired
    public void setiUserService(IUserService iUserService) {
        this.iUserService = iUserService;
    }

    /**
     * 下载 xlsx
     *
     * @param request  请求
     * @param response 响应（用于响应 Excel）
     */
    @RequestMapping("/download/xlsx")
    public void xlsx(HttpServletRequest request, HttpServletResponse response) throws IOException {

        String fileName = "测试xlsx";
        String sheetName = "测试 sheet";

        List<User> list = iUserService.list();

        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("utf-8");
        // 这里URLEncoder.encode可以防止中文乱码 当然和easyexcel没有关系
        String fileNameUrlEncoder = URLEncoder.encode(fileName, "UTF-8");
        response.setHeader("Content-disposition", "attachment;filename=" + fileNameUrlEncoder + ExcelTypeEnum.XLSX.getValue());
        EasyExcel.write(response.getOutputStream(), User.class).sheet(sheetName).doWrite(list);
    }

}
