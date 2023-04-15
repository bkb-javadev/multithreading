package org.arsen;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;

@AllArgsConstructor
@Setter
@Getter
@ToString
public class Account {
    private Long id;
    private BigDecimal balance;

    public void deposit(BigDecimal amount) {
        this.balance = balance.add(amount);
    }

    public void withdraw(BigDecimal amount) {
        this.balance = balance.subtract(amount);
    }

    public BigDecimal getBalance() {
        return this.balance;
    }

    public static void transfer(Account account1, Account account2, BigDecimal amount) {
        account1.withdraw(amount);
        account2.deposit(amount);
    }

}
