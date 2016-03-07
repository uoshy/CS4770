package main;

import java.io.File;

public class JSONFileList{

	public JSONFileObject[] fileObjs;

	public JSONFileList(int i){
		fileObjs = new JSONFileObject[i];
		for (int j = 0; j < i; j++){
			fileObjs[j] = new JSONFileObject();
		}
	}

	public class JSONFileObject {
		public String fileName;
		public boolean isDirectory;
	}
}
