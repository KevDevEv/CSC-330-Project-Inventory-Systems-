public class Data {
    private String location;
    private int stock;
    private int in;
    private int out;
    private int inRate;
    private int outRate;
    private int baseLine;
    private int lowerBound;
    private int upperBound;

    public Data(String location, int stock, int in, int out, int inRate, int outRate, int baseLine, int lowerBound, int upperBound) {
        this.location = location;
        this.stock = stock;
        this.in = in;
        this.out = out;
        this.inRate = inRate;
        this.outRate = outRate;
        this.baseLine = baseLine;
        this.lowerBound = lowerBound;
        this.upperBound = upperBound;
    }

    public Data() {
        this.location = "";
        this.stock = 0;
        this.in = 0;
        this.out = 0;
        this.inRate = 0;
        this.outRate = 0;
        this.baseLine = 0;
        this.lowerBound = 0;
        this.upperBound = 1;
    }

    public boolean inStock() {return stock != 0;}
    public void tick() {
        inc(inRate);
        dec(outRate);
    }
    public String toString() {
        return(location + " " + stock + " " + in + " " + out + " " + inRate + " " + outRate  + " " +  baseLine  + " " + lowerBound  + " " +  upperBound);
    }
    public void print() {
        System.out.println("Stock: " + stock + ", diffRate: " + (inRate - outRate) + ", location: " + location + ", in: " + in + ", out: " + out + ", inRate: " + inRate + ", outRate " + outRate  + ", baseLine: " +  baseLine  + ", lowerBound: " + lowerBound  + ", upperBound: " +  upperBound);
    }
    public void inc(int size) {
        in += size;
        stock += size;
    }
    public void dec(int size) {
        out += size;
        stock -= size;
    }
    public boolean inBounds() {return stock < upperBound && stock > lowerBound;}
    public void level() {
        if (stock > baseLine) dec(stock - baseLine);
        else inc(baseLine - stock);
    }

    public int getStock() {return stock;}
    public int getIn() {return in;}
    public int getOut() {return out;}
    public int getBaseLine() {return baseLine;}
    public int getInRate() {return inRate;}
    public String getLocation() {return location;}
    public int getLowerBound() {return lowerBound;}
    public int getOutRate() {return outRate;}
    public int getUpperBound() {return upperBound;}
    public void setStock(int stock) {this.stock = stock;}
    public void setBaseLine(int baseLine) {this.baseLine = baseLine;}
    public void setStats(int in, int out) {
        this.in = in;
        this.out = out;
    }
    public void resetStats() {
        in = 0;
        out = 0;
    }
    public void setInRate(int inRate) {this.inRate = inRate;}
    public void setLocation(String location) {this.location = location;}
    public void setLowerBound(int lowerBound) {this.lowerBound = lowerBound;}
    public void setOutRate(int outRate) {this.outRate = outRate;}
    public void setUpperBound(int upperBound) {this.upperBound = upperBound;}
}
