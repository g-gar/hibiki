package com.ggar.hibiki.features.ingestion.infrastructure.persistence.repository;

import com.ggar.hibiki.features.ingestion.infrastructure.persistence.entity.MediaEntity;
import java.util.UUID;
import org.springframework.data.neo4j.repository.ReactiveNeo4jRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReactiveNeo4jMediaRepository extends ReactiveNeo4jRepository<MediaEntity, UUID> {}
