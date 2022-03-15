package com.gestionferr.app.service.mapper;

import com.gestionferr.app.domain.FacturaCompra;
import com.gestionferr.app.service.dto.FacturaCompraDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link FacturaCompra} and its DTO {@link FacturaCompraDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface FacturaCompraMapper extends EntityMapper<FacturaCompraDTO, FacturaCompra> {}
