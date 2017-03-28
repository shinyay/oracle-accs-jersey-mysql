package com.oracle.jp.shinyay.rest.util;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.Properties;

public class DatabaseManager<T> {
    private final static String JDBC_DRIVER;
    private final static String JDBC_URL;
    private final static String JDBC_USER;
    private final static String JDBC_PASSWORD;
    private final static boolean JDBC_ENV_FLG;

    private String puName = "default_pu";

    private Class<T> type;
    private EntityManagerFactory emf;

    static {
        JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
        JDBC_URL = "jdbc:mysql://" + System.getenv("MYSQLCS_CONNECT_STRING");
        JDBC_USER = System.getenv("MYSQLCS_USER_NAME");
        JDBC_PASSWORD = System.getenv("MYSQLCS_USER_PASSWORD");
        if (JDBC_USER == null) {
            JDBC_ENV_FLG = false;
        } else {
            JDBC_ENV_FLG = true;
        }
    }

    /**
     * Constructor<br>
     * Specify ENTITY-TYPE and PERSISTENCE-UNIT-NAME<br>
     * Ex: Databasemanager dm = new DatabaseManager(Employee.class, "employee_pu");
     *
     * @param type ENTITY-TPE
     * @param pu   PERSISTENCE-UNIT-NAME
     */
    public DatabaseManager(Class<T> type, String pu) {
        this.type = type;
        emf = Persistence.createEntityManagerFactory(pu);
    }

    /**
     * Constructor <br>
     * Specify ENTITY-TYPE<br>
     * PERSISTENCE-UNIT-NAME: default_pu
     * Ex: Databasemanager dm = new DatabaseManager(Employee.class);
     *
     * @param type ENTITY-TYPE
     */
    public DatabaseManager(Class<T> type) {
        this.type = type;
        System.out.println("PERSISTENCE_UNIT=" + puName);
        System.out.println("JDBC_ENV_FLG=" + JDBC_ENV_FLG);
        if (JDBC_ENV_FLG) {
            Properties props = new Properties();
            props.setProperty("javax.persistence.jdbc.url", JDBC_URL);
            props.setProperty("javax.persistence.jdbc.driver", JDBC_DRIVER);
            props.setProperty("javax.persistence.jdbc.user", JDBC_USER);
            props.setProperty("javax.persistence.jdbc.password", JDBC_PASSWORD);
            emf = Persistence.createEntityManagerFactory(puName, props);
        } else {
            emf = Persistence.createEntityManagerFactory(puName);
        }
        //em = emf.createEntityManager();

    }

    /**
     * Save Entity as New
     *
     * @param emp ENTITY for Saving
     * @return DatabaseManager
     */
    public DatabaseManager<T> persist(T emp) {
        EntityManager em = null;
        try {
            em = emf.createEntityManager();
            em.getTransaction().begin();
            em.persist(emp);
            em.getTransaction().commit();
        } catch (Exception e) {
            System.err.println("PERSIST_ERROR\n");

        } finally {
            if (em != null) {
                em.close();
            }
        }
        return this;

    }

    /**
     * Save Entity as Update
     *
     * @param emp ENTITY for Saving
     * @return DatabaseManager
     */
    public DatabaseManager<T> merge(T emp) {
        EntityManager em = null;
        try {
            em = emf.createEntityManager();
            em.getTransaction().begin();
            em.merge(emp);
            em.getTransaction().commit();
        } catch (Exception e) {
            System.err.println("MERGE_ERROR\n");

        } finally {
            if (em != null) {
                em.close();
            }
        }
        return this;
    }

