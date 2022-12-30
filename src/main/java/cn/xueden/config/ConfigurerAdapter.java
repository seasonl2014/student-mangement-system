package cn.xueden.config;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.RegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.io.IOException;
import java.util.Arrays;

/**功能描述：配置跨域
 * @author:梁志杰
 * @date:2022/12/1
 * @description:cn.xueden.config
 * @version:1.0
 */
@Configuration
public class ConfigurerAdapter implements WebMvcConfigurer {
    @Value("${user.icon}")
    private String userIcon;


    /**
     * 跨域配置
     */
    @Override
    @Order(1)
    public void addCorsMappings(CorsRegistry registry) {
        //对那些请求路径进行跨域处理
        registry.addMapping("/**")
                // 允许的请求头，默认允许所有的请求头
                .allowedHeaders("*")
                // 允许的方法，默认允许GET、POST、HEAD
                .allowedMethods("*")
                // 探测请求有效时间，单位秒
                .maxAge(1800)
                // 是否允许证书（cookies）
                .allowCredentials(true)
                // 支持的域
                .allowedOriginPatterns("*");

    }

    /**
     * 静态资源文件路径映射
     * @param registry
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        System.out.println("userIcon:"+userIcon);
        String pathUtl = "file:" + userIcon.replace("\\","/");
        registry.addResourceHandler("/uploadFile/**").addResourceLocations(pathUtl).setCachePeriod(0);
    }


}
