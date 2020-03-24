package com.gig.lookBook.core.model;

import com.gig.lookBook.core.model.types.AntMatcherType;
import com.gig.lookBook.core.model.types.MenuType;
import com.gig.lookBook.core.model.types.YNType;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
public class Menu {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "MENU_SEQ")
    @SequenceGenerator(name = "MENU_SEQ", sequenceName = "MENU_SEQ", allocationSize = 1)
    private Long id;

    @Column
    private String menuName;
    @Column
    private String url;

    /**
     * 화면에 표시 여부
     */
    @Enumerated(EnumType.STRING)
    @Column(length = 1)
    private YNType displayYn = YNType.Y;

    /**
     * 사용여부
     */
    @Enumerated(EnumType.STRING)
    @Column(length = 1)
    private YNType activeYn = YNType.N;

    /**
     * 삭제 여부
     */
    @Enumerated(EnumType.STRING)
    @Column(length = 1)
    private YNType deleteYn = YNType.N;

    /**
     * 순서
     */
    @Column
    private int sortOrder;

    @Enumerated(EnumType.ORDINAL)
    @Column
    private AntMatcherType antMatcherType = AntMatcherType.Single;

    @Column(length = 60)
    private String iconClass;

    /**
     * 메뉴 종류
     */
    @Enumerated(EnumType.ORDINAL)
    @Column
    private MenuType menuType;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "PARENT_ID")
    private Menu parent;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("sortOrder ")
    @JoinColumn(name = "PARENT_ID")
    private List<Menu> children = new ArrayList<>();

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "LAST_MODIFIER_ID")
    private Account lastModifier;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "MENU_ROLE",
            joinColumns = @JoinColumn(name = "MENU_ID"),
            inverseJoinColumns = @JoinColumn(name = "ROLE")
    )
    private Set<Role> roles = new HashSet<>();

    /**
     * 하위 메뉴 추가
     *
     * @param menu 하위 메뉴
     */
    public void addChildren(Menu menu) {
        this.children.add(menu);
        //하위 메뉴는 상위 메뉴의 타입을 따라간다.
        menu.setMenuType(this.menuType);
        menu.setParent(this);
    }

    @NotNull
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @Column
    private LocalDateTime updatedAt;

    @PrePersist
    public void beforePersist() {
        LocalDateTime dateTime = LocalDateTime.now();
        this.createdAt = dateTime;
        this.updatedAt = dateTime;
    }

    @PreUpdate
    public void beforeUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

}
