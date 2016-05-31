package com.spartacus.solitude.utils;

import rx.Subscription;

/**
 * Created by ryan on 5/30/16.
 */
public class SubscriptionUtils {

    public static void unsubscribe(Subscription... subscriptions) {
        if (subscriptions == null) {
            return;
        }

        for (Subscription subscription : subscriptions) {
            if (subscription != null && !subscription.isUnsubscribed()) {
                subscription.unsubscribe();
            }
        }
    }
}
