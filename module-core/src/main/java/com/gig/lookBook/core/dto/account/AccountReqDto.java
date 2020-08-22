package com.gig.lookBook.core.dto.account;

import com.gig.lookBook.core.model.types.YNType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author Jake
 * @date : 2020-04-10
 */
@Getter
@Setter
@NoArgsConstructor
public class AccountReqDto {

    private Long accountId;

    private String username;

    private String name;

    private String phone;

    private String email;

    private YNType activeYn;

    private String password;
}
