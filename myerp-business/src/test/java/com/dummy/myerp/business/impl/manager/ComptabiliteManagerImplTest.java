package com.dummy.myerp.business.impl.manager;

import com.dummy.myerp.model.bean.comptabilite.CompteComptable;
import com.dummy.myerp.model.bean.comptabilite.EcritureComptable;
import com.dummy.myerp.model.bean.comptabilite.JournalComptable;
import com.dummy.myerp.model.bean.comptabilite.LigneEcritureComptable;
import com.dummy.myerp.technical.exception.FunctionalException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.math.BigDecimal;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class ComptabiliteManagerImplTest {

    ComptabiliteManagerImpl classUnderTest;

    EcritureComptable ecritureComptable;

    @BeforeEach
    public void init(){
        classUnderTest = new ComptabiliteManagerImpl();
        ecritureComptable = new EcritureComptable();
    }

    @Test
    @Tag("Validations Bean EcritureComptable")
    public void givenEcritureComptableComplete_whenCheckEcritureComptableUnit_thenAssertDoesNotThrowsFunctionalException() throws FunctionalException {
        // GIVEN
        ecritureComptable.setJournal(new JournalComptable("AC", "Achat"));
        ecritureComptable.setDate(new Date());
        ecritureComptable.setLibelle("Libelle");
        ecritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(
                new CompteComptable(1),null, new BigDecimal(123),null));
        ecritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(
                new CompteComptable(2),null, null, new BigDecimal(123)));

        // WHEN & THEN
        classUnderTest.checkEcritureComptableUnit(ecritureComptable);
    }


    @Test
    @Tag("EcritureComptableBeanValidation")
    public void givenEcritureComptableWithOnlyOneLigneEcriture_whenCheckEcritureComptableUnit_thenAssertThrowsFunctionalException() {
        // GIVEN
        ecritureComptable.setJournal(new JournalComptable("AC", "Achat"));
        ecritureComptable.setDate(new Date());
        ecritureComptable.setLibelle("Libelle");
        ecritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(
                new CompteComptable(1),null, new BigDecimal(123),null)); // only one LigneEcriture

        // WHEN & THEN
        assertThrows(FunctionalException.class, () -> classUnderTest.checkEcritureComptableUnit(ecritureComptable));
    }

    @Test
    @Tag("EcritureComptableBeanValidation")
    public void givenEcritureComptableWithTwoLigneEcritureOnlyDebit_whenCheckEcritureComptableUnit_thenAssertThrowsFunctionalException() {
        // GIVEN
        ecritureComptable.setJournal(new JournalComptable("AC", "Achat"));
        ecritureComptable.setDate(new Date());
        ecritureComptable.setLibelle("Libelle");
        ecritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(
                new CompteComptable(1),null, new BigDecimal(123),null));
        ecritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(
                new CompteComptable(1),null, new BigDecimal(123),null));

        // WHEN & THEN
        assertThrows(FunctionalException.class, () -> classUnderTest.checkEcritureComptableUnit(ecritureComptable));
    }

    @Test
    @Tag("EcritureComptableBeanValidation")
    public void givenEcritureComptableWithTwoLigneEcritureOnlyCredit_whenCheckEcritureComptableUnit_thenAssertThrowsFunctionalException() {
        // GIVEN
        ecritureComptable.setJournal(new JournalComptable("AC", "Achat"));
        ecritureComptable.setDate(new Date());
        ecritureComptable.setLibelle("Libelle");
        ecritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(
                new CompteComptable(1),null, null,new BigDecimal(123)));
        ecritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(
                new CompteComptable(1),null, null,new BigDecimal(123)));

        // WHEN & THEN
        assertThrows(FunctionalException.class, () -> classUnderTest.checkEcritureComptableUnit(ecritureComptable));
    }

    @Test
    @Tag("EcritureComptableBeanValidation")
    public void givenEcritureComptableWithoutDate_whenCheckEcritureComptableUnit_thenAssertThrowsFunctionalException() {
        // GIVEN
        ecritureComptable.setJournal(new JournalComptable("AC", "Achat"));
        ecritureComptable.setDate(null);  // no date
        ecritureComptable.setLibelle("Libelle");
        ecritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(
                new CompteComptable(1),null, new BigDecimal(123),null));
        ecritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(
                new CompteComptable(2),null, null, new BigDecimal(123)));

        // WHEN & THEN
        assertThrows(FunctionalException.class, () -> classUnderTest.checkEcritureComptableUnit(ecritureComptable));
    }

    @Test
    @Tag("EcritureComptableBeanValidation")
    public void givenEcritureComptableWithoutJournalComptable_whenCheckEcritureComptableUnit_thenAssertThrowsFunctionalException() {
        // GIVEN
        ecritureComptable.setJournal(null);
        ecritureComptable.setDate(new Date());
        ecritureComptable.setLibelle("Libelle");
        ecritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(
                new CompteComptable(1),null, new BigDecimal(123),null));
        ecritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(
                new CompteComptable(2),null, null, new BigDecimal(123)));

        // WHEN & THEN
        assertThrows(FunctionalException.class, () -> classUnderTest.checkEcritureComptableUnit(ecritureComptable));
    }

    @Test
    @Tag("EcritureComptableBeanValidation")
    public void givenEcritureComptableWithoutLibelle_whenCheckEcritureComptableUnit_thenAssertThrowsFunctionalException() {
        // GIVEN
        ecritureComptable.setJournal(new JournalComptable("AC", "Achat"));
        ecritureComptable.setDate(new Date());
        ecritureComptable.setLibelle(null); // no Libelle
        ecritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(
                new CompteComptable(1),null, new BigDecimal(123),null));
        ecritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(
                new CompteComptable(2),null, null, new BigDecimal(123)));

        // WHEN & THEN
        assertThrows(FunctionalException.class, () -> classUnderTest.checkEcritureComptableUnit(ecritureComptable));
    }

    @ParameterizedTest(name = "{0} => wrong reference pattern , should be type BQ-2020/00001 or 01-2020/00001")
    @ValueSource(strings = {"BQqq-2020/00001","01-xXxX/00001","BQ-200/00001","BQ-2020/001","BQ-2020/XxXxX"})
    @Tag("EcritureComptableBeanValidation")
    public void givenEcritureComptableWithWrongReferencePattern_whenCheckEcritureComptableUnit_thenAssertThrowsFunctionalException(String arg) {
        // GIVEN
        ecritureComptable.setJournal(new JournalComptable("AC", "Achat"));
        ecritureComptable.setDate(new Date());
        ecritureComptable.setLibelle("Libelle");
        ecritureComptable.setReference(arg);
        ecritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(
                new CompteComptable(1),null, new BigDecimal(123),null));
        ecritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(
                new CompteComptable(2),null, null, new BigDecimal(123)));

        // WHEN & THEN
        assertThrows(FunctionalException.class, () -> classUnderTest.checkEcritureComptableUnit(ecritureComptable));
    }

    @Test
    public void givenEcritureComptableNonEquilibree_whenCheckEcritureComptableUnit_thenAssertThrowsFunctionalException(){
        // GIVEN
        ecritureComptable.setJournal(new JournalComptable("AC", "Achat"));
        ecritureComptable.setDate(new Date());
        ecritureComptable.setLibelle("Libelle");
        ecritureComptable.getListLigneEcriture().add(
                new LigneEcritureComptable(new CompteComptable(1),null, new BigDecimal(123),null));
        ecritureComptable.getListLigneEcriture().add(
                new LigneEcritureComptable(new CompteComptable(2),null, null, new BigDecimal(1234)));

        // WHEN & THEN
        assertThrows(FunctionalException.class, () -> classUnderTest.checkEcritureComptableUnit(ecritureComptable));
    }

    /*@Test(expected = FunctionalException.class)
    public void checkEcritureComptableUnitRG3() throws Exception {
        EcritureComptable vEcritureComptable;
        vEcritureComptable = new EcritureComptable();
        vEcritureComptable.setJournal(new JournalComptable("AC", "Achat"));
        vEcritureComptable.setDate(new Date());
        vEcritureComptable.setLibelle("Libelle");
        vEcritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(1),
                                                                                 null, new BigDecimal(123),
                                                                                 null));
        vEcritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(1),
                                                                                 null, new BigDecimal(123),
                                                                                 null));
        classUnderTest.checkEcritureComptableUnit(vEcritureComptable);
    }*/

}
