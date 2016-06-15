/*
 * Copyright 2002-2012 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


/* Aggregate quotes and generate a single quote message. The method will be called 
 * if either the release-strategy-expression condition is true or the messagestore's 
 * timeout is exceeded. Ultimately the goal is create a single message that simulates
 * some fictitious market activity while minimizing row level contention in the database
 * by conlfating the multiple quote updates.
 *
 * 
 * @author Brian Dussault
 */

package org.springframework.nanotrader.asynch.aggregator;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.Message;
import org.springframework.nanotrader.data.domain.Quote;

import java.util.Date;
import java.util.List;

public class QuoteAggregator { 
	private static Logger log = LogManager.getLogger(QuoteAggregator.class);
	

	 
	public Message<Quote> aggregate(List<Message<?>> messages) {
		log.info("Aggregator released " + messages.size() + " message(s) at: " + new Date(System.currentTimeMillis()));
		//Just use the last message in the List of Messages
		Quote quote = (Quote) messages.get(messages.size() -1).getPayload();
		Message<Quote> aggregatedMessage = MessageBuilder.withPayload(quote).build();
		if (log.isDebugEnabled()) { 
			log.debug("QuoteAggregator.aggregate() released aggregate quote = " + quote);
		}
		return aggregatedMessage;
	}
}
