import java.io.*;
import java.net.*;
import java.util.*;

public class OrderServer {

    // ====================================
    // ORDER DATA
    // ====================================

    static HashMap<Integer, String> orderFood =
            new HashMap<>();

    static HashMap<Integer, Integer> orderQuantity =
            new HashMap<>();

    static HashMap<Integer, Integer> orderTotal =
            new HashMap<>();

    static HashMap<Integer, String> orderStatus =
            new HashMap<>();


    // ====================================
    // FOOD MENU
    // ====================================

    static String[] foodNames = {
            "Pizza",
            "Burger",
            "Biryani",
            "Pasta",
            "Sandwich"
    };


    static int[] foodPrices = {
            250,
            150,
            200,
            180,
            100
    };


    static int nextOrderId = 100;


    // ====================================
    // MAIN
    // ====================================

    public static void main(String[] args) {

        try {

            ServerSocket serverSocket =
                    new ServerSocket(5000);


            System.out.println(
                    "========================================");

            System.out.println(
                    "             ORDER SERVER");

            System.out.println(
                    "========================================");

            System.out.println(
                    "Order Server started");

            System.out.println(
                    "Port: 5000");

            System.out.println(
                    "Waiting for clients...");


            while (true) {

                Socket socket =
                        serverSocket.accept();


                System.out.println();

                System.out.println(
                        "Client connected to Order Server");


                OrderHandler handler =
                        new OrderHandler(socket);


                handler.start();
            }


        } catch (Exception e) {

            System.out.println(
                    "Order Server Error: "
                    + e.getMessage());
        }
    }


    // ====================================
    // ORDER HANDLER
    // ====================================

    static class OrderHandler extends Thread {

        Socket socket;

        BufferedReader input;

        PrintWriter output;

        String customerName;


        OrderHandler(Socket socket) {

            this.socket = socket;


            try {

                input =
                        new BufferedReader(
                                new InputStreamReader(
                                        socket.getInputStream()));


                output =
                        new PrintWriter(
                                socket.getOutputStream(),
                                true);

            } catch (Exception e) {

                System.out.println(
                        "Connection Error");
            }
        }


        // ====================================
        // RUN
        // ====================================

