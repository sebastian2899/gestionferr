package com.gestionferr.app.repository;

import com.gestionferr.app.domain.ItemFacturaVenta;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the ItemFacturaVenta entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ItemFacturaVentaRepository extends JpaRepository<ItemFacturaVenta, Long> {}
