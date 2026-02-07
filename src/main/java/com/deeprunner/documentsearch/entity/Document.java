package com.deeprunner.documentsearch.entity;

import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Data
@Table(name = "documents")
@Entity
public class Document {

  @Id
  @GeneratedValue
  private UUID id;

  private String tenantId;
  private String title;

  @Column(columnDefinition="TEXT")
  private String content;
}

