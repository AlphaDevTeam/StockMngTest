package com.alphadevs.com.repository;

import com.alphadevs.com.domain.ExUser;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the ExUser entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ExUserRepository extends JpaRepository<ExUser, Long>, JpaSpecificationExecutor<ExUser> {

}
