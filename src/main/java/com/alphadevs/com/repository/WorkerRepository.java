package com.alphadevs.com.repository;

import com.alphadevs.com.domain.Worker;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Worker entity.
 */
@SuppressWarnings("unused")
@Repository
public interface WorkerRepository extends JpaRepository<Worker, Long>, JpaSpecificationExecutor<Worker> {

}
