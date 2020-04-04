package com.gig.lookBook.admin.controller.setting;

import com.gig.lookBook.core.dto.ApiResultDto;
import com.gig.lookBook.core.dto.MenuDto;
import com.gig.lookBook.core.dto.RoleDto;
import com.gig.lookBook.core.exception.InvalidRequiredParameter;
import com.gig.lookBook.core.exception.NotFoundException;
import com.gig.lookBook.core.model.Menu;
import com.gig.lookBook.core.model.Role;
import com.gig.lookBook.core.model.types.MenuType;
import com.gig.lookBook.core.security.component.UrlCache;
import com.gig.lookBook.core.service.MenuService;
import com.gig.lookBook.core.service.RoleService;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author Jake
 * @date: 20/04/04
 */
@Controller
@RequestMapping("/setting/menu-manager")
@Slf4j
public class MenuManagerController {

    private final String DEFAULT_AC_ROLE = "ROLE_ADMIN";
    private final String DEFAULT_FE_ROLE = "ROLE_ANONYMOUS";

    @Autowired
    MenuService menuService;

    @Autowired
    RoleService roleService;

    @Autowired
    UrlCache urlCache;

    @RequestMapping
    public ModelAndView index() {
        ModelAndView mav = new ModelAndView("setting/menu-manager");

        List<MenuDto> acMenus = menuService.getAllMenuHierarchy(MenuType.AdminConsole);
        List<MenuDto> feMenus = menuService.getAllMenuHierarchy(MenuType.FrontEnd);
        List<Role> roles = roleService.roleList();
        mav.addObject("acMenus", acMenus);//admin console
        mav.addObject("feMenus", feMenus);//front end
        mav.addObject("roles", roles);

        return mav;
    }

    @RequestMapping("ajax/menu/{id}")
    @ResponseBody
    public ApiResultDto<MenuDto> getAjaxMenu(@PathVariable(name = "id") Long id) {
        try {
            Menu menu = menuService.getMenu(id);
            MenuDto dto = new MenuDto(menu);
            if (menu.getParent() != null) {
                dto.setParent(new MenuDto(menu.getParent()));
            }
            ApiResultDto<MenuDto> result = new ApiResultDto<>(ApiResultDto.RESULT_CODE_OK);
            result.setData(dto);
            return result;
        } catch (NotFoundException e) {
            ApiResultDto<MenuDto> result = new ApiResultDto<>(ApiResultDto.RESULT_CODE_NOT_FOUND);
            result.setMessage(e.getMessage());
            return result;
        }
    }

    /**
     * 메뉴정보 저장
     *
     * @param dto
     * @return
     */
    @RequestMapping("ajax/menu/save")
    @ResponseBody
    public ApiResultDto<MenuDto> saveMenu(@RequestBody MenuDto dto) {
        ApiResultDto<MenuDto> result = new ApiResultDto<>(ApiResultDto.RESULT_CODE_OK);
        try {

            //필수값 검사
            if (Strings.isEmpty(dto.getMenuName()) || Strings.isEmpty(dto.getUrl())) {
                result.setCode(ApiResultDto.RESULT_CODE_ERROR);
                result.setMessage("Missing Required Value.");
                return result;
            }


            /**
             * 기본 롤 설정
             */
            String defaultRole = dto.getMenuType() == MenuType.AdminConsole ? DEFAULT_AC_ROLE : DEFAULT_FE_ROLE;
            if (dto.getRoles().size() > 0) {
                defaultRole = dto.getRoles().get(0).getRoleName();
            }

            Menu menu = menuService.createOrModify(dto, dto.getParent(), defaultRole);
            assert menu != null && menu.getId() != null;
            Set<RoleDto> roles = new HashSet<>(dto.getRoles());
            if (roles.size() == 0) {
                roles.add(new RoleDto(defaultRole));
            }

            /**
             * 롤 반영
             */
            menuService.allChangeMenuRole(menu.getId(), new ArrayList<>(roles));
            urlCache.reload(dto.getMenuType());
        } catch (NotFoundException | InvalidRequiredParameter e) {
            log.error(e.getMessage());
            result.setCode(ApiResultDto.RESULT_CODE_ERROR);
        }
        return result;
    }

    @PostMapping("ajax/menu/reload")
    @ResponseBody
    public ApiResultDto<MenuDto> reload() {
        urlCache.reload(MenuType.AdminConsole);
        urlCache.reload(MenuType.FrontEnd);

        return new ApiResultDto<>(ApiResultDto.RESULT_CODE_OK);
    }
}
