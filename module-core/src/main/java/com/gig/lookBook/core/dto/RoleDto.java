package com.gig.lookBook.core.dto;

import com.gig.lookBook.core.model.Role;
import com.gig.lookBook.core.model.types.YNType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Jake
 * @date: 20/04/01
 */
@Getter
@Setter
@NoArgsConstructor
public class RoleDto {
    private String roleName;
    private String description;
    /**
     * 삭제 가능 여부
     */
    private YNType defaultYn;
    private List<PrivilegeDto> privileges;

    public RoleDto(Role r) {
        this.roleName = r.getRoleName();
        this.description = r.getDescription();
        this.defaultYn = r.getDefaultYn();
        this.privileges = r.getPrivileges().stream().map(PrivilegeDto::new).collect(Collectors.toList());
    }

    public RoleDto(String roleName) {
        this.roleName = roleName;
    }
}
