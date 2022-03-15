package com.gestionferr.app.service.mapper;

import com.gestionferr.app.domain.Caja;
import com.gestionferr.app.service.dto.CajaDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Caja} and its DTO {@link CajaDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface CajaMapper extends EntityMapper<CajaDTO, Caja> {}
