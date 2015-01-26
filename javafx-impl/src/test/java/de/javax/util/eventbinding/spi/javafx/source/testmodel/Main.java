package de.javax.util.eventbinding.spi.javafx.source.testmodel;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.lang.reflect.Type;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.event.EventType;

public class Main {

    public static void main(String[] args) {
        EventHandler handler = (EventHandler) Proxy.newProxyInstance(Main.class.getClassLoader(),
                new Class<?>[] { EventHandler.class }, new InvocationHandler() {

                    @Override
                    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                        if (method.getName().equals("handle")) {
                        }
                        // TODO Auto-generated method stub
                        return null;
                    }
                });
        try {
            Method method = Node.class.getMethod("addEventHandler", EventType.class, EventHandler.class);

            EventType type = new EventType();

            Type[] types = method.getGenericParameterTypes();
            System.out.println(method);
        } catch (NoSuchMethodException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (SecurityException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    static class Node {
        public final <T extends Event> void addEventHandler(EventType<T> eventType, EventHandler<? super T> eventHandler) {

        }

        public final <T extends Event> void removeEventHandler(EventType<T> eventType,
                EventHandler<? super T> eventHandler) {

        }
    }
}
