/*
 * Copyright 2019, Verizon Media.
 * Licensed under the Apache License, Version 2.0
 * See LICENSE file in project root for terms.
 */
package com.usermind.elidetest.models;

import com.yahoo.elide.annotation.Include;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.util.Date;

@Include(type = "version")
@Entity
public class ArtifactVersion {
    @Id
    private String name = "";

    private Date createdAt = new Date();

    @ManyToOne
    private ArtifactProduct artifact;
}
