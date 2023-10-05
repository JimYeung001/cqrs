package com.techbank.account.query.api.controllers;

import java.text.MessageFormat;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.techbank.account.query.api.dto.AccountLookupResponse;
import com.techbank.account.query.api.dto.EqualityType;
import com.techbank.account.query.api.query.FindAccountByHolderQuery;
import com.techbank.account.query.api.query.FindAccountByIdQuery;
import com.techbank.account.query.api.query.FindAccountWithBalanceQuery;
import com.techbank.account.query.api.query.FindAllAccountsQuery;
import com.techbank.account.query.domain.BankAccount;
import com.techbank.cqrs.core.infrastructure.QueryDispatcher;
import com.techbank.cqrs.core.query.BaseQuery;

@RestController
@RequestMapping(path = "/api/v1/accountLookup")
public class AccountLookupController {
	private final Logger logger = Logger.getLogger(AccountLookupController.class.getName());

	@Autowired
	private QueryDispatcher<BaseQuery, BankAccount> queryDispatcher;

	@GetMapping(path = "/")
	public ResponseEntity<AccountLookupResponse> getAllAccounts() {
		try {
			List<BankAccount> accounts = queryDispatcher.send(new FindAllAccountsQuery());

			if (null == accounts || accounts.isEmpty()) {
				return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
			}

			AccountLookupResponse accountLookupResponse = AccountLookupResponse.builder().accounts(accounts)
					.message(MessageFormat.format("Successfully returned {0} bank account(s)", accounts.size()))
					.build();

			return new ResponseEntity<>(accountLookupResponse, HttpStatus.OK);

		} catch (Exception e) {
			String msg = "Failed to get all bank account Request";
			logger.log(Level.SEVERE, msg, e);

			return new ResponseEntity<>(new AccountLookupResponse(msg), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping(path = "/id/{id}")
	public ResponseEntity<AccountLookupResponse> getAccountById(@PathVariable(value = "id") String id) {
		try {
			List<BankAccount> accounts = queryDispatcher.send(new FindAccountByIdQuery(id));

			if (null == accounts || accounts.isEmpty()) {
				return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
			}

			AccountLookupResponse accountLookupResponse = AccountLookupResponse.builder().accounts(accounts)
					.message("Successfully returned bank account(s)").build();

			return new ResponseEntity<>(accountLookupResponse, HttpStatus.OK);

		} catch (Exception e) {
			String msg = "Failed to get  bank account By ID Request";
			logger.log(Level.SEVERE, msg, e);

			return new ResponseEntity<>(new AccountLookupResponse(msg), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping(path = "/byHolder/{accountHolder}")
	public ResponseEntity<AccountLookupResponse> getAccountByHolder(
			@PathVariable(value = "accountHolder") String accountHolder) {
		try {
			List<BankAccount> accounts = queryDispatcher.send(new FindAccountByHolderQuery(accountHolder));

			if (null == accounts || accounts.isEmpty()) {
				return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
			}

			AccountLookupResponse accountLookupResponse = AccountLookupResponse.builder().accounts(accounts)
					.message("Successfully returned bank account(s) by Holder").build();

			return new ResponseEntity<>(accountLookupResponse, HttpStatus.OK);

		} catch (Exception e) {
			String msg = "Failed to get  bank account By Holder Request";
			logger.log(Level.SEVERE, msg, e);

			return new ResponseEntity<>(new AccountLookupResponse(msg), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping(path = "/withBalance/{equalityType}/{balance}")
	public ResponseEntity<AccountLookupResponse> getAccountByHolder(
			@PathVariable(value = "equalityType") EqualityType equalityType,
			@PathVariable(value = "balance") double balance) {
		try {
			List<BankAccount> accounts = queryDispatcher.send(new FindAccountWithBalanceQuery(equalityType, balance));

			if (null == accounts || accounts.isEmpty()) {
				return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
			}

			AccountLookupResponse accountLookupResponse = AccountLookupResponse.builder().accounts(accounts)
					.message(MessageFormat.format("Successfully returned {0} bank account(s)", accounts.size()))
					.build();

			return new ResponseEntity<>(accountLookupResponse, HttpStatus.OK);

		} catch (Exception e) {
			String msg = "Failed to get  bank account with balance Request";
			logger.log(Level.SEVERE, msg, e);

			return new ResponseEntity<>(new AccountLookupResponse(msg), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

}
