package com.deeprunner.documentsearch.service;

import java.time.Duration;
import java.util.List;

import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.BeanUtils;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.deeprunner.documentsearch.entity.Document;
import com.deeprunner.documentsearch.entity.SearchDocument;
import com.deeprunner.documentsearch.jpa.DocumentRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DocumentService {

  private final DocumentRepository repo;
  private final ElasticsearchOperations es;
  private final RedisTemplate<String,Object> redis;

  public Document create(String tenant, Document d) {

    d.setTenantId(tenant);
    Document saved = repo.save(d);
    SearchDocument sd = new SearchDocument();
    BeanUtils.copyProperties(saved, sd);
    sd.setId(saved.getId().toString());
    es.save(sd);

    return saved;
  }

  public List<SearchDocument> search(String tenant, String q) {

	    String cacheKey = tenant + ":" + q;

	    List<SearchDocument> cached =
	        (List<SearchDocument>) redis.opsForValue().get(cacheKey);

	    if (cached != null) return cached;

	    BoolQueryBuilder boolQuery = QueryBuilders.boolQuery()
	        .must(QueryBuilders.matchQuery("content", q))
	        .filter(QueryBuilders.termQuery("tenantId.keyword", tenant));

	    NativeSearchQuery query = new NativeSearchQueryBuilder()
	        .withQuery(boolQuery)
	        .build();

	    List<SearchDocument> result =
	        es.search(query, SearchDocument.class)
	          .stream()
	          .map(SearchHit::getContent)
	          .toList();

	    redis.opsForValue().set(cacheKey, result, Duration.ofSeconds(60));

	    return result;
	}

}

