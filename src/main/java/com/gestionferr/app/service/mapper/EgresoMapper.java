package com.gestionferr.app.service.mapper;

import com.gestionferr.app.domain.Egreso;
import com.gestionferr.app.service.dto.EgresoDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Egreso} and its DTO {@link EgresoDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface EgresoMapper extends EntityMapper<EgresoDTO, Egreso> {}
