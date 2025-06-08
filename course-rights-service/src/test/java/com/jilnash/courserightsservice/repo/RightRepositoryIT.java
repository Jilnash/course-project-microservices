package com.jilnash.courserightsservice.repo;

import com.jilnash.courserightsservice.model.Right;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;


@DataJpaTest
@Testcontainers
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class RightRepositoryIT {

    @Container
    static MySQLContainer<?> mysqlContainer = new MySQLContainer<>("mysql:8.0")
            .withDatabaseName("course_rights")
            .withUsername("root")
            .withPassword("root");
    @Autowired
    private RightRepository rightRepository;

    @DynamicPropertySource
    static void configureProperties(org.springframework.test.context.DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", mysqlContainer::getJdbcUrl);
        registry.add("spring.datasource.username", mysqlContainer::getUsername);
        registry.add("spring.datasource.password", mysqlContainer::getPassword);
    }

    @BeforeEach
    public void setUp() {
        rightRepository.deleteAll();
        Right right1 = new Right();
        right1.setName("READ");
        Right right2 = new Right();
        right2.setName("WRITE");
        Right right3 = new Right();
        right3.setName("DELETE");
        Right right4 = new Right();
        right4.setName("UPDATE");
        rightRepository.saveAll(List.of(right1, right2, right3, right4));
    }

    @Test
//    @Sql("classpath:sql/init_schema_then_insert_data.sql")
    public void testRightRepository() {

        List<Right> rights = rightRepository.findAll();
        System.out.println("Rights: " + rights);
        assertThat(rights).isNotEmpty();
    }
}
