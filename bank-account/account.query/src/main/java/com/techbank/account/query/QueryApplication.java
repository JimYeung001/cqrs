package com.techbank.account.query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.techbank.account.query.api.query.FindAccountByHolderQuery;
import com.techbank.account.query.api.query.FindAccountByIdQuery;
import com.techbank.account.query.api.query.FindAccountWithBalanceQuery;
import com.techbank.account.query.api.query.FindAllAccountsQuery;
import com.techbank.account.query.api.query.QueryHandler;
import com.techbank.cqrs.core.domain.BaseEntity;
import com.techbank.cqrs.core.infrastructure.QueryDispatcher;
import com.techbank.cqrs.core.query.BaseQuery;

import jakarta.annotation.PostConstruct;

@SpringBootApplication
public class QueryApplication {

	@Autowired
	private QueryDispatcher<BaseQuery, BaseEntity> queryDispatcher;

	@Autowired
	private QueryHandler queryHandler;

	public static void main(String[] args) {
		SpringApplication.run(QueryApplication.class, args);
	}

	@PostConstruct
	public void registerHandlers() {
		queryDispatcher.registerHanlder(FindAllAccountsQuery.class, queryHandler::handle);
		queryDispatcher.registerHanlder(FindAccountByIdQuery.class, queryHandler::handle);
		queryDispatcher.registerHanlder(FindAccountByHolderQuery.class, queryHandler::handle);
		queryDispatcher.registerHanlder(FindAccountWithBalanceQuery.class, queryHandler::handle);
	}

}
