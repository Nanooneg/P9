package com.dummy.myerp.testbusiness.business;

import com.dummy.myerp.business.impl.manager.ComptabiliteManagerImpl;
import com.dummy.myerp.model.bean.comptabilite.CompteComptable;
import com.dummy.myerp.model.bean.comptabilite.EcritureComptable;
import com.dummy.myerp.model.bean.comptabilite.JournalComptable;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(locations = "/com/dummy/myerp/business/applicationContext.xml")
public class ComptabiliteManagerImplIT {

    private final ComptabiliteManagerImpl comptabiliteManagerUnderTest = new ComptabiliteManagerImpl();

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
    void getListEcritureComptable() {
        // GIVEN
        List<EcritureComptable> ecritureComptableList;

        // WHEN
        ecritureComptableList = comptabiliteManagerUnderTest.getListEcritureComptable();

        // THEN
        assertThat(ecritureComptableList.size()).isEqualTo(5);
    }



}
