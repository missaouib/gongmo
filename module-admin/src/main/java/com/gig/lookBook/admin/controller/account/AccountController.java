package com.gig.lookBook.admin.controller.account;


import com.gig.lookBook.core.dto.account.AccountDto;
import com.gig.lookBook.core.dto.account.AccountReqDto;
import com.gig.lookBook.core.dto.account.AccountSearchDto;
import com.gig.lookBook.core.dto.common.ApiResultDto;
import com.gig.lookBook.core.exception.NotFoundException;
import com.gig.lookBook.core.service.AccountService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author Jake
 * @date: 2020/08/22
 */
@Slf4j
@Controller
@RequestMapping("/account")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    @RequestMapping
    public ModelAndView index(AccountSearchDto search) throws NotFoundException {
        ModelAndView mav = new ModelAndView("account/list");
        mav.addObject("pages", accountService.getUserList(search));
        mav.addObject("search", search);
        return mav;
    }

    @RequestMapping({"new", "{id}"})
    public ModelAndView editor(@PathVariable(name = "id", required = false) Long id) throws NotFoundException {
        ModelAndView mav = new ModelAndView("account/editor");
        if(id != null) {
//            mav.addObject("dto", dto);
        }
        return mav;
    }

    @PostMapping("ajax/save")
    @ResponseBody
    public ApiResultDto save(@RequestBody AccountReqDto reqDto) throws NotFoundException {
        ApiResultDto<Long> result = new ApiResultDto<>(ApiResultDto.RESULT_CODE_OK);
        Long memberId = accountService.userSaveByAdmin(reqDto);
        result.setData(memberId);
        return result;
    }
}
