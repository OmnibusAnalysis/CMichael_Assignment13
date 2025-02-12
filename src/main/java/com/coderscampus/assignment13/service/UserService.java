package com.coderscampus.assignment13.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import com.coderscampus.assignment13.domain.Address;
import com.coderscampus.assignment13.repository.AddressRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.coderscampus.assignment13.domain.Account;
import com.coderscampus.assignment13.domain.User;
import com.coderscampus.assignment13.repository.AccountRepository;
import com.coderscampus.assignment13.repository.UserRepository;

import javax.transaction.Transactional;

@Service
public class UserService {

	@Autowired
	private UserRepository userRepo;
	@Autowired
	private AccountRepository accountRepo; 
	@Autowired
	private AddressRepository addressRepo;

	public List<User> findByUsername(String username) {
		return userRepo.findByUsername(username);
	}

	public List<User> findByNameAndUsername(String name, String username) {
		return userRepo.findByNameAndUsername(name, username);
	}

	public List<User> findByCreatedDateBetween(LocalDate date1, LocalDate date2) {
		return userRepo.findByCreatedDateBetween(date1, date2);
	}

	public User findExactlyOneUserByUsername(String username) {
		List<User> users = userRepo.findExactlyOneUserByUsername(username);
		if (users.size() > 0)
			return users.get(0);
		else
			return new User();
	}

	public Set<User> findAll() {
		return userRepo.findAllUsersWithAccountsAndAddresses();
	}

	public User findById(Long userId) {
		Optional<User> userOpt = userRepo.findById(userId);
		return userOpt.orElse(new User());
	}

	@Transactional
	public User saveUser(User user) {
		if (user.getUserId() == null) {
			// This is a new user
			if (user.getAddress() == null) {
				Address address = new Address();
				user.setAddress(address);
				address.setUser(user);
			} else {
				user.getAddress().setUser(user);
			}
		} else {
			// This is an existing user
			User existingUser = userRepo.findById(user.getUserId()).orElse(null);
			if (existingUser != null) {
				existingUser.setUsername(user.getUsername());
				if (user.getPassword() != null && !user.getPassword().trim().isEmpty()) {
					existingUser.setPassword(user.getPassword());
				}
				existingUser.setName(user.getName());

				// Update or create address
				if (existingUser.getAddress() == null) {
					Address address = new Address();
					existingUser.setAddress(address);
					address.setUser(existingUser);
				}
				Address existingAddress = existingUser.getAddress();
				existingAddress.setAddressLine1(user.getAddress().getAddressLine1());
				existingAddress.setAddressLine2(user.getAddress().getAddressLine2());
				existingAddress.setCity(user.getAddress().getCity());
				existingAddress.setRegion(user.getAddress().getRegion());
				existingAddress.setCountry(user.getAddress().getCountry());
				existingAddress.setZipCode(user.getAddress().getZipCode());

				user = existingUser;
			}
		}

		return userRepo.save(user);
	}

	@Transactional
	public void delete(Long userId) {
		userRepo.deleteById(userId);
	}

	public Account findAccountBy(Long accountId) {
		Optional<Account> account = accountRepo.findById(accountId);
		return account.orElse(new Account());
	}

	public Account saveAccount(Account account) {
		accountRepo.save(account);
		return account;
	}
}