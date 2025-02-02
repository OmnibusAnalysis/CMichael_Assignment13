package com.coderscampus.assignment13.web;

import java.util.Arrays;
import java.util.Set;

import com.coderscampus.assignment13.domain.Account;
import com.coderscampus.assignment13.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.coderscampus.assignment13.domain.User;
import com.coderscampus.assignment13.service.UserService;

@Controller
public class UserController {

	@Autowired
	private UserService userService;

	@Autowired
	private AccountService accountService;


	@GetMapping("/register")
	public String getCreateUser (ModelMap model) {
		model.put("user", new User());
		return "register";
	}

	@PostMapping("/register")
	public String postCreateUser (User user) {
		userService.saveUser(user);
		return "redirect:/register";
	}

	@GetMapping("/users")
	public String getAllUsers (ModelMap model) {
		Set<User> users = userService.findAll();
		System.out.println(users);
		model.put("users", users);
		if (users.size() == 1) {
			model.put("user", users.iterator().next());
		}

		return "users";
	}

	@GetMapping("/users/{userId}")
	public String getOneUser (ModelMap model, @PathVariable Long userId) {
		User user = userService.findById(userId);
		System.out.println(user.getAccounts());
		model.put("users", Arrays.asList(user));
		model.put("user", user);
		return "users";
	}

	@PostMapping("/users/{userId}")
	public String postOneUser (@PathVariable Long userId, User user) {
		userService.saveUser(user);
		return "redirect:/users/" + user.getUserId();
	}

	@PostMapping("/users/{userId}/delete")
	public String deleteOneUser (@PathVariable Long userId) {
		userService.delete(userId);
		return "redirect:/users/";
	}

	@PostMapping("/users/{userId}/accounts")
	public String createNewAccount(@PathVariable Long userId) {
		User user = userService.findById(userId);
		if (user != null) {
			Account newAccount = new Account();
			newAccount.setAccountName("New Account");
			newAccount.addUser(user);
			accountService.saveAccount(newAccount);
			user.addAccount(newAccount);
			userService.saveUser(user);
			return "redirect:/users/" + userId +"/accounts/" + newAccount.getAccountId();
		}
		return "redirect:/users/" + userId;
	}

	@GetMapping("/users/{userId}/accounts/{accountId}")
	public String getAccountDetails(@PathVariable Long userId,
									@PathVariable Long accountId, ModelMap model) {
		User user = userService.findById(userId);
		Account account = accountService.findById(accountId);
		if (user != null && account !=null) {
			model.put("user", user);
			model.put("account", account);
			return "account";
		}

		return "redirect:/users/" +userId;
	}

	@PostMapping("/users/{userId}/accounts/{accountId}")
	public String changeAccountName(@PathVariable Long userId,
									@PathVariable Long accountId,
									@ModelAttribute Account account) {
		Account existingAccount = accountService.findById(accountId);
		if (existingAccount != null) {
			existingAccount.setAccountName(account.getAccountName());
			accountService.saveAccount(existingAccount);
			userService.saveUser(userService.findById(userId));
		}
		return "redirect:/users/" + userId + "/accounts/" + accountId;



	}
}