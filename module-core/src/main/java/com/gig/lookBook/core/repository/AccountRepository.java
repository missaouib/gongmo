package com.gig.lookBook.core.repository;

import com.gig.lookBook.core.model.Account;
import com.gig.lookBook.core.model.Role;
import com.gig.lookBook.core.model.types.YNType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * @author Jake
 * @date: 20/04/01
 */
@Transactional(readOnly = true)
@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {

    Optional<Account> findByUsernameIgnoreCase(String username);

    Optional<List<Account>> findDistinctByRolesIn(List<Role> roles);

    @Modifying
    @Query("UPDATE Account u set u.passwordFailCnt = u.passwordFailCnt + 1, u.passwordFailTime=current_timestamp WHERE upper(u.username) = upper(:username)")
    void addPasswordFailCntCount(@Param("username") String username);

    @Modifying
    @Query("UPDATE Account u set u.passwordFailCnt=0, u.passwordFailTime=null WHERE upper(u.username) = upper(:username)")
    void resetPasswordFailure(@Param("username") String username);

    @Modifying
    @Query("UPDATE Account u set u.lastLoginAt = :loginAt, u.passwordFailCnt=0, u.loginIp=:clientIp WHERE upper(u.username) = upper(:username)")
    void setLoginSuccess(@Param("loginAt") LocalDateTime loginAt, @Param("clientIp") String clientIp, @Param("username") String username);

    Optional<Account> findByUsernameIgnoreCaseAndActiveYn(String username, YNType ynType);

    boolean existsByEmail(String email);

    boolean existsByUsername(String email);
}
