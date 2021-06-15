package jdbc;

import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.sql.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * DAO
 * Data Access Object
 */
public class BaseDao extends ConnectionFactory {

    //基本类型包装类名称列表
    private static Set<Class> types;
    private static Map<String, Integer> mapTypeFromJavaToMysql;

    static {
        types = new HashSet<>();
        mapTypeFromJavaToMysql = new HashMap<>();
        types.addAll(Arrays.asList(
                String.class,
                Character.class,
                Byte.class,
                Short.class,
                Integer.class,
                Long.class,
                Float.class,
                Double.class,
                BigDecimal.class,
                Boolean.class
        ));
        Object[][] types = {
                {"int", Types.INTEGER},
                {"java.lang.Integer", Types.INTEGER}

        };
        for (Object[] type : types) {
            mapTypeFromJavaToMysql.put(type[0].toString(), Integer.parseInt(type[1].toString()));
        }
    }

    //实现BaseDao 的数据交互的标准定义
    private static class Message<T> implements DaoMessage<T> {
        private int status;
        private T t;

        static <T> Message succeed(T t) {
            return new Message(t);
        }

        static Message fail() {
            return new Message();
        }

        private Message() {
            this.status = ERROR;
        }

        private Message(T t) {
            this.status = SUCCESS;
            this.t = t;
        }

        @Override
        public int getStatus() {
            return status;
        }

        @Override
        public T getData() {
            return t;
        }
    }

    //获取数据源连接对象
    private static Connection con() throws SQLException {
        return newInstance();
    }

    //根据连接对象、SQL命令和预编译参数获取执行对象
    private static PreparedStatement pst(Connection con, String sql, Object... args) throws SQLException {
        PreparedStatement pst = con.prepareStatement(sql);
        if (null != args && args.length > 0) {
            for (int i = 0; i < args.length; i++) {
                pst.setObject(i + 1, args[i]);
            }
        }
        return pst;
    }

    //反射解析实体类型中属性名称和setter方法以键值对返回
    private static <T> Map<String, Method> map(Class<T> c) {
        Map<String, Method> map = new HashMap<>();
        for (Method m : c.getMethods()) {
            String name = m.getName();
            if (!name.startsWith("set")) {
                continue;
            }
            name = name.substring(3);
            name = name.substring(0, 1).toLowerCase() + name.substring(1);
            map.put(name, m);
        }
        return map;
    }

    //解析结果集中元数据的结构：将字段名称和顺序编号以键值对返回
    private static Map<String, Integer> map(ResultSet rst) throws SQLException {
        Map<String, Integer> map = new HashMap<>();
        ResultSetMetaData metaData = rst.getMetaData();
        for (int i = 1; i <= metaData.getColumnCount(); i++) {
            map.put(metaData.getColumnLabel(i), i);
        }
        return map;
    }

    //检查sql语句中参数
    private static int checkSql(boolean batch, String sql, Object... args) throws SQLException {
        final String R = "([,=\\(]\\?[,\\)]?)";
        int count = 0, index = 0;
        Matcher m = Pattern.compile(R).matcher(sql);
        while (m.find(index)) {
            count++;
            index = m.end() - 1;
        }
        if (args.length < count || batch && args.length % count != 0) {
            throw new SQLException("SQL参数(?)数量与实际参数(args)数量不匹配异常");
        }
        return count;
    }

    private static void checkNull(Object... obj) throws SQLException {
        for (Object o : obj) {
            if (null == o) {
                throw new SQLException("parameter obj is null");
            }
        }
    }

    //检查sql语句和方法的匹配关系
    private static void checkSql(String sql, boolean isSelect) throws SQLException {
        String prefix = sql.substring(0, 6);
        if (isSelect ? !prefix.equalsIgnoreCase("select")
                : (!prefix.equalsIgnoreCase("insert") &&
                !prefix.equalsIgnoreCase("delete") &&
                !prefix.equalsIgnoreCase("update")
        )) {
            throw new SQLException("sql 操作类型不匹配异常");
        }
    }

