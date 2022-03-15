package com.gestionferr.app.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.gestionferr.app.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class FacturaCompraTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(FacturaCompra.class);
        FacturaCompra facturaCompra1 = new FacturaCompra();
        facturaCompra1.setId(1L);
        FacturaCompra facturaCompra2 = new FacturaCompra();
        facturaCompra2.setId(facturaCompra1.getId());
        assertThat(facturaCompra1).isEqualTo(facturaCompra2);
        facturaCompra2.setId(2L);
        assertThat(facturaCompra1).isNotEqualTo(facturaCompra2);
        facturaCompra1.setId(null);
        assertThat(facturaCompra1).isNotEqualTo(facturaCompra2);
    }
}
