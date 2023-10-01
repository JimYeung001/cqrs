package com.techbank.account.query.api.query;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.techbank.account.query.api.dto.EqualityType;
import com.techbank.account.query.domain.AccountRepository;
import com.techbank.account.query.domain.BankAccount;
import com.techbank.cqrs.core.domain.BaseEntity;
import com.techbank.cqrs.core.query.BaseQuery;

@Service
public class AccountQueryHandler implements QueryHandler {

	@Autowired
	private AccountRepository accountRepository;

	@Override
	public List<BaseEntity> handle(BaseQuery query) {

		List<BaseEntity> result = null;
		if (query instanceof FindAllAccountsQuery) {
			result = findAllAccounts((FindAllAccountsQuery) query);
		}

		if (query instanceof FindAccountByHolderQuery) {
			result = findAccountByHolder((FindAccountByHolderQuery) query);
		}

		if (query instanceof FindAccountWithBalanceQuery) {
			result = findAccountWithBalance((FindAccountWithBalanceQuery) query);
		}

		if (query instanceof FindAccountByIdQuery) {
			result = findAccountById((FindAccountByIdQuery) query);
		}

		return result;
	}

	private List<BaseEntity> findAccountById(FindAccountByIdQuery query) {
		Optional<BankAccount> account = accountRepository.findById(query.getId());
		if (account.isEmpty()) {
			return null;
		}
		List<BaseEntity> result = new ArrayList<>();
		result.add((BaseEntity) (account.get()));
		return result;
	}

	private List<BaseEntity> findAccountWithBalance(FindAccountWithBalanceQuery query) {
		List<BaseEntity> accounts = query.getEqualityType() == EqualityType.GREAT_THAN
				? accountRepository.findByBalanceGreaterThan(query.getBalance())
				: accountRepository.findByBalanceLessThan(query.getBalance());
		return accounts;
	}

	private List<BaseEntity> findAccountByHolder(FindAccountByHolderQuery query) {
		Optional<BankAccount> account = accountRepository.findByAccountHolder(query.getAccountHolder());
		if (account.isEmpty()) {
			return null;
		}
		List<BaseEntity> result = new ArrayList<>();
		result.add((BaseEntity) (account.get()));
		return result;
	}

	private List<BaseEntity> findAllAccounts(FindAllAccountsQuery query) {
		Iterable<BankAccount> accounts = accountRepository.findAll();
		List<BaseEntity> result = new ArrayList<>();
		accounts.forEach(result::add);
		return result;
	}

}
