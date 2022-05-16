package example;

import net.bytebuddy.ByteBuddy;
import net.bytebuddy.dynamic.scaffold.InstrumentedType;
import net.bytebuddy.implementation.Implementation;
import net.bytebuddy.implementation.InvocationHandlerAdapter;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.implementation.bind.annotation.*;
import net.bytebuddy.implementation.bytecode.ByteCodeAppender;
import net.bytebuddy.matcher.ElementMatcher;
import net.bytebuddy.matcher.ElementMatchers;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.nio.ByteBuffer;

public class App {

    public static class BarkInterceptor {
        @RuntimeType
        public static Object intercept(
                @This Object self,
                @Origin Method method,
                @AllArguments Object[] args,
                @SuperMethod Method superMethod
        ) throws Throwable {
            System.out.println("Before......");
            Object result = superMethod.invoke(self, args);
            System.out.println("After......");
            return result;
        }
    }

    public static void main(String[] args) throws Exception{
        Class<?> dynamicDog = new ByteBuddy()
                .subclass(Dog.class)
                .method(ElementMatchers.named("bark"))
                .intercept(MethodDelegation.to(BarkInterceptor.class))
                .make()
                .load(App.class.getClassLoader())
                .getLoaded();

        Dog dog = (Dog)dynamicDog.getDeclaredConstructor().newInstance();
        dog.bark("Woof");
    }
}
