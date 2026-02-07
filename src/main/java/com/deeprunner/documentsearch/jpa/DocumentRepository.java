package com.deeprunner.documentsearch.jpa;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.deeprunner.documentsearch.entity.Document;

public interface DocumentRepository extends JpaRepository<Document, UUID> {

	Optional<Document> findByIdAndTenantId(UUID id, String tenantId);
}
