import java.util.Scanner;

public class UserInteraction {
    private Inventory inventory;
    private Scanner scanner;
    private Database database;
    private boolean hasAccess = false;

    public UserInteraction() {
        inventory = new Inventory();
        scanner = new Scanner(System.in);
        database = new Database();
    }

    public void displayMenu() {
        boolean exit = false;
        String user;
        
        System.out.print("Role? ");
        user = scanner.next();
        hasAccess = inventory.hasAccess(user);
        if (hasAccess) {
            int size;
            String fileName, item;
            while (!exit) {
                System.out.println("\n*** Inventory Management System ***");
                System.out.println("1. Load from Inventory");
                System.out.println("2. Save to File");
                System.out.println("3. Print Inventory");
                System.out.println("4. Print Stats");
                System.out.println("5. Add Item");
                System.out.println("6. Remove Item");
                System.out.println("7. Add Stock In");
                System.out.println("8. Take Stock out");
                System.out.println("9. Clear");
                System.out.println("10. Simulate");
                System.out.println("11. Test Database Connection");
                System.out.println("12. Upload Data to Database");
                System.out.println("13. Download Data from Database");
                System.out.println("0. Exit");
                System.out.print("Enter your choice: ");

                int choice = scanner.nextInt();

                switch (choice) {
                    case 0:
                        exit = true;
                        System.out.println("Exiting the program.");
                        break;                    
                    case 1:
                        System.out.println("File Name:");
                        fileName = scanner.next();
                        inventory.load(fileName);
                        break;
                    case 2:
                        System.out.println("File Name:");
                        fileName = scanner.next();
                        inventory.save(fileName);
                        break;
                    case 3:
                        inventory.print();
                        break;
                    case 4:
                        inventory.stats();
                        break;
                    case 5: {
                        System.out.println("Item: ");
                        item = scanner.next();
                        System.out.println("Data: ");
                        Data data = new Data();
                        data.setLocation(scanner.next());
                        data.setStock(scanner.nextInt());
                        int in = scanner.nextInt(), out = scanner.nextInt();
                        data.setStats(in, out);
                        data.setInRate(scanner.nextInt());
                        data.setOutRate(scanner.nextInt());
                        data.setBaseLine(scanner.nextInt());
                        data.setLowerBound(scanner.nextInt());
                        data.setUpperBound(scanner.nextInt());
                        inventory.addItem(item, data);
                        break;
                    }
                    case 6:
                        System.out.println("Item: ");
                        item = scanner.next();
                        inventory.removeItem(item);
                        break;
                    case 7:
                        System.out.println("Item: ");
                        item = scanner.next();
                        System.out.println("Size: ");
                        size = scanner.nextInt();
                        inventory.addIn(item, size);
                        break;
                    case 8:
                        System.out.println("Item: ");
                        item = scanner.next();
                        System.out.println("Size: ");
                        size = scanner.nextInt();
                        inventory.takeOut(item, size);
                        break;
                    case 9:
                        inventory.clear();
                        break;
                    case 10: {
                        System.out.println("Max Capacity: ");
                        int maxCapacity = scanner.nextInt();
                        Automatics auto = new Automatics(inventory, maxCapacity);
                        System.out.println("Time: ");
                        int time = scanner.nextInt();
                        auto.simulate(time);
                        System.out.println("Inventory is now: ");
                        auto.print();
                        break;
                    }
                    case 11:
                        database.testConnection();
                    break;
                    case 12:
                        database.setInventory(inventory.getInventory());
                        database.uploadData();
                    break;
                    case 13:
                        database.downloadData();
                    break;
                    default:
                        System.out.println("Invalid choice. Please enter a number between 0 and 13.");
                }
            }
        }
        else System.out.println("You do not have access");
    }

    public static void main(String[] args) {
        UserInteraction userInteraction = new UserInteraction();
        userInteraction.displayMenu();
    }
}
