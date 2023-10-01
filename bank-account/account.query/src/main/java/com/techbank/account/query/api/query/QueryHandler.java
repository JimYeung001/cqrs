package com.techbank.account.query.api.query;

import java.util.List;

import com.techbank.cqrs.core.domain.BaseEntity;
import com.techbank.cqrs.core.query.BaseQuery;

public interface QueryHandler {
	List<BaseEntity> handle(BaseQuery query);

}
