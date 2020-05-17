package com.gig.lookBook.core.dto;

import com.gig.lookBook.core.model.Menu;
import com.gig.lookBook.core.model.Role;
import com.gig.lookBook.core.model.types.AntMatcherType;
import com.gig.lookBook.core.model.types.MenuType;
import com.gig.lookBook.core.model.types.YNType;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Jake
 * @date: 20/04/01
 */
@Data
@NoArgsConstructor
public class MenuDto {
    private Long id;
    private String menuName;
    private String url;
    private String iconClass;
    private YNType activeYn;
    private YNType displayYn;
    private int sortOrder;
    private AntMatcherType antMatcherType;
    private MenuType menuType;
    private List<MenuDto> children = new ArrayList<>();
    private List<RoleDto> roles = new ArrayList<>();
    /**
     * parent, children 동시에 넣을 경우 json생성시 Bidirectional Issue가 발생
     * 특별한 이슈가 없을 경우 parent는 사용을 자제 한다.
     */
    @Deprecated
    private MenuDto parent;


    public MenuDto(Menu menu) {
        this.id = menu.getId();
        this.menuName = menu.getMenuName();
        this.activeYn = menu.getActiveYn();
        this.displayYn = menu.getDisplayYn();
        this.sortOrder = menu.getSortOrder();
        this.url = menu.getUrl();
        this.iconClass = menu.getIconClass();
        this.antMatcherType = menu.getAntMatcherType();
        this.roles = menu.getRoles().stream().map(RoleDto::new).collect(Collectors.toList());
        this.menuType = menu.getMenuType();
    }

    public MenuDto(Menu menu, boolean makeChildren) {
        this(menu);
        if (makeChildren && menu.getChildren() != null) {
            for (Menu c : menu.getChildren()) {
                if (c.getDeleteYn() == YNType.N)
                    this.children.add(new MenuDto(c, true));
            }
        }
    }

    /**
     * @param menu
     * @param roles
     * @param checkActive 액티브 유무 확인
     */
    public MenuDto(Menu menu, Collection<Role> roles, boolean checkActive) {
        this(menu);
        if (menu.getChildren() != null) {
            for (Menu c : menu.getChildren()) {
                if (checkActive && c.getActiveYn() == YNType.N) continue;
                if (c.getDisplayYn() == YNType.N) continue;

                long cnt = roles.stream().filter(r -> c.getRoles().contains(r)).count();
                if (cnt > 0) {
                    this.children.add(new MenuDto(c, roles, checkActive));
                }
            }
        }
    }
}
