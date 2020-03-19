package com.dummy.myerp.testbusiness.business;

import com.dummy.myerp.business.impl.manager.ComptabiliteManagerImpl;
import com.dummy.myerp.model.bean.comptabilite.CompteComptable;
import com.dummy.myerp.model.bean.comptabilite.EcritureComptable;
import com.dummy.myerp.model.bean.comptabilite.JournalComptable;
import jdk.nashorn.internal.ir.annotations.Ignore;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Ignore
public class ComptabiliteManagerImplIT {

    private final ComptabiliteManagerImpl comptabiliteManagerUnderTest = new ComptabiliteManagerImpl();

    @Test
    final void getListCompteComptable() {
        // GIVEN
        List<CompteComptable> compteComptableList;

        // WHEN
        compteComptableList = comptabiliteManagerUnderTest.getListCompteComptable();

        // THEN
        assertThat(compteComptableList).isNot(null);
    }

    @Test
    void getListJournalComptable() {
        // GIVEN
        List<JournalComptable> journalComptableList;

        // WHEN
        journalComptableList = comptabiliteManagerUnderTest.getListJournalComptable();

        // THEN
        assertThat(journalComptableList).isNot(null);
    }

    @Test
    void getListEcritureComptable() {
        // GIVEN
        List<EcritureComptable> ecritureComptableList;

        // WHEN
        ecritureComptableList = comptabiliteManagerUnderTest.getListEcritureComptable();

        // THEN
        assertThat(ecritureComptableList).isNot(null);
    }

}
