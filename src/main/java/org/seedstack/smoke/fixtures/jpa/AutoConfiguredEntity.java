/**
 * Copyright (c) 2013-2016, The SeedStack authors <http://seedstack.org>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.seedstack.smoke.fixtures.jpa;

import org.seedstack.business.domain.BaseAggregateRoot;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class AutoConfiguredEntity extends BaseAggregateRoot<Long> {
    @Id
    private Long clientId;
    private String name;

    protected AutoConfiguredEntity() {
    }

    public AutoConfiguredEntity(Long clientId) {
        this.clientId = clientId;
    }

    @Override
    public Long getEntityId() {
        return clientId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
