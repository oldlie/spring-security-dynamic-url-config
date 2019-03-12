package com.oldlie.learn.spring.security.dynamicurl.service;

import com.oldlie.learn.spring.security.dynamicurl.ConstantString;
import com.oldlie.learn.spring.security.dynamicurl.entity.database.Role;
import com.oldlie.learn.spring.security.dynamicurl.entity.database.UrlRoleMapping;
import com.oldlie.learn.spring.security.dynamicurl.entity.database.User;
import com.oldlie.learn.spring.security.dynamicurl.repository.RoleRepository;
import com.oldlie.learn.spring.security.dynamicurl.repository.UrlRoleMappingRepository;
import com.oldlie.learn.spring.security.dynamicurl.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collections;

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
 * 2019/03/12
 * InitService
 *
 * @author 陈列
 */
@Slf4j
@Service
public class InitService {

    private BCryptPasswordEncoder bCryptPasswordEncoder;
    private RoleRepository roleRepository;
    private UrlRoleMappingRepository urlRoleMappingRepository;
    private UserRepository userRepository;

    public InitService(BCryptPasswordEncoder bCryptPasswordEncoder,
                       RoleRepository roleRepository,
                       UrlRoleMappingRepository urlRoleMappingRepository,
                       UserRepository userRepository) {
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.roleRepository = roleRepository;
        this.urlRoleMappingRepository = urlRoleMappingRepository;
        this.userRepository = userRepository;
    }


    @Value("${env}")
    private String env;

    public void init() {
        if ("DEV".equals(env.toUpperCase())) {
            try {
                initBasicAccountInfo();
                initUrlRoleMapping();
            } catch (Exception e) {
                log.error(e.getLocalizedMessage());
            }
        }
    }

    private void initBasicAccountInfo() {
        Role role = this.roleRepository.findFirstByRole(ConstantString.ADMIN);
        if (role == null) {
            role = this.roleRepository.save(
                    Role.builder()
                            .role(ConstantString.ADMIN)
                            .text("管理员")
                            .build());
        }

        Role userRole = this.roleRepository.findFirstByRole(ConstantString.USER);
        if (userRole == null) {
            userRole = Role.builder().role(ConstantString.USER).text("用户").build();
            this.roleRepository.save(userRole);
        }

        User adminUser = this.userRepository.findFirstByAccount("admin");
        if (adminUser ==  null) {
            adminUser = User.builder()
                    .account("admin")
                    .password(this.bCryptPasswordEncoder.encode("admin"))
                    .roles(Arrays.asList(role, userRole))
                    .build();
            this.userRepository.save(adminUser);
        }


        User user = this.userRepository.findFirstByAccount("user");
        if (user == null) {
            user = User.builder()
                    .account("user")
                    .password(this.bCryptPasswordEncoder.encode("user"))
                    .roles(Collections.singletonList(role))
                    .build();
            this.userRepository.save(user);
        }
    }

    private void initUrlRoleMapping() {
        if (this.urlRoleMappingRepository.count() > 0){
            return;
        }
        this.urlRoleMappingRepository.saveAll(
                Arrays.asList(
                        UrlRoleMapping.builder().url("/dashboard/**").role("USER").build()
                )
        );
    }
}
