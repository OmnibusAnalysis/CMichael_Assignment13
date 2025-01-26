package com.coderscampus.assignment13.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.*;

@Entity
@Table(name="accounts")
public class Account {

	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long accountId;

	@Column(length = 100)
	private String accountName;

	@OneToMany(mappedBy = "account", cascade = CascadeType.ALL)
	private List<Transaction> transactions = new ArrayList<>();

	@ManyToMany(mappedBy = "accounts", fetch = FetchType.LAZY)
	private List<User> users = new ArrayList<>();
	
	// G&S

	public Long getAccountId() {
		return accountId;
	}
	public void setAccountId(Long accountId) {
		this.accountId = accountId;
	}

	public String getAccountName() {
		return accountName;
	}
	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}

	public List<Transaction> getTransactions() {
		return transactions;
	}

	public void setTransactions(List<Transaction> transactions) {
		this.transactions = transactions;
	}

	public List<User> getUsers() {
		return users;
	}

	public void addUser(User user) {
		if (this.users == null) {
			this.users = new ArrayList<>();
		}
		if (!this.users.contains(user)) {
			this.users.add(user);
			user.addAccount(this);
		}
	}

	public void removeUser(User user) {
		if (this.users != null && this.users.contains(user)) {
			this.users.remove(user);
			user.removeAccount(this);
		}
	}
	public void setUsers(List<User> users) {
		this.users.clear();
		if(users != null) {
			for (User user : users) {
				addUser(user);
			}
		}
		}
}
