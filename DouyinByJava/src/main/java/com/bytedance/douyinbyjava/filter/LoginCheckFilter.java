package com.bytedance.douyinbyjava.filter;


import com.alibaba.fastjson2.JSON;
import com.bytedance.douyinbyjava.response.ResponseDto;
import com.bytedance.douyinbyjava.utils.TokenUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

//@WebFilter(filterName = "loginCheckFilter", urlPatterns = "/*")
@Slf4j
//@Component
public class LoginCheckFilter implements Filter {
    public static final AntPathMatcher PATH_MATCHER = new AntPathMatcher();

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        String requestURI = request.getRequestURI();
        //log.info("拦截到请求：{}",requestURI);

        String[] urls = new String[]{
                "/backend/**",
                "/front/**",
                "/douyin/feed",
                "/douyin/user/register",
                "/douyin/user/login"
        };

        if (check(urls, requestURI)) {
            log.info("本次请求{}不需要处理", requestURI);
            filterChain.doFilter(request, response);
            return;
        }

        if (request.getSession().getAttribute("token") != null) {
            int id = Integer.parseInt(TokenUtils.parseId((String) request.getSession().getAttribute("user")));
            log.info("用户{}已登录", id);
            filterChain.doFilter(request, response);
            return;
        }

        response.getWriter().write(JSON.toJSONString(ResponseDto.failure("NOTLOGIN")));
        log.info("用户未登录，拦截到请求:{}", request.getRequestURI());
    }

    public boolean check (String[] urls, String requestURI) {
        for (String url : urls) {
            if (PATH_MATCHER.match(url, requestURI)) return true;
        }
        return false;
    }
}
