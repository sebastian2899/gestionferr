package com.gestionferr.app.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ItemFacturaCompraMapperTest {

    private ItemFacturaCompraMapper itemFacturaCompraMapper;

    @BeforeEach
    public void setUp() {
        itemFacturaCompraMapper = new ItemFacturaCompraMapperImpl();
    }
}
