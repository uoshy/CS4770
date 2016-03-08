package utility;
import java.sql.PreparedStatement;
import java.sql.SQLException;
public abstract class DBObject {
  public abstract void addToStatement(PreparedStatement statement, int index) throws SQLException;
}
