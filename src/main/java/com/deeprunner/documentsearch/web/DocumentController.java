package com.deeprunner.documentsearch.web;

import java.util.List;
import java.util.UUID;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.deeprunner.documentsearch.entity.Document;
import com.deeprunner.documentsearch.entity.SearchDocument;
import com.deeprunner.documentsearch.jpa.DocumentRepository;
import com.deeprunner.documentsearch.service.DocumentService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class DocumentController {

  private final DocumentService service;
  private final DocumentRepository repo;

  @PostMapping("/documents")
  public Document create(@RequestHeader("X-Tenant-Id") String tenant,
                         @RequestBody Document d) {
    return service.create(tenant, d);
  }

  @GetMapping("/search")
  public List<SearchDocument> search(@RequestParam String q,
		  @RequestHeader("X-Tenant-Id") String tenant) {
    return service.search(tenant, q);
  }

  @GetMapping("/documents/{id}")
  public Document get(@PathVariable UUID id,
                      @RequestHeader("X-Tenant-Id") String tenant) {
    return repo.findByIdAndTenantId(id, tenant).orElseThrow();
  }

  @DeleteMapping("/documents/{id}")
  public void delete(@PathVariable UUID id) {
    repo.deleteById(id);
  }
}
