package com.webserver.controller;

import com.webserver.annotations.Controller;
import com.webserver.annotations.RequestMapping;
import com.webserver.http.HttpServletRequest;
import com.webserver.http.HttpServletResponse;

@Controller
public class ArticleController {

    @RequestMapping("/writeArticle")
    public void writeArticle(HttpServletRequest request, HttpServletResponse response){
        System.out.println("开始处理发表文章！！！！");
    }
}
