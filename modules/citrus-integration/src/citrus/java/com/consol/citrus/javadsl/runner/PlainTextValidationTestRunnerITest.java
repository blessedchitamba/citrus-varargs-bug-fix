/*
 * Copyright 2006-2015 the original author or authors.
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

package com.consol.citrus.javadsl.runner;

import com.consol.citrus.annotations.CitrusTest;
import com.consol.citrus.dsl.definition.ReceiveMessageActionDefinition;
import com.consol.citrus.dsl.definition.SendMessageActionDefinition;
import com.consol.citrus.dsl.runner.TestActionConfigurer;
import com.consol.citrus.dsl.testng.TestNGCitrusTestRunner;
import com.consol.citrus.message.MessageType;
import org.testng.annotations.Test;

/**
 * @author Christoph Deppisch
 */
@Test
public class PlainTextValidationTestRunnerITest extends TestNGCitrusTestRunner {
    
    @CitrusTest
    public void PlainTextValidationTestRunnerITest() {
        parallel().actions(
            send(new TestActionConfigurer<SendMessageActionDefinition>() {
                @Override
                public void configure(SendMessageActionDefinition definition) {
                    definition.endpoint("httpClient")
                            .payload("Hello, World!");
                }
            }),
            sequential().actions(
                receive(new TestActionConfigurer<ReceiveMessageActionDefinition>() {
                    @Override
                    public void configure(ReceiveMessageActionDefinition definition) {
                        definition.endpoint("httpServerRequestEndpoint")
                                .messageType(MessageType.PLAINTEXT)
                                .payload("Hello, World!")
                                .extractFromHeader("citrus_jms_messageId", "correlation_id");
                    }
                }),
                send(new TestActionConfigurer<SendMessageActionDefinition>() {
                    @Override
                    public void configure(SendMessageActionDefinition definition) {
                        definition.endpoint("httpServerResponseEndpoint")
                                .payload("Hello, Citrus!")
                                .header("citrus_http_status_code", "200")
                                .header("citrus_http_version", "HTTP/1.1")
                                .header("citrus_http_reason_phrase", "OK")
                                .header("citrus_jms_correlationId", "${correlation_id}");
                    }
                })
            )
        );
        
        receive(new TestActionConfigurer<ReceiveMessageActionDefinition>() {
            @Override
            public void configure(ReceiveMessageActionDefinition definition) {
                definition.endpoint("httpClient")
                        .messageType(MessageType.PLAINTEXT)
                        .payload("Hello, Citrus!")
                        .header("citrus_http_status_code", "200")
                        .header("citrus_http_version", "HTTP/1.1")
                        .header("citrus_http_reason_phrase", "OK");
            }
        });
        
        send(new TestActionConfigurer<SendMessageActionDefinition>() {
            @Override
            public void configure(SendMessageActionDefinition definition) {
                definition.endpoint("httpClient")
                        .payload("Hello, World!");
            }
        });
        
        sleep(2000);
        
        receive(new TestActionConfigurer<ReceiveMessageActionDefinition>() {
            @Override
            public void configure(ReceiveMessageActionDefinition definition) {
                definition.endpoint("httpClient")
                        .messageType(MessageType.PLAINTEXT)
                        .header("citrus_http_status_code", "200")
                        .header("citrus_http_version", "HTTP/1.1")
                        .header("citrus_http_reason_phrase", "OK");
            }
        });
    }
}