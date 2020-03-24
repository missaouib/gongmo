package com.gig.lookBook.core.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Getter
@Setter
public class Privilege implements Serializable {
    @Id
    @Column(length = 20)
    private String privilege;

    @Column
    private String description;
}
