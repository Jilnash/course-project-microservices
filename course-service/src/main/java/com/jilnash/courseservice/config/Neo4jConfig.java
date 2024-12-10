package com.jilnash.courseservice.config;

import com.jilnash.courseservice.converter.neo4j.TaskCreateDTOConverter;
import com.jilnash.courseservice.converter.neo4j.TaskLinkDTOConverter;
import org.neo4j.cypherdsl.core.renderer.Dialect;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.neo4j.core.convert.Neo4jConversions;

import java.util.List;

@Configuration
public class Neo4jConfig {

    @Bean
    org.neo4j.cypherdsl.core.renderer.Configuration cypherDslConfiguration() {

        return org.neo4j.cypherdsl.core.renderer.Configuration.newConfig()
                .withDialect(Dialect.NEO4J_4).build();
    }

    @Bean
    public Neo4jConversions neo4jConversions() {
        return new Neo4jConversions(List.of(new TaskCreateDTOConverter(), new TaskLinkDTOConverter()));
    }
}
