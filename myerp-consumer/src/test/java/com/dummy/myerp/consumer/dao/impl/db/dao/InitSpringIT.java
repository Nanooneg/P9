package com.dummy.myerp.consumer.dao.impl.db.dao;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertNotNull;


/**
 * Classe de test de l'initialisation du contexte Spring
 */
@ExtendWith(SpringExtension.class)
@ContextConfiguration(locations = "/com/dummy/myerp/consumer/bootstrapContext.xml")
public class InitSpringIT extends ConsumerTestCase {

    /**
     * Constructeur.
     */
    public InitSpringIT() {
        super();
    }


    /**
     * Teste l'initialisation du contexte Spring
     */
    @Test
    public void testInit() {
        SpringRegistry.init();
        assertNotNull(SpringRegistry.getDaoProxy());
    }
}
