package DAY24.AbstractFactoryDesign;



    public class Mobile {
        String desc;

        public Mobile(String model) {
            this.desc = model;
        }

        public void getDesc() {
            System.out.println(this.desc); // Removed quotes to print actual value
        }
 }

