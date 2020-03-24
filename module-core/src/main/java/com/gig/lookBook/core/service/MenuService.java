package com.gig.lookBook.core.service;

import com.gig.lookBook.core.dto.MenuDto;
import com.gig.lookBook.core.model.Menu;
import com.gig.lookBook.core.model.Role;
import com.gig.lookBook.core.model.types.MenuType;
import com.gig.lookBook.core.model.types.YNType;
import com.gig.lookBook.core.repository.MenuRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MenuService {


    @Autowired
    private MenuRepository menuRepository;



    /**
     * 권한 있는 메뉴 갖고 오기
     *
     * @param roles
     * @param includeNonActive 숨김 메뉴 표시 여부
     * @return
     * @throws
     */
    public List<MenuDto> getMenuHierarchyByRoles(MenuType menuType, Collection<Role> roles, boolean includeNonActive) {
//        List<Menu> topMenus = menuRepository.findDistinctByParentIsNullAndMenuTypeAndDeleteYnAndActiveYnAndRolesIn(
//                menuType, YNType.N, YNType.Y, roles,
//                new Sort(Sort.Direction.ASC, "sortOrder", "id"));


        List<Menu> topMenus = menuRepository.getTopMenu(menuType, roles.stream().map(Role::getRoleName).collect(Collectors.toList()), new Sort(Sort.Direction.ASC, "sortOrder", "id"));
        List<MenuDto> hierarchy = new ArrayList<>();
        for (Menu m : topMenus) {
            if (!includeNonActive && m.getActiveYn() == YNType.N) continue;
            hierarchy.add(new MenuDto(m, roles, true));
        }


        return hierarchy;
    }

}
