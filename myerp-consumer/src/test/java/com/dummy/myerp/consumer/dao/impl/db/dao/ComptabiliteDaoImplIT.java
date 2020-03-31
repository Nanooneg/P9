package com.dummy.myerp.consumer.dao.impl.db.dao;

import com.dummy.myerp.model.bean.comptabilite.*;
import com.dummy.myerp.technical.exception.NotFoundException;
import org.apache.commons.lang3.ObjectUtils;
import org.junit.jupiter.api.BeforeAll;
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
@ContextConfiguration(locations = "/com/dummy/myerp/consumer/bootstrapContext.xml")
@Sql(scripts = {"classpath:/com/dummy/myerp/consumer/01_create_schema.sql",
                "classpath:/com/dummy/myerp/consumer/02_create_tables.sql",
                "classpath:/com/dummy/myerp/consumer/21_insert_data_demo.sql" })
public class ComptabiliteDaoImplIT {

    static ComptabiliteDaoImpl comptabiliteDaoUnderTest;
    EcritureComptable ecritureComptable;

    @BeforeAll
    public static void testSetupBeforeAll() {
        comptabiliteDaoUnderTest = ComptabiliteDaoImpl.getInstance();
    }

    @BeforeEach
    public void init(){
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
    public void getListCompteComptable(){
        List<CompteComptable> compteComptableList = comptabiliteDaoUnderTest.getListCompteComptable();
        assertThat(compteComptableList.size()).isEqualTo(7);
    }

    @Test
    void getListJournalComptable() {
        List<JournalComptable> journalComptableList = comptabiliteDaoUnderTest.getListJournalComptable();
        assertThat(journalComptableList.size()).isEqualTo(4);
    }

    @Test
    void getListEcritureComptable_deleteEcrireComptable() {
        List<EcritureComptable> ecritureComptableList = comptabiliteDaoUnderTest.getListEcritureComptable();
        assertThat(ecritureComptableList.size()).isEqualTo(5);
        comptabiliteDaoUnderTest.deleteEcritureComptable(ecritureComptableList.get(0).getId());
        ecritureComptableList = comptabiliteDaoUnderTest.getListEcritureComptable();
        assertThat(ecritureComptableList.size()).isEqualTo(4);
    }

    @Test
    void getEcritureComptable() throws NotFoundException {
        EcritureComptable ecritureComptableFromDB = comptabiliteDaoUnderTest.getEcritureComptable(-1);
        assertThat(ecritureComptableFromDB.getLibelle()).isEqualTo("Cartouches d’imprimante");
    }

    @Test
    void insertEcritureComptable_getEcritureComptableByRef() throws NotFoundException {
        comptabiliteDaoUnderTest.insertEcritureComptable(ecritureComptable);
        EcritureComptable ecritureComptableFromDB = comptabiliteDaoUnderTest.getEcritureComptableByRef("BQ-2020/00001");
        assertThat(ecritureComptableFromDB.getLibelle()).isEqualTo("Libelle");
    }

    @Test
    void updateEcritureComptable() throws NotFoundException {
        EcritureComptable ecritureComptableFromDB = comptabiliteDaoUnderTest.getEcritureComptable(-1);
        ecritureComptableFromDB.setLibelle("Nouveau libellé");
        comptabiliteDaoUnderTest.updateEcritureComptable(ecritureComptableFromDB);
        ecritureComptableFromDB = comptabiliteDaoUnderTest.getEcritureComptable(-1);
        assertThat(ecritureComptableFromDB.getLibelle()).isEqualTo("Nouveau libellé");
    }

    @Test
    void insertSequenceEcritureComptable_getSequenceEcritureComptable_updateSequenceEcritureComptable() throws NotFoundException {
        SequenceEcritureComptable sequenceEcritureComptable = new SequenceEcritureComptable(2020,1);
        comptabiliteDaoUnderTest.insertSequenceEcritureComptable(sequenceEcritureComptable,"BQ");
        SequenceEcritureComptable sequenceEcritureComptableFromDB = comptabiliteDaoUnderTest.getSequenceEcritureComptable("BQ",2020);
        assertThat(sequenceEcritureComptableFromDB.getDerniereValeur()).isEqualTo(1);
        sequenceEcritureComptableFromDB.setDerniereValeur(2);
        comptabiliteDaoUnderTest.updateSequenceEcritureComptable(sequenceEcritureComptableFromDB,"BQ");
        SequenceEcritureComptable sequenceEcritureComptableFromDB2 = comptabiliteDaoUnderTest.getSequenceEcritureComptable("BQ",2020);
        assertThat(sequenceEcritureComptableFromDB2.getDerniereValeur()).isEqualTo(2);
    }


}