    /**
     * Delete Entity With Primary Key
     *
     * @param key ENTITY set with Primary key
     */
    public void remove(Object key) {
        EntityManager em = null;
        try {
            em = emf.createEntityManager();
            em.getTransaction().begin();
            T obj = em.find(type, key);
            em.remove(obj);
            em.getTransaction().commit();
        } catch (Exception e) {
            System.err.println("DELETE_ERROR\n");

        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    /**
     * Find Entity with Primary Key
     *
     * @param key ENTITY set with Primary key
     * @return Found Entity or null
     */
    public T find(Object key) {
        EntityManager em = null;
        T obj = null;
        try {
            em = emf.createEntityManager();
            em.getTransaction().begin();
            obj = em.find(type, key);
            em.getTransaction().commit();
        } catch (Exception e) {
            System.err.println("FIND_ERROR\n");

        } finally {
            if (em != null) {
                em.close();
            }
        }
        return obj;
    }

    /**
     * GET ALL
     *
     * @return All Entities List
     */
    public List<T> getAll() {
        List<T> ls = null;
        String queryString = "SELECT c FROM " + type.getSimpleName() + " c";

        EntityManager em = null;
        try {
            em = emf.createEntityManager();
            em.getTransaction().begin();

            ls = em.createQuery(queryString, type).getResultList();

            em.getTransaction().commit();

        } catch (Exception e) {
            System.err.println("GET-ALL_ERROR\n" + "Query=" + queryString);
            e.printStackTrace();

        } finally {
            if (em != null) {
                em.close();
            }
        }

        return ls;
    }

    /**
     * GET Ordered All
     *
     * @param orderItem FIELD-NAME as OrderKey
     * @return All Entities List
     */
    public List<T> getAll(String orderItem) {

        List<T> ls = null;
        String queryString = "SELECT c FROM " + type.getSimpleName() + " c ORDER BY c." + orderItem;

        EntityManager em = null;
        try {
            em = emf.createEntityManager();
            em.getTransaction().begin();

            ls = em.createQuery(queryString, type).getResultList();

            em.getTransaction().commit();

        } catch (Exception e) {
            System.err.println("★getAll(String orderItem)-error\n" + "Query=" + queryString);

        } finally {
            if (em != null) {
                em.close();
            }
        }

        return ls;

    }

    /**
     * GET Ordered ALL with specified field
     *
     * @param orderItem FIELD-NAME as OrderKey
     * @param desc      true: reverse-order
     * @return All Entities List
     */
    public List<T> getAll(String orderItem, boolean desc) {
        String sort = desc ? " desc" : " asc";
        List<T> ls = getAll(orderItem + sort);
        return ls;
    }

    /**
     * Get Specified Range with start position and max
     *
     * @param from START POSITION
     * @param max  MAX
     * @return All Entities List
     */
    public List<T> get(int from, int max) {

        List<T> ls = null;
        String queryString = "SELECT c FROM " + type.getSimpleName() + " c";

        EntityManager em = null;
        try {
            em = emf.createEntityManager();
            em.getTransaction().begin();

            ls = em.createQuery(queryString, type).setFirstResult(from).setMaxResults(max).getResultList();

            em.getTransaction().commit();

        } catch (Exception e) {
            System.err.println("GET(int from, int max)_ERROR\n" + "QUERY=" + queryString);

        } finally {
            if (em != null) {
                em.close();
            }
        }

        return ls;

    }

    /**
     * Get with Specified START POSITION, MAX and ORDER FIELD
     *
     * @param from      START POSITION
     * @param max       MAX
     * @param orderItem ORDER-FIELD
     * @return All Entities List
     */
    public List<T> get(int from, int max, String orderItem) {

        List<T> ls = null;
        String queryString = "SELECT c FROM " + type.getSimpleName() + " c ORDER BY c." + orderItem;

        EntityManager em = null;
        try {
            em = emf.createEntityManager();
            em.getTransaction().begin();

            ls = em.createQuery(queryString, type).setFirstResult(from).setMaxResults(max).getResultList();

            em.getTransaction().commit();

        } catch (Exception e) {
            System.err.println("GET(int from, int max, String orderItem)_ERROR\n" + "Query=" + queryString);

        } finally {
            if (em != null) {
                em.close();
            }
        }

        return ls;

    }

    /**
     * Get with Specified START POSITION, MAX and ORDER FIELD
     *
     * @param from      START POSITION
     * @param max       MAX
     * @param orderItem ORDER-FIELD
     * @return All Entities List
     * @param desc      true: Reverse-Order
     * @return All Entities List
     */
    public List<T> get(int from, int max, String orderItem, boolean desc) {
        String sort = desc ? " desc" : " asc";
        List<T> ls = get(from, max, orderItem + sort);
        return ls;

    }

    /**
     * Get with Custom Query
     *
     * @param queryString JPQL-Query
     * @return ALL Entities List
     */
    public List<T> select(String queryString) {

        List<T> ls = null;

        EntityManager em = null;
        try {
            em = emf.createEntityManager();
            em.getTransaction().begin();

            ls = em.createQuery(queryString, type).getResultList();

            em.getTransaction().commit();

        } catch (Exception e) {
            System.err.println("SELECT(String queryString)_ERROR\n" + "QUERY=" + queryString);

        } finally {
            if (em != null) {
                em.close();
            }
        }

        return ls;

    }

    /**
     * Get with Parameterize Query
     *
     * @param queryString Parameterize Query
     * @param param       Array as Parameter
     * @return All Entities List
     */
    public List<T> select(String queryString, Object... param) {

        EntityManager em = null;
        List<T> ls = null;

        try {
            em = emf.createEntityManager();
            em.getTransaction().begin();

            TypedQuery<T> q = em.createQuery(queryString, type);
            for (int i = 0; i < param.length; i++) {
                q.setParameter(i + 1, param[i]);
            }
            ls = q.getResultList();

            em.getTransaction().commit();

        } catch (Exception e) {
            System.err.println("SELECT(String queryString)_ERROR\n" + "QUERY=" + queryString);

        } finally {
            if (em != null) {
                em.close();
            }
        }

        return ls;

    }

    /**
     * @param queryString JPQL
     * @param from        START POSITION
     * @param max         MAX
     * @return All Entities List
     */
    public List<T> select(String queryString, int from, int max) {

        List<T> ls = null;

        EntityManager em = null;
        try {
            em = emf.createEntityManager();
            em.getTransaction().begin();

            ls = em.createQuery(queryString, type).setFirstResult(from).setMaxResults(max).getResultList();

            em.getTransaction().commit();

        } catch (Exception e) {
            System.err.println("SELECT(String queryString, int from, int max)_ERROR\n" + "QUERY=" + queryString);

        } finally {
            if (em != null) {
                em.close();
            }
        }

        return ls;

    }

    /**
     * @param queryString JPQL
     * @param from        START POSITION
     * @param max         MAX
     * @param param       Parameter Array
     * @return All Entities List
     */
    public List<T> select(String queryString, int from, int max, Object... param) {

        List<T> ls = null;

        EntityManager em = null;
        try {
            em = emf.createEntityManager();
            em.getTransaction().begin();

            TypedQuery<T> q = em.createQuery(queryString, type);
            for (int i = 0; i < param.length; i++) {
                q.setParameter(i + 1, param[i]);
            }
            ls = q.setFirstResult(from).setMaxResults(max).getResultList();

            em.getTransaction().commit();

        } catch (Exception e) {
            System.err.println("★select(String queryString, int from, int max)-error\n" + "Query=" + queryString);

        } finally {
            if (em != null) {
                em.close();
            }
        }

        return ls;

    }

    /**
     * Get All Entities Number
     *
     * @return 全件数
     */
    public int count() {

        String queryString = "SELECT count(c) FROM " + type.getSimpleName() + " c";
        List<Object> ls = null;
        EntityManager em = null;

        try {
            em = emf.createEntityManager();
            em.getTransaction().begin();

            ls = em.createQuery(queryString, Object.class).getResultList();

            em.getTransaction().commit();

        } catch (Exception e) {
            System.err.println("COUNT_ERROR\n" + "QUERY=" + queryString);

        } finally {
            if (em != null) {
                em.close();
            }
        }
        Object obj = ls.get(0);
        return Integer.valueOf(obj.toString());

    }

    /**
     * Close DatabaseManager
     */
    public void close() {
        emf.close();
    }
}
