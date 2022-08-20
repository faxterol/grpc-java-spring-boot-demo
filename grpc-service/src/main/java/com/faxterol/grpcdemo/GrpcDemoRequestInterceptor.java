package com.faxterol.grpcdemo;

import org.lognet.springboot.grpc.GRpcGlobalInterceptor;

import io.grpc.Metadata;
import io.grpc.ServerCall;
import io.grpc.ServerCall.Listener;
import io.grpc.ServerCallHandler;
import io.grpc.ServerInterceptor;

@GRpcGlobalInterceptor
public class GrpcDemoRequestInterceptor implements ServerInterceptor{

    @Override
    public <ReqT, RespT> Listener<ReqT> interceptCall(ServerCall<ReqT, RespT> call, Metadata headers,
            ServerCallHandler<ReqT, RespT> next) {
        
        //Con este interceptor obtenemos los metadatos antes de que la petición pase al metodo que lo va a ejecutar
        System.out.println("Interceptando la petición");
        String mimetadato = headers.get(Metadata.Key.of("MI-METADATO", Metadata.ASCII_STRING_MARSHALLER));
        System.out.println("Header del cliente: "+mimetadato);
        

        return next.startCall(call, headers);
    }
    
}
