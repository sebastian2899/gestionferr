package com.gestionferr.app.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AbonoMapperTest {

    private AbonoMapper abonoMapper;

    @BeforeEach
    public void setUp() {
        abonoMapper = new AbonoMapperImpl();
    }
}
