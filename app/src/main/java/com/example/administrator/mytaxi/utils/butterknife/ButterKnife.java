package com.example.administrator.mytaxi.utils.butterknife;

import android.app.Activity;
import android.util.Log;
import android.view.View;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ButterKnife {
    public static void inject(Object object){
        ButterKnife butterKnife = new ButterKnife();
        try {
            butterKnife.parserField(object);
            butterKnife.parserMethod(object);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 解析成员注解
     * @param object
     * @throws Exception
     */
    private void parserField(Object object) throws Exception {
        View view = null;
        final Class clazz = object.getClass();
        Field[] fields = clazz.getDeclaredFields();
        for(Field field : fields){
            Log.d("jun","field.getName() : "+field.getName());

            if(field.isAnnotationPresent(ViewBinder.class)){
                ViewBinder inject = field.getAnnotation(ViewBinder.class);
                int id = inject.id();
                if (id < 0){
                    throw new Exception("id must not be null!!!");
                }
                if (id > 0){
                    field.setAccessible(true);
                    if(object instanceof View){
                        view = ((View) object).findViewById(id);
                    }else if (object instanceof Activity){
                        view = ((Activity) object).findViewById(id);
                    }
                    field.set(object,view);
                }
            }
        }
    }

    /**
     * 解析方法注解
     * @param object
     * @throws Exception
     */
    public void parserMethod( final Object object) throws Exception{

        View view = null;
        final Class clazz = object.getClass();
        Method[] methods = clazz.getDeclaredMethods();
        for(final Method method : methods){
            if(method.isAnnotationPresent(OnClick.class)){
                OnClick inject = method.getAnnotation(OnClick.class);
                int id = inject.id();
                if(id < 0){
                    throw new Exception("id must not be null!!!");
                }
                if(id > 0){
                    if(object instanceof View){
                        view = ((View) object).findViewById(id);
                    }else if (object instanceof Activity){
                        view = ((Activity) object).findViewById(id);
                    }
                    view.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            try {
                                method.invoke(object,null);
                            } catch (IllegalAccessException e) {
                                e.printStackTrace();
                            } catch (InvocationTargetException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }
            }
        }
    }
}
