/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.fineract.infrastructure.hooks.processor;

import static org.apache.fineract.infrastructure.hooks.api.HookApiConstants.queueName;
import static org.apache.fineract.infrastructure.hooks.api.HookApiConstants.queueURLName;
import static org.apache.fineract.infrastructure.hooks.api.HookApiConstants.queueUserName;
import static org.apache.fineract.infrastructure.hooks.api.HookApiConstants.queuePassword;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import javax.jms.ConnectionFactory;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.fineract.infrastructure.hooks.domain.Hook;
import org.apache.fineract.infrastructure.hooks.domain.HookConfiguration;
import org.apache.fineract.useradministration.domain.AppUser;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.support.converter.MappingJackson2MessageConverter;
import org.springframework.jms.support.converter.MessageConverter;
import org.springframework.jms.support.converter.MessageType;
import org.springframework.stereotype.Service;

import retrofit.Callback;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

@Service
public class QueueHookProcessor implements HookProcessor {

	@Override
	public void process(final Hook hook,
			@SuppressWarnings("unused") final AppUser appUser,
			final String payload, final String entityName,
			final String actionName, final String tenantIdentifier,
			final String authToken) {

		final Set<HookConfiguration> config = hook.getHookConfig();

		String url = "";
		String queueNm = "";
		String userName = "";
		String password = "";
		for (final HookConfiguration conf : config) {
			final String fieldName = conf.getFieldName();
			if (fieldName.equals(queueURLName)) {
				url = conf.getFieldValue();
			}
			if (fieldName.equals(queueName)) {
			  queueNm = conf.getFieldValue();
			}
			if (fieldName.equals(queueUserName)) {
              userName = conf.getFieldValue();
            }
			if (fieldName.equals(queuePassword)) {
              password = conf.getFieldValue();
            }
		}
		ConnectionFactory conn = connectionFactory(url, userName, password);
		JmsTemplate template = new JmsTemplate();
        template.setConnectionFactory(conn);
        template.setMessageConverter(jacksonJmsMessageConverter());
        template.setPubSubDomain(true);
		sendRequest(url, queueNm, payload, template,
				tenantIdentifier, authToken);

	}

	@SuppressWarnings("unchecked")
	private void sendRequest(final String url, final String queueNm,
			final String payload, final JmsTemplate template,
			final String tenantIdentifier,
			@SuppressWarnings("unused") final String authToken) {

//		final String fineractEndpointUrl = System.getProperty("baseUrl");
//		
//		final WebHookService service = ProcessorHelper
//				.createWebHookService(url);
//
//		@SuppressWarnings("rawtypes")
//		final Callback callback = ProcessorHelper.createCallback(url);

		//final JsonObject json = new JsonParser().parse(payload).getAsJsonObject();
//		service.sendQueueRequest("", "", tenantIdentifier,
//					fineractEndpointUrl, queueNm, payload, callback);
		template.convertAndSend(queueNm, payload);
	}
	
    public ConnectionFactory connectionFactory(String brokerUrl, String userName, String password){
        ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory();
        connectionFactory.setBrokerURL(brokerUrl);
        connectionFactory.setUserName(userName);
        connectionFactory.setPassword(password);
        return connectionFactory;
    }
    
    // Serialize message content to json using TextMessage
    public MessageConverter jacksonJmsMessageConverter() {
        MappingJackson2MessageConverter converter = new MappingJackson2MessageConverter();
        converter.setTargetType(MessageType.TEXT);
        converter.setTypeIdPropertyName("_type");
        return converter;
    }
}
