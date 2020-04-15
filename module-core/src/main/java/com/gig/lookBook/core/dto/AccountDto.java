package com.gig.lookBook.core.dto;

import com.gig.lookBook.core.model.Account;
import com.gig.lookBook.core.model.types.YNType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class AccountDto {

    private Long id;

    @NotBlank
    @Length(min = 3, max = 20)
    @Pattern(regexp = "^[ㄱ-ㅎ가-힣a-z0-9_-]{3,20}$")
    private String username;

    private String name;

    @NotBlank
    @Length(min = 8, max = 50)
    private String password;

    private String phone;

    @Email
    @NotBlank
    private String email;


    private String loginIp;
    private YNType activeYn = YNType.Y;
    private YNType dormancy = YNType.N;
    private LocalDateTime createdAt;
    private LocalDateTime lastLoginAt;

    public AccountDto(Account u) { this(u, false); }

    public AccountDto(Account u, boolean isPassword) {
        this.id = u.getId();
        this.username = u.getUsername();
        this.email = u.getEmail();
        this.name = u.getName();
        this.activeYn = u.getActiveYn();
        this.dormancy = u.getDormancyYn();
        this.loginIp = u.getLoginIp();

        if(isPassword)
            this.password = u.getPassword();

        this.createdAt = u.getCreatedAt();
        this.lastLoginAt = u.getLastLoginAt();
    }
}
