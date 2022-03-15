package com.gestionferr.app.repository;

import com.gestionferr.app.domain.FacturaCompra;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the FacturaCompra entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FacturaCompraRepository extends JpaRepository<FacturaCompra, Long> {}
