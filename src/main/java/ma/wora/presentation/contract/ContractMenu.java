package main.java.ma.wora.presentation.contract;

public class ContractMenu {
    public void displayMenu() {
        System.out.println("\n--- contract Management System ---");
        System.out.println("1. Display all contracts");
        System.out.println("2. Insert contract");
        System.out.println("3. Update contract");
        System.out.println("4. remove contract");
        System.out.println("5. get contract by id");
        System.out.println("6. get contract by partner");
        System.out.println("7.get terminated contracts");
        System.out.println("0. Exit");
        System.out.print("Enter your choice: ");
    }
}
