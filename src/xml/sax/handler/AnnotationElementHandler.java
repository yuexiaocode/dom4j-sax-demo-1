package xml.sax.handler;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeSet;

import org.dom4j.ElementHandler;
import org.dom4j.ElementPath;
import org.dom4j.io.SAXReader;

import racoon.util.RadixUtils;

public abstract class AnnotationElementHandler implements ElementHandler {
	public enum Type {
		START(1), END(2), START_AND_END(START.value|END.value);
		private int value;

		private Type(int value) {
			this.value = value;
		}

		public static Type valueOf(int value) {
			switch (value) {
				case 1:	return START;
				case 2: return END;
				case 3: return START_AND_END;
			}
			return null;
		}

		public int value() {
			return this.value;
		}

		public boolean contains(Type other) {

			if (other == null)
				return false;
			else {
				return RadixUtils.allBitsGteq(this.value, other.value);
			}

		}
	}

	public static void main(String[] args) {
		Type.END.toString();
		System.out.println(Type.START.contains(Type.END));
		System.out.println(Type.END.contains(Type.START));
		System.out.println(Type.START_AND_END.contains(Type.END));
		System.out.println(Type.START_AND_END.contains(Type.START_AND_END));
	}

	public Map<String, TreeSet<HandlerMethod>> handlerMethodsMap;

	private class HandlerMethod implements Comparable<HandlerMethod> {
		private Method method;
		private int order;
		private Type type;

		public HandlerMethod(Method method, int order, Type type) {
			super();
			this.method = method;
			this.order = order;
			this.type = type;
		}

		@Override
		public int compareTo(HandlerMethod o) {

			return o == null ? -1 : this.order - o.order;
		}

	}

	public AnnotationElementHandler() {
		super();

	}


	@Inherited
	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.METHOD)
	public static @interface Handler {
		String value();

		Type type() default Type.START;

		int order() default 0;

	}

	@Override
	public final void onStart(ElementPath elementPath) {
		initScanAnnotation();
		onEvent(elementPath, Type.START);

	}

	@Override
	public final void onEnd(ElementPath elementPath) {
		initScanAnnotation();
		onEvent(elementPath, Type.END);
	}

	private void initScanAnnotation() {

		if (handlerMethodsMap != null)
			return;

		this.handlerMethodsMap = new HashMap<String, TreeSet<HandlerMethod>>();
		Class<?> clazz = this.getClass();
		Method[] declaredmethods = clazz.getDeclaredMethods();
		Method[] methods = clazz.getMethods();
		addMethods(declaredmethods);
		addMethods(methods);

	}

	private void onEvent(ElementPath elementPath, Type type) {
		handlerMethodsMap.keySet();
		TreeSet<HandlerMethod> handlerMethods = handlerMethodsMap
				.get(elementPath.getPath());
		if (handlerMethods != null) {
			for (HandlerMethod handlerMethod : handlerMethods) {
				if ((handlerMethod.type.value & type.value) > 0) {
					Method method = handlerMethod.method;
					try {
						boolean isAccessible = method.isAccessible();
						method.setAccessible(true);
						method.invoke(this, elementPath);
						method.setAccessible(isAccessible);

					} catch (Exception e) {
						throw new RuntimeException(e);
					}
				}
			}
			if (isDetach(elementPath)) {
				elementPath.getCurrent().detach();
			}
			;
		}
	}

	private void addMethods(Method[] methods) {
		for (Method method : methods) {

			if (method.getModifiers() == Modifier.PRIVATE)
				continue;
			Handler hander = method.getAnnotation(Handler.class);
			if (hander != null) {
				
				TreeSet<HandlerMethod> handlerMethod = handlerMethodsMap
						.get(hander.value());
				if (handlerMethod == null) {
					handlerMethod = new TreeSet<HandlerMethod>();
					handlerMethodsMap.put(hander.value(), handlerMethod);
				}
				handlerMethod.add(new HandlerMethod(method, hander.order(),
						hander.type()));
			}
		}
	}

	public boolean isDetach(ElementPath elementPath) {
		return false;
	}

	public static void addMultiPathHandle(SAXReader saxReader,
			AnnotationElementHandler handler, String... paths) {
		if (saxReader == null || handler == null || paths == null)
			return;
		for (String path : paths) {
			saxReader.addHandler(path, handler);
		}
	}
}
