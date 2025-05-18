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

    /**
     * Configures and returns a Neo4jConversions bean to handle the mapping of custom DTOs
     * to Neo4j-specific Value objects for data persistence and retrieval.
     * <p>
     * The method registers two custom converters:
     * 1. TaskCreateDTOConverter - Converts TaskCreateDTO objects.
     * 2. TaskLinkDTOConverter - Converts TaskLinkDTO objects.
     *
     * @return a Neo4jConversions instance with the registered custom converters.
     */
    @Bean
    public Neo4jConversions neo4jConversions() {
        return new Neo4jConversions(List.of(new TaskCreateDTOConverter(), new TaskLinkDTOConverter()));
    }
}
