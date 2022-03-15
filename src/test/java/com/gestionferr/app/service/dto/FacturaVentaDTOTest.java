package com.gestionferr.app.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.gestionferr.app.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class FacturaVentaDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(FacturaVentaDTO.class);
        FacturaVentaDTO facturaVentaDTO1 = new FacturaVentaDTO();
        facturaVentaDTO1.setId(1L);
        FacturaVentaDTO facturaVentaDTO2 = new FacturaVentaDTO();
        assertThat(facturaVentaDTO1).isNotEqualTo(facturaVentaDTO2);
        facturaVentaDTO2.setId(facturaVentaDTO1.getId());
        assertThat(facturaVentaDTO1).isEqualTo(facturaVentaDTO2);
        facturaVentaDTO2.setId(2L);
        assertThat(facturaVentaDTO1).isNotEqualTo(facturaVentaDTO2);
        facturaVentaDTO1.setId(null);
        assertThat(facturaVentaDTO1).isNotEqualTo(facturaVentaDTO2);
    }
}
