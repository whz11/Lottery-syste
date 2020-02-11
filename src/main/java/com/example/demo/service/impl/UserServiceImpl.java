package com.example.demo.service.impl;


import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.model.User;
import com.example.demo.repository.UserMapper;
import com.example.demo.service.UserService;
import com.example.demo.transcation.MyException;
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public int addUser(MultipartFile file) throws Exception {

        int result = 0;
//		存放excel表中所有user细腻
        List<User> userList = new ArrayList<>();
        /**
         *
         * 判断文件版本
         */
        String fileName = file.getOriginalFilename();
        String suffix = fileName.substring(fileName.lastIndexOf(".") + 1);

        InputStream ins = file.getInputStream();

        Workbook wb = null;

        if (suffix.equals("xlsx")) {

            wb = new XSSFWorkbook(ins);

        } else {
            wb = new HSSFWorkbook(ins);
        }
        /**
         * 获取excel表单
         */
        Sheet sheet = wb.getSheetAt(0);


        /**
         * line = 2 :从表的第三行开始获取记录
         *
         */
        if (null != sheet) {

            for (int line = 2; line <= sheet.getLastRowNum(); line++) {

                User user = new User();

                Row row = sheet.getRow(line);

                if (null == row) {
                    continue;
                }
                /**
                 * 判断单元格类型是否为文本类型
                 */
                if (1 != row.getCell(0).getCellType()) {
                    throw new MyException("单元格类型不是文本类型！");
                }

                /**
                 * 获取第一个单元格的内容
                 */
                if (row.getCell(1) != null) {

                    String username = row.getCell(0).getStringCellValue();

                    row.getCell(1).setCellType(Cell.CELL_TYPE_STRING);

                    /**
                     * 获取第二个单元格的内容
                     */

                    String userid = row.getCell(1).getStringCellValue();

                    user.setUsername(username);
                    user.setUserid(userid);
                    userList.add(user);

                }

                for (User userInfo : userList) {

                    /**
                     * 判断数据库表中是否存在用户记录，若存在，则更新，不存在，则保存记录
                     */
                    String name = userInfo.getUsername();

                    int count = userMapper.selectUser(name);

                    if (0 == count) {
                        result = userMapper.addUser(userInfo);
                    } else {
                        result = userMapper.updateUser(userInfo);
                    }


                }
            }


            return result;
        }

       return 0;
    }
}
