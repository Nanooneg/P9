package com.dummy.myerp.model.bean.comptabilite;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class JournalComptableTest {

    JournalComptable journalComptableUnderTest;
    List<JournalComptable> journalComptableList = new ArrayList<>();

    @BeforeEach
    public void init() {
        // create 5 journeaux comptables for tests
        for (int i = 0; i < 5; i++){
            journalComptableUnderTest = new JournalComptable();
            journalComptableUnderTest.setLibelle("QQ");
            journalComptableUnderTest.setCode("A" + (i+1));
            journalComptableList.add(journalComptableUnderTest);
        }
    }

    @Test
    void shouldReturnJournalComptableIfPresent_whenGetByCode() {
        // GIVEN already initialized
        String journalComptableNumberSearched = "A1";

        // WHEN
        JournalComptable result = JournalComptable.getByCode(journalComptableList,journalComptableNumberSearched);

        // THEN
        assertThat(result.getCode()).isEqualTo(journalComptableNumberSearched);
    }

    @Test
    void shouldNotReturnJournalComptableIfNotPresent_whenGetBycode() {
        // GIVEN already initialized
        String journalComptableNumberSearched = "A15";

        // WHEN
        JournalComptable result = JournalComptable.getByCode(journalComptableList,journalComptableNumberSearched);

        // THEN
        assertThat(result).isEqualTo(null);
    }

    @Test
    void shouldReturnStringValueOfJournalComptable_whenToString (){
        // GIVEN already initialize

        // WHEN
        String result = journalComptableList.get(0).toString();

        // THEN
        assertThat(result).isEqualTo("JournalComptable{code='A1', libelle='QQ'}");
    }
}

