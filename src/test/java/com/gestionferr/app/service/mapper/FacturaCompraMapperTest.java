package com.gestionferr.app.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class FacturaCompraMapperTest {

    private FacturaCompraMapper facturaCompraMapper;

    @BeforeEach
    public void setUp() {
        facturaCompraMapper = new FacturaCompraMapperImpl();
    }
}
