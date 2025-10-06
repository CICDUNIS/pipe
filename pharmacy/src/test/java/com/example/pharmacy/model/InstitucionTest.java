package com.example.pharmacy.model;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class InstitucionTest {

    @Test
    public void testGettersAndSetters() {
        Institucion institucion = new Institucion();

        Long idInstitucion = 10L;
        String codigoInstitucion = "INS-001";
        String nombreInstitucion = "Hospital Nacional Central";
        String tipoInstitucion = "Pública";

        institucion.setIdInstitucion(idInstitucion);
        institucion.setCodigoInstitucion(codigoInstitucion);
        institucion.setNombreInstitucion(nombreInstitucion);
        institucion.setTipoInstitucion(tipoInstitucion);

        assertThat(institucion.getIdInstitucion()).isEqualTo(idInstitucion);
        assertThat(institucion.getCodigoInstitucion()).isEqualTo(codigoInstitucion);
        assertThat(institucion.getNombreInstitucion()).isEqualTo(nombreInstitucion);
        assertThat(institucion.getTipoInstitucion()).isEqualTo(tipoInstitucion);
    }
}
