package com.example.demo.service;


import org.springframework.web.multipart.MultipartFile;

public interface UserService {
    //	增加用户
    public int addUser(MultipartFile file) throws Exception;

}