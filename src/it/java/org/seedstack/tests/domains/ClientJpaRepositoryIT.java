/**
 * Copyright (c) 2013-2015 by The SeedStack authors. All rights reserved.
 *
 * This file is part of SeedStack, An enterprise-oriented full development stack.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
/*
 * Creation : 30 juil. 2013
 */
package org.seedstack.tests.domains;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.seedstack.business.api.domain.Factory;
import org.seedstack.business.api.domain.Repository;
import org.seedstack.seed.core.api.Logging;
import org.seedstack.seed.it.SeedITRunner;
import org.seedstack.seed.persistence.jpa.api.JpaUnit;
import org.seedstack.seed.transaction.api.Transactional;
import org.seedstack.tests.domains.client.Client;
import org.seedstack.tests.domains.product.Product;
import org.slf4j.Logger;

import javax.inject.Inject;

@RunWith(SeedITRunner.class)
public class ClientJpaRepositoryIT {
    @Inject
    private Repository<Client, Long> clientRepository;
    @Inject
    private Factory<Client> clientFactory;

    @Logging
    private static Logger logger;

    private Client createClient() {
        final long id = 1L;
        Client client = clientFactory.create(id);
        Assertions.assertThat(client.getEntityId()).isEqualTo(id);
        clientRepository.persist(client);;
        Assertions.assertThat(client.getEntityId()).isEqualTo(id);
        return client;
    }

    @Test
    @Transactional
    @JpaUnit("client-domain")
    public void checkProductResource() {
        Client client = createClient();
        clientRepository.delete(client);;
        client = clientRepository.load(client.getEntityId());
        Assertions.assertThat(client).isNull();
    }
}
