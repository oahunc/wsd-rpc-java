package com.dy.rpc.core.transport.server.socket;

import com.dy.rpc.common.entity.RpcRequest;
import com.dy.rpc.common.entity.RpcResponse;
import com.dy.rpc.core.codec.ObjectReader;
import com.dy.rpc.core.codec.ObjectWriter;
import com.dy.rpc.core.compress.Compress;
import com.dy.rpc.core.serializer.CommonSerializer;
import com.dy.rpc.core.transport.server.RequestHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

/**
 * 处理RpcRequest的工作线程
 *
 * @Author: chenyibai
 * @Date: 2021/1/19 15:44
 */
public class RequestHandlerThread implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(RequestHandlerThread.class);

    private Socket socket;
    private RequestHandler requestHandler;
    private CommonSerializer serializer;
    private Compress compress;

    public RequestHandlerThread(Socket socket, RequestHandler requestHandler, CommonSerializer serializer, Compress compress) {
        this.socket = socket;
        this.requestHandler = requestHandler;
        this.serializer = serializer;
        this.compress = compress;
    }

    @Override
    public void run() {
        try (InputStream inputStream = socket.getInputStream();
             OutputStream outputStream = socket.getOutputStream()) {
            RpcRequest rpcRequest = (RpcRequest) ObjectReader.readObject(inputStream);
            Object result = requestHandler.handle(rpcRequest);
            RpcResponse<Object> response = RpcResponse.success(result, rpcRequest.getRequestId());
            ObjectWriter.writeObject(outputStream, response, serializer, compress);
        } catch (IOException e) {
            logger.error("调用或发送时有错误发生：", e);
        }
    }

}
