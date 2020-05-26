package com.android.toolkits.methodtime;

import android.util.Log;
import android.view.View;

import com.android.toolkits.ethodtime.R;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;

import java.lang.reflect.Method;

/**
 * @author: liujc
 * @date: 2020/3/16
 * @Description: TODO(这里用一句话描述这个类的作用)
 */
@Aspect
public class TraceAspect {

    private static final String TAG = TraceAspect.class.getSimpleName();

//    // View#setOnClickListener
//    private static final String POINTCUT_ON_VIEW_CLICK =
//            "execution(* android.view.View.OnClickListener.onClick(..))";
private static final int CHECK_FOR_DEFAULT_TIME = 500;


    @Pointcut("execution(@com.android.toolkits.methodtime.AddLog * *(..))")
    public void addLogPointcut() {

    }

    @Pointcut("execution(@com.android.toolkits.methodtime.ExecTime * *(..))")
    public void execTimePointcut() {

    }

    @Pointcut("execution(* android.app.Activity+.onCreate(..))")
    public void activityOnCreatePointcut() {

    }

    @Pointcut("execution(* android.app.Activity+.onDestroy(..))")
    public void activityDestroyPointcut() {

    }

    @Around("activityOnCreatePointcut()")
    public void pageOpen(ProceedingJoinPoint joinPoint) throws Throwable {
        Object target = joinPoint.getTarget();
        String className = target.getClass().getName();
        TrackPoint.onPageOpen(className);
        joinPoint.proceed();
    }

    @Around("activityDestroyPointcut()")
    public void pageClose(ProceedingJoinPoint joinPoint) throws Throwable {
        Object target = joinPoint.getTarget();
        String className = target.getClass().getName();
        TrackPoint.onPageClose(className);
        joinPoint.proceed();
    }

    @Around("addLogPointcut()")
    public void addLog(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        AddLog addLog = signature.getMethod().getAnnotation(AddLog.class);
        if (addLog != null) {
            Object target = joinPoint.getTarget();
            String className = "";
            if (target != null) {
                className = target.getClass().getName();
            }
            Log.d(TAG, "start execute:" + className + "-" + signature.getMethod().getName());
            joinPoint.proceed();
            Log.d(TAG, "end execute:" + className + "-" + signature.getMethod().getName());
        } else {
            joinPoint.proceed();
        }
    }

    @Before("execTimePointcut()")
    public void beforeExecTime(JoinPoint joinPoint) throws Throwable{
        String key = joinPoint.getSignature().toString();
        // 在joinPoint.proceed()之前
        Log.d(TAG, "beforeExecTime -> " + key);
    }

    @Around("execTimePointcut()")
    public void execTime(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        ExecTime execTime = signature.getMethod().getAnnotation(ExecTime.class);
        if (execTime != null) {
            long start = System.currentTimeMillis();
            joinPoint.proceed();
            long end = System.currentTimeMillis();
            Object target = joinPoint.getTarget();
            String className = "";
            if (target != null) {
                className = target.getClass().getName();
            }
            Log.d(TAG,
                    "execute time:" + className + "-" + signature.getMethod().getName() + " : " + (end - start) + "ms");
        } else {
            joinPoint.proceed();
        }
    }

    @After("execTimePointcut()")
    public void afterExecTime(JoinPoint joinPoint){
        String key = joinPoint.getSignature().toString();
        // 在joinPoint.proceed()之后
        Log.d(TAG, "afterExecTime -> " + key);
    }

    // 点击事件