    //检查c 是否为基本类型或基本类型包装类
    private static void checkType(Class c) throws SQLException {
        if (!types.contains(c)) {
            throw new SQLException(c.getName() + " 非包装类型异常");
        }
    }

    //检查参数类型 c 与sql映射类型是否匹配
    private static <T> T checkType(Class<T> c, ResultSet rst) throws SQLException {
        T t = null;
        if (rst.next()) {
            Object obj = rst.getObject(1);
            if (!c.isInstance(obj)) {
                throw new SQLException("java类型" + c.getName() +
                        "和sql映射类型" + obj.getClass().getName() + "不匹配异常");
            }
            t = (T) obj;
        }
        return t;
    }

    //执行增删改操作 考点1
    public static DaoMessage<Integer> exeUpdate(String sql, Object... args) {
        Connection con = null;
        PreparedStatement pst = null;
        try {
            checkNull(sql);
            checkSql(sql, false);
            checkSql(false, sql, args);
            con = con();
            pst = pst(con, sql, args);
            return Message.succeed(pst.executeUpdate());
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close(pst, con);
        }
        return Message.fail();
    }

    //执行查询操作 考点3
    public static <T> DaoMessage<List<T>> exeQuery(Class<T> c, String sql, Object... args) {
        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rst = null;
        Map<String, Method> mapReflect = null;
        Map<String, Integer> mapMeta = null;
        try {
            checkNull(sql);
            checkSql(sql, true);
            checkSql(false, sql, args);
            con = con();
            pst = pst(con, sql, args);
            rst = pst.executeQuery();
            return Message.succeed(rstToList(c, rst));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            close(rst, pst, con);
            if (null != mapReflect) {
                mapReflect.clear();
            }
            if (null != mapMeta) {
                mapMeta.clear();
            }
        }
        return Message.fail();

    }

