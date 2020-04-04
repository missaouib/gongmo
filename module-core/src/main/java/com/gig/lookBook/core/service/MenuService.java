package com.gig.lookBook.core.service;

import com.gig.lookBook.core.dto.MenuDto;
import com.gig.lookBook.core.dto.RoleDto;
import com.gig.lookBook.core.exception.InvalidRequiredParameter;
import com.gig.lookBook.core.exception.NotFoundException;
import com.gig.lookBook.core.model.Menu;
import com.gig.lookBook.core.model.Role;
import com.gig.lookBook.core.model.types.AntMatcherType;
import com.gig.lookBook.core.model.types.MenuType;
import com.gig.lookBook.core.model.types.ModifyType;
import com.gig.lookBook.core.model.types.YNType;
import com.gig.lookBook.core.repository.MenuRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class MenuService {


    @Autowired
    private MenuRepository menuRepository;

    @Autowired
    private RoleService roleService;

    @Autowired
    private SecurityService securityService;



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


    /**
     * 메뉴 계층구조 갖고 오기
     *
     * @return
     */
    public List<MenuDto> getAllMenuHierarchy(MenuType menuType) {
        List<Menu> topMenus = menuRepository.findByMenuTypeAndDeleteYnAndParentIsNull(menuType, YNType.N,
                new Sort(Sort.Direction.ASC, "sortOrder", "id"));
        List<MenuDto> hierarchy = new ArrayList<>();
        for (Menu m : topMenus) {
            hierarchy.add(new MenuDto(m, true));
        }

        return hierarchy;
    }


    /**
     * 하나의 메뉴 갖고 오기
     *
     * @param menuId 메뉴 id
     * @return
     */
    public Menu getMenu(long menuId) throws NotFoundException {
        Optional<Menu> optMenu = menuRepository.findById(menuId);
        if (optMenu.isPresent())
            return optMenu.get();
        else
            throw new NotFoundException("Menu not found", NotFoundException.MENU_NOT_FOUND);
    }


    /**
     * 메뉴 수정 및 생성
     * menu.id가 null일 경우 새로 생성하고, root에 등록이됨.
     * 그리고 기본 권한이 없기때문에 접근이 불가능함.
     *
     * @param menuDto 메뉴
     * @return 결과
     * @throws NotFoundException
     * @throws InvalidRequiredParameter
     */
    public Menu createAndUpdateMenu(MenuDto menuDto) throws NotFoundException, InvalidRequiredParameter {
        return createOrModify(menuDto, null, null);
    }

    /**
     * 메뉴 수정 및 생성
     * menu.id가 null일 경우 새로 생성하고, root에 등록이됨.
     * 그리고 기본 권한이 없기때문에 접근이 불가능함.
     *
     * @param dto  메뉴
     * @param pDto 부모 메뉴
     * @return 결과
     * @throws NotFoundException
     * @throws InvalidRequiredParameter
     */
    public Menu createAndUpdateMenu(MenuDto dto, MenuDto pDto) throws NotFoundException, InvalidRequiredParameter {
        return createOrModify(dto, pDto, null);
    }

    /**
     * 메뉴 생성 / 수정
     *
     * @param dto         메뉴정보
     * @param pDto        부모 메뉴 정보
     * @param defaultRole 최 상단 메뉴에서의 기본 역할
     * @return 결과
     * @throws NotFoundException
     * @throws InvalidRequiredParameter 부모메뉴가 없을경우(최상위 메뉴일 경우) 기본 역할은 필수값입니다.
     */
    @Transactional
    public Menu createOrModify(MenuDto dto, MenuDto pDto, String defaultRole) throws NotFoundException, InvalidRequiredParameter {

        /*
         * crate menu이고, 부모가 없고, 기본 권한이 없을경우 오류발생
         */
        if (dto.getId() == null && pDto == null && defaultRole == null) {
            throw new InvalidRequiredParameter();
        }

        Optional<Menu> optMenu;
        Menu menu;
        Menu parent = null;
        if (dto.getId() != null) {
            optMenu = menuRepository.findById(dto.getId());
            if (optMenu.isEmpty())
                throw new NotFoundException(dto.getMenuName() + " Not found: " + dto.getId(), NotFoundException.MENU_NOT_FOUND);
            menu = optMenu.get();
        } else {
            menu = new Menu();
            //부모 메뉴가 있는지 여부
            if (pDto != null && pDto.getId() != null) {
                Optional<Menu> optParent = menuRepository.findById(pDto.getId());
                if (optParent.isEmpty())
                    throw new NotFoundException("Parent Menu Not found: " + pDto.getId(), NotFoundException.PARENT_MENU_NOT_FOUND);
                parent = optParent.get();
                parent.addChildren(menu);
            }
        }
        menu.setIconClass(dto.getIconClass());
        menu.setMenuName(dto.getMenuName());
        menu.setActiveYn(dto.getActiveYn());
        menu.setDisplayYn(dto.getDisplayYn());
        menu.setLastModifier(securityService.getLoginUser());
        menu.setUrl(dto.getUrl());
        menu.setMenuType(dto.getMenuType());

        if (menu.getChildren().size() > 0) {
            /*
             * Security 등록 방법으로 인해
             * children 메뉴가 있을 경우 antMatcher를 무조건 single로 등록되도록 해야함.
             */
            menu.setAntMatcherType(AntMatcherType.Single);
        } else {
            menu.setAntMatcherType(dto.getAntMatcherType());
        }
        /*
          최 상단 메뉴 추가시 Default Role 추가
         */
        if (menu.getRoles().size() == 0 && menu.getParent() == null && defaultRole != null) {
            Role role = roleService.findByRoleId(defaultRole);
            menu.getRoles().add(role);
            role.getMenus().add(menu);
        }

        menuRepository.save(menu);

        if (parent != null) {
            /*
              Security 등록 방법으로 인해
              children 메뉴가 있을 경우 antMatcher를 무조건 single로 등록되도록 해야함.
             */
            parent.setAntMatcherType(AntMatcherType.Single);
            menuRepository.save(parent);
        }

        return menu;
    }

    /**
     * 기존 메뉴의 롤을 클리어 하고 신규로 롤 지정
     *
     * @param menuId
     * @param roles
     */
    @Transactional
    public void allChangeMenuRole(long menuId, List<RoleDto> roles) throws NotFoundException {
        Menu menu = getMenu(menuId);
        menu.setRoles(new HashSet<>());
//        logService.menuRoleClearLog(menu);

        for (RoleDto dto : roles) {
            Role role = roleService.findByRoleId(dto.getRoleName());
            allModifyChildrenMenu(menu, role, ModifyType.Register);
        }

        menuRepository.save(menu);
    }

    /**
     * 모든 자식 메뉴에 권한 설정
     *
     * @param menu       메뉴
     * @param role       권한
     * @param modifyType 등록/삭제
     */
    @Transactional
    protected void allModifyChildrenMenu(Menu menu, Role role, ModifyType modifyType) throws NotFoundException {
        if (modifyType == ModifyType.Register) {
            menu.getRoles().add(role);
            role.getMenus().add(menu);
        } else {
            menu.getRoles().remove(role);
            role.getMenus().remove(menu);
        }
        for (Menu m : menu.getChildren()) {
            allModifyChildrenMenu(m, role, modifyType);
        }
//        logService.saveMenuRoleLog(menu, role, modifyType);
    }

}
