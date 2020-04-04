package com.gig.lookBook.core.security.component;

import com.gig.lookBook.core.model.Menu;
import com.gig.lookBook.core.model.types.AntMatcherType;
import com.gig.lookBook.core.model.types.MenuType;
import com.gig.lookBook.core.model.types.YNType;
import com.gig.lookBook.core.repository.MenuRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Jake
 * @date: 20/04/01
 */
@Component
public class UrlCache extends LinkedHashMap<RequestMatcher, Collection<ConfigAttribute>> {

    @Autowired
    private MenuRepository menuRepository;

//    /**
//     * URL에 접근 가능한 역할
//     */
//    private Map<RequestMatcher, Collection<ConfigAttribute>> requestMap = new LinkedHashMap<>();
//
//    public Map<RequestMatcher, Collection<ConfigAttribute>> getRequestMap() {
//        return requestMap;
//    }

    /**
     * 권한이 변경될경우 호출해야 함.
     */
    public void reload(MenuType menuType) {
        Sort sort = Sort.by(Sort.Order.asc("sortOrder"), Sort.Order.asc("antMatcherType"));
        List<Menu> menus = menuRepository.getTopMenusNotExistsRole("ROLE_ANONYMOUS", menuType, sort);
        this.clear();
        putData(menus);
    }

    private void putData(List<Menu> menus) {
        for (Menu m : menus) {
            String url;
//            if (m.getUrl().lastIndexOf("/") == m.getUrl().length() - 1) {
//                url = m.getUrl().substring(0, m.getUrl().length() - 1);
//            } else {
                url = m.getUrl();
//            }

            //하위 메뉴가 있을 경우는 무조건 싱글로 처리
            if (m.getChildren() != null && m.getChildren().size() > 0) {
                url += "";
            } else {
                url += (m.getAntMatcherType() != AntMatcherType.Single ? "/**" : "");
            }
            //삭제되거나 비활성화 된 메뉴에는 접근 불가 권한 넣기
            if (m.getActiveYn() == YNType.N) {
                this.put(new AntPathRequestMatcher(url), Collections.singletonList(new SecurityConfig("ROLE_NO_PERMIT")));
            } else {
                this.put(new AntPathRequestMatcher(url), m.getRoles().stream().map(r -> new SecurityConfig(r.getRoleName())).collect(Collectors.toList()));
            }
            if (m.getChildren() != null && m.getChildren().size() > 0) {
                putData(m.getChildren());
            }
        }
    }


}