    //执行【单列映射包装类型对象】查询操作
    public static <T> DaoMessage<List<T>> queryOneColumn(Class<T> c, String sql, Object... args) {
        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rst = null;
        try {
            checkNull(sql);
            checkType(c);
            checkSql(sql, true);
            checkSql(false, sql, args);
            con = con();
            pst = pst(con, sql, args);
            rst = pst.executeQuery();
            T t = checkType(c, rst);
            if (null == t)
                return Message.succeed(null);

            List<T> list = new ArrayList<>();
            list.add(t);
            while (rst.next()) {
                list.add((T) rst.getObject(1));
            }
            return Message.succeed(list);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Message.fail();
    }

    //批量插入  考点2
    public static DaoMessage<Integer> addBatch(String sql, int batchSize, Object... args) {
        Connection con = null;
        PreparedStatement pst = null;
        int affectedRows = 0;
        try {
            checkNull(sql);
            checkSql(sql, false);
            int colSize = checkSql(true, sql, args);
            con = con();
            pst = pst(con, sql);
            for (int i = 0, realSize = 0; i < args.length; i += colSize) {
                //添加一行数据
                for (int j = i, size = 0; size < colSize; size++, j++) {
                    pst.setObject(size + 1, args[j]);
                }
                //将该行作为一个批次缓存至执行器
                pst.addBatch();
                //当执行器中当前缓存数量达到batchSize时提交数据（持久化）
                if (++realSize % batchSize == 0) {
                    //提交并累加影响行数
                    affectedRows += pst.executeBatch().length;
                    //清空执行器
                    pst.clearBatch();
                }
            }
            //提交
            affectedRows += pst.executeBatch().length;
            //清空执行器缓存
            pst.clearBatch();
            return Message.succeed(affectedRows);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close(pst, con);
        }
        return Message.fail();
    }

    public static class TranItem {
        private String sql;
        private Object[] params;

        private TranItem(String sql, Object... params) {
            this.sql = sql;
            this.params = params;
        }

        public static TranItem withParams(String sql, Object[] params) {
            return new TranItem(sql, params);
        }

        public static TranItem noParams(String sql) {
            return new TranItem(sql);
        }
    }

    //执行【事务】操作
    //若有查询操作，且查询操作的结果被其他增删改语句依赖
    //1、调用jdbc中的查询方法，将结果提取出来，以常量的方式存入被依赖的增删改的，命令参数中
    //2、在mysql中创建个性化的存储过程
    public static DaoMessage<Integer> exeTran(TranItem... items) throws SQLException {
        if (items.length < 2) {
            throw new SQLException("a transaction must be based on 2 sql command at least");
        }
        Connection con = null;
        try {
            con = newInstance(Tran.openTran(Connection.TRANSACTION_READ_COMMITTED));
            for (TranItem item : items) {
                PreparedStatement pst = pst(con, item.sql, item.params);
                int rst = pst.executeUpdate();
                if (rst <= 0) {
                    close(pst);
                    throw new SQLException("execute " + item.sql + " with params " + Arrays.toString(item.params) + " exception");
                }
            }
            //commit(con);
            return Message.succeed(1);
        } catch (SQLException e) {
            e.printStackTrace();
            rollback(con);
            return Message.fail();
        } finally {
            close(con);
        }
    }

    //根据连接对象、SQL命令和预编译参数获取执行对象
    private static CallableStatement cstAllIn(Connection con, String sql, Object... args) throws SQLException {
        CallableStatement cst = con.prepareCall(sql);
        if (null != args && args.length > 0) {
            for (int i = 0; i < args.length; i++) {
                cst.setObject(i + 1, args[i]);
            }
        }
        return cst;
    }

    private static <T> List<T> rstToList(Class<T> c, ResultSet rst) throws Exception {
        Map<String, Method> mapReflect = map(c);
        Map<String, Integer> mapMeta = map(rst);
        List<T> list = new ArrayList<>();
        while (rst.next()) {
            T t = c.newInstance();
            for (Map.Entry<String, Method> e : mapReflect.entrySet()) {
                e.getValue().invoke(t, rst.getObject(mapMeta.get(e.getKey())));
            }
            list.add(t);
        }
        return list;
    }

    //"{call pro_name}(?,?,?,?,?,...)"
    //new int[]{0:in,1:out,2:inout}
    private static CallableStatement cstMix(Connection con, String sql, int[] paramTypes, Object... args) throws SQLException {
        CallableStatement cst = con.prepareCall(sql);
        if (null != args && args.length > 0) {
            for (int i = 0; i < args.length; i++) {
                switch (paramTypes[i]) {
                    case 0:
                        cst.setObject(i = 1, args[i]);
                        break;
                    case 1:
                        cst.registerOutParameter(i + 1, (Integer) args[i]);
                        break;
                    case 2:
                        cst.setObject(i = 1, args[i]);
                        cst.registerOutParameter(i + 1,
                                mapTypeFromJavaToMysql.get(args[i].getClass().getTypeName()));
                        break;
                }
                cst.setObject(i + 1, args[i]);
            }
        }
        return cst;
    }

    //执行【mysql中封装的存储过程】 : create procedure PRO_NAME(in v1 type,inout v2 type,out v3 type,...)...
    public static DaoMessage<Map<Integer, Object>> exePro(String proSql, Class[] cs, Object... params) throws Exception {
        Map<Integer, Object> map = new HashMap<>();
        Connection con = null;
        CallableStatement cst = null;
        try {
            con = newInstance();
            cst = cstAllIn(con, proSql, params);
            cst.execute();
            //所有结果全部是结果集
            int i = 0;
            do {
                ResultSet rst = cst.getResultSet();
                if (types.contains(cs[i])) {
                    map.put(i + 1, rst.next() ? rst.getObject(1) : null);
                } else {
                    map.put(i + 1, rstToList(cs[i], rst));
                }
                i++;
            } while (cst.getMoreResults());
            return Message.succeed(map);
        } catch (SQLException e) {
            e.printStackTrace();
            return Message.fail();
        } finally {
            close(cst,con);
        }
    }
}
