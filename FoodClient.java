import java.io.*;
import java.net.*;
import java.util.Scanner;

public class FoodClient {

    public static void main(String[] args) {

        try {

            // ====================================
            // CONNECT TO ORDER SERVER
            // ====================================

            Socket socket =
                    new Socket("localhost", 5000);

            BufferedReader input =
                    new BufferedReader(
                            new InputStreamReader(
                                    socket.getInputStream()));

            PrintWriter output =
                    new PrintWriter(
                            socket.getOutputStream(),
                            true);

            Scanner sc =
                    new Scanner(System.in);


            // ====================================
            // HEADER
            // ====================================

            System.out.println(
                    "====================================");

            System.out.println(
                    "   DISTRIBUTED FOOD ORDERING SYSTEM");

            System.out.println(
                    "====================================");


            // ====================================
            // WELCOME
            // ====================================

            System.out.println("WELCOME");

            System.out.print("Enter your name: ");

            String customerName =
                    sc.nextLine();

            output.println(customerName);


            // ====================================
            // MAIN MENU
            // ====================================

            boolean menuViewed = false;

            while (true) {

                System.out.println();

                System.out.println(
                        "========== MAIN MENU ==========");

                System.out.println(
                        "1. View Food Menu");

                System.out.println(
                        "2. Place Order");

                System.out.println(
                        "3. Check Order Status");

                System.out.println(
                        "4. View Order Details");

                System.out.println(
                        "5. Cancel Order");

                System.out.println(
                        "6. Exit");

                System.out.print(
                        "Enter your choice: ");


                int choice =
                        sc.nextInt();


                // ====================================
                // VIEW FOOD MENU
                // ====================================

                if (choice == 1) {

                    output.println("MENU");

                    String response =
                            input.readLine();


                    if (response != null &&
                            response.equals("MENU_SUCCESS")) {

                        System.out.println();

                        System.out.println(
                                "========== FOOD MENU ==========");

                        for (int i = 0; i < 5; i++) {

                            System.out.println(
                                    input.readLine());
                        }

                        menuViewed = true;
                    }

                    else {

                        System.out.println(
                                "Unable to load menu.");
                    }
                }


                // ====================================
                // PLACE ORDER
                // ====================================

                else if (choice == 2) {

                    System.out.println();

                    System.out.println(
                            "========== PLACE ORDER ==========");


                    // User must view menu first
                    if (menuViewed == false) {

                        System.out.println(
                                "Please view the food menu first.");

                        continue;
                    }


                    System.out.print(
                            "Enter food number: ");

                    int food =
                            sc.nextInt();


                    System.out.print(
                            "Enter quantity: ");

                    int quantity =
                            sc.nextInt();


                    if (food < 1 || food > 5) {

                        System.out.println(
                                "Invalid food number.");

                        continue;
                    }


                    if (quantity <= 0) {

                        System.out.println(
                                "Invalid quantity.");

                        continue;
                    }


                    // ====================================
                    // SEND ORDER
                    // ====================================

                    output.println(
                            "ORDER|"
                            + food
                            + "|"
                            + quantity);


                    String response =
                            input.readLine();


                    // ====================================
                    // ORDER SUCCESS
                    // ====================================

                    if (response != null &&
                            response.equals(
                                    "ORDER_SUCCESS")) {


                        int orderId =
                                Integer.parseInt(
                                        input.readLine());


                        String foodName =
                                input.readLine();


                        int orderedQuantity =
                                Integer.parseInt(
                                        input.readLine());


                        int price =
                                Integer.parseInt(
                                        input.readLine());


                        int total =
                                Integer.parseInt(
                                        input.readLine());


                        String payment =
                                input.readLine();


                        String deliveryPartner =
                                input.readLine();


                        String status =
                                input.readLine();


                        System.out.println();

                        System.out.println(
                                "========== ORDER CONFIRMED ==========");


                        System.out.println(
                                "Order ID: "
                                + orderId);


                        System.out.println(
                                "Food: "
                                + foodName);


                        System.out.println(
                                "Quantity: "
                                + orderedQuantity);


                        System.out.println(
                                "Price: Rs."
                                + price);


                        System.out.println(
                                "Total Bill: Rs."
                                + total);


                        System.out.println(
                                "Payment: "
                                + payment);


                        System.out.println(
                                "Delivery Partner: "
                                + deliveryPartner);


                        System.out.println(
                                "Status: "
                                + status);
                    }

                    else {

                        System.out.println(
                                input.readLine());
                    }
                }


                // ====================================
                // CHECK ORDER STATUS
                // ====================================

                else if (choice == 3) {

                    System.out.print(
                            "Enter Order ID: ");

                    int orderId =
                            sc.nextInt();


                    output.println(
                            "STATUS|"
                            + orderId);


                    String response =
                            input.readLine();


                    if (response != null &&
                            response.equals(
                                    "STATUS_SUCCESS")) {


                        String status =
                                input.readLine();


                        System.out.println();

                        System.out.println(
                                "========== ORDER STATUS ==========");


                        System.out.println(
                                "Order ID: "
                                + orderId);


                        System.out.println(
                                "Status: "
                                + status);
                    }

                    else {

                        System.out.println(
                                input.readLine());
                    }
                }


                // ====================================
                // ORDER DETAILS
                // ====================================

                else if (choice == 4) {

                    System.out.print(
                            "Enter Order ID: ");

                    int orderId =
                            sc.nextInt();


                    output.println(
                            "DETAILS|"
                            + orderId);


                    String response =
                            input.readLine();


                    if (response != null &&
                            response.equals(
                                    "DETAILS_SUCCESS")) {


                        System.out.println();

                        System.out.println(
                                "========== ORDER DETAILS ==========");


                        System.out.println(
                                "Order ID: "
                                + input.readLine());


                        System.out.println(
                                "Food: "
                                + input.readLine());


                        System.out.println(
                                "Quantity: "
                                + input.readLine());


                        System.out.println(
                                "Total: Rs."
                                + input.readLine());


                        System.out.println(
                                "Status: "
                                + input.readLine());
                    }

                    else {

                        System.out.println(
                                input.readLine());
                    }
                }


                // ====================================
                // CANCEL ORDER
                // ====================================

                else if (choice == 5) {

                    System.out.print(
                            "Enter Order ID: ");

                    int orderId =
                            sc.nextInt();


                    output.println(
                            "CANCEL|"
                            + orderId);


                    String response =
                            input.readLine();


                    if (response != null &&
                            response.equals(
                                    "CANCEL_SUCCESS")) {


                        System.out.println();

                        System.out.println(
                                "========== ORDER CANCELLED ==========");

                        System.out.println(
                                input.readLine());

                        System.out.println(
                                "Payment Server: "
                                + input.readLine());

                        System.out.println(
                                "Delivery Server: "
                                + input.readLine());
                    }

                    else {

                        System.out.println(
                                input.readLine());
                    }
                }


                // ====================================
                // EXIT
                // ====================================

                else if (choice == 6) {

                    output.println("EXIT");

                    String response =
                            input.readLine();

                    System.out.println(
                            response);

                    break;
                }


                else {

                    System.out.println(
                            "Invalid choice.");
                }
            }


            socket.close();

            sc.close();


        } catch (ConnectException e) {

            System.out.println();

            System.out.println(
                    "Order Server is not running.");

            System.out.println(
                    "Start OrderServer first.");

        } catch (Exception e) {

            System.out.println();

            System.out.println(
                    "Client Error: "
                    + e.getMessage());
        }
    }
}