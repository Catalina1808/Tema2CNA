package service;

import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import proto.AutumnGrpc;
import proto.AutumnOuterClass;
import proto.SummerOuterClass;

import java.io.FileNotFoundException;
import java.text.ParseException;
import java.util.ArrayList;

public class AutumnImpl extends AutumnGrpc.AutumnImplBase {
    @Override
    public void getZodiacSign(AutumnOuterClass.DateRequest request, StreamObserver<AutumnOuterClass.SignResponse> responseObserver) {
        AutumnOuterClass.SignResponse.Builder response = AutumnOuterClass.SignResponse.newBuilder();
        ArrayList<ZodiacSign> zodiacSigns= new ArrayList<>();
        FindZodiacSign findZodiacSign=new FindZodiacSign();
        try {
            zodiacSigns=findZodiacSign.readFromFile( "C:\\univ\\cna\\tema2_server\\src\\main\\resources\\AutumnZodiacDates.txt");

            try {
                String zodiacSign = findZodiacSign.findZodiac(request.getDate(), zodiacSigns);

                System.out.println("Zodiac sign:" + zodiacSign);

                response.setSign(zodiacSign);

                responseObserver.onNext(response.build());
                responseObserver.onCompleted();

            } catch (ParseException e) {
                Status status = Status.INVALID_ARGUMENT.withDescription("Data invalida!");
                responseObserver.onError(status.asRuntimeException());
                e.printStackTrace();
            }
        } catch (FileNotFoundException e) {
            Status status = Status.NOT_FOUND.withDescription("Fisierul nu a fost gasit!");
            responseObserver.onError(status.asRuntimeException());
            e.printStackTrace();
        }

    }
}
