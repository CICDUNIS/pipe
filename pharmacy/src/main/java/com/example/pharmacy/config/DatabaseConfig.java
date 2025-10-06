package com.example.pharmacy.config;

import com.example.pharmacy.util.BooleanConverter;
import com.example.pharmacy.util.BooleanToStringConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jdbc.core.convert.JdbcCustomConversions;
import org.springframework.data.jdbc.core.convert.DefaultJdbcTypeFactory;
import org.springframework.data.jdbc.core.convert.JdbcConverter;
import org.springframework.data.jdbc.core.convert.RelationResolver;
import org.springframework.data.jdbc.core.mapping.JdbcMappingContext;
import org.springframework.data.jdbc.repository.config.AbstractJdbcConfiguration;
import org.springframework.data.relational.core.dialect.Dialect;
import org.springframework.data.relational.core.dialect.OracleDialect;
import org.springframework.data.relational.core.mapping.NamingStrategy;
import org.springframework.data.relational.core.mapping.RelationalMappingContext;
import org.springframework.data.relational.core.mapping.RelationalPersistentProperty;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;

import javax.sql.DataSource;
import java.util.Arrays;

@Configuration
public class DatabaseConfig extends AbstractJdbcConfiguration {

    @Bean
    public JdbcCustomConversions jdbcCustomConversions() {
        return new JdbcCustomConversions(Arrays.asList(
            new BooleanConverter(),
            new BooleanToStringConverter()
        ));
    }

    @Bean
    public Dialect jdbcDialect(NamedParameterJdbcOperations operations) {
        return OracleDialect.INSTANCE;
    }

    @Bean
    public NamingStrategy namingStrategy() {
        return new NamingStrategy() {
            @Override
            public String getTableName(Class<?> type) {
                return NamingStrategy.super.getTableName(type);
            }

            @Override
            public String getColumnName(RelationalPersistentProperty property) {
                return property.getName().toUpperCase();
            }
        };
    }
}


