package utility;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * A wrapper for a String object to be inserted into the database.
 * @author Alex
 *
 */
public class DBStringObject extends DBObject {
  private String _String;
  
  /**
   * Create a new DBStringObject given a String to wrap.
   * @param s the string to wrap.
   */
  public DBStringObject(String s){
    _String = s;
  }
  public void addToStatement(PreparedStatement statement, int index) throws SQLException {
      statement.setString(index, _String);
  }
}
