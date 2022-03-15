package com.gestionferr.app.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.gestionferr.app.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class FacturaCompraDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(FacturaCompraDTO.class);
        FacturaCompraDTO facturaCompraDTO1 = new FacturaCompraDTO();
        facturaCompraDTO1.setId(1L);
        FacturaCompraDTO facturaCompraDTO2 = new FacturaCompraDTO();
        assertThat(facturaCompraDTO1).isNotEqualTo(facturaCompraDTO2);
        facturaCompraDTO2.setId(facturaCompraDTO1.getId());
        assertThat(facturaCompraDTO1).isEqualTo(facturaCompraDTO2);
        facturaCompraDTO2.setId(2L);
        assertThat(facturaCompraDTO1).isNotEqualTo(facturaCompraDTO2);
        facturaCompraDTO1.setId(null);
        assertThat(facturaCompraDTO1).isNotEqualTo(facturaCompraDTO2);
    }
}
