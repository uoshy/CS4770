package utility;
import java.sql.PreparedStatement;
import java.sql.SQLException;
public class DBIntObject extends DBObject {
  private int _int;
  public DBIntObject(int i){
    _int = i;
  }
  public void addToStatement(PreparedStatement statement, int index) throws SQLException{
      statement.setInt(index, _int);
  }
}
