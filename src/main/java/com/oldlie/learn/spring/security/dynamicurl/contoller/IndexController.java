package com.oldlie.learn.spring.security.dynamicurl.contoller;

import com.oldlie.learn.spring.security.dynamicurl.ConstantString;
import com.oldlie.learn.spring.security.dynamicurl.entity.database.Role;
import com.oldlie.learn.spring.security.dynamicurl.entity.database.User;
import com.oldlie.learn.spring.security.dynamicurl.repository.RoleRepository;
import com.oldlie.learn.spring.security.dynamicurl.repository.UserRepository;
import com.oldlie.learn.spring.security.dynamicurl.service.InitService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * ━━━━━━神兽出没━━━━━━
 * 　　　┏┓　　　┏┓
 * 　　┏┛┻━━━┛┻┓
 * 　　┃　　　　　　　┃
 * 　　┃　　　━　　　┃
 * 　　┃　┳┛　┗┳　┃
 * 　　┃　　　　　　　┃
 * 　　┃　　　┻　　　┃
 * 　　┃　　　　　　　┃
 * 　　┗━┓　　　┏━┛
 * 　　　　┃　　　┃  神兽保佑
 * 　　　　┃　　　┃  代码无bug
 * 　　　　┃　　　┗━━━┓
 * 　　　　┃　　　　　　　┣┓
 * 　　　　┃　　　　　　　┏┛
 * 　　　　┗┓┓┏━┳┓┏┛
 * 　　　　　┃┫┫　┃┫┫
 * 　　　　　┗┻┛　┗┻┛
 * ━━━━━━━━━━━━━━━━━
 * 2019/03/08
 * IndexController
 *
 * @author 陈列
 */
@Controller
@Slf4j
public class IndexController {

    private InitService initService;

    public IndexController(InitService initService) {
        this.initService = initService;
    }

    @GetMapping("/")
    public String index() {
        initService.init();
        return "index";
    }


}
