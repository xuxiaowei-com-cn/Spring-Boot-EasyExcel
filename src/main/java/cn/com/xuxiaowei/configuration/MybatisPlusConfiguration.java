package cn.com.xuxiaowei.configuration;

import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * Mybatis Plus 配置
 * <p>
 * 扫描 Mapper 接口
 * <p>
 * MyBatis Plus 性能分析插件
 * 性能分析拦截器，用于输出每条 SQL 语句及其执行时间
 * 参见：p6spy
 * <p>
 * 分页插件
 *
 * @author xuxiaowei
 * @since 0.0.1
 */
@Configuration
@EnableTransactionManagement
@MapperScan("cn.com.xuxiaowei.mapper.**")
public class MybatisPlusConfiguration {

    /**
     * 分页插件
     */
    @Bean
    PaginationInterceptor paginationInterceptor() {
        return new PaginationInterceptor();
    }

}
