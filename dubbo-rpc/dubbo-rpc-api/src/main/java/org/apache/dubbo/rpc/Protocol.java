/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.dubbo.rpc;

import org.apache.dubbo.common.URL;
import org.apache.dubbo.common.extension.Adaptive;
import org.apache.dubbo.common.extension.SPI;

import java.util.Collections;
import java.util.List;

/**
 * Protocol. (API/SPI, Singleton, ThreadSafe)
 */
@SPI("dubbo")
public interface Protocol {

    int getDefaultPort(); // 默认端口

    // 将一个Invoker发布出去，export()方法实现需要是幂等的，
    // 即同一个服务暴露多次和暴露一次的效果是相同的
    /*
    Protocol的export方法是标注了@Adaptive注解的，因此会生成代理类，然后代理类会根据Invoker里面的  URL参数  得知具体的协议
    然后通过Dubbo SPI机制选择对应的实现类进行export，而这个方法就会调用对应实现，如InjvmProtocol#export 方法。
    原文链接：https://blog.csdn.net/weixin_41605937/article/details/115376526
     */
    @Adaptive
    <T> Exporter<T> export(Invoker<T> invoker) throws RpcException;

    // 引用一个Invoker，refer()方法会根据参数返回一个Invoker对象，
    // Consumer端可以通过这个Invoker请求到Provider端的服务
    @Adaptive
    <T> Invoker<T> refer(Class<T> type, URL url) throws RpcException;

    // 销毁export()方法以及refer()方法使用到的Invoker对象，释放
    // 当前Protocol对象底层占用的资源
    void destroy();

    // 返回当前Protocol底层的全部ProtocolServer
    default List<ProtocolServer> getServers() {
        return Collections.emptyList();
    }

}