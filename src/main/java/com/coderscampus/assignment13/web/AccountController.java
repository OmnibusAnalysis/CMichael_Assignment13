package com.coderscampus.assignment13.web;

import com.coderscampus.assignment13.domain.Account;
import com.coderscampus.assignment13.domain.User;
import com.coderscampus.assignment13.service.AccountService;
import com.coderscampus.assignment13.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class AccountController {

	@Autowired
	private AccountService accountService;
	@Autowired
	private UserService userService;

	@PostMapping("users/{userId}/accounts")
	public String postOneAccount(@PathVariable Long userId, RedirectAttributes redirectAttributes) {
		Account newAccount = accountService.saveAccount(userId);
		redirectAttributes.addFlashAttribute("newAccountId", newAccount.getAccountId());
		return "redirect/users/" + userId;
	}
//	public String postOneAccount(@PathVariable Long userId) {
//		accountService.saveAccount(userId);
//		System.out.println(userService.findById(userId).getAccounts().size());
//
//		return "redirect:/users/" + userId;
//	}

	@GetMapping("/users/{userId}/accounts/{accountId}")
	public String getUserAccount(@PathVariable Long userId,
								 @PathVariable Long accountId,
								 ModelMap model) {
		User user = userService.findById(userId);
		Account account = accountService.findAccountById(accountId);
		model.put("user", user);
		model.put("account", account);
		return "account";

//		if (user != null && account != null && user.getAccounts().contains(account)) {
//			model.put("user", user);
//			model.put("account", account);
//			return "userAccount";
//		} else {
//			return "redirect:/users/" + userId;
//		}
	}
//	@GetMapping("users/{userId}/accounts/{accountId}")
//	public String getAccountFromUser(ModelMap model, @PathVariable Long accountId) {
//		Account account = accountService.findAccountById(accountId);
//		User user = account.getUsers().get(0);
//		model.put("account", account);
//		model.put("user", user);
//
//		return "account";
//	}

	@PostMapping("/users/{userId}/accounts/{accountId}")
	public String changeAccountName(@PathVariable Long userId,
									@PathVariable Long accountId,
									@ModelAttribute Account account) {
		Account existingAccount = accountService.findAccountById(accountId);
		if (existingAccount != null) {
			existingAccount.setAccountName(account.getAccountName());
			accountService.saveAccount(existingAccount);
			userService.saveUser(userService.findById(userId));
		}
		return "redirect:/users/" + userId + "/accounts/" + accountId;
	}
}