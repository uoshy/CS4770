import java.sql.PreparedStatement;
import java.sql.SQLException;
public class DBStringObject extends DBObject {
  private String _String;
  public DBStringObject(String s){
    _String = s;
  }
  public void addToStatement(PreparedStatement statement, int index){
    try {
      statement.setString(index, _String);
    }
    catch (SQLException sqle){
      sqle.printStackTrace();
    }
  }
}
