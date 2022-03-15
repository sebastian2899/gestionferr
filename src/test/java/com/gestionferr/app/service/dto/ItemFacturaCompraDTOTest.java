package com.gestionferr.app.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.gestionferr.app.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ItemFacturaCompraDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ItemFacturaCompraDTO.class);
        ItemFacturaCompraDTO itemFacturaCompraDTO1 = new ItemFacturaCompraDTO();
        itemFacturaCompraDTO1.setId(1L);
        ItemFacturaCompraDTO itemFacturaCompraDTO2 = new ItemFacturaCompraDTO();
        assertThat(itemFacturaCompraDTO1).isNotEqualTo(itemFacturaCompraDTO2);
        itemFacturaCompraDTO2.setId(itemFacturaCompraDTO1.getId());
        assertThat(itemFacturaCompraDTO1).isEqualTo(itemFacturaCompraDTO2);
        itemFacturaCompraDTO2.setId(2L);
        assertThat(itemFacturaCompraDTO1).isNotEqualTo(itemFacturaCompraDTO2);
        itemFacturaCompraDTO1.setId(null);
        assertThat(itemFacturaCompraDTO1).isNotEqualTo(itemFacturaCompraDTO2);
    }
}
