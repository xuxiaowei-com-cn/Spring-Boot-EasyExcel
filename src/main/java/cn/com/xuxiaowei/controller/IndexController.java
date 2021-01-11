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

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 主页
 *
 * @author xuxiaowei
 * @since 0.0.1
 */
@Slf4j
@Controller
public class IndexController {

    /**
     * 主页 页面
     *
     * @param request  请求
     * @param response 响应
     * @param model    页面中的值
     * @return 返回 主页 页面位置
     */
    @RequestMapping("")
    public String upload(HttpServletRequest request, HttpServletResponse response, Model model) {

        return "index";
    }

}
