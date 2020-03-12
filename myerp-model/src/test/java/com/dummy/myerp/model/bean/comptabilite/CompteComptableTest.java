package com.dummy.myerp.model.bean.comptabilite;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class CompteComptableTest {

    CompteComptable compteComptableUnderTest;
    List<CompteComptable> compteComptableList = new ArrayList<>();

    @BeforeEach
    public void init() {
        // create 5 comptes comptables for tests
        for (int i = 0; i < 5; i++){
            compteComptableUnderTest = new CompteComptable();
            compteComptableUnderTest.setLibelle("QQ");
            compteComptableUnderTest.setNumero(i+1);
            compteComptableList.add(compteComptableUnderTest);
        }
    }

    @Test
    void shouldReturnCompteComptableIfPresent_whenGetByNumero() {
        // GIVEN already initialized
        int compteComptableNumberSearched = 1;

        // WHEN
        CompteComptable result = CompteComptable.getByNumero(compteComptableList,compteComptableNumberSearched);

        // THEN
        assertThat(result.getNumero()).isEqualTo(compteComptableNumberSearched);
    }

    @Test
    void shouldNotReturnCompteComptableIfNotPresent_whenGetByNumero() {
        // GIVEN already initialized
        int compteComptableNumberSearched = 10;

        // WHEN
        CompteComptable result = CompteComptable.getByNumero(compteComptableList,compteComptableNumberSearched);

        // THEN
        assertThat(result).isEqualTo(null);
    }
}