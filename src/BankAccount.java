import java.util.*;
public abstract class BankAccount {
    private String accountNuber;
    private String holder;
    private double balance;

    public BankAccount(String accountNuber, String holder, double balance) {
        this.accountNuber = accountNuber;
        this.holder = holder;
        this.balance = balance;
    }

    public String getAccountNumber() {
        return accountNuber;
    }

    public String getHolder() {
        return holder;
    }

    public double getBalance() {
        return balance;
    }

    public void setAccountNuber(String accountNuber) {
        this.accountNuber = accountNuber;
    }

    public void setHolder(String holder) {
        this.holder = holder;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public void deposit(double amount) throws InvalidAmountException {
        // Kiểm tra tiền điều kiện
        if (amount <= 0) {
            throw new InvalidAmountException("Số tiền nạp phải lớn hơn 0.");
        }

        double oldBalance = getBalance();
        oldBalance += amount;

        // Kiểm tra hậu điều kiện và bất biến (bật bằng: java -ea)

    }

    public abstract void withdraw(double amount)
            throws InvalidAmountException, InsufficientBalanceException;

}
