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
        } catch (InputMismatchException e) {
            System.out.println(e);
            return false;
        } catch (ParseException e) {
            System.out.println(e);
            return false;
        } catch (DateTimeException e) {
            System.out.println(e);
            return false;
        }
    }
    public static void main(String[] args) {

        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 8999).usePlaintext().build();
        GateGrpc.GateStub gateStub = GateGrpc.newStub(channel);

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
                        gateStub.getZodiacSign(GateOuterClass.DateRequest.newBuilder().setDate(date).build(), new StreamObserver<GateOuterClass.SignResponse>() {

                            @Override
                            public void onNext(GateOuterClass.SignResponse signResponse) {
                                System.out.println(signResponse);

                            }

                            @Override
                            public void onError(Throwable throwable) {
                                System.out.println("Error : " + throwable.getMessage());
                            }

                            @Override
                            public void onCompleted() {
                            }

                        });
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