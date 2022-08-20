package com.faxterol.grpcdemo;

import java.util.Random;

import com.faxterol.grpcdemo.interfaces.EnviarMensaje;
import com.faxterol.grpcdemo.interfaces.RecibirMensaje;

import io.grpc.stub.StreamObserver;

public class MultiplesMensajesStream implements StreamObserver<EnviarMensaje>{
    private StreamObserver<RecibirMensaje> responseObserver;
    private Random random = new Random();

    //Necesitamos inyectar el stream observer para poder responder al cliente el mensaje
    public MultiplesMensajesStream(StreamObserver<RecibirMensaje> responseObserver){
        this.responseObserver = responseObserver;
    }

    @Override
    public void onNext(EnviarMensaje value) {
        System.out.println("MultiplesMensajesStream - Nuevo mensaje recibido: "+value);
        
    }

    @Override
    public void onError(Throwable t) {
        System.out.println("MultiplesMensajesStream - Ocurrio un error: "+t.getMessage());
        
    }

    @Override
    public void onCompleted() {
        //Los streams unidireccionales de cliente a servidor, solo requieren una respuesta del servidor.
        RecibirMensaje respuesta = RecibirMensaje.newBuilder()
                                    .setFrom(random.nextInt())
                                    .setMessage("Hola mundo desde el servidor: "+random.nextInt())
                                    .build();

        this.responseObserver.onNext(respuesta);
        this.responseObserver.onCompleted();
    }
    
}