        public void run() {

            try {

                // ====================================
                // RECEIVE CUSTOMER NAME
                // ====================================

                customerName =
                        input.readLine();


                System.out.println(
                        "Customer: "
                        + customerName);


                while (true) {

                    String request =
                            input.readLine();


                    if (request == null) {

                        break;
                    }


                    // ====================================
                    // MENU
                    // ====================================

                    if (request.equals("MENU")) {

                        System.out.println();

                        System.out.println(
                                customerName
                                + " requested menu");


                        output.println(
                                "MENU_SUCCESS");


                        for (int i = 0;
                             i < 5;
                             i++) {

                            output.println(
                                    (i + 1)
                                    + ". "
                                    + foodNames[i]
                                    + " - Rs."
                                    + foodPrices[i]);
                        }
                    }


                    // ====================================
                    // PLACE ORDER
                    // ====================================

                    else if (request.startsWith(
                            "ORDER|")) {


                        String[] data =
                                request.split("\\|");


                        int foodNumber =
                                Integer.parseInt(
                                        data[1]);


                        int quantity =
                                Integer.parseInt(
                                        data[2]);


                        if (foodNumber < 1 ||
                                foodNumber > 5 ||
                                quantity <= 0) {


                            output.println(
                                    "ORDER_FAILED");


                            output.println(
                                    "Invalid food number or quantity.");

                            continue;
                        }


                        int index =
                                foodNumber - 1;


                        String food =
                                foodNames[index];


                        int price =
                                foodPrices[index];


                        int total =
                                price * quantity;


                        int orderId;


                        synchronized (OrderServer.class) {

                            orderId =
                                    nextOrderId++;

                        }


                        // ====================================
                        // STORE ORDER
                        // ====================================

                        orderFood.put(
                                orderId,
                                food);


                        orderQuantity.put(
                                orderId,
                                quantity);


                        orderTotal.put(
                                orderId,
                                total);


                        orderStatus.put(
                                orderId,
                                "Confirmed");


                        System.out.println();

                        System.out.println(
                                "========== NEW ORDER ==========");

                        System.out.println(
                                "Customer: "
                                + customerName);

                        System.out.println(
                                "Order ID: "
                                + orderId);

                        System.out.println(
                                "Food: "
                                + food);

                        System.out.println(
                                "Quantity: "
                                + quantity);

                        System.out.println(
                                "Total: Rs."
                                + total);


                        // ====================================
                        // SEND PAYMENT REQUEST
                        // ====================================

                        String paymentResult =
                                sendPaymentRequest(
                                        orderId,
                                        total);


                        if (!paymentResult.equals(
                                "PAYMENT_SUCCESS")) {


                            orderStatus.put(
                                    orderId,
                                    "Payment Failed");


                            output.println(
                                    "ORDER_FAILED");


                            output.println(
                                    "Payment failed.");

                            continue;
                        }


                        System.out.println();

                        System.out.println(
                                "Payment successful.");


                        // ====================================
                        // SEND DELIVERY REQUEST
                        // ====================================

                        String deliveryResult =
                                sendDeliveryRequest(
                                        orderId,
                                        food);


                        if (!deliveryResult.equals(
                                "DELIVERY_SUCCESS")) {


                            orderStatus.put(
                                    orderId,
                                    "Delivery Failed");


                            output.println(
                                    "ORDER_FAILED");


                            output.println(
                                    "Delivery failed.");

                            continue;
                        }


                        System.out.println();

                        System.out.println(
                                "Delivery assigned.");


                        // ====================================
                        // SEND ORDER RESULT
                        // ====================================

                        output.println(
                                "ORDER_SUCCESS");

                        output.println(
                                orderId);

                        output.println(
                                food);

                        output.println(
                                quantity);

                        output.println(
                                price);

                        output.println(
                                total);

                        output.println(
                                "Successful");

                        output.println(
                                "Ravi");

                        output.println(
                                "Out for Delivery");
                    }


                    // ====================================
                    // CHECK STATUS
                    // ====================================

                    else if (request.startsWith(
                            "STATUS|")) {


                        String[] data =
                                request.split("\\|");


                        int orderId =
                                Integer.parseInt(
                                        data[1]);


                        if (orderStatus.containsKey(
                                orderId)) {


                            output.println(
                                    "STATUS_SUCCESS");


                            output.println(
                                    orderStatus.get(
                                            orderId));
                        }

                        else {

                            output.println(
                                    "STATUS_FAILED");


                            output.println(
                                    "Order not found.");
                        }
                    }


                    // ====================================
                    // ORDER DETAILS
                    // ====================================

                    else if (request.startsWith(
                            "DETAILS|")) {


                        String[] data =
                                request.split("\\|");


                        int orderId =
                                Integer.parseInt(
                                        data[1]);


                        if (orderStatus.containsKey(
                                orderId)) {


                            output.println(
                                    "DETAILS_SUCCESS");


                            output.println(
                                    orderId);

                            output.println(
                                    orderFood.get(
                                            orderId));

                            output.println(
                                    orderQuantity.get(
                                            orderId));

                            output.println(
                                    orderTotal.get(
                                            orderId));

                            output.println(
                                    orderStatus.get(
                                            orderId));
                        }

                        else {

                            output.println(
                                    "DETAILS_FAILED");


                            output.println(
                                    "Order not found.");
                        }
                    }


                    // ====================================
                    // CANCEL ORDER
                    // ====================================

                    else if (request.startsWith(
                            "CANCEL|")) {


                        String[] data =
                                request.split("\\|");


                        int orderId =
                                Integer.parseInt(
                                        data[1]);


                        System.out.println();

                        System.out.println(
                                "========== CANCEL REQUEST ==========");

                        System.out.println(
                                "Customer: "
                                + customerName);

                        System.out.println(
                                "Order ID: "
                                + orderId);


                        if (!orderStatus.containsKey(
                                orderId)) {


                            output.println(
                                    "CANCEL_FAILED");


                            output.println(
                                    "Order not found.");

                            continue;
                        }


                        String oldStatus =
                                orderStatus.get(
                                        orderId);


                        System.out.println(
                                "Previous Status: "
                                + oldStatus);


                        if (oldStatus.equals(
                                "Cancelled")) {


                            output.println(
                                    "CANCEL_FAILED");


                            output.println(
                                    "Order is already cancelled.");

                            continue;
                        }


                        // ====================================
                        // UPDATE ORDER SERVER
                        // ====================================

                        orderStatus.put(
                                orderId,
                                "Cancelled");


                        System.out.println(
                                "Order "
                                + orderId
                                + " cancelled successfully.");


                        // ====================================
                        // CANCEL PAYMENT
                        // ====================================

                        String paymentCancel =
                                sendCancelToPayment(
                                        orderId);


                        // ====================================
                        // CANCEL DELIVERY
                        // ====================================

                        String deliveryCancel =
                                sendCancelToDelivery(
                                        orderId);


                        // ====================================
                        // SEND RESULT TO CLIENT
                        // ====================================

                        output.println(
                                "CANCEL_SUCCESS");


                        output.println(
                                "Order "
                                + orderId
                                + " cancelled.");


                        output.println(
                                paymentCancel);


                        output.println(
                                deliveryCancel);
                    }


                    // ====================================
                    // EXIT
                    // ====================================

                    else if (request.equals(
                            "EXIT")) {


                        output.println(
                                "GOODBYE");

                        break;
                    }
                }


                System.out.println(
                        "Customer "
                        + customerName
                        + " disconnected.");


                socket.close();


            } catch (Exception e) {

                System.out.println();

                System.out.println(
                        "Order Server Error: "
                        + e.getMessage());
            }
        }


