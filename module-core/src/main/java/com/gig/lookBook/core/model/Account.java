package com.gig.lookBook.core.model;

import com.gig.lookBook.core.model.types.YNType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Account {

    @Id
    @SequenceGenerator(name = "ACCOUNT_SEQ", sequenceName = "ACCOUNT_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ACCOUNT_SEQ")
    private Long id;

    @Column
    private String username;

    @Column
    private String password;

    @Column
    private String email;

    @Column
    private String name;

    @Column
    private LocalDateTime passwordModifyAt = LocalDateTime.now();

    @Column
    private int passwordFailCnt = 0;

    @Column
    private LocalDateTime passwordFailTime;

    @Column
    private LocalDateTime lastLoginAt;

    @Column
    private String loginIp;

    @Enumerated(EnumType.STRING)
    @Column(length = 1)
    private YNType activeYn = YNType.Y;

    @Enumerated(EnumType.STRING)
    @Column(length = 1)
    private YNType dormancyYn = YNType.N;

    @NotNull
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @Column
    private LocalDateTime updatedAt;

}
