/**
 * Copyright (c) 2013-2016, The SeedStack authors <http://seedstack.org>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
/*
 * Creation : 30 juil. 2013
 */
package org.seedstack.smoke.tests.jpa;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.seedstack.business.domain.Factory;
import org.seedstack.business.domain.Repository;
import org.seedstack.jpa.Jpa;
import org.seedstack.jpa.JpaUnit;
import org.seedstack.seed.it.ITBind;
import org.seedstack.seed.transaction.Transactional;
import org.seedstack.smoke.SmokeTestRunner;
import org.seedstack.smoke.fixtures.jpa.AutoConfiguredEntity;

import javax.inject.Inject;

@ITBind
@RunWith(SmokeTestRunner.class)
public class AutoConfiguredJpaIT {
    @Inject
    @Jpa
    private Repository<AutoConfiguredEntity, Long> clientRepository;
    @Inject
    private Factory<AutoConfiguredEntity> clientFactory;

    private AutoConfiguredEntity createClient() {
        final long id = 1L;
        AutoConfiguredEntity autoConfiguredEntity = clientFactory.create(id);
        Assertions.assertThat(autoConfiguredEntity.getEntityId()).isEqualTo(id);
        clientRepository.persist(autoConfiguredEntity);
        Assertions.assertThat(autoConfiguredEntity.getEntityId()).isEqualTo(id);
        return autoConfiguredEntity;
    }

    @Test
    @Transactional
    @JpaUnit("auto-unit")
    public void testBasicOperation() {
        AutoConfiguredEntity autoConfiguredEntity = createClient();
        clientRepository.delete(autoConfiguredEntity);
        autoConfiguredEntity = clientRepository.load(autoConfiguredEntity.getEntityId());
        Assertions.assertThat(autoConfiguredEntity).isNull();
    }
}
