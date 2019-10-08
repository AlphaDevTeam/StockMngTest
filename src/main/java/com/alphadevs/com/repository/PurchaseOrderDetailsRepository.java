package com.alphadevs.com.repository;

import com.alphadevs.com.domain.PurchaseOrderDetails;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the PurchaseOrderDetails entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PurchaseOrderDetailsRepository extends JpaRepository<PurchaseOrderDetails, Long>, JpaSpecificationExecutor<PurchaseOrderDetails> {

}
