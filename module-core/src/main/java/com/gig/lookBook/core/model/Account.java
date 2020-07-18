package com.gig.lookBook.core.model;

import com.gig.lookBook.core.model.types.YNType;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Account {

    @Id
    @SequenceGenerator(name = "ACCOUNT_SEQ", sequenceName = "ACCOUNT_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ACCOUNT_SEQ")
    private Long id;

    @Column(unique = true)
    private String email;

    @Column(unique = true)
    private String username;

    private String password;

    private LocalDateTime passwordModifyAt = LocalDateTime.now();

    private int passwordFailCnt = 0;

    private LocalDateTime passwordFailTime;

    private String name;

    private boolean emailVerified;

    private String emailCheckToken;

    private String bio;

    private String url;

    private String occupation;

    private String location;

    @Lob @Basic(fetch = FetchType.EAGER)
    private String profileImage;

    private LocalDateTime lastLoginAt;

    private String loginIp;

    @Enumerated(EnumType.STRING)
    @Column(length = 1)
    private YNType activeYn = YNType.Y;

    @Enumerated(EnumType.STRING)
    @Column(length = 1)
    private YNType dormancyYn = YNType.N;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "ACCOUNT_ROLE",
            joinColumns = @JoinColumn(name = "ACCOUNT_ID"),
            inverseJoinColumns = @JoinColumn(name = "ROLE")
    )
    private Set<Role> roles = new HashSet<>();

    public void addRole(Role role) {
        this.roles.add(role);
    }

    public void removeRole(Role role) {
        this.roles.remove(role);
    }

    @NotNull
    @Column(updatable = false)
    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

}
