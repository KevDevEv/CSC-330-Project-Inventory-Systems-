import java.util.Map;

public class Automatics extends Inventory
{
    private int maxCapacity;
    
    Automatics(int maxCapacity) {
        super();
        this.maxCapacity = maxCapacity;
    }
    Automatics(Inventory inv, int maxCapacity) {
        this.inventory = inv.inventory;
        this.maxCapacity = maxCapacity;
    }
    Automatics() {
        super();
        this.maxCapacity = 0;
    }

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
            if (!inCapacity()) levelOff();
            for (Map.Entry<String,Data> entry : inventory.entrySet()) {
                if (!entry.getValue().inBounds()) entry.getValue().level();
                entry.getValue().tick();
            }
        }
    }
}
