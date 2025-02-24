package com.coderscampus.assignment13.domain;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "users")
public class User {
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long userId;

	private String username;
	private String password;
	private String name;
	private LocalDate createdDate;

	@ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinTable(name = "user_account",
			joinColumns = @JoinColumn(name = "user_id"),
			inverseJoinColumns = @JoinColumn(name = "account_id"))
	private List<Account> accounts = new ArrayList<>();

	@OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
	private Address address;

	public Long getUserId() {

		return userId;
	}
	public void setUserId(Long userId) {

		this.userId = userId;
	}
	public String getUsername() {

		return username;
	}
	public void setUsername(String username) {

		this.username = username;
	}
	public String getPassword() {

		return password;
	}
	public void setPassword(String password) {

		this.password = password;
	}
	public String getName() {

		return name;
	}
	public void setName(String name) {

		this.name = name;
	}

	public LocalDate getCreatedDate() {

		return createdDate;
	}
	public void setCreatedDate(LocalDate createdDate) {

		this.createdDate = createdDate;
	}

	public List<Account> getAccounts() {

		return accounts;
	}
	public void setAccounts(List<Account> accounts) {
		this.accounts.clear();
		if (accounts != null) {
			for (Account account : accounts) {
				addAccount(account);
			}
		}
	}
	public void addAccount(Account account) {
		if (this.accounts == null) {
			this.accounts = new ArrayList<>();
		}

		if (!this.accounts.contains(account)) {
			this.accounts.add(account);
			account.addUser(this);
		}
		}
	

	public void removeAccount(Account account) {
		if (this.accounts !=null && this.accounts.contains(account)) {
			this.accounts.remove(account);
			account.removeUser(this);
		}
	}

	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		if (this.address != null) {
			this.address.setUser(null);
		}
		this.address = address; 
		if (address != null) {
			address.setUser(this); 
		}
	}

	@Override
	public String toString() {

		return "User [userId=" + userId + ", username=" + username + ", password=" + password + ", name=" + name
				+ ", accounts=" + accounts + ", address=" + address + "]";
	}
	@Override
	public int hashCode() {

		final int prime = 31;
		int result = 1;
		result = prime * result + ((userId == null) ? 0 : userId.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {

		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		User other = (User) obj;
		if (userId == null) {
			return other.userId == null;
		} else return userId.equals(other.userId);
	}
}
