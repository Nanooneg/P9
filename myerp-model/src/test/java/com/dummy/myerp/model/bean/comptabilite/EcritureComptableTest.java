package com.dummy.myerp.model.bean.comptabilite;

import org.apache.commons.lang3.ObjectUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;


public class EcritureComptableTest {

    EcritureComptable ecritureComptableUnderTest;

    @BeforeEach
    public void init() {
        ecritureComptableUnderTest = new EcritureComptable();
        ecritureComptableUnderTest.setLibelle("Equilibrée");
        ecritureComptableUnderTest.getListLigneEcriture().add(this.createLigne(1, "200.50", null));
        ecritureComptableUnderTest.getListLigneEcriture().add(this.createLigne(1, "100.50", "33"));
        ecritureComptableUnderTest.getListLigneEcriture().add(this.createLigne(2, null, "301"));
        ecritureComptableUnderTest.getListLigneEcriture().add(this.createLigne(2, "40", "7"));
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
        // GIVEN already initialized

        // WHEN
        BigDecimal resultTest = ecritureComptableUnderTest.getTotalDebit();

        // THEN
        assertThat(resultTest.toString()).isEqualTo("341");
    }

    @Test
    public void givenListLigneEcriture_whenGetTotalCredit_thenReturnSumOfCredit (){
        // GIVEN already initialized

        // WHEN
        BigDecimal resultTest = ecritureComptableUnderTest.getTotalCredit();

        // THEN
        assertThat(resultTest.toString()).isEqualTo("341");
    }

    @Test
    public void ShouldReturnTrue_WhenTotalCreditIsEqualToTotalDebit() {
        // GIVEN already initialized

        // WHEN
        boolean resultTest = ecritureComptableUnderTest.isEquilibree();

        // THEN
        assertThat(resultTest).isEqualTo(true);
    }

    @Test
    public void ShouldReturnFalse_WhenTotalCreditIsNotEqualToTotalDebit() {
        // GIVEN
        ecritureComptableUnderTest.setLibelle("Non équilibrée");
        ecritureComptableUnderTest.getListLigneEcriture().add(this.createLigne(1, "200", null));


        // WHEN
        boolean resultTest = ecritureComptableUnderTest.isEquilibree();

        // THEN
        assertThat(resultTest).isEqualTo(false);
    }

    @Test
    void shouldReturnStringValueOfEcritureComptable_whenToString (){
        // GIVEN
        ecritureComptableUnderTest.getListLigneEcriture().clear();
        ecritureComptableUnderTest.getListLigneEcriture().add(this.createLigne(1, "200.50", null));

        // WHEN
        String result = ecritureComptableUnderTest.toString();

        // THEN
        assertThat(result)
                .isEqualTo("EcritureComptable{id=null, " +
                        "journal=null, " +
                        "reference='null', " +
                        "date=null, " +
                        "libelle='Equilibrée', " +
                        "totalDebit=200.5, " +
                        "totalCredit=0, " +
                        "listLigneEcriture=[\n" +
                            "LigneEcritureComptable{compteComptable=CompteComptable{numero=1, libelle='null'}, " +
                            "libelle='200.50', " +
                            "debit=200.50, " +
                            "credit=null}" +
                        "\n]" +
                        "}");
    }


}
