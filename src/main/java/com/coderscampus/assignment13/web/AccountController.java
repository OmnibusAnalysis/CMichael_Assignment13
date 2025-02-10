package com.coderscampus.assignment13.web;

import com.coderscampus.assignment13.domain.Account;
import com.coderscampus.assignment13.domain.User;
import com.coderscampus.assignment13.service.AccountService;
import com.coderscampus.assignment13.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class AccountController {

	@Autowired
	private AccountService accountService;

	@Autowired
	private UserService userService;

	@PostMapping("users/{userId}/accounts")
	public String postOneAccount(@PathVariable Long userId) {
		accountService.saveAccount(userId);
		return "redirect:/users/" + userId;
	}

	@GetMapping("users/{userId}/accounts/{accountId}")
	public String getAccountFromUser(ModelMap model,
									 @PathVariable Long userId,
									 @PathVariable Long accountId) {
		Account account = accountService.findAccountById(accountId);
		User user = userService.findById(userId);
		model.put("account", account);
		model.put("user", user);
		return "account";
	}
	@PostMapping("users/{userId}/accounts/{accountId}")
	public String changeUserAccountName(@PathVariable Long userId,
										@PathVariable Long accountId,
										Account account) {
		Account existingAccount = accountService.findAccountById(accountId);
		existingAccount.setAccountName(account.getAccountName());
		accountService.saveAccount(existingAccount);
		return "redirect:/users/" + userId + "/accounts/" + accountId;
	}
}