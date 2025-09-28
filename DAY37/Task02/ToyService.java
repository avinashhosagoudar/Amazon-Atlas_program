package DAY37.Task02;

public class ToyService {

    public String getToyName(int id) {
        if (id == 1) {
            return "Lego";
        } else if (id == 2) {
            return "Barbie";
        } else {
            return getFallbackName();
        }
    }

    public String getFallbackName() {
        return "Unknown toy";
    }
}
