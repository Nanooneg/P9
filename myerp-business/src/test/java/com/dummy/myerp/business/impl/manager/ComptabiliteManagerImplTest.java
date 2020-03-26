package com.dummy.myerp.business.impl.manager;

import com.dummy.myerp.business.contrat.BusinessProxy;
import com.dummy.myerp.business.impl.AbstractBusinessManager;
import com.dummy.myerp.business.impl.TransactionManager;
import com.dummy.myerp.consumer.dao.contrat.DaoProxy;
import com.dummy.myerp.model.bean.comptabilite.*;
import com.dummy.myerp.technical.exception.FunctionalException;
import com.dummy.myerp.technical.exception.NotFoundException;
import org.apache.commons.lang3.ObjectUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ComptabiliteManagerImplTest {

    ComptabiliteManagerImpl comptabiliteManagerUnderTest;
    EcritureComptable ecritureComptable;

    private DaoProxy mockDaoProxy;

    @BeforeEach
    public void init(){
        comptabiliteManagerUnderTest = new ComptabiliteManagerImpl();
        ecritureComptable = new EcritureComptable();
        ecritureComptable.setId(1);
        ecritureComptable.setJournal(new JournalComptable("AC", "Achat"));
        ecritureComptable.setDate(new Date());
        ecritureComptable.setLibelle("Libelle");
        ecritureComptable.setReference("BQ-2020/00001");
        ecritureComptable.getListLigneEcriture().add(this.createLigne(1, "200.50", null));
        ecritureComptable.getListLigneEcriture().add(this.createLigne(1, "100.50", "33"));
        ecritureComptable.getListLigneEcriture().add(this.createLigne(2, null, "301"));
        ecritureComptable.getListLigneEcriture().add(this.createLigne(2, "40", "7"));
    }

    public void initMock(){
        mockDaoProxy = mock(DaoProxy.class, Mockito.RETURNS_DEEP_STUBS);
        BusinessProxy mockBusinessProxy = mock(BusinessProxy.class, RETURNS_DEEP_STUBS);
        TransactionManager mockTransactionManager = mock(TransactionManager.class, RETURNS_DEEP_STUBS);
        AbstractBusinessManager.configure(mockBusinessProxy, mockDaoProxy, mockTransactionManager);
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
    final void getListCompteComptable() {
        initMock();

        comptabiliteManagerUnderTest.getListCompteComptable();
        then(mockDaoProxy).should(times(1)).getComptabiliteDao();
    }

    @Test
    void getListJournalComptable() {
        initMock();

        comptabiliteManagerUnderTest.getListJournalComptable();
        then(mockDaoProxy).should(times(1)).getComptabiliteDao();
    }

    @Test
    void getListEcritureComptable() {
        initMock();

        comptabiliteManagerUnderTest.getListEcritureComptable();
        then(mockDaoProxy).should(times(1)).getComptabiliteDao();
    }

    @Test
    void insertEcritureComptable() throws FunctionalException, NotFoundException {
        initMock();

        EcritureComptable returnedEcriturecomptable = new EcritureComptable();
        returnedEcriturecomptable.setReference("BQ-2020/00001");
        returnedEcriturecomptable.setId(1);
        when(mockDaoProxy.getComptabiliteDao().getEcritureComptableByRef(anyString()))
                .thenReturn(returnedEcriturecomptable);

        comptabiliteManagerUnderTest.insertEcritureComptable(ecritureComptable);
        then(mockDaoProxy).should(atLeast(1)).getComptabiliteDao();
    }

    @Test
    void updateEcritureComptable() throws FunctionalException, NotFoundException {
        initMock();

        EcritureComptable returnedEcriturecomptable = new EcritureComptable();
        returnedEcriturecomptable.setReference("BQ-2020/00001");
        returnedEcriturecomptable.setId(1);
        when(mockDaoProxy.getComptabiliteDao().getEcritureComptableByRef(anyString()))
                .thenReturn(returnedEcriturecomptable);

        comptabiliteManagerUnderTest.updateEcritureComptable(ecritureComptable);
        then(mockDaoProxy).should(atLeast(1)).getComptabiliteDao();
    }

    @Test
    void deleteEcritureComptable() {
        initMock();

        comptabiliteManagerUnderTest.deleteEcritureComptable(ecritureComptable.getId());
        then(mockDaoProxy).should(times(1)).getComptabiliteDao();
    }

    @Test
    @Tag("BeanValidation")
    public void givenEcritureComptableComplete_whenCheckEcritureComptableUnit_thenAssertDoesNotThrowsFunctionalException() throws FunctionalException {
        // GIVEN already init

        // WHEN & THEN
        comptabiliteManagerUnderTest.checkEcritureComptableUnit(ecritureComptable);
    }

    @Test
    @Tag("BeanValidation")
    public void givenEcritureComptableWithoutDate_whenCheckEcritureComptableUnit_thenAssertThrowsFunctionalException() {
        // GIVEN
        ecritureComptable.setDate(null);

        // WHEN
        FunctionalException exception =
                assertThrows(FunctionalException.class, () -> comptabiliteManagerUnderTest.checkEcritureComptableUnit(ecritureComptable));

        // THEN
        assertThat(exception.getMessage()).isEqualTo("L'écriture comptable ne respecte pas les règles de gestion.");
    }

    @Test
    @Tag("BeanValidation")
    public void givenEcritureComptableWithoutJournalComptable_whenCheckEcritureComptableUnit_thenAssertThrowsFunctionalException() {
        // GIVEN
        ecritureComptable.setJournal(null);

        // WHEN
        FunctionalException exception =
                assertThrows(FunctionalException.class, () -> comptabiliteManagerUnderTest.checkEcritureComptableUnit(ecritureComptable));

        // THEN
        assertThat(exception.getMessage()).isEqualTo("L'écriture comptable ne respecte pas les règles de gestion.");
    }

    @Test
    @Tag("BeanValidation")
    public void givenEcritureComptableWithoutLibelle_whenCheckEcritureComptableUnit_thenAssertThrowsFunctionalException() {
        // GIVEN
        ecritureComptable.setLibelle(null);

        // WHEN
        FunctionalException exception =
                assertThrows(FunctionalException.class, () -> comptabiliteManagerUnderTest.checkEcritureComptableUnit(ecritureComptable));

        // THEN
        assertThat(exception.getMessage()).isEqualTo("L'écriture comptable ne respecte pas les règles de gestion.");
    }

    @ParameterizedTest(name = "{0} => wrong reference pattern , should be type BQ-2020/00001 or 01-2020/00001")
    @ValueSource(strings = {"BQqq-2020/00001","01-xXxX/00001","BQ-200/00001","BQ-2020/001","BQ-2020/XxXxX",""})
    @Tag("BeanValidation")
    public void givenEcritureComptableWithWrongReferencePattern_whenCheckEcritureComptableUnit_thenAssertThrowsFunctionalException(String arg) {
        // GIVEN
        ecritureComptable.setReference(arg);

        // WHEN
        FunctionalException exception =
                assertThrows(FunctionalException.class, () -> comptabiliteManagerUnderTest.checkEcritureComptableUnit(ecritureComptable));

        // THEN
        assertThat(exception.getMessage()).isEqualTo("L'écriture comptable ne respecte pas les règles de gestion.");
    }

    @Test
    @Tag("BeanValidation")
    public void givenEcritureComptableWithWrongMontantInLigneEcriture_whenCheckEcritureComptableUnit_thenAssertDoesNotThrowsFunctionalException() {
        // GIVEN
        ecritureComptable.getListLigneEcriture().add(this.createLigne(1,"123.456",null));

        // WHEN
        FunctionalException exception =
                assertThrows(FunctionalException.class, () -> comptabiliteManagerUnderTest.checkEcritureComptableUnit(ecritureComptable));

        // THEN
        assertThat(exception.getMessage()).isEqualTo("L'écriture comptable ne respecte pas les règles de gestion.");
    }

    @Test
    @Tag("RG2")
    public void givenEcritureComptableNonEquilibree_whenCheckEcritureComptableUnit_thenAssertThrowsFunctionalException(){
        // GIVEN
        ecritureComptable.getListLigneEcriture().add(this.createLigne(1,"123",null));

        // WHEN
        FunctionalException exception =
                assertThrows(FunctionalException.class, () -> comptabiliteManagerUnderTest.checkEcritureComptableUnit(ecritureComptable));

        // THEN
        assertThat(exception.getMessage()).isEqualTo("L'écriture comptable n'est pas équilibrée.");
    }

    @Test
    @Tag("RG3")
    public void givenEcritureComptableWithOnlyOneLigneEcriture_whenCheckEcritureComptableUnit_thenAssertThrowsFunctionalException() {
        // GIVEN
        ecritureComptable.getListLigneEcriture().clear();
        ecritureComptable.getListLigneEcriture().add(this.createLigne(1,"123",null));

        // WHEN
        FunctionalException exception =
                assertThrows(FunctionalException.class, () -> comptabiliteManagerUnderTest.checkEcritureComptableUnit(ecritureComptable));

        // THEN
        assertThat(exception.getMessage()).isEqualTo("L'écriture comptable ne respecte pas les règles de gestion.");
    }

    @Test
    @Tag("RG3")
    public void givenEcritureComptableWithTwoLigneEcritureOnlyDebit_whenCheckEcritureComptableUnit_thenAssertThrowsFunctionalException() {
        // GIVEN
        ecritureComptable.getListLigneEcriture().clear();
        ecritureComptable.getListLigneEcriture().add(this.createLigne(1,"123",null));
        ecritureComptable.getListLigneEcriture().add(this.createLigne(2,"123",null));

        // WHEN
        FunctionalException exception =
                assertThrows(FunctionalException.class, () -> comptabiliteManagerUnderTest.checkEcritureComptableUnit(ecritureComptable));

        // THEN
        assertThat(exception.getMessage()).isEqualTo("L'écriture comptable n'est pas équilibrée.");
    }

    @Test
    @Tag("RG3")
    public void givenEcritureComptableWithTwoLigneEcritureOnlyCredit_whenCheckEcritureComptableUnit_thenAssertThrowsFunctionalException() {
        // GIVEN
        ecritureComptable.getListLigneEcriture().clear();
        ecritureComptable.getListLigneEcriture().add(this.createLigne(1,null,"123"));
        ecritureComptable.getListLigneEcriture().add(this.createLigne(2,null,"123"));

        // WHEN
        FunctionalException exception =
                assertThrows(FunctionalException.class, () -> comptabiliteManagerUnderTest.checkEcritureComptableUnit(ecritureComptable));

        // THEN
        assertThat(exception.getMessage()).isEqualTo("L'écriture comptable n'est pas équilibrée.");
    }

    @Test
    @Tag("RG5")
    public void givenEcritureComptableWithLastYearInReference_whenCheckEcritureComptableUnit_thenAssertThrowsFunctionalException() {
        // GIVEN
        ecritureComptable.setReference("01-" + (Calendar.getInstance().get(Calendar.YEAR)-1) + "/00001");

        // WHEN
        FunctionalException exception =
                assertThrows(FunctionalException.class, () -> comptabiliteManagerUnderTest.checkEcritureComptableUnit(ecritureComptable));

        // THEN
        assertThat(exception.getMessage()).isEqualTo("La référence de l'écriture comptable doit contenir l'année courante.");
    }

    @Test
    @Tag("RG6")
    public void givenEcritureComptableWithAlreadyUsedRef_whenCheckEcritureComptableContext_thenAssertThrowsFunctionalException() throws NotFoundException {
        // GIVEN
        initMock();

        EcritureComptable returnedEcritureComptable = new EcritureComptable();
        returnedEcritureComptable.setId(ecritureComptable.getId() + 1); // not the same id !!
        returnedEcritureComptable.setReference(ecritureComptable.getReference());
        when(mockDaoProxy.getComptabiliteDao().getEcritureComptableByRef("BQ-2020/00001"))
                .thenReturn(returnedEcritureComptable);

        // WHEN
        FunctionalException exception =
                assertThrows(FunctionalException.class, () -> comptabiliteManagerUnderTest.checkEcritureComptableContext(ecritureComptable));

        // THEN
        assertThat(exception.getMessage()).isEqualTo("Une autre écriture comptable existe déjà avec la même référence.");
    }

    @Test
    @Tag("RG6")
    public void givenEcritureComptableAlreadyInDB_whenCheckEcritureComptableContext_thenAssertNotThrowsFunctionalException() throws NotFoundException, FunctionalException {
        // GIVEN
        initMock();

        EcritureComptable returnedEcriturecomptable = new EcritureComptable();
        returnedEcriturecomptable.setReference("BQ-2020/00001");
        returnedEcriturecomptable.setId(1);
        when(mockDaoProxy.getComptabiliteDao().getEcritureComptableByRef(anyString()))
                .thenReturn(returnedEcriturecomptable);

        // WHEN & THEN
        comptabiliteManagerUnderTest.checkEcritureComptableContext(ecritureComptable);
    }

    @Test
    @Tag("RG6")
    public void givenEcritureComptableWithNewRef_whenCheckEcritureComptableContext_thenAssertNotThrowsFunctionalException() throws NotFoundException, FunctionalException {
        // GIVEN
        initMock();

        when(mockDaoProxy.getComptabiliteDao().getEcritureComptableByRef(anyString()))
                .thenThrow(NotFoundException.class); //

        // WHEN & THEN
        comptabiliteManagerUnderTest.checkEcritureComptableContext(ecritureComptable);
    }

    @Test
    @Tag("All-RG")
    public void givenGoodEcritureComptable_whenCheckEcritureComptable_thenAssertNotThrowFunctionalException () throws FunctionalException, NotFoundException {
        // GIVEN
        initMock();

        when(mockDaoProxy.getComptabiliteDao().getEcritureComptableByRef(anyString()))
                .thenThrow(NotFoundException.class); //

        // WHEN & THEN
        comptabiliteManagerUnderTest.checkEcritureComptable(ecritureComptable);
    }

    @Test
    @Tag("ContextReference")
    public void givenFirstEcritureComptableOfYear_whenAddReference_thenAddNumber1InReferenceAndSaveNumber1InSequenceEcritureComptable() throws NotFoundException {
        // GIVEN
        initMock();

        when(mockDaoProxy.getComptabiliteDao().getSequenceEcritureComptable("AC",2020)).thenReturn(null);

        // WHEN
        comptabiliteManagerUnderTest.addReference(ecritureComptable);
        String[] reference = ecritureComptable.getReference().split("[-/]");
        String sequenceNumber = reference[2];

        // THEN
        assertThat(sequenceNumber).isEqualTo("00001");
        then(mockDaoProxy).should(atLeast(2)).getComptabiliteDao();
    }

    @Test
    @Tag("ContextReference")
    public void givenXThEcritureComptableOfYear_whenAddReference_thenAddNumberXPlusOneInReferenceAndSaveNewNumberInSequenceEcritureComptable() throws NotFoundException {
        // GIVEN
        initMock();

        when(mockDaoProxy.getComptabiliteDao().getSequenceEcritureComptable("AC",2020))
                .thenReturn(new SequenceEcritureComptable(2020,15));

        // WHEN
        comptabiliteManagerUnderTest.addReference(ecritureComptable);
        String[] reference = ecritureComptable.getReference().split("[-/]");
        String sequenceNumber = reference[2];

        // THEN
        assertThat(sequenceNumber).isEqualTo("00016");
        then(mockDaoProxy).should(atLeast(2)).getComptabiliteDao();
    }
}
