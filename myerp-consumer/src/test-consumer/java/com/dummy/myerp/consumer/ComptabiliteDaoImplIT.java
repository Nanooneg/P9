package com.dummy.myerp.consumer;

import com.dummy.myerp.consumer.dao.impl.db.dao.ComptabiliteDaoImpl;
import com.dummy.myerp.consumer.db.AbstractDbConsumer;
import com.dummy.myerp.model.bean.comptabilite.CompteComptable;
import com.dummy.myerp.model.bean.comptabilite.EcritureComptable;
import com.dummy.myerp.model.bean.comptabilite.JournalComptable;
import com.dummy.myerp.model.bean.comptabilite.LigneEcritureComptable;
import org.apache.commons.lang3.ObjectUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional(propagation = Propagation.REQUIRES_NEW)
@ExtendWith(SpringExtension.class)
@ContextConfiguration(locations = "/com/dummy/myerp/consumer/applicationContext.xml")
public class ComptabiliteDaoImplIT extends AbstractDbConsumer{


    /*static ComptabiliteDaoImpl comptabiliteDaoUnderTest;
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
        // GIVEN
        List<CompteComptable> compteComptableList;

        // WHEN
        compteComptableList = comptabiliteDaoUnderTest.getListCompteComptable();

        // THEN
        assertThat(compteComptableList.size()).isEqualTo(7);
    }*/

}
