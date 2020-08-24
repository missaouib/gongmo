package com.gig.lookBook.core.dto.account;

import com.gig.lookBook.core.model.Account;
import com.gig.lookBook.core.model.types.YNType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * @author Jake
 * @date : 2020-04-10
 */
@Getter
@Setter
@NoArgsConstructor
public class AccountLightDto {

    private Long accountId;

    private String username;

    private String name;

    private String phone;

    private String email;

    private YNType activeYn;

    private LocalDateTime createdAt;

    public AccountLightDto(Account account) {
        this.accountId = account.getId();
        this.username = account.getUsername();
        this.name = account.getName();
        this.phone = account.getPassword();
        this.email = account.getEmail();
        this.activeYn = account.getActiveYn();
        this.createdAt = account.getCreatedAt();
    }
}
