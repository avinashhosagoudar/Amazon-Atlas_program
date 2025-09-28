package DAY20;

// BankNotifications.java
public interface BankNotifications {
    void sendOTP();
}
// EmailNotify.java
class EmailNotify implements BankNotifications {
    public void sendOTP() {
        System.out.println("Email sent to your mail ID.");
    }
}
// MobileNotify.java
class MobileNotify implements BankNotifications {
    public void sendOTP() {
        System.out.println("Message sent to your Mobile number.");
    }
}

// WhatsappNotify.java
class WhatsappNotify implements BankNotifications {
    public void sendOTP() {
        System.out.println("Message sent to your WhatsApp.");
    }
}
// PhysicalNotify.java
class PhysicalNotify implements BankNotifications {
    public void sendOTP() {
        System.out.println("Physical OTP letter sent to your address.");
    }
}
