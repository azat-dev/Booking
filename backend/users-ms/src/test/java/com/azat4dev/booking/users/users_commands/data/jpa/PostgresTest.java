package com.azat4dev.booking.users.users_commands.data.jpa;

import io.zonky.test.db.AutoConfigureEmbeddedDatabase;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.annotation.AliasFor;
import org.springframework.core.env.Environment;

import java.lang.annotation.*;

@Documented
@Inherited
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@JdbcTest
@AutoConfigureEmbeddedDatabase(
    beanName = "dataSource",
    type = AutoConfigureEmbeddedDatabase.DatabaseType.POSTGRES,
    refresh = AutoConfigureEmbeddedDatabase.RefreshMode.BEFORE_EACH_TEST_METHOD
)
public @interface PostgresTest {
    /**
     * Properties in form {@literal key=value} that should be added to the Spring
     * {@link Environment} before the test runs.
     * @return the properties to add
     * @since 2.1.0
     */
    String[] properties() default {};

    /**
     * Determines if default filtering should be used with
     * {@link SpringBootApplication @SpringBootApplication}. By default no beans are
     * included.
     * @see #includeFilters()
     * @see #excludeFilters()
     * @return if default filters should be used
     */
    boolean useDefaultFilters() default true;

    /**
     * A set of include filters which can be used to add otherwise filtered beans to the
     * application context.
     * @return include filters to apply
     */
    ComponentScan.Filter[] includeFilters() default {};

    /**
     * A set of exclude filters which can be used to filter beans that would otherwise be
     * added to the application context.
     * @return exclude filters to apply
     */
    ComponentScan.Filter[] excludeFilters() default {};

    /**
     * Auto-configuration exclusions that should be applied for this test.
     * @return auto-configuration exclusions to apply
     */
    @AliasFor(annotation = ImportAutoConfiguration.class, attribute = "exclude")
    Class<?>[] excludeAutoConfiguration() default {};
}
