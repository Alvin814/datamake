package jdbc;

//同一Dao层数据交互格式
public interface DaoMessage<T> {
    int ERROR = 400;
    int SUCCESS = 200;

    int getStatus();
    T getData();

}
