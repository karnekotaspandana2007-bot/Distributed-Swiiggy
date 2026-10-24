import java.io.*;
import java.net.*;
import java.util.HashMap;

public class DeliveryServer {

    // ====================================
    // DELIVERY DATA
    // ====================================

    static HashMap<Integer, String> deliveryStatus =
            new HashMap<>();

    static HashMap<Integer, String> deliveryPartner =
            new HashMap<>();


    // ====================================
    // MAIN
    // ====================================

    public static void main(String[] args) {

        try {

            ServerSocket serverSocket =
                    new ServerSocket(7000);


            System.out.println(
                    "========================================");

            System.out.println(
                    "          DELIVERY SERVER");

            System.out.println(
                    "========================================");

            System.out.println(
                    "Delivery Server started");

            System.out.println(
                    "Port: 7000");

            System.out.println(
                    "Waiting for delivery requests...");


            while (true) {

                Socket socket =
                        serverSocket.accept();


                System.out.println();

                System.out.println(
                        "Order Server connected to Delivery Server");


                DeliveryHandler handler =
                        new DeliveryHandler(socket);


                handler.start();
            }


        } catch (Exception e) {

            System.out.println(
                    "Delivery Server Error: "
                    + e.getMessage());
        }
    }


    // ====================================
    // DELIVERY HANDLER
    // ====================================

    static class DeliveryHandler extends Thread {

        Socket socket;

        BufferedReader input;

        PrintWriter output;


        DeliveryHandler(Socket socket) {

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

                String request =
                        input.readLine();


                // ====================================
                // DELIVERY REQUEST
                // ====================================

                if (request != null &&
                        request.startsWith(
                                "DELIVERY|")) {


                    String[] data =
                            request.split("\\|");


                    int orderId =
                            Integer.parseInt(
                                    data[1]);


                    String food =
                            data[2];


                    System.out.println();

                    System.out.println(
                            "========== DELIVERY REQUEST ==========");

                    System.out.println(
                            "Order ID: "
                            + orderId);

                    System.out.println(
                            "Food: "
                            + food);


                    System.out.println(
                            "Finding delivery partner...");


                    Thread.sleep(1000);


                    String partner =
                            "Ravi";


                    // Store delivery
                    deliveryPartner.put(
                            orderId,
                            partner);


                    deliveryStatus.put(
                            orderId,
                            "Out for Delivery");


                    System.out.println(
                            "Delivery Partner: "
                            + partner);


                    System.out.println(
                            "Estimated Time: 30 minutes");


                    System.out.println(
                            "Delivery Status: Out for Delivery");


                    output.println(
                            "DELIVERY_SUCCESS");
                }


                // ====================================
                // CANCEL DELIVERY
                // ====================================

                else if (request != null &&
                        request.startsWith(
                                "CANCEL|")) {


                    String[] data =
                            request.split("\\|");


                    int orderId =
                            Integer.parseInt(
                                    data[1]);


                    System.out.println();

                    System.out.println(
                            "========== DELIVERY CANCEL REQUEST ==========");

                    System.out.println(
                            "Order ID: "
                            + orderId);


                    if (deliveryStatus.containsKey(
                            orderId)) {


                        String oldStatus =
                                deliveryStatus.get(
                                        orderId);


                        System.out.println(
                                "Previous Delivery Status: "
                                + oldStatus);


                        if (oldStatus.equals(
                                "CANCELLED")) {


                            System.out.println(
                                    "Delivery is already cancelled.");


                            output.println(
                                    "DELIVERY_CANCEL_SUCCESS");
                        }

                        else {


                            deliveryStatus.put(
                                    orderId,
                                    "CANCELLED");


                            System.out.println(
                                    "Delivery Partner: "
                                    + deliveryPartner.get(
                                            orderId));


                            Thread.sleep(500);


                            System.out.println(
                                    "Delivery Status: CANCELLED");


                            System.out.println(
                                    "Delivery assignment removed.");


                            output.println(
                                    "DELIVERY_CANCEL_SUCCESS");
                        }
                    }


                    else {

                        System.out.println(
                                "Delivery record not found.");


                        output.println(
                                "DELIVERY_CANCEL_FAILED");
                    }
                }


                // ====================================
                // UNKNOWN
                // ====================================

                else {

                    System.out.println();

                    System.out.println(
                            "Unknown delivery request.");

                    output.println(
                            "DELIVERY_FAILED");
                }


                socket.close();


            } catch (Exception e) {

                System.out.println();

                System.out.println(
                        "Delivery Error: "
                        + e.getMessage());
            }
        }
    }
}