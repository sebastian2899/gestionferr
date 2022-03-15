package com.gestionferr.app.service.mapper;

import com.gestionferr.app.domain.ItemFacturaVenta;
import com.gestionferr.app.service.dto.ItemFacturaVentaDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ItemFacturaVenta} and its DTO {@link ItemFacturaVentaDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface ItemFacturaVentaMapper extends EntityMapper<ItemFacturaVentaDTO, ItemFacturaVenta> {}
