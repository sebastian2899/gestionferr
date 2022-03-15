package com.gestionferr.app.service.mapper;

import com.gestionferr.app.domain.Abono;
import com.gestionferr.app.service.dto.AbonoDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Abono} and its DTO {@link AbonoDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface AbonoMapper extends EntityMapper<AbonoDTO, Abono> {}
