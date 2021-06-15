package jdbc;

import java.io.File;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public abstract class ConnectionFactory {

    //连接工厂数据源配置工具
    static Properties config;

    //事务封装
    public static class Tran {
        public boolean openTran;
        public int tranIsolationLevel;

        private Tran(boolean openTran, int tranIsolationLevel) {
            this.openTran = openTran;
            this.tranIsolationLevel = tranIsolationLevel;
        }

        public static Tran openTran(final int TRAN_ISOLATION_LEVEL) {
            return new Tran(true, TRAN_ISOLATION_LEVEL);
        }

        public static Tran closeTran() {
            return new Tran(false, -1);
        }
    }

    //静态代码块完成工厂资源初始化
    static {
        config = new Properties();
        //通过线程的内置上下文对象获取项目target根目录
        String root = Thread.currentThread().getContextClassLoader()
                .getResource("").getFile();
        //通过File的树结构特征提取项目根目录的上一级目录
        File rootDir = new File(root).getParentFile().getParentFile().getParentFile();
        //构建配置文件路径
        final String CONFIG_PATH = rootDir.getPath() + "/config/jdbc.properties";
        try {
            config.load(new FileReader(CONFIG_PATH));
            //加载数据源驱动
            Class.forName(config.getProperty("mysql.driver"));
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }

    //创建连接对象
    static Connection newInstance(Tran... trans) throws SQLException {
        Connection con = DriverManager.getConnection(
                config.getProperty("mysql.url"),
                config.getProperty("mysql.username"),
                config.getProperty("mysql.password")
        );
        if (trans.length > 0 && trans[0].openTran) {
            con.setTransactionIsolation(trans[0].tranIsolationLevel);
            con.setAutoCommit(false);
        }
        return con;
    }

    //回滚基于某事务的连接
    static void rollback(Connection con) {
        if (null != con) {
            try {
                con.rollback();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    //提交基于某事务的连接
    static void commit(Connection con) throws SQLException {
        con.commit();
    }

    //释放基于AutoCloseable的资源
    protected static void close(AutoCloseable... closes) {
        for (AutoCloseable close : closes) {
            if (null != close) {
                try {
                    close.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
