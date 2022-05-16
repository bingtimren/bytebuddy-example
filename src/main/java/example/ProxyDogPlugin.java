package example;

import net.bytebuddy.build.Plugin;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.dynamic.ClassFileLocator;
import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.dynamic.loading.ClassLoadingStrategy;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.implementation.bind.annotation.*;
import net.bytebuddy.matcher.ElementMatchers;

import java.io.IOException;
import java.lang.reflect.Method;

public class ProxyDogPlugin implements Plugin, Plugin.Factory {


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


    @Override
    public DynamicType.Builder<?> apply(DynamicType.Builder<?> builder, TypeDescription typeDescription, ClassFileLocator classFileLocator) {
        return builder
                .method(ElementMatchers.named("bark"))
                .intercept(MethodDelegation.to(BarkInterceptor.class))
                ;
    }

    @Override
    public void close() throws IOException {

    }

    @Override
    public boolean matches(TypeDescription target) {
        boolean result = target.represents(Dog.class);
        System.out.println("Examining "+target+": "+result);
        return result;
    }

    @Override
    public Plugin make() {
        return this;
    }
}
