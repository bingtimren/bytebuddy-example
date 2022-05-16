package example;

import net.bytebuddy.ByteBuddy;
import net.bytebuddy.dynamic.ClassFileLocator;
import net.bytebuddy.dynamic.loading.ClassLoadingStrategy;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.implementation.bind.annotation.*;
import net.bytebuddy.matcher.ElementMatchers;
import net.bytebuddy.pool.TypePool;

import java.lang.reflect.Method;

public class App {

    public static class BarkInterceptor {
        @RuntimeType
        public static void intercept(
                @This Object self,
                @Origin Method method,
                @AllArguments Object[] args,
                @SuperMethod Method superMethod
        ) throws Throwable {
            System.out.println("Before......");
            superMethod.invoke(self, (String)args[0]+","+args[0]);
            System.out.println("After......");
        }
    }

    public static void main(String[] args) throws Exception{
        // powerful stuff, rebase a class definition!

        TypePool typePool = TypePool.Default.ofSystemLoader();
        new ByteBuddy()
                .rebase(typePool.describe("example.Dog").resolve(),
                        ClassFileLocator.ForClassLoader.ofSystemLoader())
                .method(ElementMatchers.named("bark"))
                .intercept(MethodDelegation.to(BarkInterceptor.class))
                .make()
                .load(App.class.getClassLoader(), ClassLoadingStrategy.Default.INJECTION);


        Dog dog = new Dog();
        dog.bark("Woof");
    }
}
