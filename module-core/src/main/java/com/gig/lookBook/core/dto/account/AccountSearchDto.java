package com.gig.lookBook.core.dto.account;

import com.gig.lookBook.core.dto.common.BaseSearchDto;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Jake
 * @date: 2020-08-22
 */
@Getter
@Setter
public class AccountSearchDto extends BaseSearchDto {
    private String email;
}
