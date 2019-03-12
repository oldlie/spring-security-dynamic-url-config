package com.oldlie.learn.spring.security.dynamicurl.service;

import com.oldlie.learn.spring.security.dynamicurl.entity.database.Role;
import com.oldlie.learn.spring.security.dynamicurl.entity.database.UrlRoleMapping;
import com.oldlie.learn.spring.security.dynamicurl.entity.database.User;
import com.oldlie.learn.spring.security.dynamicurl.repository.UrlRoleMappingRepository;
import com.oldlie.learn.spring.security.dynamicurl.repository.UserRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
 * UserDetailsImplService
 *
 * @author 陈列
 */
@Service
public class UserDetailsImplService implements UserDetailsService {

    private UrlRoleMappingRepository urlRoleMappingRepository;
    private UserRepository userRepository;

    public UserDetailsImplService(UrlRoleMappingRepository urlRoleMappingRepository,
                                  UserRepository userRepository) {
        this.urlRoleMappingRepository = urlRoleMappingRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = this.userRepository.findFirstByAccount(username);

        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        for (Role role : user.getRoles()) {
            authorities.add(new SimpleGrantedAuthority("ROLE_" + role.getRole()));
        }

        return new org.springframework.security.core.userdetails.User(user.getAccount(), user.getPassword(),
                authorities);
    }

    private Map<String, String> urlRoleMap = null;

    public Map<String, String> loadUrlMap() {
        urlRoleMap = new HashMap<>();
        List<UrlRoleMapping> list = this.urlRoleMappingRepository.findAll();
        list.forEach(x -> urlRoleMap.put(x.getUrl(), "ROLE_" + x.getRole() + ",USER"));
        return urlRoleMap;
    }
}
