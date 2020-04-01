package com.gig.lookBook.core.repository;

import com.gig.lookBook.core.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * @author Jake
 * @date: 20/04/01
 */
public interface RoleRepository extends JpaRepository<Role, String> {

    Optional<List<Role>> findByRoleNameIn(List<String> roleNames);

    Role findByRoleName(String roleName);
}