        // ====================================
        // PAYMENT SERVER COMMUNICATION
        // ====================================

        String sendPaymentRequest(
                int orderId,
                int amount) {

            try {

                Socket paymentSocket =
                        new Socket(
                                "localhost",
                                6000);


                BufferedReader paymentInput =
                        new BufferedReader(
                                new InputStreamReader(
                                        paymentSocket.getInputStream()));


                PrintWriter paymentOutput =
                        new PrintWriter(
                                paymentSocket.getOutputStream(),
                                true);


                System.out.println(
                        "Sending payment request...");


                paymentOutput.println(
                        "PAY|"
                        + orderId
                        + "|"
                        + amount);


                String response =
                        paymentInput.readLine();


                paymentSocket.close();


                return response;

            } catch (Exception e) {

                System.out.println(
                        "Payment Server Error: "
                        + e.getMessage());

                return "PAYMENT_FAILED";
            }
        }


        // ====================================
        // DELIVERY SERVER COMMUNICATION
        // ====================================

        String sendDeliveryRequest(
                int orderId,
                String food) {

            try {

                Socket deliverySocket =
                        new Socket(
                                "localhost",
                                7000);


                BufferedReader deliveryInput =
                        new BufferedReader(
                                new InputStreamReader(
                                        deliverySocket.getInputStream()));


                PrintWriter deliveryOutput =
                        new PrintWriter(
                                deliverySocket.getOutputStream(),
                                true);


                System.out.println(
                        "Sending delivery request...");


                deliveryOutput.println(
                        "DELIVERY|"
                        + orderId
                        + "|"
                        + food);


                String response =
                        deliveryInput.readLine();


                deliverySocket.close();


                return response;

            } catch (Exception e) {

                System.out.println(
                        "Delivery Server Error: "
                        + e.getMessage());

                return "DELIVERY_FAILED";
            }
        }


        // ====================================
        // CANCEL PAYMENT
        // ====================================

        String sendCancelToPayment(
                int orderId) {

            try {

                Socket paymentSocket =
                        new Socket(
                                "localhost",
                                6000);


                BufferedReader paymentInput =
                        new BufferedReader(
                                new InputStreamReader(
                                        paymentSocket.getInputStream()));


                PrintWriter paymentOutput =
                        new PrintWriter(
                                paymentSocket.getOutputStream(),
                                true);


                System.out.println(
                        "Sending cancellation to Payment Server...");


                paymentOutput.println(
                        "CANCEL|"
                        + orderId);


                String response =
                        paymentInput.readLine();


                paymentSocket.close();


                if (response != null &&
                        response.equals(
                                "PAYMENT_CANCEL_SUCCESS")) {

                    return "CANCELLED";
                }

                return "Payment cancellation failed.";

            } catch (Exception e) {

                return "Payment Server not available.";
            }
        }


        // ====================================
        // CANCEL DELIVERY
        // ====================================

        String sendCancelToDelivery(
                int orderId) {

            try {

                Socket deliverySocket =
                        new Socket(
                                "localhost",
                                7000);


                BufferedReader deliveryInput =
                        new BufferedReader(
                                new InputStreamReader(
                                        deliverySocket.getInputStream()));


                PrintWriter deliveryOutput =
                        new PrintWriter(
                                deliverySocket.getOutputStream(),
                                true);


                System.out.println(
                        "Sending cancellation to Delivery Server...");


                deliveryOutput.println(
                        "CANCEL|"
                        + orderId);


                String response =
                        deliveryInput.readLine();


                deliverySocket.close();


                if (response != null &&
                        response.equals(
                                "DELIVERY_CANCEL_SUCCESS")) {

                    return "CANCELLED";
                }

                return "Delivery cancellation failed.";

            } catch (Exception e) {

                return "Delivery Server not available.";
            }
        }
    }
}