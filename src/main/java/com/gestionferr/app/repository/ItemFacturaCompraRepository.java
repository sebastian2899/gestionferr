package com.gestionferr.app.repository;

import com.gestionferr.app.domain.ItemFacturaCompra;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the ItemFacturaCompra entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ItemFacturaCompraRepository extends JpaRepository<ItemFacturaCompra, Long> {}
