package com.gestionferr.app.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.gestionferr.app.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ItemFacturaCompraTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ItemFacturaCompra.class);
        ItemFacturaCompra itemFacturaCompra1 = new ItemFacturaCompra();
        itemFacturaCompra1.setId(1L);
        ItemFacturaCompra itemFacturaCompra2 = new ItemFacturaCompra();
        itemFacturaCompra2.setId(itemFacturaCompra1.getId());
        assertThat(itemFacturaCompra1).isEqualTo(itemFacturaCompra2);
        itemFacturaCompra2.setId(2L);
        assertThat(itemFacturaCompra1).isNotEqualTo(itemFacturaCompra2);
        itemFacturaCompra1.setId(null);
        assertThat(itemFacturaCompra1).isNotEqualTo(itemFacturaCompra2);
    }
}
