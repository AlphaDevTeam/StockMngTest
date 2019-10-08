package com.alphadevs.com.repository;

import com.alphadevs.com.domain.Location;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data  repository for the Location entity.
 */
@SuppressWarnings("unused")
@Repository
public interface LocationRepository extends JpaRepository<Location, Long>, JpaSpecificationExecutor<Location> {

    @Query("select location from Location location where location.user.login = ?#{principal.username}")
    List<Location> findByUserIsCurrentUser();

}