    @Pointcut("execution(* onClick(..))")
    public void onClickPointcut() {

    }
    @Around("onClickPointcut()")
    public void execJoinPointonClick(ProceedingJoinPoint joinPoint) throws Throwable {
        Object target = joinPoint.getTarget();
        String className = "";
        if (target != null) {
            className = target.getClass().getName();
        }
        Object[] args = joinPoint.getArgs();
        if (args.length > 0 && args[0] instanceof View) {
            View view = (View) args[0];
            String entryName = view.getResources().getResourceEntryName(view.getId());
            TrackPoint.onClick(className, entryName);
        }
        joinPoint.proceed();
    }


//    @Around("onClickPointcut()")
    public void processJoinPoint(ProceedingJoinPoint joinPoint) throws Throwable {
        Log.d(TAG, "-----method is click--- ");
        try {
            Signature signature = joinPoint.getSignature();
            if (!(signature instanceof MethodSignature)){
                Log.d(TAG, "method is no MethodSignature, so proceed it");
//                joinPoint.proceed();
                execJoinPoint(joinPoint);
                return;

            }

            MethodSignature methodSignature = (MethodSignature) signature;
            Method method = methodSignature.getMethod();
            boolean isHasLimitAnnotation = method.isAnnotationPresent(ClickLimit.class);
            String methodName = method.getName();
            int intervalTime = CHECK_FOR_DEFAULT_TIME;
            if (isHasLimitAnnotation){
                ClickLimit clickLimit = method.getAnnotation(ClickLimit.class);
                int limitTime = clickLimit.value();
                // not limit click
                if (limitTime <= 0){
                    Log.d(TAG, "method: " + methodName + " limitTime is zero, so proceed it");
//                    joinPoint.proceed();
                    execJoinPoint(joinPoint);
                    return;
                }
                intervalTime = limitTime;
                Log.d(TAG, "methodName " +  methodName + " intervalTime is " + intervalTime);
            }


            Object[] args = joinPoint.getArgs();
            View view = getViewFromArgs(args);
            if (view == null) {
                Log.d(TAG, "view is null, proceed");
//                joinPoint.proceed();
                execJoinPoint(joinPoint);
                return;
            }

            Object viewTimeTag =  view.getTag(R.integer.view_onclick_limit_tag);
            // first click viewTimeTag is null.
            if (viewTimeTag == null){
                Log.d(TAG, "lastClickTime is zero , proceed");
                proceedAnSetTimeTag(joinPoint, view);
                return;
            }

            long lastClickTime = (long) viewTimeTag;
            if (lastClickTime <= 0){
                Log.d(TAG, "lastClickTime is zero , proceed");
                proceedAnSetTimeTag(joinPoint, view);
                return;
            }

            // in limit time
            if (!canClick(lastClickTime, intervalTime)){
                Log.d(TAG, "is in limit time , return");
                return;

            }
            proceedAnSetTimeTag(joinPoint, view);
            Log.d(TAG, "view proceed.");
        } catch (Throwable e) {
            e.printStackTrace();
            Log.d(TAG, e.getMessage());
//            joinPoint.proceed();
            execJoinPoint(joinPoint);
        }
    }

    public void proceedAnSetTimeTag(ProceedingJoinPoint joinPoint, View view) throws Throwable {
        view.setTag(R.integer.view_onclick_limit_tag, System.currentTimeMillis());
//        joinPoint.proceed();
        execJoinPoint(joinPoint);
    }

    private void execJoinPoint(ProceedingJoinPoint joinPoint) throws Throwable{
        joinPoint.proceed();
    }

    /**
     * 获取 view 参数
     *
     * @param args
     * @return
     */
    public View getViewFromArgs(Object[] args) {
        if (args != null && args.length > 0) {
            Object arg = args[0];
            if (arg instanceof View) {
                return (View) arg;
            }
        }
        return null;
    }

    /**
     * 判断是否达到可以点击的时间间隔
     *
     * @param lastClickTime
     * @return
     */
    public boolean canClick(long lastClickTime, int intervalTime) {
        long currentTime = System.currentTimeMillis();
        long realIntervalTime  = currentTime - lastClickTime;
        Log.d(TAG, "canClick currentTime= " + currentTime + " lastClickTime= " + lastClickTime +
                " realIntervalTime= " + realIntervalTime);
        return realIntervalTime >= intervalTime;
    }

}
