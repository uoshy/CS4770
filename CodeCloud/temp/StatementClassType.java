import java.sql.PreparedStatement;
public enum StatementClassType {

	DBFLOAT {
		void addToStatement(PreparedStatement statement, Float f, int i){
			statement.setFloat(i, f);
		}
	}

	DBINT {
		void addToStatement(PreparedStatement statement, int j, int i){
			statement.setInt(i,j);
		}
	}

	DBSTRING {
		void addToStatement(PreparedStatement statement, String s, int i){
			statement.setString(i, s);
		}
	}
}
