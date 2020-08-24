package com.gig.lookBook.core.service;

import com.gig.lookBook.core.dto.account.AccountDto;
import com.gig.lookBook.core.dto.account.AccountLightDto;
import com.gig.lookBook.core.dto.account.AccountReqDto;
import com.gig.lookBook.core.dto.account.AccountSearchDto;
import com.gig.lookBook.core.exception.AlreadyEntity;
import com.gig.lookBook.core.exception.UserNotFoundException;
import com.gig.lookBook.core.model.Account;
import com.gig.lookBook.core.model.Role;
import com.gig.lookBook.core.model.types.YNType;
import com.gig.lookBook.core.repository.AccountRepository;
import com.gig.lookBook.core.repository.RoleRepository;
import javassist.NotFoundException;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.time.LocalDateTime;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;
    private final RoleRepository roleRepository;

    @PersistenceContext
    private EntityManager em;

    public Page<AccountLightDto> getUserList(AccountSearchDto search) {
        int page = search.getPage();
        int size = search.getSize();

        TypedQuery<Account> tq = searchUserList(search);
        long total = searchCount(search);
        tq.setFirstResult(page * size).setMaxResults(size);
        List<Account> results = tq.getResultList();

        return new PageImpl<>(getUsers(results), PageRequest.of(page, size), total);
    }

    private long searchCount(AccountSearchDto searchDto) {
        CriteriaQuery<Long> countQuery = makeUserQuery(searchDto, true);
        Long count = em.createQuery(countQuery).getSingleResult();
        return count;
    }

    private TypedQuery<Account> searchUserList(AccountSearchDto searchDto) {
        return em.createQuery(makeUserQuery(searchDto, false));
    }

    private CriteriaQuery makeUserQuery(AccountSearchDto searchDto, boolean isTotalCount) {
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery criteriaQuery;

        if (isTotalCount) criteriaQuery = builder.createQuery(Long.class);
        else criteriaQuery = builder.createQuery(Account.class);

        Root<Account> root = criteriaQuery.from(Account.class);

        if (isTotalCount)
            criteriaQuery.select(builder.count(root));

        List<Predicate> likeOr = new ArrayList<>();
        List<Predicate> predicates = new ArrayList<>();


        predicates.add(builder.equal(root.join("roles").get("roleName"), "ROLE_USER"));


        if (likeOr.size() > 0 && predicates.size() > 0)
            criteriaQuery.where(builder.and(predicates.toArray(new Predicate[predicates.size()])), builder.or((likeOr.toArray(new Predicate[likeOr.size()]))));
        else if (likeOr.size() > 0)
            criteriaQuery.where(builder.or((likeOr.toArray(new Predicate[likeOr.size()]))));
        else if (predicates.size() > 0)
            criteriaQuery.where(builder.and(predicates.toArray(new Predicate[predicates.size()])));

        // order by
        criteriaQuery.orderBy(builder.desc(root.get("createdAt")));

        return criteriaQuery;
    }




    public List<AccountLightDto> getUsers(List<Account> users) {
        return users.stream().map(AccountLightDto::new).collect(Collectors.toList());
    }


    public Account findByUsername(String username) throws UserNotFoundException {
        Optional<Account> findUser = accountRepository.findByUsernameIgnoreCase(username);
        if (findUser.isEmpty()) {
            throw new UserNotFoundException(username, UserNotFoundException.USER_NOT_FOUND);
        }
        return findUser.get();
    }

    /**
     * 사용자 추가
     *
     * @param dto
     * @throws AlreadyEntity
     */
    public Account createUser(AccountDto dto) throws UserNotFoundException, NotFoundException {
        Account account = findByUsername(dto.getUsername());
        account.setName(dto.getName());
        account.setEmail(dto.getEmail());

        if (dto.getPassword() != null) {
            account.setPassword(dto.getPassword());
        }

        accountRepository.save(account);

        return account;
    }

    /**
     * 로그인 비밀번호 오류 카운트 추가
     */
    @Transactional
    public void addPasswordFailCnt(String userName) {
        accountRepository.addPasswordFailCntCount(userName);
    }

    /**
     * 비밀번호 리셋
     *
     * @param username
     */
    @Transactional
    public void resetPasswordFailure(String username) {
        accountRepository.resetPasswordFailure(username);
    }

    /**
     * 로그인 성공
     *
     * @param clientIp
     * @param username
     */
    @Transactional
    public void loginSuccess(String clientIp, String username) {
        accountRepository.setLoginSuccess(LocalDateTime.now(), clientIp, username);
    }

    public Long userSaveByAdmin(AccountReqDto accountDto) {
        Account account;

        if (accountDto.getAccountId() != null) {
            Optional<Account> optAccount = accountRepository.findById(accountDto.getAccountId());
            account = optAccount.get();
        } else {
            account = new Account();
        }
        account.setUsername(accountDto.getUsername());
        account.setName(accountDto.getName());
        account.setEmail(accountDto.getEmail());

        Role role = roleRepository.findByRoleName("ROLE_USER");
        account.getRoles().add(role);
        accountRepository.save(account);
        return account.getId();
    }
}
