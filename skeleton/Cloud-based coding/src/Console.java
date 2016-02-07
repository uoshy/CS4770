import java.util.Map;
public class Console {
   private Map<long,UserProcess> activeProcesses;
   public Console(Map<long,UserProcess> activeProcesses){
	   this.activeProcesses = activeProcesses;
    }
   
   public void execute(UserFile[] userfile, String command,String runOption){
	   
   }
   public UserProcess getProcess(long processID){
	   
   }
}
