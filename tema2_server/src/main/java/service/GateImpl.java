package service;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import proto.*;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.DateTimeException;
import java.util.Date;
import java.util.InputMismatchException;

public class GateImpl extends GateGrpc.GateImplBase {

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

    @Override
    public void getZodiacSign(GateOuterClass.DateRequest request, StreamObserver<GateOuterClass.SignResponse> responseObserver) {

        String date = request.getDate();
        System.out.println("Date: " + date);

        if (!verify(date)) {
            System.out.println("Invalid date!");

            Status status = Status.INVALID_ARGUMENT.withDescription("Invalid date!");
            responseObserver.onError(status.asRuntimeException());

        } else {
                 ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 8999).usePlaintext().build();
                int index=date.indexOf("/");
                String monthString=date.substring(0, index);
                int month=Integer.parseInt(monthString);
                System.out.println(index);
                if(month<3 || month==12) {
                    WinterGrpc.WinterBlockingStub winterStub = WinterGrpc.newBlockingStub(channel);
                    WinterOuterClass.SignResponse response = winterStub.getZodiacSign(WinterOuterClass.DateRequest.newBuilder().setDate(date).build());
                    GateOuterClass.SignResponse.Builder responseGate = GateOuterClass.SignResponse.newBuilder();
                    responseGate.setSign(response.getSign());
                    responseObserver.onNext(responseGate.build());

                }
                else
                    if(month<6){
                        SpringGrpc.SpringBlockingStub springStub = SpringGrpc.newBlockingStub(channel);
                        SpringOuterClass.SignResponse response = springStub.getZodiacSign(SpringOuterClass.DateRequest.newBuilder().setDate(date).build());

                        GateOuterClass.SignResponse.Builder responseGate = GateOuterClass.SignResponse.newBuilder();
                        responseGate.setSign(response.getSign());
                        responseObserver.onNext(responseGate.build());
                    }
                    else
                        if(month<9){
                            SummerGrpc.SummerBlockingStub summerStub = SummerGrpc.newBlockingStub(channel);
                            SummerOuterClass.SignResponse response = summerStub.getZodiacSign(SummerOuterClass.DateRequest.newBuilder().setDate(date).build());

                            GateOuterClass.SignResponse.Builder responseGate = GateOuterClass.SignResponse.newBuilder();
                            responseGate.setSign(response.getSign());
                            responseObserver.onNext(responseGate.build());
                        }
                        else
                        {
                            AutumnGrpc.AutumnBlockingStub autumnStub = AutumnGrpc.newBlockingStub(channel);
                            AutumnOuterClass.SignResponse response = autumnStub.getZodiacSign(AutumnOuterClass.DateRequest.newBuilder().setDate(date).build());

                            GateOuterClass.SignResponse.Builder responseGate = GateOuterClass.SignResponse.newBuilder();
                            responseGate.setSign(response.getSign());
                            responseObserver.onNext(responseGate.build());
                        }



            responseObserver.onCompleted();
            channel.shutdown();
        }

    }
}
