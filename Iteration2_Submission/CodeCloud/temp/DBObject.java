import java.sql.PreparedStatement;
public abstract class DBObject {
  public void addToStatement(PreparedStatement statement, int index){}
}
