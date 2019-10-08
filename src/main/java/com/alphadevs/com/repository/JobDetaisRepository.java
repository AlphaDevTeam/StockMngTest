package com.alphadevs.com.repository;

import com.alphadevs.com.domain.JobDetais;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the JobDetais entity.
 */
@SuppressWarnings("unused")
@Repository
public interface JobDetaisRepository extends JpaRepository<JobDetais, Long>, JpaSpecificationExecutor<JobDetais> {

}
