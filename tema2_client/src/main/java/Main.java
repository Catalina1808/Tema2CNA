import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;
import proto.GateGrpc;
import proto.GateOuterClass;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.DateTimeException;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Main {

    public static boolean verify(String date) {
        try {
            DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
            dateFormat.setLenient(false);
            dateFormat.parse(date);
            return true;
        } catch (InputMismatchException | ParseException | DateTimeException e) {
            System.out.println(e);
            return false;
        }
    }
    public static void main(String[] args) {

        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 8999).usePlaintext().build();
      //  GateGrpc.GateStub gateStub = GateGrpc.newStub(channel);
        GateGrpc.GateBlockingStub gateStub = GateGrpc.newBlockingStub(channel);

        System.out.println("MENU");
        System.out.println("1. Get zodiac sign.");
        System.out.println("2. Quit");
        boolean isConnected = true;
        while (isConnected) {
            Scanner input = new Scanner(System.in);
            System.out.println("Choose:");
            int option = input.nextInt();

            switch (option) {
                case 1: {
                    Scanner read = new Scanner(System.in).useDelimiter("\n");
                    System.out.println("Date: ");
                    String date = read.next();

                    if (verify(date)) {
                        GateOuterClass.SignResponse response=gateStub.getZodiacSign(GateOuterClass.DateRequest.newBuilder().setDate(date).build());
                        System.out.println(response.getSign());
                    }
                    else
                        System.out.println("Invalid date!");


                    break;
                }
                case 2: {
                    isConnected = false;
                    break;
                }
                default:
                    System.out.println("Unknown command, insert a valid command!");
            }
        }
        channel.shutdown();
    }
}