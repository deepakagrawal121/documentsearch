package com.deeprunner.documentsearch.entity;

import javax.persistence.Id;

import org.springframework.data.elasticsearch.annotations.Document;

import lombok.Data;

@Document(indexName = "documents")
@Data
public class SearchDocument {

  @Id
  private String id;

  private String tenantId;
  private String title;
  private String content;
}

