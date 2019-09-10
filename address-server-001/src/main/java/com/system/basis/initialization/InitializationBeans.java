package com.system.basis.initialization;

import com.alibaba.druid.pool.DruidDataSource;
import org.apache.ibatis.session.AutoMappingBehavior;
import org.apache.ibatis.session.ExecutorType;
import org.mybatis.spring.annotation.MapperScan;
import org.mybatis.spring.boot.autoconfigure.ConfigurationCustomizer;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

/**
 * @CreateTime: 2019-09-10 22:06
 * @Description: 配置类
 * @Author: WH
 */
@Configuration
@MapperScan(value = "com.**.dao")
public class InitializationBeans {

    /**
     * mybatis 配置
     */
    @Bean
    public ConfigurationCustomizer configurationCustomizer() {
        return new ConfigurationCustomizer() {
            @Override
            public void customize(org.apache.ibatis.session.Configuration configuration) {
                configuration.setMapUnderscoreToCamelCase(true);
                configuration.setCacheEnabled(true);
                configuration.setLazyLoadingEnabled(true);
                configuration.setAggressiveLazyLoading(true);
                configuration.setMultipleResultSetsEnabled(true);
                configuration.setUseColumnLabel(true);
                configuration.setAutoMappingBehavior(AutoMappingBehavior.FULL);
                configuration.setDefaultExecutorType(ExecutorType.BATCH);
                configuration.setDefaultStatementTimeout(3600);
            }
        };
    }

    /**
     * druid
     */
    @Bean
    @ConfigurationProperties(prefix = "spring.datasource")
    public DataSource druid(){
        return  new DruidDataSource();
    }

}
