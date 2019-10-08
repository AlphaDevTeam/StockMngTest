package com.alphadevs.com.repository;

import com.alphadevs.com.domain.CashBook;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the CashBook entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CashBookRepository extends JpaRepository<CashBook, Long>, JpaSpecificationExecutor<CashBook> {

}
