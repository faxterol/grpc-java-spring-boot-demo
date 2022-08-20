package com.faxterol.grpcdemo.services;

import com.faxterol.grpcdemo.interfaces.ChatServiceGrpc.ChatServiceImplBase;

import java.util.Random;

import org.lognet.springboot.grpc.GRpcService;

import com.faxterol.grpcdemo.MultiplesMensajesStream;
import com.faxterol.grpcdemo.MultiplesRespuestasStream;
import com.faxterol.grpcdemo.interfaces.EnviarMensaje;
import com.faxterol.grpcdemo.interfaces.RecibirMensaje;

import io.grpc.stub.StreamObserver;

@GRpcService
public class ChatService extends ChatServiceImplBase{

    @Override
    public void enviarMensaje(EnviarMensaje request, StreamObserver<RecibirMensaje> responseObserver) {
        //Crea la respuesta
        RecibirMensaje response = RecibirMensaje.newBuilder()
                                        .setFrom(1)
                                        .setMessage("Hello back!")
                                        .build();

        //Envia el mensaje
        responseObserver.onNext(response);

        //Cierra la conexión
        responseObserver.onCompleted();
    }

    @Override
    public StreamObserver<EnviarMensaje> enviarMultiplesMensajes(StreamObserver<RecibirMensaje> responseObserver) {
        return new MultiplesMensajesStream(responseObserver);
        /*
         * Una solución alternativa es crear una clase anomina, esto es hacer el new StreamObserver e implementar los métodos necesarios. 
         */
        //return new StreamObserver<EnviarMensaje>(){} 
    }

    @Override
    public StreamObserver<EnviarMensaje> enviarRecibirMultiplesMensajes(StreamObserver<RecibirMensaje> responseObserver) {
        return new MultiplesRespuestasStream(responseObserver);
    }

    @Override
    public void recibirMultiplesRespuestas(EnviarMensaje request, StreamObserver<RecibirMensaje> responseObserver) {
        Random random = new Random();
        RecibirMensaje mensajeRespuesta = null;
        for(int i=0;i<10;i++){
            mensajeRespuesta = RecibirMensaje.newBuilder()
                                .setFrom(random.nextInt())
                                .setMessage("Hola stream desde servidor "+random.nextInt())
                                .build();
            responseObserver.onNext(mensajeRespuesta);
        }

        responseObserver.onCompleted();
    }
    
}
