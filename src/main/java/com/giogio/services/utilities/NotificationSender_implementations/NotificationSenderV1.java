package com.giogio.services.utilities.NotificationSender_implementations;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import com.giogio.services.utilities.NotificationSender;

@Primary
@Qualifier("notificationSenderV1")
@Component
public class NotificationSenderV1 implements NotificationSender{

	@Override
	public void notifyMessage(String message) {
		System.out.println("°°°°°°°°°°°°°°°-"+message+"-°°°°°°°°°°°°°°°");
		
	}

}
