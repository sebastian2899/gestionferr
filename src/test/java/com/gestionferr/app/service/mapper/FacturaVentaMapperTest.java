package com.gestionferr.app.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class FacturaVentaMapperTest {

    private FacturaVentaMapper facturaVentaMapper;

    @BeforeEach
    public void setUp() {
        facturaVentaMapper = new FacturaVentaMapperImpl();
    }
}
