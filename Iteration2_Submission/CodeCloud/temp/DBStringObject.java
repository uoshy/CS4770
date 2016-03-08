import java.sql.PreparedStatement;
import java.sql.SQLException;
public class DBStringObject extends DBObject {
  private String _String;
  public DBStringObject(String s){
    _String = s;
  }
  public void addToStatement(PreparedStatement statement, int index) throws SQLException {
      statement.setString(index, _String);
  }
}
