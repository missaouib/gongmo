package com.gig.lookBook.core.service;

import com.gig.lookBook.core.exception.AlreadyEntity;
import com.gig.lookBook.core.exception.NotFoundException;
import com.gig.lookBook.core.model.Privilege;
import com.gig.lookBook.core.model.Role;
import com.gig.lookBook.core.model.types.ModifyType;
import com.gig.lookBook.core.model.types.YNType;
import com.gig.lookBook.core.repository.PrivilegeRepository;
import com.gig.lookBook.core.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * 역할 및 권한 서비스
 */
@Service
@Transactional
public class RoleService {

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PrivilegeRepository privilegeRepository;

//    @Autowired
//    private LogService logService;

    /**
     * 롤 생성
     *
     * @param roleName    Role 이름
     * @param description Role 설명
     * @throws AlreadyEntity 이미 생성된 Role
     */
    public Role createRole(String roleName, String description) throws AlreadyEntity {
        String roleId = roleName.toUpperCase();
        if (!roleName.startsWith("ROLE_")) {
            roleId = "ROLE_" + roleName.toUpperCase();
        }
        Optional<Role> optRole = roleRepository.findById(roleId);
        if (optRole.isPresent())
            throw new AlreadyEntity("Role");

        Role role = new Role();
        role.setRoleName(roleId);
        role.setDescription(description);
        role.setDefaultYn(YNType.N);
        roleRepository.save(role);

        return role;
    }

    /**
     * Role 목록
     *
     * @return
     */
    public List<Role> roleList() {
        Sort sort = new Sort(Sort.Direction.ASC, "sortOrder");
        return roleRepository.findAll();
    }


    /**
     * 권한 생성
     *
     * @param privilege   권한명
     * @param description 설명
     * @return
     * @throws AlreadyEntity
     */
    public Privilege createPrivilege(String privilege, String description) throws AlreadyEntity {
        Optional<Privilege> findData = privilegeRepository.findById(privilege);
        if (findData.isPresent()) {
            throw new AlreadyEntity("Privilege");
        }

        Privilege p = new Privilege();
        p.setPrivilege(privilege);
        p.setDescription(description);

        privilegeRepository.save(p);
        return p;
    }

    /**
     * 권한 역할
     *
     * @return
     */
    public List<Privilege> privileges() {
        return privilegeRepository.findAll();
    }

    /**
     * 역할의 권한 수정
     *
     * @param role       역할
     * @param privilege  권한
     * @param modifyType 수정 내용
     */
    @Transactional
    protected Role modifyPrivilegeOfRole(String role, String privilege, ModifyType modifyType) throws NotFoundException {
        Optional<Role> findRole = roleRepository.findById(role);
        if (findRole.isEmpty())
            throw new NotFoundException(NotFoundException.ROLE_NOT_FOUND);

        Optional<Privilege> findPrivilege = privilegeRepository.findById(privilege);
        if (findPrivilege.isEmpty())
            throw new NotFoundException(NotFoundException.PRIVILEGE_NOT_FOUND);

        Role r = findRole.get();
        Privilege p = findPrivilege.get();

        if (modifyType == ModifyType.Register) {
            r.getPrivileges().add(p);
        } else {
            r.getPrivileges().remove(p);
        }

        roleRepository.save(r);
//        logService.saveRolePrivilegeLog(r, p, modifyType);

        return r;
    }

    /**
     * 역할에 권한 추가
     *
     * @param role      역할명
     * @param privilege 권한명
     */
    @Transactional
    public Role addPrivilegeToRole(String role, String privilege) throws NotFoundException {

        return modifyPrivilegeOfRole(role, privilege, ModifyType.Register);
    }

    /**
     * 역할에 권한 삭제
     *
     * @param role      역할명
     * @param privilege 권한명
     * @throws NotFoundException
     */
    @Transactional
    public Role removePrivilegeFromRole(String role, String privilege) throws NotFoundException {
        return modifyPrivilegeOfRole(role, privilege, ModifyType.Remove);
    }

    public Role findByRoleId(String roleId) throws NotFoundException {
        Optional<Role> findRole = roleRepository.findById(roleId);
        if (findRole.isPresent())
            return findRole.get();
        else
            throw new NotFoundException(NotFoundException.ROLE_NOT_FOUND);

    }
}
