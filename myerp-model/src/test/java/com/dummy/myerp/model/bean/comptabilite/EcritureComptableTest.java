package com.dummy.myerp.model.bean.comptabilite;

import org.apache.commons.lang3.ObjectUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;


public class EcritureComptableTest {

    EcritureComptable classUnderTest;

    @BeforeEach
    public void init() {
        classUnderTest = new EcritureComptable();
    }

    private LigneEcritureComptable createLigne(Integer pCompteComptableNumero, String pDebit, String pCredit) {
        BigDecimal vDebit = pDebit == null ? null : new BigDecimal(pDebit);
        BigDecimal vCredit = pCredit == null ? null : new BigDecimal(pCredit);
        String vLibelle = ObjectUtils.defaultIfNull(vDebit, BigDecimal.ZERO)
                                     .subtract(ObjectUtils.defaultIfNull(vCredit, BigDecimal.ZERO)).toPlainString();
        return new LigneEcritureComptable(new CompteComptable(pCompteComptableNumero),
                                                                    vLibelle,
                                                                    vDebit, vCredit);
    }

    @Test
    public void givenListLigneEcriture_whenGetTotalDebit_thenReturnSumOfDebit (){
        // GIVEN
        classUnderTest.getListLigneEcriture().add(this.createLigne(1, "200.50", null));
        classUnderTest.getListLigneEcriture().add(this.createLigne(1, "100.50", "33"));
        classUnderTest.getListLigneEcriture().add(this.createLigne(2, null, "301"));
        classUnderTest.getListLigneEcriture().add(this.createLigne(2, "40", "7"));

        // WHEN
        BigDecimal resultTest = classUnderTest.getTotalDebit();

        // THEN
        assertThat(resultTest.toString()).isEqualTo("341.00");
    }

    @Test
    public void givenListLigneEcriture_whenGetTotalCredit_thenReturnSumOfCredit (){
        // GIVEN
        classUnderTest.getListLigneEcriture().add(this.createLigne(1, "200.50", null));
        classUnderTest.getListLigneEcriture().add(this.createLigne(1, "100.50", "33"));
        classUnderTest.getListLigneEcriture().add(this.createLigne(2, null, "301"));
        classUnderTest.getListLigneEcriture().add(this.createLigne(2, "40", "7"));

        // WHEN
        BigDecimal resultTest = classUnderTest.getTotalCredit();

        // THEN
        assertThat(resultTest.toString()).isEqualTo("341.00");
    }

    @Test
    public void ShouldReturnTrue_WhenTotalCreditIsEqualToTotalDebit() {
        // GIVEN
        classUnderTest.setLibelle("Equilibrée");
        classUnderTest.getListLigneEcriture().add(this.createLigne(1, "200.50", null));
        classUnderTest.getListLigneEcriture().add(this.createLigne(1, "100.50", "33"));
        classUnderTest.getListLigneEcriture().add(this.createLigne(2, null, "301"));
        classUnderTest.getListLigneEcriture().add(this.createLigne(2, "40", "7"));

        // WHEN
        boolean resultTest = classUnderTest.isEquilibree();

        // THEN
        assertThat(resultTest).isEqualTo(classUnderTest.getLibelle().equalsIgnoreCase("Equilibrée"));
    }

    @Test
    public void ShouldReturnFalse_WhenTotalCreditIsNotEqualToTotalDebit() {
        // GIVEN
        classUnderTest.setLibelle("Non équilibrée");
        classUnderTest.getListLigneEcriture().add(this.createLigne(1, "200.50", null));
        classUnderTest.getListLigneEcriture().add(this.createLigne(1, "100.50", null));
        classUnderTest.getListLigneEcriture().add(this.createLigne(2, null, "301"));
        classUnderTest.getListLigneEcriture().add(this.createLigne(2, "40", "80"));

        // WHEN
        boolean resultTest = classUnderTest.isEquilibree();

        // THEN
        assertThat(resultTest).isNotEqualTo(classUnderTest.getLibelle().equalsIgnoreCase("Equilibrée"));
    }


}
