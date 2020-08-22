package com.gig.lookBook.core.dto.common;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class BaseSearchDto {
	private int page = 0;
	private int size = 10;
	private String searchType = "ALL";
	private String keyword;
}
