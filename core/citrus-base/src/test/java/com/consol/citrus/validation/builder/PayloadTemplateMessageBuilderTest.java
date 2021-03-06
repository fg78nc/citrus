/*
 * Copyright 2006-2011 the original author or authors.
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

package com.consol.citrus.validation.builder;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.consol.citrus.CitrusSettings;
import com.consol.citrus.message.Message;
import com.consol.citrus.message.MessageType;
import com.consol.citrus.testng.AbstractTestNGUnitTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

public class PayloadTemplateMessageBuilderTest extends AbstractTestNGUnitTest {

    private final String variablePayloadResource = "classpath:com/consol/citrus/validation/builder/variable-data-resource.txt";
    private final String initialVariableTestPayload = "{ \"person\": { \"name\": \"${name}\", \"age\": 20} }";
    private final String resultingVariableTestPayload = "{ \"person\": { \"name\": \"Frauke\", \"age\": 20} }";

    private PayloadTemplateMessageBuilder messageBuilder;

    @BeforeMethod
    public void setUp() {
        messageBuilder = new PayloadTemplateMessageBuilder();
        messageBuilder.setPayloadData("TestMessagePayload");

        context.getMessageProcessors().setMessageProcessors(Collections.emptyList());
    }

    @Test
    public void testMessageBuilder() {
        Message resultingMessage = messageBuilder.buildMessageContent(context, CitrusSettings.DEFAULT_MESSAGE_TYPE);

        assertEquals(resultingMessage.getPayload(), "TestMessagePayload");
    }

    @Test
    public void testMessageBuilderVariableSupport() {
        messageBuilder.setPayloadData("This ${placeholder} contains variables!");
        context.setVariable("placeholder", "payload data");

        Message resultingMessage = messageBuilder.buildMessageContent(context, CitrusSettings.DEFAULT_MESSAGE_TYPE);

        assertEquals(resultingMessage.getPayload(), "This payload data contains variables!");
    }

    @Test
    public void testMessageBuilderWithPayloadResource() {
        String textPayloadResource = "classpath:com/consol/citrus/validation/builder/payload-data-resource.txt";

        messageBuilder = new PayloadTemplateMessageBuilder();
        messageBuilder.setPayloadResourcePath(textPayloadResource);

        Message resultingMessage = messageBuilder.buildMessageContent(context, CitrusSettings.DEFAULT_MESSAGE_TYPE);

        assertEquals(resultingMessage.getPayload(), "TestMessageData");
    }

    @Test
    public void testMessageBuilderWithPayloadResourceVariableSupport() {
        messageBuilder = new PayloadTemplateMessageBuilder();

        messageBuilder.setPayloadResourcePath(variablePayloadResource);
        context.setVariable("placeholder", "payload data");

        Message resultingMessage = messageBuilder.buildMessageContent(context, CitrusSettings.DEFAULT_MESSAGE_TYPE);

        assertEquals(resultingMessage.getPayload(), "This payload data contains variables!");
    }

    @Test
    public void testMessageBuilderWithHeaders() {
        Map<String, Object> headers = new HashMap<>();
        headers.put("operation", "unitTesting");
        messageBuilder.setMessageHeaders(headers);

        Message resultingMessage = messageBuilder.buildMessageContent(context, CitrusSettings.DEFAULT_MESSAGE_TYPE);

        assertEquals(resultingMessage.getPayload(), "TestMessagePayload");
        assertNotNull(resultingMessage.getHeader("operation"));
        assertEquals(resultingMessage.getHeader("operation"), "unitTesting");
    }

    @Test
    public void testMessageBuilderWithHeaderTypes() {
        Map<String, Object> headers = new HashMap<>();
        headers.put("intValue", "{integer}:5");
        headers.put("longValue", "{long}:5");
        headers.put("floatValue", "{float}:5.0");
        headers.put("doubleValue", "{double}:5.0");
        headers.put("boolValue", "{boolean}:true");
        headers.put("shortValue", "{short}:5");
        headers.put("byteValue", "{byte}:1");
        headers.put("stringValue", "{string}:5.0");
        messageBuilder.setMessageHeaders(headers);

        Message resultingMessage = messageBuilder.buildMessageContent(context, CitrusSettings.DEFAULT_MESSAGE_TYPE);

        assertEquals(resultingMessage.getPayload(), "TestMessagePayload");
        assertNotNull(resultingMessage.getHeader("intValue"));
        assertEquals(resultingMessage.getHeader("intValue"), 5);
        assertNotNull(resultingMessage.getHeader("longValue"));
        assertEquals(resultingMessage.getHeader("longValue"), 5L);
        assertNotNull(resultingMessage.getHeader("floatValue"));
        assertEquals(resultingMessage.getHeader("floatValue"), 5.0f);
        assertNotNull(resultingMessage.getHeader("doubleValue"));
        assertEquals(resultingMessage.getHeader("doubleValue"), 5.0);
        assertNotNull(resultingMessage.getHeader("boolValue"));
        assertEquals(resultingMessage.getHeader("boolValue"), Boolean.TRUE);
        assertNotNull(resultingMessage.getHeader("shortValue"));
        assertEquals(resultingMessage.getHeader("shortValue"), new Short("5"));
        assertNotNull(resultingMessage.getHeader("byteValue"));
        assertEquals(resultingMessage.getHeader("byteValue"), new Byte("1"));
        assertNotNull(resultingMessage.getHeader("stringValue"));
        assertEquals(resultingMessage.getHeader("stringValue"), "5.0");
    }

    @Test
    public void testMessageBuilderWithHeadersVariableSupport() {
        Map<String, Object> headers = new HashMap<>();
        headers.put("operation", "${operation}");
        messageBuilder.setMessageHeaders(headers);

        context.setVariable("operation", "unitTesting");

        Message resultingMessage = messageBuilder.buildMessageContent(context, CitrusSettings.DEFAULT_MESSAGE_TYPE);

        assertEquals(resultingMessage.getPayload(), "TestMessagePayload");
        assertNotNull(resultingMessage.getHeader("operation"));
        assertEquals(resultingMessage.getHeader("operation"), "unitTesting");
    }

    @Test
    public void testMessageBuilderWithHeaderData() {
        messageBuilder.getHeaderData().add("MessageHeaderData");

        Message resultingMessage = messageBuilder.buildMessageContent(context, CitrusSettings.DEFAULT_MESSAGE_TYPE);

        assertEquals(resultingMessage.getPayload(), "TestMessagePayload");
        assertEquals(resultingMessage.getHeaderData().size(), 1L);
        assertEquals(resultingMessage.getHeaderData().get(0), "MessageHeaderData");
    }

    @Test
    public void testMessageBuilderWithMultipleHeaderData() {
        messageBuilder.getHeaderData().add("MessageHeaderData1");
        messageBuilder.getHeaderData().add("MessageHeaderData2");

        Message resultingMessage = messageBuilder.buildMessageContent(context, CitrusSettings.DEFAULT_MESSAGE_TYPE);

        assertEquals(resultingMessage.getPayload(), "TestMessagePayload");
        assertEquals(resultingMessage.getHeaderData().size(), 2L);
        assertEquals(resultingMessage.getHeaderData().get(0), "MessageHeaderData1");
        assertEquals(resultingMessage.getHeaderData().get(1), "MessageHeaderData2");
    }

    @Test
    public void testMessageBuilderWithHeaderDataVariableSupport() {
        messageBuilder.getHeaderData().add("This ${placeholder} contains variables!");
        context.setVariable("placeholder", "header data");

        Message resultingMessage = messageBuilder.buildMessageContent(context, CitrusSettings.DEFAULT_MESSAGE_TYPE);

        assertEquals(resultingMessage.getPayload(), "TestMessagePayload");
        assertEquals(resultingMessage.getHeaderData().size(), 1L);
        assertEquals(resultingMessage.getHeaderData().get(0), "This header data contains variables!");
    }

    @Test
    public void testMessageBuilderWithHeaderResource() {
        String headerResource = "classpath:com/consol/citrus/validation/builder/header-data-resource.txt";
        messageBuilder.getHeaderResources().add(headerResource);

        Message resultingMessage = messageBuilder.buildMessageContent(context, CitrusSettings.DEFAULT_MESSAGE_TYPE);

        assertEquals(resultingMessage.getPayload(), "TestMessagePayload");
        assertEquals(resultingMessage.getHeaderData().size(), 1L);
        assertEquals(resultingMessage.getHeaderData().get(0), "MessageHeaderData");
    }

    @Test
    public void testMessageBuilderWithHeaderResourceVariableSupport() {
        messageBuilder.getHeaderResources().add(variablePayloadResource);
        context.setVariable("placeholder", "header data");

        Message resultingMessage = messageBuilder.buildMessageContent(context, CitrusSettings.DEFAULT_MESSAGE_TYPE);

        assertEquals(resultingMessage.getPayload(), "TestMessagePayload");
        assertEquals(resultingMessage.getHeaderData().size(), 1L);
        assertEquals(resultingMessage.getHeaderData().get(0), "This header data contains variables!");
    }

    @Test
    public void testVariablesInMessagePayloadsAreReplaced(){

        //GIVEN
        context.setVariable("name", "Frauke");
        messageBuilder.setPayloadData(initialVariableTestPayload);

        //WHEN
        final Message message = messageBuilder.buildMessageContent(context, MessageType.JSON.name());

        //THEN
        assertEquals(message.getPayload(), resultingVariableTestPayload);
    }
}
