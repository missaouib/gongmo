package com.gig.lookBook.core.validation;

import com.gig.lookBook.core.dto.AccountDto;
import com.gig.lookBook.core.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
@RequiredArgsConstructor
public class SignUpFormValidator implements Validator {

    private final AccountRepository accountRepository;

    @Override
    public boolean supports(Class<?> aClass) {
        return aClass.isAssignableFrom(AccountDto.class);
    }

    @Override
    public void validate(Object target, Errors errors) {
        // TODO email, username
        AccountDto dto = (AccountDto) errors;
        if (accountRepository.existsByEmail(dto.getEmail())) {
            errors.rejectValue("email", "invalid.email", new Object[]{dto.getEmail()}, "이미 사용중인 이메일입니다.");
        }

        if (accountRepository.existsByUsername(dto.getEmail())) {
            errors.rejectValue("username", "invalid.username", new Object[]{dto.getEmail()}, "이미 사용중인 닉네임입니다.");
        }
    }
}
