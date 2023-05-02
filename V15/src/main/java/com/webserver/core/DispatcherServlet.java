package com.webserver.core;

import com.webserver.controller.UserController;
import com.webserver.http.HttpServletRequest;
import com.webserver.http.HttpServletResponse;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;

/**用于完成一个http交互流程中处理请求的缓解工程*/
public class DispatcherServlet {
    private static DispatcherServlet instance=new DispatcherServlet();
    private static File dir;
    private static File staticDir;

    static {
        //定位环境变量ClassPath(类加载路径)中"."的位置
        //在IDEA中执行项目时,类加载路径是从target/classes开始的
        try {
            dir = new File(
                    DispatcherServlet.class.getClassLoader()
                            .getResource(".").toURI()
            );
            //定位target/classes/static目录
            staticDir = new File(dir,"static");
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    private DispatcherServlet(){}

    public static DispatcherServlet getInstance(){
        return  instance;
    }



    public void service(HttpServletRequest request, HttpServletResponse response) {
        String path = request.getRequestURI();
        System.out.println("请求的抽象路径:" + path);

        //首先判断该请求是否为请求一个业务
        if ("/regUser".equals(path)) {
            System.out.println("开始处理注册业务！！！！！！");
            UserController controller=new UserController();
            controller.reg(request,response);

        }else if("loginUser".equals(path)){
            UserController controller=new UserController();
            controller.login(request,response);
        }
            else {
            File file = new File(staticDir, path);
            if (file.isFile()) {//浏览器请求的资源是否存在且是一个文件
                //正确响应其请求的文件
                response.setContentFile(file);
            } else {
                //响应404
                response.setStatusCode(404);
                response.setStatusReason("NOT FOUND");
                file = new File(staticDir, "/root/404.html");
                response.setContentFile(file);
            }

        }
    }

}
