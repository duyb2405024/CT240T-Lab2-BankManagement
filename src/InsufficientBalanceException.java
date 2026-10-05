// Kế thừa Exception để làm Checked Exception (bắt buộc dùng try-catch khi gọi)
public class InsufficientBalanceException extends Exception {
    public InsufficientBalanceException(String message) {
        super(message);
    }
}