package com.gig.lookBook.core.dto.account;

import com.gig.lookBook.core.model.types.YNType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

/**
 * @author Jake
 * @date : 2020-04-10
 */
@Getter
@Setter
@NoArgsConstructor
public class AccountReqDto {

    private Long accountId;

    @Email
    @NotBlank
    private String username;

    @NotBlank
    @Length(min = 3, max = 20)
    @Pattern(regexp = "^[ㄱ-ㅎ가-힣a-z0-9_-]{3,20}$")
    private String nickname;

    private String name;

    private String phone;

    @Email
    @NotBlank
    private String email;

    private YNType activeYn;

    private String password;
}
