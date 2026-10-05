import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class BankManager {
    // Sử dụng Map<String, BankAccount> để quản lý danh sách tài khoản theo accountNumber
    private Map<String, BankAccount> accounts = new HashMap<>();

    // Thêm tài khoản vào hệ thống
    public void addAccount(BankAccount account) {
        if (account != null) {
            accounts.put(account.getAccountNumber(), account);
        }
    }

    // Tìm kiếm tài khoản theo accountNumber
    public BankAccount findAccount(String accountNumber) {
        return accounts.get(accountNumber);
    }

    // Chuyển tiền giữa 2 tài khoản
    public void transferMoney(String fromAccNumber, String toAccNumber, double amount)
            throws InvalidAmountException, InsufficientBalanceException {

        BankAccount fromAcc = findAccount(fromAccNumber);
        BankAccount toAcc = findAccount(toAccNumber);

        if (fromAcc == null) {
            throw new IllegalArgumentException("Không tìm thấy tài khoản nguồn: " + fromAccNumber);
        }
        if (toAcc == null) {
            throw new IllegalArgumentException("Không tìm thấy tài khoản đích: " + toAccNumber);
        }

        // Thực hiện rút tiền từ tài khoản nguồn (sẽ tự động validate tiền điều kiện và bất biến)
        fromAcc.withdraw(amount);

        // Nếu rút tiền thành công thì nạp tiền vào tài khoản đích
        try {
            toAcc.deposit(amount);
        } catch (InvalidAmountException e) {
            // Rollback: Hoàn lại tiền cho tài khoản nguồn nếu nạp tiền thất bại
            fromAcc.deposit(amount);
            throw e;
        }
    }

    /**
     * Generics Utility Method sử dụng Wildcards (? extends BankAccount)
     * Cho phép tính tổng số dư của bất kỳ tập hợp chứa BankAccount hoặc các lớp con của nó (vd: SavingAccount)
     */
    public static double calculateTotalBalance(Collection<? extends BankAccount> accountCollection) {
        double total = 0;
        if (accountCollection != null) {
            for (BankAccount acc : accountCollection) {
                if (acc != null) {
                    total += acc.getBalance();
                }
            }
        }
        return total;
    }

    // Phương thức gọi hàm tính tổng số dư cho toàn bộ hệ thống BankManager
    public double getTotalSystemBalance() {
        return calculateTotalBalance(this.accounts.values());
    }
}