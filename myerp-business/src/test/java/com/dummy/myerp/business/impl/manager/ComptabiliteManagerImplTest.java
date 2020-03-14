package com.dummy.myerp.business.impl.manager;

import com.dummy.myerp.business.impl.AbstractBusinessManager;
import com.dummy.myerp.business.impl.TransactionManager;
import com.dummy.myerp.consumer.dao.contrat.ComptabiliteDao;
import com.dummy.myerp.consumer.dao.contrat.DaoProxy;
import com.dummy.myerp.model.bean.comptabilite.*;
import com.dummy.myerp.technical.exception.FunctionalException;
import com.dummy.myerp.technical.exception.NotFoundException;
import org.apache.commons.lang3.ObjectUtils;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;

import static com.dummy.myerp.consumer.ConsumerHelper.getDaoProxy;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ComptabiliteManagerImplTest {

    ComptabiliteManagerImpl classUnderTest;
    EcritureComptable ecritureComptable;

    private static DaoProxy mockDaoProxy = mock(DaoProxy.class, Mockito.RETURNS_DEEP_STUBS);
    private static TransactionManager mockTransactionManager = mock(TransactionManager.class, RETURNS_DEEP_STUBS);

    @BeforeEach
    public void init(){
        classUnderTest = new ComptabiliteManagerImpl();
        ecritureComptable = new EcritureComptable();
        ecritureComptable.setId(1);  // TODO just do that now go launch !! ^^
        ecritureComptable.setJournal(new JournalComptable("AC", "Achat"));
        ecritureComptable.setDate(new Date());
        ecritureComptable.setLibelle("Libelle");
        ecritureComptable.setReference("BQ-2020/00001");
        ecritureComptable.getListLigneEcriture().add(this.createLigne(1, "200.50", null));
        ecritureComptable.getListLigneEcriture().add(this.createLigne(1, "100.50", "33"));
        ecritureComptable.getListLigneEcriture().add(this.createLigne(2, null, "301"));
        ecritureComptable.getListLigneEcriture().add(this.createLigne(2, "40", "7"));
    }

    @BeforeAll
    public static void setUp(){
        AbstractBusinessManager.configure(null, mockDaoProxy, mockTransactionManager);
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
    @Tag("BeanValidation")
    public void givenEcritureComptableComplete_whenCheckEcritureComptableUnit_thenAssertDoesNotThrowsFunctionalException() throws FunctionalException {
        // GIVEN already init

        // WHEN & THEN
        classUnderTest.checkEcritureComptableUnit(ecritureComptable);
    }

    @Test
    @Tag("BeanValidation")
    public void givenEcritureComptableWithoutDate_whenCheckEcritureComptableUnit_thenAssertThrowsFunctionalException() {
        // GIVEN
        ecritureComptable.setDate(null);

        // WHEN
        FunctionalException exception =
                assertThrows(FunctionalException.class, () -> classUnderTest.checkEcritureComptableUnit(ecritureComptable));

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
                assertThrows(FunctionalException.class, () -> classUnderTest.checkEcritureComptableUnit(ecritureComptable));

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
                assertThrows(FunctionalException.class, () -> classUnderTest.checkEcritureComptableUnit(ecritureComptable));

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
                assertThrows(FunctionalException.class, () -> classUnderTest.checkEcritureComptableUnit(ecritureComptable));

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
                assertThrows(FunctionalException.class, () -> classUnderTest.checkEcritureComptableUnit(ecritureComptable));

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
                assertThrows(FunctionalException.class, () -> classUnderTest.checkEcritureComptableUnit(ecritureComptable));

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
                assertThrows(FunctionalException.class, () -> classUnderTest.checkEcritureComptableUnit(ecritureComptable));

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
                assertThrows(FunctionalException.class, () -> classUnderTest.checkEcritureComptableUnit(ecritureComptable));

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
                assertThrows(FunctionalException.class, () -> classUnderTest.checkEcritureComptableUnit(ecritureComptable));

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
                assertThrows(FunctionalException.class, () -> classUnderTest.checkEcritureComptableUnit(ecritureComptable));

        // THEN
        assertThat(exception.getMessage()).isEqualTo("La référence de l'écriture comptable doit contenir l'année courante.");
    }

    @Test
    @Tag("RG6")
    public void givenEcritureComptableWithAlreadyUsedRef_whenCheckEcritureComptableContext_thenAssertThrowsFunctionalException() throws NotFoundException {
        // GIVEN
        when(mockDaoProxy.getComptabiliteDao().getEcritureComptableByRef("BQ-2020/00001"))
                .thenReturn(ecritureComptable);

        // WHEN
        FunctionalException exception =
                assertThrows(FunctionalException.class, () -> classUnderTest.checkEcritureComptableContext(ecritureComptable));

        // THEN
        assertThat(exception.getMessage()).isEqualTo("Une autre écriture comptable existe déjà avec la même référence.");
    }

    @Test
    @Tag("RG6")
    public void givenEcritureComptableWithNewRef_whenCheckEcritureComptableContext_thenAssertNotThrowsFunctionalException() throws NotFoundException, FunctionalException {
        // GIVEN
        when(mockDaoProxy.getComptabiliteDao().getEcritureComptableByRef("BQ-2020/00001"))
                .thenReturn(null);

        // WHEN
        classUnderTest.checkEcritureComptableContext(ecritureComptable);

        // THEN
    }

    @Test
    @Tag("ContextReference")
    public void givenFirstEcritureComptableOfYear_whenAddReference_thenAddNumber1InReferenceAndSaveNumber1InSequenceEcritureComptable(){
        // GIVEN
        when(mockDaoProxy.getComptabiliteDao().getSequenceEcritureComptableByYear("AC",2020)).thenReturn(null);

        // WHEN
        classUnderTest.addReference(ecritureComptable);
        String[] reference = ecritureComptable.getReference().split("[-/]");
        String sequenceNumber = reference[2];

        // THEN
        assertThat(sequenceNumber).isEqualTo("00001");
    }

    @Test
    @Tag("ContextReference")
    public void givenXThEcritureComptableOfYear_whenAddReference_thenAddNumberXPlusOneInReferenceAndSaveNewNumberInSequenceEcritureComptable(){
        // GIVEN
        when(mockDaoProxy.getComptabiliteDao().getSequenceEcritureComptableByYear("AC",2020))
                .thenReturn(new SequenceEcritureComptable(2020,15));

        // WHEN
        classUnderTest.addReference(ecritureComptable);
        String[] reference = ecritureComptable.getReference().split("[-/]");
        String sequenceNumber = reference[2];

        // THEN
        assertThat(sequenceNumber).isEqualTo("00016");
    }

}
