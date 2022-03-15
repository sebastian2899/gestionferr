package com.gestionferr.app.service.mapper;

import com.gestionferr.app.domain.FacturaVenta;
import com.gestionferr.app.service.dto.FacturaVentaDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link FacturaVenta} and its DTO {@link FacturaVentaDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface FacturaVentaMapper extends EntityMapper<FacturaVentaDTO, FacturaVenta> {}
