/**
 * Copyright (c) 2013-2016, The SeedStack authors <http://seedstack.org>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.seedstack.smoke.fixtures.jpa;


import org.seedstack.business.domain.BaseAggregateRoot;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class ManuallyConfiguredEntity extends BaseAggregateRoot<Long> {
    @Id
    private Long entityId;
    private String designation;
    @Column(length = 500)
    private String summary;
    @Column(length = 10000)
    private String details;
    private String picture;
    private Double price;

    protected ManuallyConfiguredEntity() {
    }

    public ManuallyConfiguredEntity(Long entityId) {
        super();
        this.entityId = entityId;
    }

    @Override
    public Long getEntityId() {
        return entityId;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

}
