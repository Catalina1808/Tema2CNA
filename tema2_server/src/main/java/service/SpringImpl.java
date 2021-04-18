package service;

import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import proto.AutumnOuterClass;
import proto.SpringGrpc;
import proto.SpringOuterClass;

import java.io.FileNotFoundException;
import java.text.ParseException;
import java.util.ArrayList;

public class SpringImpl  extends SpringGrpc.SpringImplBase {
    @Override
    public void getZodiacSign(SpringOuterClass.DateRequest request, StreamObserver<SpringOuterClass.SignResponse> responseObserver) {
        SpringOuterClass.SignResponse.Builder response = SpringOuterClass.SignResponse.newBuilder();
        ArrayList<ZodiacSign> zodiacSigns= new ArrayList<>();
        FindZodiacSign findZodiacSign=new FindZodiacSign();
        try {
            zodiacSigns=findZodiacSign.readFromFile( "C:\\univ\\cna\\tema2_server\\src\\main\\resources\\SpringZodiacDates.txt");

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
