package com.ezlippi;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class Demo {
    /**
     * 自己优先加载的类加载器,只加载指定类,其他委托给父加载器 
     */
    private static class CustomClassLoader extends ClassLoader {
        private Set<String> urls;
        private String label;

        public CustomClassLoader(String name, ClassLoader parent, String... url) {
            super(parent);
            this.label = name;
            this.urls = new HashSet<String>(Arrays.asList(url));
        }

        @Override
        public Class<?> findClass(String name) throws ClassNotFoundException {
            if (urls.contains(name)) {
                try {
                    String location = name.replace('.', '/') + ".class";
                    InputStream inputStream = Demo.class.getClassLoader().getResourceAsStream(location);
                    byte[] buf = new byte[2000];
                    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                    int length; 
                    while ((length = inputStream.read(buf)) != -1) {
                        outputStream.write(buf, 0 , length);
                    }
                    //流的关闭应该放到finally块,这里为了演示简化了
                    inputStream.close();
                    System.out.println(label + ": Loading " + name + " in " +
                            label + " classloader");
                    byte[] data = outputStream.toByteArray();
                    return defineClass(name, data, 0, data.length);
                } catch (IOException e) {
                    throw new ClassNotFoundException(name, e);
                }  
            }

            throw new ClassNotFoundException(name);
        }

        public Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
            if (findLoadedClass(name) != null) {
                System.out.println(label + ": already loaded(" + name + ")");
                return findLoadedClass(name);
            }

            // 
            if (urls.contains(name)) {
                return findClass(name);
            } else {
                System.out.println(label + ": super.loadclass(" + name + ")");
                return super.loadClass(name, resolve);
            }
        }

        public String toString() {
            return label;
        }
    }


    public static class User {
    }

    public static class LoginService {
        static {
            System.out.println("LoginService loaded");
        }
        public static void login(User user) {
        }
    }

    public static class Servlet {
        public static void doGet() {
            User user = new User();
            System.out.println("Logging in with User loaded in " + user.getClass().getClassLoader());
            LoginService.login(user);
        }
    }

    public static void test1() throws Exception {
        //beanLoader只加载User和LoginService类
        CustomClassLoader beanLoader = new CustomClassLoader("BeanLoader  ",
                Demo.class.getClassLoader(),
                "com.ezlippi.Demo$User", "com.ezlippi.Demo$LoginService");
        //webLoader只加载Servlet类,父加载器为beanLoader
        CustomClassLoader webLoder = new CustomClassLoader("WebLoader",
                beanLoader, "com.ezlippi.Demo$Servlet");
        //webBeanLoader加载User和Servlet类,父加载器也为beanLoader
        CustomClassLoader webBeanLoader = new CustomClassLoader("webBeanLoader", beanLoader,
                "com.ezlippi.Demo$User", "com.ezlippi.Demo$Servlet");
        System.out.println("Loading BeanLoader");
        beanLoader.loadClass("com.ezlippi.Demo$LoginService", true).newInstance();

        System.out.println("Logging in, self-first");
        webBeanLoader.loadClass("com.ezlippi.Demo$Servlet", false).
                getMethod("doGet").invoke(null);

        System.out.println("check methods of LoginService");
        //调用getMethods()时LoginService和User类建立了引用关系,所以会去加载User类
//        beanLoader.loadClass("com.ezlippi.Demo$LoginService", false)
//                .getMethods();

        System.out.println("Logging in");
        webLoder.loadClass("com.ezlippi.Demo$Servlet", false).
                getMethod("doGet").invoke(null);
    }

    public static void main(String[] args) {
        try {
            test1();
        } catch (Throwable e) {
            e.printStackTrace(System.out);
        }
    }
}
