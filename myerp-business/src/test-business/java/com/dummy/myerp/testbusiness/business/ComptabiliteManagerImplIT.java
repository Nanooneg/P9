package com.dummy.myerp.testbusiness.business;

import com.dummy.myerp.business.impl.manager.ComptabiliteManagerImpl;
import com.dummy.myerp.model.bean.comptabilite.CompteComptable;
import com.dummy.myerp.model.bean.comptabilite.EcritureComptable;
import com.dummy.myerp.model.bean.comptabilite.JournalComptable;
import com.dummy.myerp.model.bean.comptabilite.LigneEcritureComptable;
import com.dummy.myerp.technical.exception.FunctionalException;
import com.dummy.myerp.technical.exception.NotFoundException;
import org.apache.commons.lang3.ObjectUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional(propagation = Propagation.REQUIRES_NEW)
@ExtendWith(SpringExtension.class)
@ContextConfiguration(locations = "/com/dummy/myerp/business/applicationContext.xml")
@Sql(scripts = {"classpath:/com/dummy/myerp/business/01_create_schema.sql",
        "classpath:/com/dummy/myerp/business/02_create_tables.sql",
        "classpath:/com/dummy/myerp/business/21_insert_data_demo.sql" })
public class ComptabiliteManagerImplIT{

    ComptabiliteManagerImpl comptabiliteManagerUnderTest;
    EcritureComptable ecritureComptable;

    @BeforeEach
    public void init(){
        comptabiliteManagerUnderTest = new ComptabiliteManagerImpl();
        ecritureComptable = new EcritureComptable();
        ecritureComptable.setJournal(new JournalComptable("BQ", "Banque"));
        ecritureComptable.setDate(new Date());
        ecritureComptable.setLibelle("Libelle");
        ecritureComptable.setReference("BQ-2020/00001");
        ecritureComptable.getListLigneEcriture().add(this.createLigne(401, "200.50", null));
        ecritureComptable.getListLigneEcriture().add(this.createLigne(401, "100.50", "33"));
        ecritureComptable.getListLigneEcriture().add(this.createLigne(411, null, "301"));
        ecritureComptable.getListLigneEcriture().add(this.createLigne(411, "40", "7"));
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
        // GIVEN
        List<CompteComptable> compteComptableList;

        // WHEN
        compteComptableList = comptabiliteManagerUnderTest.getListCompteComptable();

        // THEN
        assertThat(compteComptableList.size()).isEqualTo(7);
    }

    @Test
    void getListJournalComptable() {
        // GIVEN
        List<JournalComptable> journalComptableList;

        // WHEN
        journalComptableList = comptabiliteManagerUnderTest.getListJournalComptable();

        // THEN
        assertThat(journalComptableList.size()).isEqualTo(4);
    }

    @Test
    void getListEcritureComptable_insertEcritureComptable_updateEcritureComptable_deleteEcritureComptable() throws FunctionalException {
        // GIVEN
        List<EcritureComptable> ecritureComptableList;
        EcritureComptable ecritureComptableFromDb;

        // WHEN
        ecritureComptableList = comptabiliteManagerUnderTest.getListEcritureComptable();

        // THEN
        assertThat(ecritureComptableList.size()).isEqualTo(5);

        // WHEN
        comptabiliteManagerUnderTest.insertEcritureComptable(ecritureComptable);
        ecritureComptableList = comptabiliteManagerUnderTest.getListEcritureComptable();

        // THEN
        assertThat(ecritureComptableList.size()).isEqualTo(6);

        // WHEN
        ecritureComptableFromDb = ecritureComptableList.get(0);
        String oldLibelle = ecritureComptableFromDb.getLibelle();
        String newLibelle = "NewLibelle";
        int ecritureComptableFromDbID = ecritureComptableFromDb.getId();
        ecritureComptableFromDb.setLibelle(newLibelle);
        ecritureComptableFromDb.setReference("BQ-2020/00011");
        comptabiliteManagerUnderTest.updateEcritureComptable(ecritureComptableFromDb);
        ecritureComptableList = comptabiliteManagerUnderTest.getListEcritureComptable();
        for (EcritureComptable e : ecritureComptableList) {
            if (e.getId() == ecritureComptableFromDbID)
                ecritureComptableFromDb = e;
        }

        // THEN
        assertThat(oldLibelle).isNotEqualTo(newLibelle);
        assertThat(ecritureComptableFromDb.getLibelle()).isEqualTo(newLibelle);
        assertThat(ecritureComptableFromDb.getReference()).isEqualTo("BQ-2020/00011");
        assertThat(ecritureComptableList.size()).isEqualTo(6);

        // WHEN
        comptabiliteManagerUnderTest.deleteEcritureComptable(ecritureComptableFromDbID);
        ecritureComptableList = comptabiliteManagerUnderTest.getListEcritureComptable();
        EcritureComptable deletedEcritureComptable = null;
        for (EcritureComptable e : ecritureComptableList) {
            if (e.getId() == ecritureComptableFromDbID)
                deletedEcritureComptable = e;
        }

        // THEN
        assertThat(ecritureComptableList.size()).isEqualTo(5);
        assertThat(deletedEcritureComptable).isEqualTo(null);

    }

    @Test
    public void addReferenceFirstEcritureComptableOfTheYear() throws NotFoundException {
        // GIVEN
        ecritureComptable.setReference(null);


        // WHEN
        comptabiliteManagerUnderTest.addReference(ecritureComptable);

        // THEN
        assertThat(ecritureComptable.getReference()).isEqualTo("BQ-2020/00001");
    }

    @Test
    public void addReferenceNotFirstEcritureComptableOfTheYear() throws NotFoundException {
        // GIVEN
        ecritureComptable.setReference(null);
        ecritureComptable.setJournal(new JournalComptable("AC", "Achat"));


        // WHEN
        comptabiliteManagerUnderTest.addReference(ecritureComptable);

        // THEN
        assertThat(ecritureComptable.getReference()).isEqualTo("AC-2020/00041");
    }

    // TODO test new sequenceEcritureComptable is well insert in DB after implement that feature obv !!!

}
