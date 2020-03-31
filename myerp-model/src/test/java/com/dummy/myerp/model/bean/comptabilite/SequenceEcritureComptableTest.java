package com.dummy.myerp.model.bean.comptabilite;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class SequenceEcritureComptableTest {

    @Test
    public void shouldReturnStringValueOfSequenceEcritureComptable_whenToString (){
        // GIVEN
        SequenceEcritureComptable sequenceEcritureComptableUnderTest = new SequenceEcritureComptable(2020,1);

        // WHEN
        String result = sequenceEcritureComptableUnderTest.toString();

        // THEN
        assertThat(result).isEqualTo("SequenceEcritureComptable{annee=2020, derniereValeur=1}");
    }

}
