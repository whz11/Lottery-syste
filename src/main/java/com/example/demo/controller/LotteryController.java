package com.example.demo.controller;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;

@Controller
public class LotteryController {

    /**
   获奖人
     */
    @RequestMapping("/lottery")
    @ResponseBody
    public String lotteryShow(){

        String result =null;
        String sql = "SELECT * FROM user ORDER BY RAND() LIMIT 1";//SQL语句    输入表名information_test
        ResultSet rs = null;
        final String url = "jdbc:mysql://localhost:3306/springimport_excel?useSSL=false&useUnicode=true&characterEncoding=UTF-8";//输入数据库名test
        final String user = "root";//用户名
        final String password = "123456";//密码
        Connection conn = null;
        PreparedStatement ps = null;
            try {
                Class.forName("com.mysql.jdbc.Driver");//指定连接类型
                conn = DriverManager.getConnection(url, user, password);//获取连接
                ps = conn.prepareStatement(sql);//准备执行语句
            }
            catch (Exception e) {
                e.printStackTrace();
            }

            //显示数据
            try {
                rs = ps.executeQuery();//执行语句
                rs.next();

                 result=rs.getString(2);//根据名字在第几列进行调整
                //关闭连接
                rs.close();
                conn.close();
                ps.close();
            }
            catch (SQLException e) {
                e.printStackTrace();
            }

         return result;

    }

}
