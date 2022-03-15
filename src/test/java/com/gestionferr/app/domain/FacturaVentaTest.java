package com.gestionferr.app.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.gestionferr.app.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class FacturaVentaTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(FacturaVenta.class);
        FacturaVenta facturaVenta1 = new FacturaVenta();
        facturaVenta1.setId(1L);
        FacturaVenta facturaVenta2 = new FacturaVenta();
        facturaVenta2.setId(facturaVenta1.getId());
        assertThat(facturaVenta1).isEqualTo(facturaVenta2);
        facturaVenta2.setId(2L);
        assertThat(facturaVenta1).isNotEqualTo(facturaVenta2);
        facturaVenta1.setId(null);
        assertThat(facturaVenta1).isNotEqualTo(facturaVenta2);
    }
}
