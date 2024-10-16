package com.giogio.services.utilities.NotificationSender_implementations;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import com.giogio.services.utilities.NotificationSender;

@Primary
@Qualifier("notificationSender_V1")
@Component
public class NotificationSender_V1 implements NotificationSender{

	@Override
	public void notifyMessage(String message) {
		System.out.println("°°°°°°°°°°°°°°°-"+message+"-°°°°°°°°°°°°°°°");
		
	}

}
