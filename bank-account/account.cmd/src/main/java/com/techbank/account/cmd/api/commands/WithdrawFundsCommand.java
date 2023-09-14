package com.techbank.account.cmd.api.commands;

import com.techbank.cqrs.core.commonds.BaseCommand;

import lombok.Data;

@Data
public class WithdrawFundsCommand extends BaseCommand {
	private double amount;
}
