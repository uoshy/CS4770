import java.util.Collection;
public class CompilerReturn implements HTMLDisplayable{
    private String compilerMessage;
    private Collection<UserFile> returnedFiles;
    public CompilerReturn(String compilerMessage,Collection<UserFile> returnedFiles){
    	this.compilerMessage= compilerMessage;
    	this.returnedFiles= returnedFiles;
    }
    public String displayAsHTML(){
    	return null;
    }
}
