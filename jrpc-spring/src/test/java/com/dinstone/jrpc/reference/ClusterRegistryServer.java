/*
 * Copyright (C) 2014~2016 dinstone<dinstone@163.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.dinstone.jrpc.reference;

import java.io.IOException;

import com.dinstone.jrpc.demo.HelloService;
import com.dinstone.jrpc.demo.HelloServiceImpl;
import com.dinstone.jrpc.mina.MinaServer;
import com.dinstone.jrpc.srd.zookeeper.RegistryDiscoveryConfig;
import com.dinstone.jrpc.srd.zookeeper.ZookeeperServiceRegistry;
import com.dinstone.jrpc.transport.TransportConfig;

public class ClusterRegistryServer {

    public static void main(String[] args) {

        RegistryDiscoveryConfig registryConfig = new RegistryDiscoveryConfig();
        registryConfig.set("zookeeper.node.list", "localhost:2181");

        ZookeeperServiceRegistry serviceRegistry = new ZookeeperServiceRegistry(registryConfig);

        MinaServer server = new MinaServer("localhost", 9090, new TransportConfig(), serviceRegistry);

        // ImplementBinding implementBinding = new DefaultImplementBinding("localhost", 9090, serviceRegistry);
        //
        // DefaultServiceExporter exporter = new DefaultServiceExporter(implementBinding);

        server.regist(HelloService.class, "", 2000, new HelloServiceImpl());

        // MinaAcceptance acceptance = new MinaAcceptance(new TransportConfig(), implementBinding);
        // acceptance.bind();

        server.start();

        System.out.println("server start");

        try {
            System.in.read();
        } catch (IOException e) {
        }

        serviceRegistry.destroy();
        server.stop();

        // implementBinding.destroy();
        // acceptance.destroy();

        System.out.println("server stop");
    }

}
