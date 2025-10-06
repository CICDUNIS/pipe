package com.example.pharmacy.dto;

import com.example.pharmacy.model.Medicamento;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import java.util.List;

@JacksonXmlRootElement(localName = "Medicamentos")
public class MedicamentoListWrapper {

    @JacksonXmlElementWrapper(useWrapping = false)
    private List<Medicamento> medicamentos;

    public MedicamentoListWrapper() { }

    public MedicamentoListWrapper(List<Medicamento> medicamentos) {
        this.medicamentos = medicamentos;
    }

    public List<Medicamento> getMedicamentos() {
        return medicamentos;
    }

    public void setMedicamentos(List<Medicamento> medicamentos) {
        this.medicamentos = medicamentos;
    }
}


