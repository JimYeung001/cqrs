package com.techbank.account.query.infrastructure;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.techbank.cqrs.core.domain.BaseEntity;
import com.techbank.cqrs.core.infrastructure.QueryDispatcher;
import com.techbank.cqrs.core.query.BaseQuery;
import com.techbank.cqrs.core.query.QueryHandlerMethod;

@Service
public class AccountQueryDispatcher<T extends BaseQuery> implements QueryDispatcher<BaseQuery> {

	private final Map<Class<? extends BaseQuery>, List<QueryHandlerMethod<BaseQuery>>> routes = new HashMap<>();

	@Override
	public void registerHanlder(Class<? extends BaseQuery> type, QueryHandlerMethod<BaseQuery> handler) {
		List<QueryHandlerMethod<BaseQuery>> handlers = routes.computeIfAbsent(type, c -> new ArrayList<>());
		handlers.add(handler);

	}

	@Override
	public List<BaseEntity> send(BaseQuery query) {
		List<QueryHandlerMethod<BaseQuery>> handlers = routes.get(query.getClass());
		if (handlers == null || handlers.size() <= 0) {
			throw new RuntimeException("NO Query handler registered");
		}

		if (handlers.size() > 1) {
			throw new RuntimeException("Can not sent query to more than one hanlder.");
		}

		return handlers.get(0).handle(query);
	}

}
