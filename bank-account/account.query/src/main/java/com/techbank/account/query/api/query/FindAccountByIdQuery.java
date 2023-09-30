package com.techbank.account.query.api.query;

import com.techbank.cqrs.core.query.BaseQuery;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FindAccountByIdQuery extends BaseQuery {
	private String id;
}
