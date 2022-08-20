package com.faxterol.grpcdemo;

import org.lognet.springboot.grpc.GRpcGlobalInterceptor;

import io.grpc.ForwardingServerCall;
import io.grpc.Metadata;
import io.grpc.ServerCall;
import io.grpc.ServerCall.Listener;
import io.grpc.ServerCallHandler;
import io.grpc.ServerInterceptor;

@GRpcGlobalInterceptor
public class GrpcDemoResponseInterceptor implements ServerInterceptor {

    @Override
    public <ReqT, RespT> Listener<ReqT> interceptCall(ServerCall<ReqT, RespT> call, Metadata headers,
            ServerCallHandler<ReqT, RespT> next) {
    
        //Con este interceptor, interceptamos la respuesta antes de enviarla al cliente
         return next.startCall(new InterceptorRespuesta<>(call),headers);
    }
    

    private class InterceptorRespuesta<ReqT, RespT> extends ForwardingServerCall.SimpleForwardingServerCall<ReqT, RespT>{

        InterceptorRespuesta(ServerCall serverCall) {
            super(serverCall);
        }

        @Override
        public void sendMessage(RespT message) {
            //Antes de enviar la respuesta, podemos loggear lo que enviamos, para debug
            System.out.println("Esta es la respuesta: "+message);

            super.sendMessage(message);
        }

        @Override
        public void sendHeaders(Metadata headers) {
            //Agregamos metadatos antes de enviar la respuesta. 
            headers.put(Metadata.Key.of("MI-METADATO", Metadata.ASCII_STRING_MARSHALLER),"HolaMundoServer");
            super.sendHeaders(headers);
        }
    }
}
