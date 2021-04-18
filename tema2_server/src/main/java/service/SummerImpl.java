package service;

import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import proto.SummerGrpc;
import proto.SummerOuterClass;

import java.io.File;
import java.io.FileNotFoundException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Scanner;

public class SummerImpl extends SummerGrpc.SummerImplBase {


    @Override
    public void getZodiacSign(SummerOuterClass.DateRequest request, StreamObserver<SummerOuterClass.SignResponse> responseObserver) {
        SummerOuterClass.SignResponse.Builder response = SummerOuterClass.SignResponse.newBuilder();
        ArrayList<ZodiacSign> zodiacSigns;
        FindZodiacSign findZodiacSign=new FindZodiacSign();
        String zodiacSign="";
        try {
            zodiacSigns=findZodiacSign.readFromFile( "C:\\univ\\cna\\tema2_server\\src\\main\\resources\\SummerZodiacDates.txt");

            try {
                zodiacSign = findZodiacSign.findZodiac(request.getDate(), zodiacSigns);

                System.out.println("Zodiac sign:" + zodiacSign);

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
        response.setSign(zodiacSign);

        responseObserver.onNext(response.build());
        responseObserver.onCompleted();
    }
}
