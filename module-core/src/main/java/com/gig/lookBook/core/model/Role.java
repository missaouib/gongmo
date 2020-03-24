package com.gig.lookBook.core.model;

import com.gig.lookBook.core.model.types.YNType;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * 역할
 */
@Entity
@Getter
@Setter
public class Role implements Serializable {
    @Id
    @Column(length = 20)
    private String roleName;

    @Column
    private String description;

    @Column
    private int sortOrder;

    /**
     * 삭제 가능 여부
     */
    @Enumerated(EnumType.STRING)
    @Column(length = 1)
    private YNType defaultYn = YNType.N;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "ROLE_PRIVILEGE",
            joinColumns = @JoinColumn(name = "ROLE"),
            inverseJoinColumns = @JoinColumn(name = "PRIVILEGE")
    )
    private Set<Privilege> privileges = new HashSet<>();


    @ManyToMany(mappedBy = "roles")
    private Set<Menu> menus = new HashSet<>();
}
