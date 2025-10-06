package com.example.pharmacy.dto;

import com.example.pharmacy.model.Medicamento;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class MedicamentoListWrapperTest {

    @Test
    void testDefaultConstructor() {
        MedicamentoListWrapper wrapper = new MedicamentoListWrapper();
        
        assertNull(wrapper.getMedicamentos());
    }

    @Test
    void testParameterizedConstructor() {
        List<Medicamento> medicamentos = new ArrayList<>();
        MedicamentoListWrapper wrapper = new MedicamentoListWrapper(medicamentos);
        
        assertEquals(medicamentos, wrapper.getMedicamentos());
    }

    @Test
    void testGetMedicamentos() {
        MedicamentoListWrapper wrapper = new MedicamentoListWrapper();
        assertNull(wrapper.getMedicamentos());
        
        List<Medicamento> medicamentos = new ArrayList<>();
        wrapper.setMedicamentos(medicamentos);
        assertEquals(medicamentos, wrapper.getMedicamentos());
    }

    @Test
    void testSetMedicamentos() {
        MedicamentoListWrapper wrapper = new MedicamentoListWrapper();
        
        List<Medicamento> medicamentos = new ArrayList<>();
        wrapper.setMedicamentos(medicamentos);
        assertEquals(medicamentos, wrapper.getMedicamentos());
        
        wrapper.setMedicamentos(null);
        assertNull(wrapper.getMedicamentos());
    }

    @Test
    void testSetMedicamentos_WithEmptyList() {
        MedicamentoListWrapper wrapper = new MedicamentoListWrapper();
        
        List<Medicamento> emptyList = new ArrayList<>();
        wrapper.setMedicamentos(emptyList);
        assertEquals(emptyList, wrapper.getMedicamentos());
        assertTrue(wrapper.getMedicamentos().isEmpty());
    }

    @Test
    void testSetMedicamentos_WithSingleItem() {
        MedicamentoListWrapper wrapper = new MedicamentoListWrapper();
        
        List<Medicamento> singleItemList = new ArrayList<>();
        singleItemList.add(new Medicamento());
        wrapper.setMedicamentos(singleItemList);
        
        assertEquals(singleItemList, wrapper.getMedicamentos());
        assertEquals(1, wrapper.getMedicamentos().size());
    }

    @Test
    void testSetMedicamentos_WithMultipleItems() {
        MedicamentoListWrapper wrapper = new MedicamentoListWrapper();
        
        List<Medicamento> multipleItemsList = new ArrayList<>();
        multipleItemsList.add(new Medicamento());
        multipleItemsList.add(new Medicamento());
        multipleItemsList.add(new Medicamento());
        wrapper.setMedicamentos(multipleItemsList);
        
        assertEquals(multipleItemsList, wrapper.getMedicamentos());
        assertEquals(3, wrapper.getMedicamentos().size());
    }

    @Test
    void testConstructorWithNullList() {
        MedicamentoListWrapper wrapper = new MedicamentoListWrapper(null);
        
        assertNull(wrapper.getMedicamentos());
    }

    @Test
    void testConstructorWithEmptyList() {
        List<Medicamento> emptyList = new ArrayList<>();
        MedicamentoListWrapper wrapper = new MedicamentoListWrapper(emptyList);
        
        assertEquals(emptyList, wrapper.getMedicamentos());
        assertTrue(wrapper.getMedicamentos().isEmpty());
    }

    @Test
    void testConstructorWithSingleItem() {
        List<Medicamento> singleItemList = new ArrayList<>();
        singleItemList.add(new Medicamento());
        MedicamentoListWrapper wrapper = new MedicamentoListWrapper(singleItemList);
        
        assertEquals(singleItemList, wrapper.getMedicamentos());
        assertEquals(1, wrapper.getMedicamentos().size());
    }

    @Test
    void testConstructorWithMultipleItems() {
        List<Medicamento> multipleItemsList = new ArrayList<>();
        multipleItemsList.add(new Medicamento());
        multipleItemsList.add(new Medicamento());
        MedicamentoListWrapper wrapper = new MedicamentoListWrapper(multipleItemsList);
        
        assertEquals(multipleItemsList, wrapper.getMedicamentos());
        assertEquals(2, wrapper.getMedicamentos().size());
    }

    @Test
    void testMultipleFieldUpdates() {
        MedicamentoListWrapper wrapper = new MedicamentoListWrapper();
        
        List<Medicamento> list1 = new ArrayList<>();
        list1.add(new Medicamento());
        wrapper.setMedicamentos(list1);
        
        assertEquals(list1, wrapper.getMedicamentos());
        assertEquals(1, wrapper.getMedicamentos().size());
        
        // Update with different list
        List<Medicamento> list2 = new ArrayList<>();
        list2.add(new Medicamento());
        list2.add(new Medicamento());
        wrapper.setMedicamentos(list2);
        
        assertEquals(list2, wrapper.getMedicamentos());
        assertEquals(2, wrapper.getMedicamentos().size());
    }

    @Test
    void testNullToValueToNull() {
        MedicamentoListWrapper wrapper = new MedicamentoListWrapper();
        
        // Start with null
        assertNull(wrapper.getMedicamentos());
        
        // Set value
        List<Medicamento> medicamentos = new ArrayList<>();
        medicamentos.add(new Medicamento());
        wrapper.setMedicamentos(medicamentos);
        
        assertEquals(medicamentos, wrapper.getMedicamentos());
        assertEquals(1, wrapper.getMedicamentos().size());
        
        // Set back to null
        wrapper.setMedicamentos(null);
        assertNull(wrapper.getMedicamentos());
    }

    @Test
    void testMultipleInstances() {
        List<Medicamento> list1 = new ArrayList<>();
        list1.add(new Medicamento());
        
        List<Medicamento> list2 = new ArrayList<>();
        list2.add(new Medicamento());
        list2.add(new Medicamento());
        
        MedicamentoListWrapper wrapper1 = new MedicamentoListWrapper(list1);
        MedicamentoListWrapper wrapper2 = new MedicamentoListWrapper(list2);
        
        assertEquals(list1, wrapper1.getMedicamentos());
        assertEquals(1, wrapper1.getMedicamentos().size());
        assertEquals(list2, wrapper2.getMedicamentos());
        assertEquals(2, wrapper2.getMedicamentos().size());
        
        // Verify they are independent
        List<Medicamento> newList = new ArrayList<>();
        newList.add(new Medicamento());
        newList.add(new Medicamento());
        newList.add(new Medicamento());
        
        wrapper1.setMedicamentos(newList);
        
        assertEquals(newList, wrapper1.getMedicamentos());
        assertEquals(3, wrapper1.getMedicamentos().size());
        assertEquals(list2, wrapper2.getMedicamentos());
        assertEquals(2, wrapper2.getMedicamentos().size());
    }

    @Test
    void testSetMedicamentos_WithModifiableList() {
        MedicamentoListWrapper wrapper = new MedicamentoListWrapper();
        
        List<Medicamento> medicamentos = new ArrayList<>();
        medicamentos.add(new Medicamento());
        wrapper.setMedicamentos(medicamentos);
        
        assertEquals(1, wrapper.getMedicamentos().size());
        
        // Modify the original list
        medicamentos.add(new Medicamento());
        
        // The wrapper should reflect the changes
        assertEquals(2, wrapper.getMedicamentos().size());
    }

    @Test
    void testSetMedicamentos_WithUnmodifiableList() {
        MedicamentoListWrapper wrapper = new MedicamentoListWrapper();
        
        List<Medicamento> medicamentos = Arrays.asList(new Medicamento(), new Medicamento());
        wrapper.setMedicamentos(medicamentos);
        
        assertEquals(medicamentos, wrapper.getMedicamentos());
        assertEquals(2, wrapper.getMedicamentos().size());
    }

    @Test
    void testSetMedicamentos_WithLargeList() {
        MedicamentoListWrapper wrapper = new MedicamentoListWrapper();
        
        List<Medicamento> largeList = new ArrayList<>();
        for (int i = 0; i < 1000; i++) {
            largeList.add(new Medicamento());
        }
        
        wrapper.setMedicamentos(largeList);
        
        assertEquals(largeList, wrapper.getMedicamentos());
        assertEquals(1000, wrapper.getMedicamentos().size());
    }

    @Test
    void testSetMedicamentos_WithNestedLists() {
        MedicamentoListWrapper wrapper = new MedicamentoListWrapper();
        
        List<Medicamento> medicamentos = new ArrayList<>();
        medicamentos.add(new Medicamento());
        medicamentos.add(new Medicamento());
        
        wrapper.setMedicamentos(medicamentos);
        
        // Create a new list with the same content
        List<Medicamento> newMedicamentos = new ArrayList<>(medicamentos);
        wrapper.setMedicamentos(newMedicamentos);
        
        assertEquals(newMedicamentos, wrapper.getMedicamentos());
        assertEquals(2, wrapper.getMedicamentos().size());
        
        // Verify they are different objects but same content
        assertNotSame(medicamentos, wrapper.getMedicamentos());
        assertEquals(medicamentos, wrapper.getMedicamentos());
    }

    @Test
    void testSetMedicamentos_WithEmptyArrayList() {
        MedicamentoListWrapper wrapper = new MedicamentoListWrapper();
        
        List<Medicamento> emptyArrayList = new ArrayList<>();
        wrapper.setMedicamentos(emptyArrayList);
        
        assertEquals(emptyArrayList, wrapper.getMedicamentos());
        assertTrue(wrapper.getMedicamentos().isEmpty());
        assertEquals(0, wrapper.getMedicamentos().size());
    }

    @Test
    void testSetMedicamentos_WithNullThenEmpty() {
        MedicamentoListWrapper wrapper = new MedicamentoListWrapper();
        
        // Start with null
        assertNull(wrapper.getMedicamentos());
        
        // Set to empty list
        List<Medicamento> emptyList = new ArrayList<>();
        wrapper.setMedicamentos(emptyList);
        
        assertEquals(emptyList, wrapper.getMedicamentos());
        assertTrue(wrapper.getMedicamentos().isEmpty());
        
        // Set back to null
        wrapper.setMedicamentos(null);
        assertNull(wrapper.getMedicamentos());
    }
}


