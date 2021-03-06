/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.camel.component.paho;

import org.apache.camel.Exchange;
import org.apache.camel.support.DefaultProducer;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttMessage;

public class PahoProducer extends DefaultProducer {

    public PahoProducer(PahoEndpoint endpoint) {
        super(endpoint);
    }

    @Override
    public void process(Exchange exchange) throws Exception {
        MqttClient client = getEndpoint().getClient();

        String topic = exchange.getIn().getHeader(PahoConstants.CAMEL_PAHO_OVERRIDE_TOPIC, getEndpoint().getTopic(), String.class);
        int qos = exchange.getIn().getHeader(PahoConstants.CAMEL_PAHO_MSG_QOS, getEndpoint().getQos(), Integer.class);
        boolean retained = exchange.getIn().getHeader(PahoConstants.CAMEL_PAHO_MSG_RETAINED, getEndpoint().isRetained(), Boolean.class);
        byte[] payload = exchange.getIn().getBody(byte[].class);

        MqttMessage message = new MqttMessage(payload);
        message.setQos(qos);
        message.setRetained(retained);

        log.debug("Publishing to topic: {}, qos: {}, retrained: {}", topic, qos, retained);
        client.publish(topic, message);
    }

    @Override
    public PahoEndpoint getEndpoint() {
        return (PahoEndpoint)super.getEndpoint();
    }

}
