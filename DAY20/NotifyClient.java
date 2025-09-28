package DAY20;

public class NotifyClient {
    public static void main(String[] args) {
        BankNotifications notify1 = new EmailNotify();
        BankNotifications notify2 = new MobileNotify();
        BankNotifications notify3 = new WhatsappNotify();
        BankNotifications notify4 = new PhysicalNotify();

        notify1.sendOTP();
        notify2.sendOTP();
        notify3.sendOTP();
        notify4.sendOTP();
    }
}
