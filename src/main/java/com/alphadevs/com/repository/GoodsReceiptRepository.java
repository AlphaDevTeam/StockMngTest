package com.alphadevs.com.repository;

import com.alphadevs.com.domain.GoodsReceipt;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the GoodsReceipt entity.
 */
@SuppressWarnings("unused")
@Repository
public interface GoodsReceiptRepository extends JpaRepository<GoodsReceipt, Long>, JpaSpecificationExecutor<GoodsReceipt> {

}
