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
import org.seedstack.smoke.fixtures.jpa.ManuallyConfiguredEntity;

import javax.inject.Inject;

@ITBind
@RunWith(SmokeTestRunner.class)
public class ManuallyConfiguredIT {
    @Inject
    @Jpa
    private Repository<ManuallyConfiguredEntity, Long> productRepository;
    @Inject
    private Factory<ManuallyConfiguredEntity> productFactory;

    private ManuallyConfiguredEntity createProduct() {
        final long id = 1L;
        ManuallyConfiguredEntity manuallyConfiguredEntity = productFactory.create(id);
        Assertions.assertThat(manuallyConfiguredEntity.getEntityId()).isEqualTo(id);
        productRepository.persist(manuallyConfiguredEntity);
        Assertions.assertThat(manuallyConfiguredEntity.getEntityId()).isEqualTo(id);
        return manuallyConfiguredEntity;
    }

    @Test
    @Transactional
    @JpaUnit("manual-unit")
    public void testBasicOperation() {
        ManuallyConfiguredEntity manuallyConfiguredEntity = createProduct();
        productRepository.delete(manuallyConfiguredEntity);
        manuallyConfiguredEntity = productRepository.load(manuallyConfiguredEntity.getEntityId());
        Assertions.assertThat(manuallyConfiguredEntity).isNull();
    }
}
