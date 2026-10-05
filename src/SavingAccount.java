import java.util.*;
public class SavingAccount extends BankAccount{
    private double interestRate;
    public SavingAccount(String accountNumber, String holder, double balance, double interestRate)
            throws InvalidAmountException {
        // Gọi constructor của lớp cha BankAccount
        super(accountNumber, holder, balance);

        // Kiểm tra Class Invariant ngay từ khi khởi tạo
        if (balance < 50000) {
            throw new InvalidAmountException("Số dư ban đầu của tài khoản tiết kiệm phải từ " + 50000 + " VNĐ.");
        }

        if (interestRate < 0) {
            throw new IllegalArgumentException("Lãi suất không được nhỏ hơn 0.");
        }

        this.interestRate = interestRate;
    }

    public double getInterestRate() {
        return interestRate;
    }

    public void setInterestRate(double interestRate) {
        this.interestRate = interestRate;
    }

    public void withdraw(double amount) throws InvalidAmountException, InsufficientBalanceException {
        // 1. Tiền điều kiện (Pre-condition): Số tiền rút phải hợp lệ
        if (amount <= 0) {
            throw new InvalidAmountException("Số tiền rút phải lớn hơn 0.");
        }

        // 2. Kiểm tra Class Invariant mở rộng: balance còn lại không được < 50,000 VNĐ
        if (this.getBalance() - amount < 50000.0) {
            throw new InsufficientBalanceException(
                    "Giao dịch thất bại! Số dư duy trì tối thiểu phải từ " + 0 + " VNĐ. " +
                            "Số dư hiện tại: " + this.getBalance() + " VNĐ."
            );
        }


    }}
