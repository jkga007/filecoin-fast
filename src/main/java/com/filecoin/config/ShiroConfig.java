package com.filecoin.config;

import com.filecoin.modules.sys.oauth2.OAuth2Filter;
import com.filecoin.modules.sys.oauth2.OAuth2Realm;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Filter;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Shiro配置
 *
 * @author r25437,g20416
 * @email support@filecoinon.com
 * @date 2017-04-20 18:33
 */
@Configuration
public class ShiroConfig {

    @Bean("sessionManager")
    public SessionManager sessionManager(){
        DefaultWebSessionManager sessionManager = new DefaultWebSessionManager();
        sessionManager.setSessionValidationSchedulerEnabled(true);
        sessionManager.setSessionIdCookieEnabled(true);
        return sessionManager;
    }

    @Bean("securityManager")
    public SecurityManager securityManager(OAuth2Realm oAuth2Realm, SessionManager sessionManager) {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        securityManager.setRealm(oAuth2Realm);
        securityManager.setSessionManager(sessionManager);

        return securityManager;
    }

    @Bean("shiroFilter")
    public ShiroFilterFactoryBean shirFilter(SecurityManager securityManager) {
        ShiroFilterFactoryBean shiroFilter = new ShiroFilterFactoryBean();
        shiroFilter.setSecurityManager(securityManager);

        //oauth过滤
        Map<String, Filter> filters = new HashMap<>();
        filters.put("oauth2", new OAuth2Filter());
        shiroFilter.setFilters(filters);

        Map<String, String> filterMap = new LinkedHashMap<>();
        filterMap.put("/webjars/**", "anon");
        filterMap.put("/druid/**", "anon");
        filterMap.put("/app/**", "anon");

        //登录
        filterMap.put("/sys/login", "anon");
        filterMap.put("/sys/loginBack", "anon");
        //注册
        filterMap.put("/sys/regist", "anon");
        //跳转登录页
        filterMap.put("/sys/gologin", "anon");
        //注册邮箱激活
        filterMap.put("/sys/activation/**", "anon");
        //跳转首页
        filterMap.put("/sys/goindex", "anon");
        //跳转注册页
        filterMap.put("/sys/goregist", "anon");
        //跳转用户主页
        filterMap.put("/sys/gomasterindex", "anon");
        //跳转矿工管理
        filterMap.put("/sys/gominer", "anon");
        //跳转付款记录
        filterMap.put("/sys/gopay", "anon");
        //跳转付款设置
        filterMap.put("/sys/gomywallet", "anon");
        //跳转安全中心
        filterMap.put("/sys/gosecurity", "anon");
        //跳转个人中心
        filterMap.put("/sys/gosettings", "anon");
        //跳转帮助中心
        filterMap.put("/sys/gofaq", "anon");
        //跳转推广有礼
        filterMap.put("/sys/gohighway", "anon");
        //重新发送邮件
        filterMap.put("/sys/resendMail", "anon");
        //返回修改邮箱用户信息
        filterMap.put("/sys/getEditMailUser", "anon");
        //获取交易所实时价格
        filterMap.put("/sys/getCoinTickers", "anon");

        filterMap.put("/**/*.css", "anon");
        filterMap.put("/**/*.js", "anon");
        filterMap.put("/**/*.html", "anon");
        filterMap.put("/fonts/**", "anon");
        filterMap.put("/filecoin/**", "anon");
        filterMap.put("/plugins/**", "anon");
        filterMap.put("/testsimplemail/**", "anon");
        filterMap.put("/swagger/**", "anon");
        filterMap.put("/favicon.ico", "anon");
        filterMap.put("/captcha.jpg", "anon");
        filterMap.put("/", "anon");
        filterMap.put("/**", "oauth2");
        shiroFilter.setFilterChainDefinitionMap(filterMap);

        return shiroFilter;
    }

    @Bean("lifecycleBeanPostProcessor")
    public LifecycleBeanPostProcessor lifecycleBeanPostProcessor() {
        return new LifecycleBeanPostProcessor();
    }

    @Bean
    public DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator() {
        DefaultAdvisorAutoProxyCreator proxyCreator = new DefaultAdvisorAutoProxyCreator();
        proxyCreator.setProxyTargetClass(true);
        return proxyCreator;
    }

    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(SecurityManager securityManager) {
        AuthorizationAttributeSourceAdvisor advisor = new AuthorizationAttributeSourceAdvisor();
        advisor.setSecurityManager(securityManager);
        return advisor;
    }

}
