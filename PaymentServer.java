import java.io.*;
import java.net.*;
import java.util.HashMap;

public class PaymentServer {

    // ====================================
    // PAYMENT DATA
    // ====================================

    static HashMap<Integer, String> paymentStatus =
            new HashMap<>();

    static HashMap<Integer, String> transactionIds =
            new HashMap<>();


    // ====================================
    // MAIN
    // ====================================

    public static void main(String[] args) {

        try {

            ServerSocket serverSocket =
                    new ServerSocket(6000);


            System.out.println(
                    "========================================");

            System.out.println(
                    "          PAYMENT SERVER");

            System.out.println(
                    "========================================");

            System.out.println(
                    "Payment Server started");

            System.out.println(
                    "Port: 6000");

            System.out.println(
                    "Waiting for payment requests...");


            while (true) {

                Socket socket =
                        serverSocket.accept();


                System.out.println();

                System.out.println(
                        "Order Server connected to Payment Server");


                PaymentHandler handler =
                        new PaymentHandler(socket);


                handler.start();
            }


        } catch (Exception e) {

            System.out.println(
                    "Payment Server Error: "
                    + e.getMessage());
        }
    }


    // ====================================
    // PAYMENT HANDLER
    // ====================================

    static class PaymentHandler extends Thread {

        Socket socket;

        BufferedReader input;

        PrintWriter output;


        PaymentHandler(Socket socket) {

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
                // PAYMENT
                // ====================================

                if (request != null &&
                        request.startsWith("PAY|")) {


                    String[] data =
                            request.split("\\|");


                    int orderId =
                            Integer.parseInt(
                                    data[1]);


                    int amount =
                            Integer.parseInt(
                                    data[2]);


                    System.out.println();

                    System.out.println(
                            "========== PAYMENT REQUEST ==========");

                    System.out.println(
                            "Order ID: "
                            + orderId);

                    System.out.println(
                            "Amount: Rs."
                            + amount);


                    System.out.println(
                            "Processing payment...");


                    Thread.sleep(1000);


                    // Store payment
                    paymentStatus.put(
                            orderId,
                            "SUCCESS");


                    String transactionId =
                            "TXN"
                            + orderId;


                    transactionIds.put(
                            orderId,
                            transactionId);


                    System.out.println(
                            "Payment Status: SUCCESS");


                    System.out.println(
                            "Transaction ID: "
                            + transactionId);


                    output.println(
                            "PAYMENT_SUCCESS");
                }


                // ====================================
                // CANCEL PAYMENT
                // ====================================

                else if (request != null &&
                        request.startsWith("CANCEL|")) {


                    String[] data =
                            request.split("\\|");


                    int orderId =
                            Integer.parseInt(
                                    data[1]);


                    System.out.println();

                    System.out.println(
                            "========== PAYMENT CANCEL REQUEST ==========");

                    System.out.println(
                            "Order ID: "
                            + orderId);


                    if (paymentStatus.containsKey(
                            orderId)) {


                        String oldStatus =
                                paymentStatus.get(
                                        orderId);


                        System.out.println(
                                "Previous Payment Status: "
                                + oldStatus);


                        if (oldStatus.equals(
                                "CANCELLED")) {


                            System.out.println(
                                    "Payment is already cancelled.");


                            output.println(
                                    "PAYMENT_CANCEL_SUCCESS");

                        }

                        else {


                            paymentStatus.put(
                                    orderId,
                                    "CANCELLED");


                            System.out.println(
                                    "Transaction ID: "
                                    + transactionIds.get(
                                            orderId));


                            Thread.sleep(500);


                            System.out.println(
                                    "Payment Status: CANCELLED");


                            System.out.println(
                                    "Refund Status: INITIATED");


                            output.println(
                                    "PAYMENT_CANCEL_SUCCESS");
                        }
                    }


                    else {

                        System.out.println(
                                "Payment record not found.");


                        output.println(
                                "PAYMENT_CANCEL_FAILED");
                    }
                }


                // ====================================
                // UNKNOWN
                // ====================================

                else {

                    System.out.println(
                            "Unknown payment request.");

                    output.println(
                            "PAYMENT_FAILED");
                }


                socket.close();


            } catch (Exception e) {

                System.out.println();

                System.out.println(
                        "Payment Error: "
                        + e.getMessage());
            }
        }
    }
}