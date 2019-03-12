package com.oldlie.learn.spring.security.dynamicurl.filter;

import com.oldlie.learn.spring.security.dynamicurl.entity.database.User;
import com.oldlie.learn.spring.security.dynamicurl.service.UserDetailsImplService;
import io.jsonwebtoken.Jwts;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

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
 * JWTAuthenticationFilter
 *
 * @author 陈列
 */
@Slf4j
public class JWTAuthenticationFilter extends BasicAuthenticationFilter {

    private UserDetailsImplService userDetailsImplService;

    public JWTAuthenticationFilter(AuthenticationManager authenticationManager,
                                   UserDetailsImplService userDetailsImplService) {
        super(authenticationManager);
        this.userDetailsImplService = userDetailsImplService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain) throws IOException, ServletException {

        HttpSession session = request.getSession();
        Object object =  session.getAttribute("username");
        String username = null;
        if (object != null) {
            username = (String) object;
        } else {
            String token = request.getHeader("Authorization");
            if (token != null) {
                username = Jwts.parser()
                        .setSigningKey("MyJwtSecret")
                        .parseClaimsJws(token.replace("DynamicUrl ", ""))
                        .getBody()
                        .getSubject();
            }
        }

        UsernamePasswordAuthenticationToken authentication = null;
        if (username != null) {
            UserDetails userDetails = this.userDetailsImplService.loadUserByUsername(username);
            authentication = new UsernamePasswordAuthenticationToken(userDetails.getUsername(),
                    null,
                    userDetails.getAuthorities());
        }

        SecurityContextHolder.getContext().setAuthentication(authentication);
        chain.doFilter(request, response);
    }
}
