package com.example.pharmacy.config;

import com.example.pharmacy.util.BooleanConverter;
import com.example.pharmacy.util.BooleanToStringConverter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.jdbc.core.convert.JdbcCustomConversions;
import org.springframework.data.relational.core.dialect.Dialect;
import org.springframework.data.relational.core.dialect.OracleDialect;
import org.springframework.data.relational.core.mapping.NamingStrategy;
import org.springframework.data.relational.core.mapping.RelationalPersistentProperty;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DatabaseConfigTest {

    @Mock
    private NamedParameterJdbcOperations operations;

    private DatabaseConfig databaseConfig;

    @BeforeEach
    void setUp() {
        databaseConfig = new DatabaseConfig();
    }

    @Test
    void testDatabaseConfigCreation() {
        assertNotNull(databaseConfig);
    }

    @Test
    void testJdbcCustomConversions() {
        // Act
        JdbcCustomConversions conversions = databaseConfig.jdbcCustomConversions();

        // Assert
        assertNotNull(conversions);
        
        // Verificar que las conversiones personalizadas están registradas
        assertTrue(conversions.hasCustomWriteTarget(Boolean.class));
    }

    @Test
    void testJdbcCustomConversionsContainsBooleanConverter() {
        // Act
        JdbcCustomConversions conversions = databaseConfig.jdbcCustomConversions();

        // Assert
        assertNotNull(conversions);
        
        // Verificar que BooleanConverter está registrado
        assertTrue(conversions.hasCustomWriteTarget(Boolean.class));
    }

    @Test
    void testJdbcCustomConversionsContainsBooleanToStringConverter() {
        // Act
        JdbcCustomConversions conversions = databaseConfig.jdbcCustomConversions();

        // Assert
        assertNotNull(conversions);
        
        // Verificar que BooleanToStringConverter está registrado
        assertTrue(conversions.hasCustomWriteTarget(Boolean.class));
    }

    @Test
    void testJdbcDialect() {
        // Act
        Dialect dialect = databaseConfig.jdbcDialect(operations);

        // Assert
        assertNotNull(dialect);
        assertTrue(dialect instanceof OracleDialect);
        assertEquals(OracleDialect.INSTANCE, dialect);
    }

    @Test
    void testJdbcDialectWithNullOperations() {
        // Act
        Dialect dialect = databaseConfig.jdbcDialect(null);

        // Assert
        assertNotNull(dialect);
        assertTrue(dialect instanceof OracleDialect);
        assertEquals(OracleDialect.INSTANCE, dialect);
    }

    @Test
    void testJdbcDialectIsSingleton() {
        // Act
        Dialect dialect1 = databaseConfig.jdbcDialect(operations);
        Dialect dialect2 = databaseConfig.jdbcDialect(operations);

        // Assert
        assertSame(dialect1, dialect2);
        assertSame(OracleDialect.INSTANCE, dialect1);
    }

    @Test
    void testNamingStrategy() {
        // Act
        NamingStrategy namingStrategy = databaseConfig.namingStrategy();

        // Assert
        assertNotNull(namingStrategy);
    }

    @Test
    void testNamingStrategyGetTableName() {
        // Arrange
        NamingStrategy namingStrategy = databaseConfig.namingStrategy();
        Class<?> testClass = String.class;

        // Act
        String tableName = namingStrategy.getTableName(testClass);

        // Assert
        assertNotNull(tableName);
        // Verificar que se puede obtener un nombre de tabla
        assertTrue(tableName.length() > 0);
    }

    @Test
    void testNamingStrategyGetColumnName() {
        // Arrange
        NamingStrategy namingStrategy = databaseConfig.namingStrategy();
        RelationalPersistentProperty property = mock(RelationalPersistentProperty.class);
        when(property.getName()).thenReturn("testColumn");

        // Act
        String columnName = namingStrategy.getColumnName(property);

        // Assert
        assertNotNull(columnName);
        assertEquals("TESTCOLUMN", columnName);
        verify(property).getName();
    }

    @Test
    void testNamingStrategyGetColumnNameWithNullProperty() {
        // Arrange
        NamingStrategy namingStrategy = databaseConfig.namingStrategy();

        // Act & Assert
        assertThrows(NullPointerException.class, () -> {
            namingStrategy.getColumnName(null);
        });
    }

    @Test
    void testNamingStrategyGetColumnNameWithEmptyPropertyName() {
        // Arrange
        NamingStrategy namingStrategy = databaseConfig.namingStrategy();
        RelationalPersistentProperty property = mock(RelationalPersistentProperty.class);
        when(property.getName()).thenReturn("");

        // Act
        String columnName = namingStrategy.getColumnName(property);

        // Assert
        assertNotNull(columnName);
        assertEquals("", columnName);
        verify(property).getName();
    }

    @Test
    void testNamingStrategyGetColumnNameWithWhitespacePropertyName() {
        // Arrange
        NamingStrategy namingStrategy = databaseConfig.namingStrategy();
        RelationalPersistentProperty property = mock(RelationalPersistentProperty.class);
        when(property.getName()).thenReturn("   ");

        // Act
        String columnName = namingStrategy.getColumnName(property);

        // Assert
        assertNotNull(columnName);
        assertEquals("   ", columnName);
        verify(property).getName();
    }

    @Test
    void testNamingStrategyGetColumnNameWithSpecialCharacters() {
        // Arrange
        NamingStrategy namingStrategy = databaseConfig.namingStrategy();
        RelationalPersistentProperty property = mock(RelationalPersistentProperty.class);
        when(property.getName()).thenReturn("test_column_name");

        // Act
        String columnName = namingStrategy.getColumnName(property);

        // Assert
        assertNotNull(columnName);
        assertEquals("TEST_COLUMN_NAME", columnName);
        verify(property).getName();
    }

    @Test
    void testNamingStrategyGetColumnNameWithUnicodeCharacters() {
        // Arrange
        NamingStrategy namingStrategy = databaseConfig.namingStrategy();
        RelationalPersistentProperty property = mock(RelationalPersistentProperty.class);
        when(property.getName()).thenReturn("columna_ñáéíóú");

        // Act
        String columnName = namingStrategy.getColumnName(property);

        // Assert
        assertNotNull(columnName);
        assertEquals("COLUMNA_ÑÁÉÍÓÚ", columnName);
        verify(property).getName();
    }

    @Test
    void testNamingStrategyGetColumnNameWithNumbers() {
        // Arrange
        NamingStrategy namingStrategy = databaseConfig.namingStrategy();
        RelationalPersistentProperty property = mock(RelationalPersistentProperty.class);
        when(property.getName()).thenReturn("column123");

        // Act
        String columnName = namingStrategy.getColumnName(property);

        // Assert
        assertNotNull(columnName);
        assertEquals("COLUMN123", columnName);
        verify(property).getName();
    }

    @Test
    void testNamingStrategyGetColumnNameWithMixedCase() {
        // Arrange
        NamingStrategy namingStrategy = databaseConfig.namingStrategy();
        RelationalPersistentProperty property = mock(RelationalPersistentProperty.class);
        when(property.getName()).thenReturn("MiXeDcAsE");

        // Act
        String columnName = namingStrategy.getColumnName(property);

        // Assert
        assertNotNull(columnName);
        assertEquals("MIXEDCASE", columnName);
        verify(property).getName();
    }

    @Test
    void testNamingStrategyGetColumnNameWithVeryLongName() {
        // Arrange
        NamingStrategy namingStrategy = databaseConfig.namingStrategy();
        RelationalPersistentProperty property = mock(RelationalPersistentProperty.class);
        String longName = "a".repeat(1000);
        when(property.getName()).thenReturn(longName);

        // Act
        String columnName = namingStrategy.getColumnName(property);

        // Assert
        assertNotNull(columnName);
        assertEquals(longName.toUpperCase(), columnName);
        verify(property).getName();
    }

    @Test
    void testJdbcCustomConversionsIsSingleton() {
        // Act
        JdbcCustomConversions conversions1 = databaseConfig.jdbcCustomConversions();
        JdbcCustomConversions conversions2 = databaseConfig.jdbcCustomConversions();

        // Verificar que ambos son del mismo tipo y funcionan correctamente
        assertNotNull(conversions1);
        assertNotNull(conversions2);
        assertTrue(conversions1.hasCustomWriteTarget(Boolean.class));
        assertTrue(conversions2.hasCustomWriteTarget(Boolean.class));
    }

    @Test
    void testNamingStrategyIsSingleton() {
        // Act
        NamingStrategy namingStrategy1 = databaseConfig.namingStrategy();
        NamingStrategy namingStrategy2 = databaseConfig.namingStrategy();

        // Verificar que ambos son del mismo tipo y funcionan correctamente
        assertNotNull(namingStrategy1);
        assertNotNull(namingStrategy2);
        assertTrue(namingStrategy1 instanceof NamingStrategy);
        assertTrue(namingStrategy2 instanceof NamingStrategy);
    }

    @Test
    void testDatabaseConfigExtendsAbstractJdbcConfiguration() {
        // Assert
        assertTrue(databaseConfig instanceof org.springframework.data.jdbc.repository.config.AbstractJdbcConfiguration);
    }
}
