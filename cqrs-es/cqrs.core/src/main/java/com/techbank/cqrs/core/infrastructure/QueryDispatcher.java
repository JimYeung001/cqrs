package com.techbank.cqrs.core.infrastructure;

import java.util.List;

import com.techbank.cqrs.core.domain.BaseEntity;
import com.techbank.cqrs.core.query.BaseQuery;
import com.techbank.cqrs.core.query.QueryHandlerMethod;

public interface QueryDispatcher<T extends BaseQuery> {

	<U extends BaseEntity> List<U> send(BaseQuery query);

	void registerHanlder(Class<? extends BaseQuery> type, QueryHandlerMethod<T> handler);

}
