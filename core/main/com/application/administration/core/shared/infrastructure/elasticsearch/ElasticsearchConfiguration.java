package com.application.administration.core.shared.infrastructure.elasticsearch;

import com.application.administration.core.shared.infrastructure.config.Parameter;
import com.application.administration.core.shared.infrastructure.config.ParameterNotExist;
import org.apache.http.HttpHost;
import org.elasticsearch.client.Request;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.ResourcePatternResolver;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.Scanner;

@Configuration
public class ElasticsearchConfiguration {
    private final Parameter               config;
    private final ResourcePatternResolver resourceResolver;

    public ElasticsearchConfiguration(Parameter config, ResourcePatternResolver resourceResolver) {
        this.config           = config;
        this.resourceResolver = resourceResolver;
    }

    @Bean
    public ElasticsearchClient elasticsearchClient() throws ParameterNotExist, IOException {
        ElasticsearchClient client = new ElasticsearchClient(
            new RestHighLevelClient(
                RestClient.builder(
                    new HttpHost(
                        config.get("ELASTICSEARCH_HOST"),
                        config.getInt("ELASTICSEARCH_PORT"),
                        "http"
                    )
                )
            ),
            RestClient.builder(
                new HttpHost(
                    config.get("ELASTICSEARCH_HOST"),
                    config.getInt("ELASTICSEARCH_PORT"),
                    "http"
                )).build(),
            config.get("ELASTICSEARCH_INDEX_PREFIX")
        );

        generateIndexIfNotExists(client, "clinics");

        return client;
    }

    private void generateIndexIfNotExists(ElasticsearchClient client, String contextName) throws IOException {
        Resource[] jsonsIndexes = resourceResolver.getResources(
            String.format("classpath:elasticsearch/%s/*.json", contextName)
        );

        for (Resource jsonIndex : jsonsIndexes) {
            String indexName = Objects.requireNonNull(jsonIndex.getFilename()).replace(".json", "");

            if (!indexExists(indexName, client)) {
                String indexBody = new Scanner(
                    jsonIndex.getInputStream(),
                        StandardCharsets.UTF_8
                ).useDelimiter("\\A").next();

                Request request = new Request("PUT", indexName);
                request.setJsonEntity(indexBody);

                client.lowLevelClient().performRequest(request);
            }
        }
    }

    private boolean indexExists(String indexName, ElasticsearchClient client) throws IOException {
        return client.highLevelClient().indices().exists(new GetIndexRequest(indexName), RequestOptions.DEFAULT);
    }
}
