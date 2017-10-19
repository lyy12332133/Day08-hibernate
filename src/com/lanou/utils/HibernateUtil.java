package com.lanou.utils;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

/**
 * Created by 蓝鸥科技有限公司  www.lanou3g.com.
 */
public class HibernateUtil {
    private static Configuration cfg;

    //声明成员变量
    private static SessionFactory sessionFactory;

    //创建ThreadLocal用来保存session对象
    private static final ThreadLocal<Session> threadLocal = new ThreadLocal<>();

    //hibernate配置文件的路径
    public static final String HIBERNATE_CFG_PATH = "hibernate.cfg.xml";

    //使用静态代码块初始化Hibernate
    static {
        /**
         * 静态代码块会随着类的加载而执行,且只会执行一次
         * 所以当程序第一次调用HibernateUtil类时,
         * 这个静态代码块会执行一次,建立SessionFactory对象
         */


        //创建Configuration对象后,调用configure方法,
        // 该方法就去加载了src下的hibernate.cfg.xml配置文件
        cfg = new Configuration().configure(HIBERNATE_CFG_PATH);

        //创建SessionFactory对象
        sessionFactory = cfg.buildSessionFactory();
    }

    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    /**
     * 静态方法,用来获得Session对象
     *
     * @return
     */
    public static Session getSession() {
        Session session = threadLocal.get();
        if (session == null || !session.isOpen()) {

            //如果SessionFactory为null,说明初始化出错了,重新来一遍
            if (sessionFactory == null) {
                rebuildSessionFactory();
            }

            //通过SessionFactory对象创建Session对象
            if (sessionFactory != null) {
                session = sessionFactory.openSession();
            }

            //将新打开的Session对象,保存到线程局部变量threadLocal中
            threadLocal.set(session);
        }

        return session;
    }

    /**
     * 关闭session对象
     */
    public static void closeSession() {
        /**
         * 从ThreadLocal中,取出之前存入的session实例
         */
        Session session = threadLocal.get();

        //当前线程先存一个null进去,这样就减少了一个session的引用
        //下次ThreadLocal就获得不到了
        threadLocal.set(null);

        if (session != null) {
            //在调用session的关闭方法,关闭session
            session.close();
        }
    }

    /**
     * 重建SessionFactory
     */
    public static void rebuildSessionFactory() {
        //读取配置文件
        cfg.configure(HIBERNATE_CFG_PATH);
        //创建SessionFactory对象
        sessionFactory = cfg.buildSessionFactory();

    }

    /**
     * 关闭缓存和连接池
     */
    public static void shutdown() {
        getSessionFactory().close();
    }
}
