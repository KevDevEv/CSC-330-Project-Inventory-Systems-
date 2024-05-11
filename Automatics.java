import java.util.Map;

public class Automatics extends Inventory
{
    private int maxCapacity;
    
    Automatics(int maxCapacity) {this.maxCapacity = maxCapacity;}
    Automatics() {this.maxCapacity = 0;}

    public void setMaxCapacity(int maxCapacity) {this.maxCapacity = maxCapacity;}
    public int getMaxCapacity() {return maxCapacity;}

    public Boolean inCapacity() {
        int totalStock = 0;
        for (Map.Entry<String,Data> entry : inventory.entrySet()) totalStock += entry.getValue().getStock();
        if (totalStock > maxCapacity) return false;
        return true;
    }

    public void levelOff() {
        for (Map.Entry<String,Data> entry : inventory.entrySet()) entry.getValue().level();
    }
    
    public void simulate(int time) {
        for (int i = 0; i < time; i++) {
            for (Map.Entry<String,Data> entry : inventory.entrySet()) {
                if (!inCapacity()) levelOff();
                else if (!entry.getValue().inBounds()) entry.getValue().level();
                entry.getValue().tick();
            }
        }
    }
}
