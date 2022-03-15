package com.gestionferr.app.service.mapper;

import com.gestionferr.app.domain.ItemFacturaCompra;
import com.gestionferr.app.service.dto.ItemFacturaCompraDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ItemFacturaCompra} and its DTO {@link ItemFacturaCompraDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface ItemFacturaCompraMapper extends EntityMapper<ItemFacturaCompraDTO, ItemFacturaCompra> {}
