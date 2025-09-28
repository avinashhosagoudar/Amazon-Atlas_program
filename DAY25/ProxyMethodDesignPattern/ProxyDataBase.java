package DAY25.ProxyMethodDesignPattern;

import java.util.Objects;
public class ProxyDataBase implements DatabaseExecute{
    String desig;
    DataBase db;
    public ProxyDataBase(String desig){
        this.desig = desig;
        db = new DataBase();
    }
    @Override
    public void executeQuery(String Type){
        if(Type.equals("Delete") && (!Objects.equals(this.desig, "ADMIN"))) {
            System.out.println("you don't have permission to delete");
            return;
        }
        db.execute(Type, this.desig);
    }
}
