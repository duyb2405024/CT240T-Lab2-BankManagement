import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.*;
// Lớp con cụ thể đại diện cho Tài khoản thường (không áp dụng số dư tối thiểu 50k)
class CheckingAccount extends BankAccount {
    public CheckingAccount(String accountNumber, String holder, double balance) throws InvalidAmountException {
        super(accountNumber, holder, balance);
    }

    @Override
    public void withdraw(double amount) throws InvalidAmountException, InsufficientBalanceException {
        if (amount <= 0) {
            throw new InvalidAmountException("Số tiền rút phải lớn hơn 0.");
        }
        if (amount > getBalance()) {
            throw new InsufficientBalanceException("Số dư không đủ để thực hiện giao dịch.");
        }
        // Gọi tới logic rút tiền chính (cần triển khai trong BankAccount hoặc tự trừ balance nếu protected)
        // Ví dụ giả định BankAccount đã hỗ trợ xử lý giảm balance hợp lệ
    }
}

public class Main {
    private static final BankManager bankManager = new BankManager();
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        boolean exit = false;

        while (!exit) {
            printMenu();
            System.out.print("Mời bạn chọn chức năng (0-6): ");

            try {
                int choice = Integer.parseInt(scanner.nextLine().trim());

                switch (choice) {
                    case 1 -> createAccount();
                    case 2 -> depositMoney();
                    case 3 -> withdrawMoney();
                    case 4 -> transferMoney();
                    case 5 -> showAccountInfo();
                    case 6 -> showTotalSystemBalance();
                    case 0 -> {
                        System.out.println("Cảm ơn bạn đã sử dụng dịch vụ ngân hàng. Tạm biệt!");
                        exit = true;
                    }
                    default -> System.out.println("❌ Lựa chọn không hợp lệ! Vui lòng chọn từ 0 đến 6.");
                }
            } catch (NumberFormatException e) {
                System.out.println("❌ Lỗi định dạng: Vui lòng chỉ nhập số nguyên cho lựa chọn menu!");
            } catch (Exception e) {
                // Bẫy toàn bộ ngoại lệ chưa lường trước để chương trình không bị văng
                System.out.println("❌ Đã xảy ra lỗi hệ thống: " + e.getMessage());
            }

            System.out.println("\n------------------------------------------------");
        }
    }

    private static void printMenu() {
        System.out.println("\n=== HỆ THỐNG QUẢN LÝ TÀI KHOẢN NGÂN HÀNG ===");
        System.out.println("1. Thêm tài khoản mới");
        System.out.println("2. Nạp tiền vào tài khoản");
        System.out.println("3. Rút tiền từ tài khoản");
        System.out.println("4. Chuyển tiền");
        System.out.println("5. Tra cứu thông tin tài khoản");
        System.out.println("6. Xem tổng số dư toàn hệ thống");
        System.out.println("0. Thoát chương trình");
    }

    // 1. Thêm tài khoản
    private static void createAccount() {
        try {
            System.out.println("\n--- Thêm Tài Khoản Mới ---");
            System.out.println("1. Tài khoản thường (Checking)");
            System.out.println("2. Tài khoản tiết kiệm (Saving)");
            System.out.print("Chọn loại tài khoản: ");
            int type = Integer.parseInt(scanner.nextLine().trim());

            System.out.print("Nhập số tài khoản: ");
            String accNum = scanner.nextLine().trim();

            if (bankManager.findAccount(accNum) != null) {
                System.out.println("❌ Số tài khoản này đã tồn tại trên hệ thống!");
                return;
            }

            System.out.print("Nhập tên chủ tài khoản: ");
            String holder = scanner.nextLine().trim();

            System.out.print("Nhập số dư ban đầu (VNĐ): ");
            double balance = Double.parseDouble(scanner.nextLine().trim());

            BankAccount newAcc;
            if (type == 1) {
                newAcc = new CheckingAccount(accNum, holder, balance);
            } else if (type == 2) {
                System.out.print("Nhập lãi suất (vd: 0.05 tương ứng 5%): ");
                double rate = Double.parseDouble(scanner.nextLine().trim());
                newAcc = new SavingAccount(accNum, holder, balance, rate);
            } else {
                System.out.println("❌ Loại tài khoản không hợp lệ!");
                return;
            }

            bankManager.addAccount(newAcc);
            System.out.println("✅ Thêm tài khoản thành công!");

        } catch (InvalidAmountException e) {
            System.out.println("❌ Lỗi dữ liệu số tiền: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.out.println("❌ Lỗi: Số tiền hoặc số lựa chọn phải là chữ số hợp lệ!");
        } catch (IllegalArgumentException e) {
            System.out.println("❌ Lỗi tham số: " + e.getMessage());
        }
    }

    // 2. Nạp tiền
    private static void depositMoney() {
        try {
            System.out.print("Nhập số tài khoản cần nạp: ");
            String accNum = scanner.nextLine().trim();

            BankAccount acc = bankManager.findAccount(accNum);
            if (acc == null) {
                System.out.println("❌ Không tìm thấy tài khoản!");
                return;
            }

            System.out.print("Nhập số tiền muốn nạp (VNĐ): ");
            double amount = Double.parseDouble(scanner.nextLine().trim());

            acc.deposit(amount);
            System.out.println("✅ Nạp tiền thành công! Số dư mới: " + acc.getBalance() + " VNĐ");

        } catch (InvalidAmountException e) {
            System.out.println("❌ Lỗi nạp tiền: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.out.println("❌ Lỗi: Số tiền nạp phải là định dạng số!");
        }
    }

    // 3. Rút tiền
    private static void withdrawMoney() {
        try {
            System.out.print("Nhập số tài khoản cần rút: ");
            String accNum = scanner.nextLine().trim();

            BankAccount acc = bankManager.findAccount(accNum);
            if (acc == null) {
                System.out.println("❌ Không tìm thấy tài khoản!");
                return;
            }

            System.out.print("Nhập số tiền muốn rút (VNĐ): ");
            double amount = Double.parseDouble(scanner.nextLine().trim());

            acc.withdraw(amount);
            System.out.println("✅ Rút tiền thành công! Số dư còn lại: " + acc.getBalance() + " VNĐ");

        } catch (InvalidAmountException e) {
            System.out.println("❌ Lỗi số tiền nhập vào: " + e.getMessage());
        } catch (InsufficientBalanceException e) {
            System.out.println("❌ Rút tiền thất bại: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.out.println("❌ Lỗi: Số tiền rút phải là định dạng số!");
        }
    }

    // 4. Chuyển tiền
    private static void transferMoney() {
        try {
            System.out.print("Nhập số tài khoản nguồn (gửi): ");
            String fromAcc = scanner.nextLine().trim();

            System.out.print("Nhập số tài khoản đích (nhận): ");
            String toAcc = scanner.nextLine().trim();

            System.out.print("Nhập số tiền chuyển (VNĐ): ");
            double amount = Double.parseDouble(scanner.nextLine().trim());

            bankManager.transferMoney(fromAcc, toAcc, amount);
            System.out.println("✅ Chuyển tiền thành công!");

        } catch (InvalidAmountException e) {
            System.out.println("❌ Lỗi giao dịch: " + e.getMessage());
        } catch (InsufficientBalanceException e) {
            System.out.println("❌ Chuyển tiền thất bại: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.out.println("❌ Lỗi tài khoản: " + e.getMessage());
        }
    }

    // 5. Xem thông tin tài khoản
    private static void showAccountInfo() {
        System.out.print("Nhập số tài khoản cần tra cứu: ");
        String accNum = scanner.nextLine().trim();

        BankAccount acc = bankManager.findAccount(accNum);
        if (acc == null) {
            System.out.println("❌ Không tìm thấy tài khoản!");
            return;
        }

        System.out.println("\n--- THÔNG TIN TÀI KHOẢN ---");
        System.out.println("Số tài khoản: " + acc.getAccountNumber());
        System.out.println("Chủ tài khoản: " + acc.getHolder());
        System.out.println("Số dư: " + acc.getBalance() + " VNĐ");

        if (acc instanceof SavingAccount saving) {
            System.out.println("Loại tài khoản: Tiết kiệm (Saving)");
            System.out.println("Lãi suất: " + (saving.getInterestRate() * 100) + "%");
        } else {
            System.out.println("Loại tài khoản: Thường (Checking)");
        }
    }

    // 6. Tính tổng số dư hệ thống
    private static void showTotalSystemBalance() {
        double total = bankManager.getTotalSystemBalance();
        System.out.printf("💰 Tổng số dư của toàn bộ tài khoản trong hệ thống: %,.2f VNĐ\n", total);
    }
}